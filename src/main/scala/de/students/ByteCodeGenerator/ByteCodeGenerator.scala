package de.students.ByteCodeGenerator

import scala.collection.mutable.ArrayBuffer
import org.objectweb.asm.{ClassWriter, Label, MethodVisitor}
import org.objectweb.asm.Opcodes.*
import de.students.Parser.*

import scala.collection.mutable;

type ClassBytecode = Array[Byte]

def generateBytecode(pack: Package): List[ClassBytecode] = {
  pack.classes.map(generateClassBytecode)
}

case class ByteCodeGeneratorException(msg: String) extends RuntimeException(msg)

private def generateClassBytecode(classDecl: ClassDecl): ClassBytecode = {
  val cw = new ClassWriter(0)

  // set class header
  cw.visit(
    V1_4,
    visibilityModifier(classDecl),
    classDecl.name,
    null, // signature
    classDecl.parent, // .getOrElse("java/lang/Object"), // superName
    null // interfaces
  )

  // there is currently no definable constructor, so an empty one is added
  generateConstructor(cw)

  // set fields
  classDecl.fields.foreach(varDecl => cw.visitField(
    visibilityModifier(varDecl),
    varDecl.name,
    asmType(varDecl.varType),
    null, // signature
    null // initial value, only used for static fields
  ).visitEnd())

  // set methods
  classDecl.methods.foreach(methodDecl => generateMethodBody(methodDecl, cw.visitMethod(
    visibilityModifier(methodDecl),
    methodDecl.name,
    asmType(functionType(methodDecl)),
    null, // signature
    null // exceptions
  ), MethodGeneratorState(
    classDecl.fields,
    methodDecl.returnType,
    classDecl.name,
    0, 0,
    ArrayBuffer.empty,
    ArrayBuffer.empty,
    0,
    mutable.HashMap.empty,
  )))

  cw.visitEnd()

  cw.toByteArray
}

// default empty constructor for now
private def generateConstructor(cw: ClassWriter): Unit = {
  val mv = cw.visitMethod(
    0,
    "<init>",
    "()V",
    null,
    null
  )
  mv.visitCode()

  mv.visitVarInsn(ALOAD, 0)
  mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
  mv.visitInsn(RETURN)

  mv.visitMaxs(1, 1)
  mv.visitEnd()
}

private def generateMethodBody(methodDecl: MethodDecl, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  methodVisitor.visitCode()

  state.localVariableCount = 1 // methodDecl.params.size + 1 // `this` is param #0
  state.stackDepth = 0
  generateStatement(methodDecl.body, methodVisitor, state)

  methodVisitor.visitMaxs(state.stackDepth, state.localVariableCount)
  methodVisitor.visitEnd()
}

private case class MethodGeneratorState(
                                 val fields: List[VarDecl],
                                 val returnType: Type,
                                 val className: String,
                                 var stackDepth: Int,
                                 var localVariableCount: Int,
                                 val scopeEnds: ArrayBuffer[Label],
                                 val loopStarts: ArrayBuffer[Label],
                                 var currentScope: Int,
                                 val variables: mutable.HashMap[String, VariableInfo],
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

  def addVariable(name: String): Int = {
    val res = variables.put(name, VariableInfo(localVariableCount, currentScope))
    if (res.isDefined) {
      throw ByteCodeGeneratorException(f"variable $name already exists")
    }
    localVariableCount += 1
    localVariableCount - 1
  }

  def checkVariableScopes(): Unit = {
    variables.filterInPlace { (_, variableInfo) => variableInfo.scopeId >= currentScope }
  }
}

private case class VariableInfo(
                               id: Int,
                               scopeId: Int,
                               )