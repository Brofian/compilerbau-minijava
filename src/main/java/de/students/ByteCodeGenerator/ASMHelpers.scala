package de.students.ByteCodeGenerator

import org.objectweb.asm.Opcodes.*

import de.students.Parser.*

private def visibilityModifier(classDecl: ClassDecl): Int = 0 // ACC_PUBLIC
private def visibilityModifier(varDecl: VarDecl): Int = 0 // ACC_PUBLIC
private def visibilityModifier(methodDecl: MethodDecl): Int = {
  if methodDecl.name == "main" then ACC_PUBLIC + ACC_STATIC else 0
}

private def asmType(t: Type): String = t match {
  case NoneType => "" // TODO find out descriptor of NoneType
  case IntType => "I"
  case BoolType => "Z"
  case VoidType => "V"
  case ArrayType(baseType) => f"[${asmType(baseType)}"
  case UserType(name) => f"L$name;"
  case FunctionType(returnType, parameterTypes) => {
    val parameters = parameterTypes.map(asmType).fold("")((a ,b) => a + b)
    f"(${parameters})${asmType(returnType)}"
  }
}

private def functionType(methodDecl: MethodDecl): FunctionType =
  FunctionType(methodDecl.returnType, methodDecl.params.map(varDecl => varDecl.varType))

private enum Operation {
  case Add, Sub, Mul, Div, Rem // TODO the rest
}

private def stringToOperation(op: String): Operation = op match {
  case "+" => Operation.Add
  case "-" => Operation.Sub
  case "*" => Operation.Mul
  case "/" => Operation.Div
  case "%" => Operation.Rem
  case _ => throw NotImplementedError("rest of operations is not yet implemented")
}
private def binaryOpcode(op: Operation, t: Type): String = {
  val prefix = t match {
    case IntType => "I"
    case _ => throw RuntimeException("this type is not allowed")
  }
  val opName = op match {
    case Operation.Add => "ADD"
    case Operation.Sub => "SUB"
    case Operation.Mul => "MUL"
    case Operation.Div => "DIV"
    case Operation.Rem => "REM"
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

private def asmReturnCode(t: Type): Int = t match {
  case IntType => IRETURN
  case BoolType => IRETURN
  case ArrayType(baseType) => ARETURN
  case UserType(name) => ARETURN
  case _ => throw RuntimeException(f"return type \"$t\" is not allowed")
}
