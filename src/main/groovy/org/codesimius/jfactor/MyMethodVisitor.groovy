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
	// INEG, LNEG, FNEG, DNEG,
	// ISHL, LSHL, ISHR, LSHR,
	// IUSHR, LUSHR,
	// I2L, I2F, I2D, L2I, L2F, L2D, F2I, F2L, F2D, D2I, D2L, D2F, I2B, I2C, I2S,
	// LCMP, FCMPL, FCMPG, DCMPL, DCMPG,
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
			case I2D: rec("i2d")
				break
			case POP: rec("pop")
				break
			case POP2: rec("pop2")
				break
			case DUP: rec("dup")
				break
			case IADD: rec("X-add")
				break
			case LADD: rec("X-add")
				break
			case FADD: rec("X-add")
				break
			case DADD: rec("X-add")
				break
			case ISUB: rec("X-sub")
				break
			case LSUB: rec("X-sub")
				break
			case FSUB: rec("X-sub")
				break
			case DSUB: rec("X-sub")
				break
			case IMUL: rec("X-mul")
				break
			case LMUL: rec("X-mul")
				break
			case FMUL: rec("X-mul")
				break
			case DMUL: rec("X-mul")
				break
			case IDIV: rec("X-div")
				break
			case LDIV: rec("X-div")
				break
			case FDIV: rec("X-div")
				break
			case DDIV: rec("X-div")
				break
			case IREM: rec("X-rem")
				break
			case LREM: rec("X-rem")
				break
			case FREM: rec("X-rem")
				break
			case DREM: rec("X-rem")
				break
			case IAND: rec("X-band")
				break
			case LAND: rec("X-band")
				break
			case IOR: rec("X-bor")
				break
			case LOR: rec("X-bor")
				break
			case IXOR: rec("X-bxor")
				break
			case LXOR: rec("X-bxor")
				break
			case IRETURN: rec("X-return")
				break
			case LRETURN: rec("X-return")
				break
			case FRETURN: rec("X-return")
				break
			case DRETURN: rec("X-return")
				break
			case ARETURN: rec("X-return")
				break
			case RETURN: rec("X-return-void")
				break
			default: throw new RuntimeException()
		}
	}

	void visitVarInsn(int opcode, int var) {
		counter++
		switch (opcode) {
			case ILOAD: rec("X-load", var)
				break
			case LLOAD: rec("X-load", var)
				break
			case FLOAD: rec("X-load", var)
				break
			case DLOAD: rec("X-load", var)
				break
			case ALOAD: rec("X-load", var)
				break
			case ISTORE: rec("X-store", var)
				break
			case LSTORE: rec("X-store", var)
				break
			case FSTORE: rec("X-store", var)
				break
			case DSTORE: rec("X-store", var)
				break
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

	// IFEQ, IFNE, IFLT, IFGE, IFGT, IFLE, IF_ICMPEQ, IF_ICMPNE, IF_ICMPLT,
	// IF_ICMPGE, IF_ICMPGT, IF_ICMPLE, IF_ACMPEQ, IF_ACMPNE, GOTO, JSR, IFNULL or IFNONNULL

	void visitJumpInsn(int opcode, Label label) {
		counter++
		switch (opcode) {
			default:
				throw new RuntimeException()
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
	}

	void visitLineNumber(int line, Label start) {
		println "----- $line ($start)"
	}

	void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
		println "$index Local $descriptor $name ($signature), start: $start, end: $end"
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
