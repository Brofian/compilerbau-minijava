package de.students.ByteCodeGenerator

import org.objectweb.asm.{MethodVisitor, Label}
import org.objectweb.asm.Opcodes.*

import de.students.Parser.*

private object Instructions {
  def pushConstant(constant: Any, state: MethodGeneratorState): Unit = {
    state.pushStack()
    state.methodVisitor.visitLdcInsn(constant)
  }

  def pushTrue(state: MethodGeneratorState): Unit = {
    pushConstant(1, state)
  }

  def pushFalse(state: MethodGeneratorState): Unit = {
    pushConstant(0, state)
  }

   def goto(label: Label, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitJumpInsn(GOTO, label)
  }

   def visitLabel(label: Label, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitLabel(label)
  }

   def nop(state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitInsn(NOP)
  }

   def condJump(opcode: Int, label: Label, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitJumpInsn(opcode, label)
    state.popStack(2)
  }

   def switch(default: Label, labels: Array[Label], keys: Array[Int], state: MethodGeneratorState) = {
    state.methodVisitor.visitLookupSwitchInsn(default, keys, labels)
  }

   def storeVar(varId: Int, varType: Type, state: MethodGeneratorState) = {
    // TODO multiple types
    state.methodVisitor.visitVarInsn(asmStoreInsn(varType), varId)
    state.popStack(1)
  }

   def loadVar(varId: Int, varType: Type, state: MethodGeneratorState) = {
    // TODO multiple types
    state.methodVisitor.visitVarInsn(asmLoadInsn(varType), varId)
    state.pushStack()
  }

   def pop(state: MethodGeneratorState) = {
    state.methodVisitor.visitInsn(POP)
    state.popStack(1)
  }

   def duplicateTop(state: MethodGeneratorState) = {
    state.methodVisitor.visitInsn(DUP)
    state.pushStack()
  }

   def loadThis(state: MethodGeneratorState) = {
    state.methodVisitor.visitVarInsn(ALOAD, 0)
    state.pushStack()
  }

  def storeField(name: String, fieldType: Type, state: MethodGeneratorState) = {
    loadThis(state)
    state.methodVisitor.visitFieldInsn(PUTFIELD, state.className, name, asmType(fieldType))
    // this is popped and field is pushed
  }

  def loadField(name: String, fieldType: Type, state: MethodGeneratorState) = {
    loadThis(state)
    state.methodVisitor.visitFieldInsn(GETFIELD, state.className, name, asmType(fieldType))
    state.popStack(1)
  }

   def binaryOperation(opcode: Int, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitInsn(opcode)
    state.popStack(1) // the instruction takes two arguments from the stack and then pushes the result
  }

  // NOTE this has to be loaded before calling
  def callOwnMethod(name: String, argumentCount: Int, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, state.className, name, state.methodDescriptors(name), false)
    state.popStack(1 + argumentCount)
  }
}