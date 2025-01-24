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

  /**
   * generates the bytecode instructions for a conditional jump
   * this consumes one operand from the stack, it is compared against 0
   * behaviour from JVM Spec:
   * {{{
   * - IFEQ succeeds iff value == 0
   * - IFNE succeeds iff value != 0
   * - IFLT succeeds iff value < 0
   * - IFLE succeeds iff value <= 0
   * - IFGT succeeds iff value > 0
   * - IFGE succeeds iff value >= 0
   * }}}
   *
   * @param opcode the branch instruction (see top)
   * @param label the label to which should be jumped on success
   * @param state
   */
   def condJump(opcode: Int, label: Label, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitJumpInsn(opcode, label)
    state.popStack(1)
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

  def duplicateTopTwo(state: MethodGeneratorState) = {
    state.methodVisitor.visitInsn(DUP2)
    state.pushStack()
    state.pushStack()
  }

  def loadThis(state: MethodGeneratorState) = {
    state.methodVisitor.visitVarInsn(ALOAD, 0)
    state.pushStack()
  }

  def loadClass(className: String, state: MethodGeneratorState) = {
    state.methodVisitor.visitVarInsn
  }

  def storeField(name: String, fieldType: Type, state: MethodGeneratorState) = {
    state.methodVisitor.visitFieldInsn(PUTFIELD, javaifyClass(state.className), name, asmType(fieldType))
    state.popStack(1)
  }

  /**
   * pop object off stack and push field
   * @param name field name
   * @param fieldType field type
   * @param state
   */
  def loadField(name: String, fieldType: Type, state: MethodGeneratorState) = {
    state.methodVisitor.visitFieldInsn(GETFIELD, javaifyClass(state.className), name, asmType(fieldType))
    // object is popped and field is pushed
  }

   def binaryOperation(opcode: Int, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitInsn(opcode)
    state.popStack(1) // the instruction takes two arguments from the stack and then pushes the result
  }

  def callMethod(className: String, methodName: String, argumentCount: Int, methodDescriptor: String, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, javaifyClass(className), methodName, methodDescriptor, false)
    state.popStack(1 + argumentCount)
  }

  def returnVoid(state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitInsn(RETURN)
  }

  def returnType(descriptor: Type, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitInsn(asmReturnCode(descriptor))
    state.popStack(1)
  }

  def newObject(className: String, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitTypeInsn(NEW, javaifyClass(className))
    state.pushStack()
  }

  def callConstructor(className: String, parameterDescriptors: List[Type], state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitMethodInsn(INVOKESPECIAL, javaifyClass(className), "<init>", asmConstructorType(parameterDescriptors), false)
    state.popStack(1 + parameterDescriptors.size)
  }
}