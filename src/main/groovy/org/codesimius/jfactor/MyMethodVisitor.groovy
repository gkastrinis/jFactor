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
	int counter

	Label firstLabel
	int formalCounter

	MyMethodVisitor(int access, String name, String desc, String signature, String[] exceptions, String declaringType) {
		super(ASM9)
		this.access = access
		this.name = name
		this.desc = desc
		this.signature = signature
		this.exceptions = exceptions
		this.declaringType = declaringType
		println "$access $name $desc $signature"
		counter = -1
		formalCounter = 0
	}

	// NOP, ACONST_NULL,
	// IALOAD, LALOAD, FALOAD, DALOAD, AALOAD, BALOAD, CALOAD,
	// SALOAD, IASTORE, LASTORE, FASTORE, DASTORE, AASTORE, BASTORE, CASTORE, SASTORE,
	// DUP_X1, DUP_X2, DUP2, DUP2_X1, DUP2_X2, SWAP,
	// I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F, I2B, I2C, I2S,
	// ARRAYLENGTH, ATHROW,
	// MONITORENTER, or MONITOREXIT.

	void visitInsn(int opcode) {
		counter++
		switch (opcode) {
			case ICONST_M1: rec("X-const", "-1")
				break
			case ICONST_0: rec("X-const", "0")
				break
			case ICONST_1: rec("X-const", "1")
				break
			case ICONST_2: rec("X-const", "2")
				break
			case ICONST_3: rec("X-const", "3")
				break
			case ICONST_4: rec("X-const", "4")
				break
			case ICONST_5: rec("X-const", "5")
				break
			case LCONST_0: rec("X-const", "0L")
				break
			case LCONST_1: rec("X-const", "1L")
				break
			case FCONST_0: rec("X-const", "0.0f")
				break
			case FCONST_1: rec("X-const", "1.0f")
				break
			case FCONST_2: rec("X-const", "2.0f")
				break
			case DCONST_0: rec("X-const", "0.0")
				break
			case DCONST_1: rec("X-const", "1.0")
				break
			case ACONST_NULL: rec("X-const", "NULL")
				break
			case I2D: rec("i2d")
				break
			case POP: rec("pop")
				break
			case POP2: rec("pop2")
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
			case RETURN: rec("X-return-void")
				break
			default: throw new RuntimeException(Integer.toHexString(opcode) )
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
			case RET:
				throw new RuntimeException()
			default: throw new RuntimeException()
		}
	}

	void visitTypeInsn(int opcode, String type) {
		counter++
		switch (opcode) {
			case NEW:
				def heap = "${methID()}/new $type/$counter"
				Database.instance.allocTypes << "${methID()}\t$counter\t$type\n"
				rec("new", heap)
				break
			case ANEWARRAY:
				throw new RuntimeException()
			case CHECKCAST:
				throw new RuntimeException()
			case INSTANCEOF:
				throw new RuntimeException()
			default: rec(opcode, "??")
				break
		}
	}

	void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		counter++
		def sig = "${owner.replace("/", ".")}.${name}$descriptor"
		switch (opcode) {
			case INVOKEVIRTUAL:
				callInfo(sig)
				rec("invokevirtual", sig)
				break
			case INVOKESPECIAL:
				callInfo(sig)
				rec(name == "<init>" ? "X-invokeinit" : "invokespecial", sig)
				break
			case INVOKESTATIC:
				callInfo(sig)
				rec("invokestatic", sig)
				break
			case INVOKEINTERFACE:
				throw new RuntimeException()
			default: rec(opcode, "??")
				break
		}
	}


	// JSR,

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
			default:
				throw new RuntimeException(Integer.toHexString(opcode) )
		}
	}

	void visitLdcInsn(Object value) {
		counter++
		rec("ldc", value)
	}

	void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		counter++
		switch (opcode) {
			case GETSTATIC: rec("getstatic", "<$owner: $descriptor $name>")
				break
			case PUTSTATIC:
				throw new RuntimeException()
			case GETFIELD:
				rec(opcode, "??")
				break//throw new RuntimeException()
			case PUTFIELD: rec("putfield", "<$owner: $descriptor $name>")
				break
			default: rec(opcode, "??")
				break
		}
	}

	void visitIntInsn(int opcode, int operand) {
		counter++
		switch (opcode) {
			case BIPUSH: rec("bipush", operand)
				break
			case SIPUSH: rec("sipush", operand)
				break
			case NEWARRAY:
				throw new RuntimeException()
			default: rec(opcode, "??")
				break
		}
	}

	void visitLabel(Label label) {
		if (!firstLabel) firstLabel = label
		Database.instance.labels << "${methID()}\t$label\t${counter + 1}\n"
//		Database.instance.opcodes << "${methID()}\t${counter + 1}\t--LABEL--\t$label\n"
	}

	void visitLineNumber(int line, Label start) {
//		println "----- $line ($start)"
	}

	void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
//		println "$index Local $descriptor $name ($signature), start: $start, end: $end"
		def (type, rest) = typeFromJVM(descriptor)
		Database.instance.vars << "${methID()}\t$index\t${varID(name)}\t$name\t$type\n"
		if (name != "this" && start == firstLabel)
			Database.instance.formals << "${methID()}\t${formalCounter++}\t${varID(name)}\n"
	}

	void rec(def opcode, def oper = "_") {
		Database.instance.opcodes << "${methID()}\t$counter\t$opcode\t$oper\n"
	}

	def methID() { "<${declaringType} ${name}$desc>" }

	def varID(def name) { "${methID()}/$name" }

	static def typeFromJVM(String str) {
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

	static void callInfo(String sig) {
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
		Database.instance.invocations << "$origSig\t$argc\t$retType\n"
	}
}
