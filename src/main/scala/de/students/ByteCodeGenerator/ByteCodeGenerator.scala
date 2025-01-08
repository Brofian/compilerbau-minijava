package de.students.ByteCodeGenerator

import org.objectweb.asm.{ClassWriter, MethodVisitor}
import org.objectweb.asm.Opcodes.*

import de.students.Parser.*;

type ClassBytecode = Array[Byte]

def generateBytecode(pack: Package): List[ClassBytecode] = {
  pack.classes.map(generateClassBytecode)
}

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
  ), MethodGeneratorState(classDecl.fields, methodDecl.returnType, classDecl.name, 0, 0)))

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

  state.localVariableCount = methodDecl.params.size + 1 // `this` is param #0
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
                               )
