package de.students.ByteCodeGenerator

import org.objectweb.asm.Opcodes.*
import de.students.Parser.*
import de.students.util.Logger
import org.objectweb.asm.MethodVisitor

private def asmFinalModifier(isFinal: Boolean): Int = if isFinal then ACC_FINAL else 0
private def asmAbstractModifier(isFinal: Boolean): Int = if isFinal then ACC_ABSTRACT else 0

private def accessModifier(classDecl: ClassDecl): Int = asmAbstractModifier(classDecl.isAbstract)

private def accessModifier(varDecl: VarDecl): Int = 0

private def accessModifier(fieldDecl: FieldDecl): Int =
  asmFinalModifier(fieldDecl.isFinal)
    + (fieldDecl.accessModifier match {
      case None              => 0
      case Some("public")    => ACC_PUBLIC
      case Some("private")   => ACC_PRIVATE
      case Some("protected") => ACC_PROTECTED
      case Some(other)       => throw ByteCodeGeneratorException(f"access modifier $other is not recognized")
    })

private def visibilityModifier(methodDecl: MethodDecl): Int = {
  0
    + (if methodDecl.static then ACC_STATIC else 0)
    + (if methodDecl.isFinal then ACC_FINAL else 0)
    + (methodDecl.accessModifier match {
      case None              => 0
      case Some("public")    => ACC_PUBLIC
      case Some("private")   => ACC_PRIVATE
      case Some("protected") => ACC_PROTECTED
      case Some(other)       => throw ByteCodeGeneratorException(f"access modifier $other is not recognized")
    })
}

private def typeStackSize(t: Type): Int = t match {
  case IntType      => 1
  case BoolType     => 1
  case ShortType    => 1
  case LongType     => 2
  case ByteType     => 1
  case FloatType    => 1
  case DoubleType   => 2
  case CharType     => 1
  case ArrayType(_) => 1
  case UserType(_)  => 1
  case _            => throw ByteCodeGeneratorException(f"type $t can't be pushed onto the stack")
}

private def javaifyClass(fullName: String) = makeObjectClassName(fullName.replace('.', '/'))
private def makeObjectClassName(name: String) =
  if name.contains("Object") then "java/lang/Object" else name // TODO temporary, make issue for type check

def javaSignature(t: Type): String = t match {
  case IntType             => "I"
  case BoolType            => "Z"
  case VoidType            => "V"
  case ShortType           => "S"
  case LongType            => "L"
  case ByteType            => "B"
  case FloatType           => "F"
  case DoubleType          => "D"
  case CharType            => "C"
  case ArrayType(baseType) => f"[${javaSignature(baseType)}"
  case UserType(name) => {
    f"L${javaifyClass(name)};"
  }
  case FunctionType(returnType, parameterTypes) => {
    val parameters = parameterTypes.map(javaSignature).fold("")((a, b) => a + b)
    f"($parameters)${javaSignature(returnType)}"
  }
  case _ => throw ByteCodeGeneratorException(s"Unknown type $t cannot be converted to ASM-type")
}

private def asmUserType(t: Type): String = t match {
  case UserType(name) => javaifyClass(name)
  case _              => throw ByteCodeGeneratorException(f"$t is no user defined type")
}

private def asmConstructorType(parameters: List[Type]): String = {
  javaSignature(FunctionType(VoidType, parameters))
}

private def functionType(methodDecl: MethodDecl): FunctionType =
  FunctionType(methodDecl.returnType, methodDecl.params.map(varDecl => varDecl.varType))

private def constructorType(constructorDecl: ConstructorDecl): FunctionType =
  FunctionType(VoidType, constructorDecl.params.map(param => param.varType))

private def isBooleanOpcode(op: String): Boolean = {
  op == "==" ||
  op == "!=" ||
  op == "<" ||
  op == "<=" ||
  op == ">" ||
  op == ">=" ||
  op == "&&" ||
  op == "||"
}

private def makePrintStatement(toPrint: Expression, methodVisitor: MethodVisitor, state: MethodGeneratorState): Unit = {
  methodVisitor.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
  val t = generateTypedExpression(toPrint.asInstanceOf[TypedExpression], state)
  state.stackDepth += 1
  methodVisitor.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", f"(${javaSignature(t)})V", false)
}

private def debugLogStack(state: MethodGeneratorState, where: String): Unit = {
  // Logger.debug(f"stack size ${state.stackDepth} | max stack size ${state.maxStackDepth} | $where")
  Logger.debug(f"s ${state.stackDepth} | smax ${state.maxStackDepth} | ${state.stackTypes} | $where")
}
private def stringifyExpression(expr: Expression): String = expr match {
  case VarRef(name)                        => f"$name"
  case Literal(value)                      => f"$value"
  case BinaryOp(left, op, right)           => f"${stringifyExpression(left)} $op ${stringifyExpression(right)}"
  case ThisAccess(name)                    => f"this.$name"
  case MemberAccess(className, memberName) => f"$className.$memberName"
  case NewObject(className, arguments) =>
    f"new $className(${arguments.foldLeft("")((acc, e) => acc + f", ${stringifyExpression(e)}")})"
  case NewArray(arrayType, dimensions) => f"new $arrayType[$dimensions]"
  case ArrayAccess(array, index)       => f"$array[$index]"
  case MethodCall(target, methodName, args, isStatic) =>
    f"${stringifyExpression(target)}.$methodName(${args.foldLeft("")((acc, e) => acc + f", ${stringifyExpression(e)}")})"
  case TypedExpression(expr, exprType) => stringifyExpression(expr)
  case StaticClassRef(name)            => f"$name<static>"
  case UnaryOp(op, expr)               => f"$op ${stringifyExpression(expr)}"

}
