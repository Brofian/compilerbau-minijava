package de.students.ByteCodeGenerator

import org.objectweb.asm.Opcodes.*
import de.students.Parser.*
import de.students.util.Logger
import org.objectweb.asm.MethodVisitor

private def visibilityModifier(classDecl: ClassDecl): Int = 0 // ACC_PUBLIC
private def visibilityModifier(varDecl: VarDecl): Int = 0 // ACC_PUBLIC
private def visibilityModifier(methodDecl: MethodDecl): Int = {
  0
    + (if methodDecl.static then ACC_STATIC else 0)
    + ACC_PUBLIC
}

private def javaifyClass(fullName: String) = fullName.replace('.', '/')

private def asmType(t: Type): String = t match {
  case NoneType => "" // TODO find out descriptor of NoneType
  case IntType => "I"
  case BoolType => "Z"
  case VoidType => "V"
  case ArrayType(baseType) => f"[${asmType(baseType)}"
  case UserType(name) => {
      f"L${javaifyClass(name)};"
  }
  case FunctionType(returnType, parameterTypes) => {
    val parameters = parameterTypes.map(asmType).fold("")((a ,b) => a + b)
    f"($parameters)${asmType(returnType)}"
  }
}

private def asmConstructorType(parameters: List[Type]): String = {
  asmType(FunctionType(VoidType, parameters))
}

private def functionType(methodDecl: MethodDecl): FunctionType =
  FunctionType(methodDecl.returnType, methodDecl.params.map(varDecl => varDecl.varType))

private def constructorType(constructorDecl: ConstructorDecl): FunctionType =
  FunctionType(VoidType, constructorDecl.params.map(param => param.varType))

private def binaryOpcode(op: String, t: Type): String = {
  val prefix = t match {
    case IntType => "I"
    case BoolType => "I" // NOTE: this should be Z
    case _ => throw ByteCodeGeneratorException(f"the type $t is not allowed")
  }
  val opName = op match {
    case "+" => "ADD"
    case "-" => "SUB"
    case "*" => "MUL"
    case "/" => "DIV"
    case "%" => "REM"
    case _ => throw ByteCodeGeneratorException(f"the operator \"$op\" is not allowed for binary operations")
  }
  prefix + opName
}
private def asmOpcode(opName: String): Int = opName match {
  case "IADD" => IADD
  case "ISUB" => ISUB
  case "IMUL" => IMUL
  case "IDIV" => IDIV
  case "IREM" => IREM
  case _ => throw NotImplementedError("asm opcode")
}

private def isBooleanOpcode(op: String): Boolean =
  op == "==" ||
  op == "!=" ||
  op == "<" ||
  op == "<=" ||
  op == ">" ||
  op == ">=" ||
  op == "&&" ||
  op == "||"

private def asmLoadInsn(t: Type): Int = t match {
  case IntType => ILOAD
  case BoolType => ILOAD
  case ArrayType(baseType) => ALOAD
  case UserType(name) => ALOAD
  case _ => throw ByteCodeGeneratorException(f"type ${asmType(t)} can not be fetched as variable")
}
private def asmStoreInsn(t: Type): Int = t match {
  case IntType => ISTORE
  case BoolType => ISTORE
  case ArrayType(baseType) => ASTORE
  case UserType(name) => ASTORE
  case _ => throw ByteCodeGeneratorException(f"type ${asmType(t)} can not be saved as variable")
}

private def asmReturnCode(t: Type): Int = t match {
  case IntType => IRETURN
  case BoolType => IRETURN
  case ArrayType(baseType) => ARETURN
  case UserType(name) => ARETURN
  case _ => throw ByteCodeGeneratorException(f"return type \"$t\" is not allowed")
}

private def makePrintStatement(toPrint: Expression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
  val t = generateTypedExpression(toPrint.asInstanceOf[TypedExpression], state)
  state.stackDepth += 1
  methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", f"(${asmType(t)})V", false)
}

private def debugLogStack(state: MethodGeneratorState, where: String): Unit = {
  Logger.debug(f"stack size ${state.stackDepth} | max stack size ${state.maxStackDepth} | $where")
}