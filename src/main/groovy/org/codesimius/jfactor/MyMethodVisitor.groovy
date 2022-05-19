package org.codesimius.jfactor

import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class MyMethodVisitor extends MethodVisitor implements Opcodes {

	int access
	String name
	String desc
	String signature
	String[] exceptions
	String declaringType
	Set<String> visitedFlds

	int counter
	Label firstLabel
	int formalCounter

	MyMethodVisitor(int access, String name, String desc, String signature, String[] exceptions, String declaringType, Set<String> visitedFlds) {
		super(ASM9)
		this.access = access
		this.name = name
		this.desc = desc
		this.signature = signature
		this.exceptions = exceptions
		this.declaringType = declaringType
		this.visitedFlds = visitedFlds
		//println "$access $name $desc $signature"
		counter = -1
		formalCounter = 0
		exceptions?.each {
			// Conf.instance.methodExceptions << "$name\t${it.replace("/", ".")}\n"
		}
	}


	void visitIincInsn(int var, int increment) {
		counter++
		bc(increment >= 0 ? "X-inc" : "X-dec", var)
		increment = Math.abs(increment)
		def type
		if (increment <= Byte.MAX_VALUE) type = "byte"
		else if (increment <= Short.MAX_VALUE) type = "short"
		else if (increment <= Integer.MAX_VALUE) type = "int"
		else if (increment <= Long.MAX_VALUE) type = "long"
		else throw new RuntimeException("weird size")
		emit(Conf.instance.incValues, [stmtID(counter), increment, type])
	}

	// NOP, ACONST_NULL,
	// DUP_X1, DUP_X2, DUP2, DUP2_X1, DUP2_X2, SWAP,
	// MONITORENTER, or MONITOREXIT.

	void visitInsn(int opcode) {
		counter++
		switch (opcode) {
			case ICONST_M1: bc("X-Bconst", "-1")
				break
			case ICONST_0: bc("X-Bconst", "0")
				break
			case ICONST_1: bc("X-Bconst", "1")
				break
			case ICONST_2: bc("X-Bconst", "2")
				break
			case ICONST_3: bc("X-Bconst", "3")
				break
			case ICONST_4: bc("X-Bconst", "4")
				break
			case ICONST_5: bc("X-Bconst", "5")
				break
			case LCONST_0: bc("X-Lconst", "0L")
				break
			case LCONST_1: bc("X-Lconst", "1L")
				break
			case FCONST_0: bc("X-Fconst", "0.0f")
				break
			case FCONST_1: bc("X-Fconst", "1.0f")
				break
			case FCONST_2: bc("X-Fconst", "2.0f")
				break
			case DCONST_0: bc("X-Dconst", "0.0")
				break
			case DCONST_1: bc("X-Dconst", "1.0")
				break
			case ACONST_NULL: bc("X-NULL", "NULL")
				break
			case I2L: bc("i2l")
				break
			case I2F: bc("i2f")
				break
			case I2D: bc("i2d")
				break
			case L2I: bc("l2i")
				break
			case L2F: bc("l2f")
				break
			case L2D: bc("l2d")
				break
			case F2I: bc("f2i")
				break
			case F2L: bc("f2l")
				break
			case F2D: bc("f2d")
				break
			case D2I: bc("d2i")
				break
			case D2L: bc("d2l")
				break
			case D2F: bc("d2f")
				break
			case I2B: bc("i2b")
				break
			case I2C: bc("i2c")
				break
			case I2S: bc("i2s")
				break
			case POP: bc("pop")
				break
			case POP2: wat(opcode)
				break
			case DUP: bc("dup")
				break
			case IADD:
			case LADD:
			case FADD:
			case DADD: bc("X-add")
				break
			case ISUB:
			case LSUB:
			case FSUB:
			case DSUB: bc("X-sub")
				break
			case IMUL:
			case LMUL:
			case FMUL:
			case DMUL: bc("X-mul")
				break
			case IDIV:
			case LDIV:
			case FDIV:
			case DDIV: bc("X-div")
				break
			case IREM:
			case LREM:
			case FREM:
			case DREM: bc("X-rem")
				break
			case INEG:
			case LNEG:
			case FNEG:
			case DNEG: bc("X-neg")
				break
			case IAND:
			case LAND: bc("X-band")
				break
			case IOR:
			case LOR: bc("X-bor")
				break
			case IXOR:
			case LXOR: bc("X-bxor")
				break
			case ISHL:
			case LSHL: bc("X-shl")
				break
			case ISHR:
			case LSHR: bc("X-shr")
				break
			case IUSHR:
			case LUSHR: bc("X-ushr")
				break
			case LCMP: bc("lcmp")
				break
			case FCMPL:
			case DCMPL: bc("cmpl") // -1 on NaN
				break
			case FCMPG:
			case DCMPG: bc("cmpg") // 1 on NaN
				break
			case IRETURN:
			case LRETURN:
			case FRETURN:
			case DRETURN:
			case ARETURN: bc("X-return")
				break
			case RETURN: bc("return")
				break
			case ATHROW: bc("athrow")
				break
			case ARRAYLENGTH: bc("arraylength")
				break
			case IALOAD:
			case LALOAD:
			case FALOAD:
			case DALOAD:
			case BALOAD:
			case CALOAD:
			case SALOAD:
			case AALOAD: bc("X-aload")
				break
			case IASTORE:
			case LASTORE:
			case FASTORE:
			case DASTORE:
			case BASTORE:
			case CASTORE:
			case SASTORE:
			case AASTORE: bc("X-astore")
				break
			default: wat(opcode)
		}
	}

	void visitVarInsn(int opcode, int var) {
		counter++
		switch (opcode) {
			case ILOAD:
			case LLOAD:
			case FLOAD:
			case DLOAD:
			case ALOAD: bc("X-load", var)
				break
			case ISTORE:
			case LSTORE:
			case FSTORE:
			case DSTORE:
			case ASTORE: bc("X-store", var)
				break
			case RET: wat(opcode) // Deprecated in Java 7
				break
			default: wat(opcode)
		}
	}

	void visitTypeInsn(int opcode, String type) {
		counter++
		type = type.replace("/", ".")
		switch (opcode) {
			case NEW:
				def heap = "${methID()}/new $type/$counter"
				emit(Conf.instance.allocTypes, [stmtID(counter), type])
				bc("new", heap)
				break
			case ANEWARRAY: wat(opcode)
				break
			case CHECKCAST: bc("checkcast", type)
				break
			case INSTANCEOF: wat(opcode)
				break
			default: wat(opcode)
		}
	}

	void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		counter++
		owner = owner.replace("/", ".")
		def sig = "$owner.${name}$descriptor"
		callInfo(sig, owner, name)
		switch (opcode) {
			case INVOKEVIRTUAL: bc("invokevirtual", sig)
				break
			case INVOKESPECIAL: bc(name == "<init>" ? "X-invokeinit" : "invokespecial", sig)
				break
			case INVOKESTATIC: bc("invokestatic", sig)
				break
			case INVOKEINTERFACE: bc("invokeinterface", sig)
				break
			default: wat(opcode)
		}
	}

	void visitJumpInsn(int opcode, Label label) {
		counter++
		switch (opcode) {
			case IFEQ: bc("ifeq", label)
				break
			case IFNE: bc("ifne", label)
				break
			case IFLT: bc("iflt", label)
				break
			case IFGE: bc("ifge", label)
				break
			case IFGT: bc("ifgt", label)
				break
			case IFLE: bc("ifle", label)
				break
			case IF_ICMPEQ: bc("if_icmpeq", label)
				break
			case IF_ICMPNE: bc("if_icmpne", label)
				break
			case IF_ICMPLT: bc("if_icmplt", label)
				break
			case IF_ICMPGE: bc("if_icmpge", label)
				break
			case IF_ICMPGT: bc("if_icmpgt", label)
				break
			case IF_ICMPLE: bc("if_icmple", label)
				break
			case IF_ACMPEQ: bc("if_acmpeq", label)
				break
			case IF_ACMPNE: bc("if_acmpne", label)
				break
			case IFNULL: bc("ifnull", label)
				break
			case IFNONNULL: bc("ifnonnull", label)
				break
			case GOTO: bc("goto", label)
				break
			case JSR: wat(opcode) // Deprecated in Java 7
				break
			default: wat(opcode)
		}
	}

	void visitLdcInsn(Object value) {
		counter++
		if (value instanceof Byte)
			bc("X-Bconst", value)
		else if (value instanceof Character)
			bc("X-Cconst", value)
		else if (value instanceof Float)
			bc("X-Fconst", value)
		else if (value instanceof Double)
			bc("X-Dconst", value)
		else if (value instanceof Integer)
			bc("X-Iconst", value)
		else if (value instanceof Long)
			bc("X-Lconst", value)
		else if (value instanceof Short)
			bc("X-Sconst", value)
		else if (value instanceof String) {
			value = "\"${value.replaceAll("\t", "\\\\t").replaceAll("\"", "\\\\\"")}\""
			bc("X-String", value)
		} else
			throw new RuntimeException("Should handle type: " + value.class)
	}

	void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		counter++
		owner = owner.replace("/", ".")
		def (type, rest) = typeFromJVM(descriptor)
		def fld = "<$owner: $type $name>" as String
		if (fld !in visitedFlds) {
			// Conf.instance.fields << "$fld\t$type\t$name\t$owner\n"
			visitedFlds << fld
		}
		switch (opcode) {
			case GETSTATIC: bc("getstatic", fld)
				break
			case PUTSTATIC: bc("putstatic", fld)
				break
			case GETFIELD: bc("getfield", fld)
				break
			case PUTFIELD: bc("putfield", fld)
				break
			default: wat(opcode)
		}
	}

	void visitIntInsn(int opcode, int operand) {
		counter++
		switch (opcode) {
			case BIPUSH: bc("X-Bconst", operand)
				break
			case SIPUSH: bc("X-Sconst", operand)
				break
			case NEWARRAY:
				def type = toPrimitiveType(operand)
				def heap = "${methID()}/new[] $type/$counter"
				emit(Conf.instance.allocTypes, [stmtID(counter), type])
				emit(Conf.instance.elemTypes, ["$type[]", type])
				bc("newarray", heap)
				break
			default: wat(opcode)
		}
	}

	void visitLabel(Label label) {
		if (!firstLabel) firstLabel = label
		emit(Conf.instance.labels, [methID(), label, stmtID(counter + 1), counter + 1])
	}

	void visitLineNumber(int line, Label start) {
		// Conf.instance.lineNumbers << "${methID()}\t${stmtID(counter + 1)}\t$line\n"
	}

	void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
		def (type, rest) = typeFromJVM(descriptor)
		emit(Conf.instance.vars, [methID(), index, varID(name), name, type, start, end])
		if (start == firstLabel) {
			def pos = name == "this" ? -1 : formalCounter++
			// Conf.instance.formals << "${methID()}\t$pos\t${varID(name)}\n"
		}
	}

	void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		def finalType = type ? type.replace("/", ".") : "java.lang.Throwable"
		// Conf.instance.handlers << "${methID()}\t$start\t$end\t$handler\t$finalType\n"
	}


	def methID() { "<${declaringType} ${name}$desc>" }

	def stmtID(def index) { "${methID()}/BC/$index" }

	def varID(def name) { "${methID()}/$name" }

	static String toPrimitiveType(int operand) {
		switch (operand) {
			case T_BOOLEAN: return "boolean"
			case T_BYTE: return "byte"
			case T_CHAR: return "char"
			case T_DOUBLE: return "double"
			case T_FLOAT: return "float"
			case T_INT: return "int"
			case T_LONG: return "long"
			case T_SHORT: return "short"
			default: return NULL
		}
	}

	static List typeFromJVM(String str) {
		switch (str[0]) {
			case 'B': return ["byte", str.drop(1)]
			case 'C': return ["char", str.drop(1)]
			case 'D': return ["double", str.drop(1)]
			case 'F': return ["float", str.drop(1)]
			case 'I': return ["int", str.drop(1)]
			case 'J': return ["long", str.drop(1)]
			case 'S': return ["short", str.drop(1)]
			case 'Z': return ["boolean", str.drop(1)]
			case 'V': return ["void", str.drop(1)]
			case 'L':
				def end = str.indexOf(";")
				return [str[1..end - 1].replace("/", "."), str.drop(end + 1)]
			case '[':
				def (type, rest) = typeFromJVM(str.drop(1))
				return ["[$type", rest]
			default: return ["", str]
		}
	}

	static void callInfo(String sig, String owner, String name) {
		int argc = 0
		def origSig = sig
		sig = sig[(sig.indexOf("(") + 1)..-1]
		while (true) {
			if (sig[0] == ")") break
			def (type, rest) = typeFromJVM(sig)
			argc++
			sig = rest
		}
		def (retType, rest) = typeFromJVM(sig.drop(1))
		emit(Conf.instance.invocations, [origSig, argc, retType, "$owner::$name"])
	}

	def wat(int opcode, boolean thr = true) {
		bc(Integer.toHexString(opcode), "??")
		if (thr) throw new RuntimeException(Integer.toHexString(opcode) + "??")
	}

	void bc(String opcode, def oper = "_") {
		emit(Conf.instance.bytecodes, [methID(), counter, stmtID(counter), opcode, oper])
	}

	static def emit(File f, ArrayList args) { f << args.join(Conf.instance.DELIM) << "\n" }
}
