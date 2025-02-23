package de.students.ByteCodeGenerator

import scala.collection.mutable.ArrayBuffer
import org.objectweb.asm.{ClassWriter, Label, MethodVisitor}
import org.objectweb.asm.Opcodes.*
import de.students.Parser.*

import scala.collection.mutable;
import scala.collection.immutable;

case class ClassBytecode(
  bytecode: Array[Byte],
  className: String
)

def generateBytecode(project: Project): List[ClassBytecode] = {
  project.packages.flatMap(p => generateBytecode(p))
}

def generateBytecode(pack: Package): List[ClassBytecode] = {
  pack.classes.map(generateClassBytecode)
}

case class ByteCodeGeneratorException(msg: String) extends RuntimeException(msg)

private def generateClassBytecode(classDecl: ClassDecl): ClassBytecode = {
  val classWriter = new ClassWriter(0)

  val javaClassName = javaifyClass(classDecl.name)
  val parent = javaifyClass(classDecl.parent) // "java/lang/Object" // TODO use real parent

  // set class header
  classWriter.visit(
    V1_4,
    accessModifier(classDecl),
    javaClassName,
    null, // signature
    parent, // super class
    null // interfaces
  )

  // set constructors
  classDecl.constructors.foreach(constructorDecl =>
    generateConstructor(
      classDecl,
      classWriter,
      constructorDecl
    )
  )
  if (classDecl.constructors.isEmpty) {
    generateConstructor(
      classDecl,
      classWriter,
      ConstructorDecl(Some("public"), "", List(), ReturnStatement(None))
    )
  }

  // set fields
  classDecl.fields.foreach(fieldDecl =>
    classWriter
      .visitField(
        accessModifier(fieldDecl),
        fieldDecl.name,
        javaSignature(fieldDecl.varType),
        null, // signature
        null // initial value, only used for static fields
      )
      .visitEnd()
  )

  // set methods
  classDecl.methods.foreach(methodDecl => generateMethodBody(classDecl, methodDecl, classWriter))

  classWriter.visitEnd()

  ClassBytecode(
    classWriter.toByteArray,
    classDecl.name
  )
}

// default empty constructor for now
private def generateConstructor(
  classDecl: ClassDecl,
  classWriter: ClassWriter,
  constructorDecl: ConstructorDecl
): Unit = {
  val methodVisitor = classWriter.visitMethod(
    ACC_PUBLIC,
    "<init>",
    javaSignature(constructorType(constructorDecl)),
    null, // signature
    null // exceptions
  )
  val state = defaultMethodGeneratorState(
    classDecl,
    methodVisitor,
    VoidType
  )
  state.localVariableCount = 1 // this

  constructorDecl.params.foreach(param => state.addVariable(param.name, param.varType))

  state.stackDepth = state.localVariableCount

  methodVisitor.visitCode()

  Instructions.callSuper(classDecl.parent, state)

  generateStatement(constructorDecl.body, state)

  methodVisitor.visitMaxs(state.maxStackDepth, state.localVariableCount)
  methodVisitor.visitEnd()
}

private def generateMethodBody(
  classDecl: ClassDecl,
  methodDecl: MethodDecl,
  classWriter: ClassWriter
): Unit = {
  val methodVisitor = classWriter.visitMethod(
    visibilityModifier(methodDecl),
    methodDecl.name,
    javaSignature(functionType(methodDecl)),
    null, // signature
    null // exceptions
  )
  val state = defaultMethodGeneratorState(
    classDecl,
    methodVisitor,
    methodDecl.returnType
  )
  state.localVariableCount = (if methodDecl.static then 0 else 1) // if method is not static`this` is param #0

  methodDecl.params.foreach(param => state.addVariable(param.name, param.varType))

  methodVisitor.visitCode()

  state.stackDepth = state.localVariableCount
  if (methodDecl.body.isDefined) {
    generateStatement(methodDecl.body.get, state)
  }

  methodVisitor.visitMaxs(state.maxStackDepth, state.localVariableCount)
  methodVisitor.visitEnd()
}

private case class MethodGeneratorState(
  val methodVisitor: MethodVisitor,
  val fields: List[FieldDecl],
  val returnType: Type,
  val className: String,
  var stackDepth: Int,
  var maxStackDepth: Int,
  var localVariableCount: Int,
  val scopeEnds: ArrayBuffer[Label],
  val loopStarts: ArrayBuffer[Label],
  var currentScope: Int,
  val variables: mutable.HashMap[String, VariableInfo],
  val stackTypes: ArrayBuffer[Type]
) {
  def startSimpleScope(end: Label): Unit = {
    scopeEnds += end
    currentScope += 1
  }

  def endSimpleScope(): Unit = {
    scopeEnds.remove(scopeEnds.size - 1)
    currentScope -= 1
  }

  def startLoopScope(start: Label, end: Label): Unit = {
    loopStarts += start
    startSimpleScope(end)
  }

  def endLoopScope(): Unit = {
    loopStarts.remove(loopStarts.size - 1)
    endSimpleScope()
  }

  def pushStack(t: Type): Unit = {
    stackTypes.append(t)
    pushStack(typeStackSize(t))
  }
  private def pushStack(count: Int): Unit = {
    stackDepth += count
    maxStackDepth = Math.max(stackDepth, maxStackDepth)
  }
  def popStack(): Unit = {
    if (stackTypes.isEmpty) {
      throw ByteCodeGeneratorException("empty stack cannot be popped, this is a bug in the bytecode generator")
    }
    val typeSize = typeStackSize(stackTypes.last)

    stackDepth -= typeSize
    stackTypes.remove(stackTypes.size - 1)
  }
  def popStack(count: Int): Unit = {
    for (i <- 0 until count) {
      popStack()
    }
  }

  def addVariable(name: String, t: Type): Int = {
    val field = fields.find(fieldDecl => fieldDecl.name == name)
    if (field.isDefined) {
      throw ByteCodeGeneratorException(f"variable $name already exists, it is a field")
    }

    val res = variables.put(name, VariableInfo(localVariableCount, currentScope, t, false))
    if (res.isDefined) {
      throw ByteCodeGeneratorException(f"variable $name already exists")
    }
    val typeSize = typeStackSize(t)
    localVariableCount += typeSize
    localVariableCount - typeSize
  }

  def getVariable(name: String): VariableInfo = {
    val res = variables.get(name)
    val field = fields.find(fieldDecl => fieldDecl.name == name)
    if (res.isDefined) {
      res.get
    } else if (field.isDefined) {
      VariableInfo(0, 0, field.get.varType, true)
    } else {
      throw ByteCodeGeneratorException(f"variable $name does not exist")
    }
  }

  def checkVariableScopes(): Unit = {
    variables.filterInPlace { (_, variableInfo) => variableInfo.scopeId >= currentScope }
  }
}

private def defaultMethodGeneratorState(
  classDecl: ClassDecl,
  methodVisitor: MethodVisitor,
  returnType: Type
): MethodGeneratorState =
  MethodGeneratorState(
    methodVisitor,
    classDecl.fields,
    returnType,
    classDecl.name,
    0,
    0,
    0,
    ArrayBuffer.empty,
    ArrayBuffer.empty,
    0,
    mutable.HashMap.empty,
    ArrayBuffer(UserType("this")) // TODO use own type
  )

private case class VariableInfo(
  id: Int,
  scopeId: Int,
  t: Type,
  isField: Boolean
)
