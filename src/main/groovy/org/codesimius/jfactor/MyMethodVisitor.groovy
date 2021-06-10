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
			Database.instance.methodExceptions << "$name\t${it.replace("/", ".")}\n"
		}
	}

	void visitIincInsn(int var, int increment) {
		counter++
		rec(increment >= 0 ? "X-inc" : "X-dec", var)
		increment = Math.abs(increment)
		def type
		if (increment <= Byte.MAX_VALUE) type = "byte"
		else if (increment <= Short.MAX_VALUE) type = "short"
		else if (increment <= Integer.MAX_VALUE) type = "int"
		else if (increment <= Long.MAX_VALUE) type = "long"
		else throw new RuntimeException("weird size")
		Database.instance.incValues << "${stmtID(counter)}\t$increment\t$type\n"
	}

	// NOP, ACONST_NULL,
	// IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD,
	// SALOAD, IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE,
	// DUP_X1, DUP_X2, DUP2, DUP2_X1, DUP2_X2, SWAP,
	// ARRAYLENGTH,
	// MONITORENTER, or MONITOREXIT.

	void visitInsn(int opcode) {
		counter++
		switch (opcode) {
			case ICONST_M1: rec("X-Bconst", "-1")
				break
			case ICONST_0: rec("X-Bconst", "0")
				break
			case ICONST_1: rec("X-Bconst", "1")
				break
			case ICONST_2: rec("X-Bconst", "2")
				break
			case ICONST_3: rec("X-Bconst", "3")
				break
			case ICONST_4: rec("X-Bconst", "4")
				break
			case ICONST_5: rec("X-Bconst", "5")
				break
			case LCONST_0: rec("X-Lconst", "0L")
				break
			case LCONST_1: rec("X-Jconst", "1L")
				break
			case FCONST_0: rec("X-Jconst", "0.0f")
				break
			case FCONST_1: rec("X-Fconst", "1.0f")
				break
			case FCONST_2: rec("X-Fconst", "2.0f")
				break
			case DCONST_0: rec("X-Dconst", "0.0")
				break
			case DCONST_1: rec("X-Dconst", "1.0")
				break
			case ACONST_NULL: rec("X-Lconst", "NULL")
				break
			case I2L: rec("i2l")
				break
			case I2F: rec("i2f")
				break
			case I2D: rec("i2d")
				break
			case L2I: rec("l2i")
				break
			case L2F: rec("l2f")
				break
			case L2D: rec("l2d")
				break
			case F2I: rec("f2i")
				break
			case F2L: rec("f2l")
				break
			case F2D: rec("f2d")
				break
			case D2I: rec("d2i")
				break
			case D2L: rec("d2l")
				break
			case D2F: rec("d2f")
				break
			case I2B: rec("i2b")
				break
			case I2C: rec("i2c")
				break
			case I2S: rec("i2s")
				break
			case POP: rec("pop")
				break
			case POP2: wat(opcode)
				break
			case DUP: rec("dup")
				break
			case IADD:
			case LADD:
			case FADD:
			case DADD: rec("X-add")
				break
			case ISUB:
			case LSUB:
			case FSUB:
			case DSUB: rec("X-sub")
				break
			case IMUL:
			case LMUL:
			case FMUL:
			case DMUL: rec("X-mul")
				break
			case IDIV:
			case LDIV:
			case FDIV:
			case DDIV: rec("X-div")
				break
			case IREM:
			case LREM:
			case FREM:
			case DREM: rec("X-rem")
				break
			case INEG:
			case LNEG:
			case FNEG:
			case DNEG: rec("X-neg")
				break
			case IAND:
			case LAND: rec("X-band")
				break
			case IOR:
			case LOR: rec("X-bor")
				break
			case IXOR:
			case LXOR: rec("X-bxor")
				break
			case ISHL:
			case LSHL: rec("X-shl")
				break
			case ISHR:
			case LSHR: rec("X-shr")
				break
			case IUSHR:
			case LUSHR: rec("X-ushr")
				break
			case LCMP: rec("lcmp")
				break
			case FCMPL:
			case DCMPL: rec("cmpl") // -1 on NaN
				break
			case FCMPG:
			case DCMPG: rec("cmpg") // 1 on NaN
				break
			case IRETURN:
			case LRETURN:
			case FRETURN:
			case DRETURN:
			case ARETURN: rec("X-return")
				break
			case RETURN: rec("return")
				break
			case ATHROW: rec("athrow")
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
			case ALOAD: rec("X-load", var)
				break
			case ISTORE:
			case LSTORE:
			case FSTORE:
			case DSTORE:
			case ASTORE: rec("X-store", var)
				break
			case RET: wat(opcode) // Deprecated in Java 7
				break
			default: wat(opcode)
		}
	}

	void visitTypeInsn(int opcode, String type) {
		counter++
		switch (opcode) {
			case NEW:
				def heap = "${methID()}/new $type/$counter"
				Database.instance.allocTypes << "${stmtID(counter)}\t$type\n"
				rec("new", heap)
				break
			case ANEWARRAY:
				throw new RuntimeException()
			case CHECKCAST:
				throw new RuntimeException()
			case INSTANCEOF:
				throw new RuntimeException()
			default: wat(opcode)
		}
	}

	void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		counter++
		def sig = "${owner.replace("/", ".")}.${name}$descriptor"
		callInfo(sig, owner, name)
		switch (opcode) {
			case INVOKEVIRTUAL: rec("invokevirtual", sig)
				break
			case INVOKESPECIAL: rec(name == "<init>" ? "X-invokeinit" : "invokespecial", sig)
				break
			case INVOKESTATIC: rec("invokestatic", sig)
				break
			case INVOKEINTERFACE: rec("invokeinterface", sig)
				break
			default: wat(opcode)
		}
	}

	void visitJumpInsn(int opcode, Label label) {
		counter++
		switch (opcode) {
			case IFEQ: rec("ifeq", label)
				break
			case IFNE: rec("ifne", label)
				break
			case IFLT: rec("iflt", label)
				break
			case IFGE: rec("ifge", label)
				break
			case IFGT: rec("ifgt", label)
				break
			case IFLE: rec("ifle", label)
				break
			case IF_ICMPEQ: rec("if_icmpeq", label)
				break
			case IF_ICMPNE: rec("if_icmpne", label)
				break
			case IF_ICMPLT: rec("if_icmplt", label)
				break
			case IF_ICMPGE: rec("if_icmpge", label)
				break
			case IF_ICMPGT: rec("if_icmpgt", label)
				break
			case IF_ICMPLE: rec("if_icmple", label)
				break
			case IF_ACMPEQ: rec("if_acmpeq", label)
				break
			case IF_ACMPNE: rec("if_acmpne", label)
				break
			case IFNULL: rec("ifnull", label)
				break
			case IFNONNULL: rec("ifnonnull", label)
				break
			case GOTO: rec("goto", label)
				break
			case JSR: wat(opcode) // Deprecated in Java 7
				break
			default: wat(opcode)
		}
	}

	void visitLdcInsn(Object value) {
		counter++
		if (value instanceof Byte)
			rec("X-Bconst", value)
		else if (value instanceof Character)
			rec("X-Cconst", value)
		else if (value instanceof Float)
			rec("X-Fconst", value)
		else if (value instanceof Double)
			rec("X-Dconst", value)
		else if (value instanceof Integer)
			rec("X-Iconst", value)
		else if (value instanceof Long)
			rec("X-Jconst", value)
		else if (value instanceof Short)
			rec("X-Sconst", value)
		else if (value instanceof String) {
			value = "\"${value.replaceAll("\t", "\\\\t").replaceAll("\"", "\\\\\"")}\""
			rec("X-Lconst", value)
		} else
			throw new RuntimeException("Should handle type: " + value.class)
	}

	void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		counter++
		owner = owner.replace("/", ".")
		def (type, rest) = typeFromJVM(descriptor)
		def fld = "<$owner: $type $name>" as String
		if (fld !in visitedFlds) {
			Database.instance.fields << "$fld\t$type\t$name\t$owner\n"
			visitedFlds << fld
		}
		switch (opcode) {
			case GETSTATIC: rec("getstatic", fld)
				break
			case PUTSTATIC: rec("putstatic", fld)
				break
			case GETFIELD: rec("getfield", fld)
				break
			case PUTFIELD: rec("putfield", fld)
				break
			default: wat(opcode)
		}
	}

	void visitIntInsn(int opcode, int operand) {
		counter++
		switch (opcode) {
			case BIPUSH: rec("X-Bconst", operand)
				break
			case SIPUSH: rec("X-Sconst", operand)
				break
			case NEWARRAY: wat(opcode)
				break
			default: wat(opcode)
		}
	}

	void visitLabel(Label label) {
		if (!firstLabel) firstLabel = label
		Database.instance.labels << "${methID()}\t$label\t${stmtID(counter + 1)}\t${counter + 1}\n"
	}

	void visitLineNumber(int line, Label start) {
		Database.instance.lineNumbers << "${methID()}\t${stmtID(counter + 1)}\t$line\n"
	}

	void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
		def (type, rest) = typeFromJVM(descriptor)
		Database.instance.vars << "${methID()}\t$index\t${varID(name)}\t$name\t$type\t$start\t$end\n"
		if (start == firstLabel) {
			def pos = name == "this" ? -1 : formalCounter++
			Database.instance.formals << "${methID()}\t$pos\t${varID(name)}\n"
		}
	}

	void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
		def finalType = type ? type.replace("/", ".") : "java.lang.Throwable"
		Database.instance.handlers << "${methID()}\t$start\t$end\t$handler\t$finalType\n"
	}


	void rec(def opcode, def oper = "_") {
		Database.instance.opcodes << "${stmtID(counter)}\t$opcode\t$oper\n"
		Database.instance.stmts << "${stmtID(counter)}\t${methID()}\t$counter\n"
	}

	def methID() { "<${declaringType} ${name}$desc>" }

	def stmtID(def index) { "${methID()}/BC/$index" }

	def varID(def name) { "${methID()}/$name" }

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
				return [str[1..end-1].replace("/", "."), str.drop(end+1)]
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
		Database.instance.invocations << "$origSig\t$argc\t$retType\t$owner::$name\n"
	}

	def wat(int opcode, boolean thr = true) {
		rec(Integer.toHexString(opcode), "??")
		if (thr) throw new RuntimeException(Integer.toHexString(opcode) + "??")
	}
}
