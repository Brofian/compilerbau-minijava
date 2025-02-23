package de.students.ByteCodeGenerator

import org.objectweb.asm.{MethodVisitor, Label}
import org.objectweb.asm.Opcodes.*

import de.students.Parser.*

private object Instructions {
  def pushConstant(constant: Any, constantType: Type, state: MethodGeneratorState): Unit = {
    state.pushStack(constantType)
    state.methodVisitor.visitLdcInsn(constant)
  }

  def pushTrue(state: MethodGeneratorState): Unit = {
    pushConstant(1, BoolType, state)
  }

  def pushFalse(state: MethodGeneratorState): Unit = {
    pushConstant(0, BoolType, state)
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
    state.popStack()
  }

  def switch(default: Label, labels: Array[Label], keys: Array[Int], state: MethodGeneratorState) = {
    state.methodVisitor.visitLookupSwitchInsn(default, keys, labels)
  }

  def storeVar(varId: Int, varType: Type, state: MethodGeneratorState) = {
    // TODO multiple types
    state.methodVisitor.visitVarInsn(asmStoreInsn(varType), varId)
    state.popStack()
  }

  def loadVar(varId: Int, varType: Type, state: MethodGeneratorState) = {
    // TODO multiple types
    state.methodVisitor.visitVarInsn(asmLoadInsn(varType), varId)
    state.pushStack(varType)
  }

  def loadStaticClassMember(
    staticClassName: String,
    memberName: String,
    memberType: Type,
    state: MethodGeneratorState
  ) = {
    state.methodVisitor.visitFieldInsn(GETSTATIC, javaifyClass(staticClassName), memberName, javaSignature(memberType))
    state.pushStack(memberType)
  }

  def storeStaticClassMember(
    staticClassName: String,
    memberName: String,
    memberType: Type,
    state: MethodGeneratorState
  ) = {
    state.methodVisitor.visitFieldInsn(PUTSTATIC, javaifyClass(staticClassName), memberName, javaSignature(memberType))
    state.pushStack(memberType)
  }

  def popType(state: MethodGeneratorState) = {
    val size = typeStackSize(state.stackTypes.last)
    if (size == 2) { popTwo(state) }
    else { for (i <- 0 until size) { pop(state) } }
  }
  def pop(state: MethodGeneratorState) = {
    state.methodVisitor.visitInsn(POP)
    state.popStack()
  }
  def popTwo(state: MethodGeneratorState) = {
    state.methodVisitor.visitInsn(POP2)
    state.popStack()
  }

  def duplicateTopType(state: MethodGeneratorState) = {
    val size = typeStackSize(state.stackTypes.last)
    if (size == 2) { duplicateTopTwo(state) }
    else { for (i <- 0 until size) { duplicateTop(state) } }
  }
  def duplicateTop(state: MethodGeneratorState) = {
    state.methodVisitor.visitInsn(DUP)
    state.pushStack(state.stackTypes.last)
  }

  def duplicateTopTwo(state: MethodGeneratorState) = {
    state.methodVisitor.visitInsn(DUP2)
    state.pushStack(state.stackTypes.last)
    state.pushStack(state.stackTypes.last)
  }

  def loadThis(state: MethodGeneratorState) = {
    state.methodVisitor.visitVarInsn(ALOAD, 0)
    state.pushStack(UserType(javaifyClass(state.className)))
  }

  // def loadClass(className: String, state: MethodGeneratorState) = {
  //   state.methodVisitor.visitVarInsn
  // }

  def storeField(name: String, fieldType: Type, state: MethodGeneratorState) = {
    state.methodVisitor.visitFieldInsn(PUTFIELD, javaifyClass(state.className), name, javaSignature(fieldType))
    state.popStack()
  }

  /**
   * pop object off stack and push field
   * @param name field name
   * @param fieldType field type
   * @param state
   */
  def loadField(name: String, fieldType: Type, state: MethodGeneratorState) = {
    val insn = if name == "length" then ARRAYLENGTH else GETFIELD
    state.methodVisitor.visitFieldInsn(insn, javaifyClass(state.className), name, javaSignature(fieldType))
    // object is popped and field is pushed
  }

  def binaryOperation(opcode: Int, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitInsn(opcode)
    state.popStack() // the instruction takes two arguments from the stack and then pushes the result
  }

  def callMethod(
    className: String,
    methodName: String,
    argumentCount: Int,
    methodDescriptor: String,
    state: MethodGeneratorState
  ): Unit = {
    state.methodVisitor.visitMethodInsn(INVOKEVIRTUAL, javaifyClass(className), methodName, methodDescriptor, false)
    state.popStack(1 + argumentCount)
  }

  def callStaticMethod(
    className: String,
    methodName: String,
    argumentCount: Int,
    methodDescriptor: String,
    state: MethodGeneratorState
  ): Unit = {
    state.methodVisitor.visitMethodInsn(INVOKESTATIC, javaifyClass(className), methodName, methodDescriptor, false)
    state.popStack(argumentCount)
  }

  def returnVoid(state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitInsn(RETURN)
  }

  def returnType(descriptor: Type, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitInsn(asmReturnCode(descriptor))
    state.popStack()
  }

  def newObject(className: String, state: MethodGeneratorState): Unit = {
    val javaifiedClass = javaifyClass(className)
    state.methodVisitor.visitTypeInsn(NEW, javaifiedClass)
    state.pushStack(UserType(javaifiedClass))
  }

  def callConstructor(className: String, parameterDescriptors: List[Type], state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitMethodInsn(
      INVOKESPECIAL,
      javaifyClass(className),
      "<init>",
      asmConstructorType(parameterDescriptors),
      false
    )
    state.popStack(1 + parameterDescriptors.size)
  }

  def newArray(arrayType: String, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitTypeInsn(ANEWARRAY, arrayType)
  }

  def accessArray(arrayType: Type, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitInsn(asmArrayLoadInsn(arrayType))
  }

  def storeArray(arrayType: Type, state: MethodGeneratorState): Unit = {
    state.methodVisitor.visitInsn(asmArrayStoreInsn(arrayType))
  }

  def callSuper(parentName: String, state: MethodGeneratorState): Unit = {
    state.pushStack(UserType(parentName))

    state.methodVisitor.visitVarInsn(ALOAD, 0) // load this
    state.methodVisitor.visitMethodInsn( // super()
      INVOKESPECIAL,
      javaifyClass(parentName),
      "<init>",
      "()V",
      false
    )

    state.popStack()
  }
}
