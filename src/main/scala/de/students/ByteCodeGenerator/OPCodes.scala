package de.students.ByteCodeGenerator

import org.objectweb.asm.Opcodes.*
import de.students.Parser.*

private def asmLoadInsn(t: Type): Int = try {
  stringToOpcode(typePrefix(t) + "LOAD")
} catch {
  case ByteCodeGeneratorException(e) => throw ByteCodeGeneratorException(f"type $t can not be fetched as variable: $e")
}

private def asmStoreInsn(t: Type): Int = try {
  stringToOpcode(typePrefix(t) + "STORE")
} catch {
  case ByteCodeGeneratorException(e) => throw ByteCodeGeneratorException(f"type $t can not be saved as variable: $e")
}

private def asmArrayLoadInsn(t: Type): Int = try {
  stringToOpcode(typePrefix(t) + "ALOAD")
} catch {
  case ByteCodeGeneratorException(e) => throw ByteCodeGeneratorException(f"type $t can not be fetched from array: $e")
}

private def asmArrayStoreInsn(t: Type): Int = try {
  stringToOpcode(typePrefix(t) + "ASTORE")
} catch {
  case ByteCodeGeneratorException(e) => throw ByteCodeGeneratorException(f"type $t can not be stored in array: $e")
}

private def asmReturnCode(t: Type): Int = try {
  stringToOpcode(typePrefix(t) + "RETURN")
} catch {
  case ByteCodeGeneratorException(e) => throw ByteCodeGeneratorException(f"type $t is not allowed as return type")
}

private def typePrefix(t: Type): String = t match {
  case IntType | CharType | ShortType | ByteType => "I"
  case BoolType                                  => "I" // TODO should there be a specific opcode?
  case LongType                                  => "L"
  case FloatType                                 => "F"
  case DoubleType                                => "D"
  case ArrayType(_) | UserType(_)                => "A"
  case _                                         => throw ByteCodeGeneratorException(f"the type $t is not allowed")
}

private def binaryOpName(op: String): String = op match {
  case "+" => "ADD"
  case "-" => "SUB"
  case "*" => "MUL"
  case "/" => "DIV"
  case "%" => "REM"
  case _   => throw ByteCodeGeneratorException(f"the operator \"$op\" is not allowed for binary operations")
}

private def binaryOpcode(op: String, t: Type): Int = stringToOpcode(typePrefix(t) + binaryOpName(op))

private def stringToOpcode(name: String): Int = name match {
  case "ILOAD" => ILOAD
  case "LLOAD" => LLOAD
  case "FLOAD" => FLOAD
  case "DLOAD" => DLOAD
  case "ALOAD" => ALOAD

  case "ISTORE" => ISTORE
  case "LSTORE" => LSTORE
  case "FSTORE" => FSTORE
  case "DSTORE" => DSTORE
  case "ASTORE" => ASTORE

  case "IALOAD" => IALOAD
  case "LALOAD" => LALOAD
  case "FALOAD" => FALOAD
  case "DALOAD" => DALOAD
  case "AALOAD" => AALOAD

  case "IASTORE" => IASTORE
  case "LASTORE" => LASTORE
  case "FASTORE" => FASTORE
  case "DASTORE" => DASTORE
  case "AASTORE" => AASTORE

  case "IRETURN" => IRETURN
  case "LRETURN" => LRETURN
  case "FRETURN" => FRETURN
  case "DRETURN" => DRETURN
  case "ARETURN" => ARETURN

  case "IADD" => IADD
  case "LADD" => LADD
  case "FADD" => FADD
  case "DADD" => DADD

  case "ISUB" => ISUB
  case "LSUB" => LSUB
  case "FSUB" => FSUB
  case "DSUB" => DSUB

  case "IMUL" => IMUL
  case "LMUL" => LMUL
  case "FMUL" => FMUL
  case "DMUL" => DMUL

  case "IDIV" => IDIV
  case "LDIV" => LDIV
  case "FDIV" => FDIV
  case "DDIV" => DDIV

  case "IREM" => IREM
  case "LREM" => LREM
  case "FREM" => FREM
  case "DREM" => DREM

  case _ => throw ByteCodeGeneratorException(f"opcode $name not recognized")
}
