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

	void visitInsn(int opcode) {
		counter++
		switch (opcode) {
			case ICONST_0: rec("iconst_0")
				break
			case I2D: rec("i2d")
				break
			case POP: rec("pop")
				break
			case POP2: rec("pop2")
				break
			case DUP: rec("dup")
				break
			case DMUL: rec("dmul")
				break
			case IRETURN: rec("ireturn")
				break
			case DRETURN: rec("dreturn")
				break
			case RETURN: rec("return")
				break
			default: rec(opcode, "??")
				break
		}
	}

	void visitVarInsn(int opcode, int var) {
		counter++
		switch (opcode) {
			case ALOAD: rec("aload", var)
				break
			case ILOAD: rec("iload", var)
				break
			case DLOAD: rec("fload", var)
				break
			case DLOAD: rec("dload", var)
				break
			case ASTORE: rec("astore", var)
				break
			case DSTORE: rec("dstore", var)
				break
			default: rec(opcode, "??")
				break
		}
	}

	void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
		counter++
		def sig = "${owner.replace("/", ".")}.${name}$descriptor"
		switch (opcode) {
			case INVOKESPECIAL:
				callInfo(sig)
				rec("invokespecial", sig)
				break
			case INVOKEVIRTUAL:
				callInfo(sig)
				rec("invokevirtual", sig)
				break
			default: rec(opcode, "??")
				break
		}
	}

	void visitLdcInsn(Object value) {
		counter++
		rec("ldc", value)
	}

	void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
		counter++
		switch (opcode) {
			case PUTFIELD: rec("putfield", "<$owner: $descriptor $name>")
				break
			case GETSTATIC: rec("getstatic", "<$owner: $descriptor $name>")
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
		Database.instance.vars << "${methID()}\t$index\t${varID(name)}\t$descriptor\n"
		if (name != "this" && start == firstLabel) Database.instance.formals << "${methID()}\t${formalCounter++}\t${varID(name)}\n"
	}

	void rec(def opcode, def oper = "_") {
		Database.instance.opcodes << "${methID()}\t$counter\t$opcode\t$oper\n"
	}

	def methID() { "<${declaringType} ${name}$desc>" }

	def varID(def name) { "${methID()}/$name" }

	static void callInfo(String sig) {
		int argc = 0, i = sig.indexOf("(") + 1
		while (true) {
			if (sig[i] in ["B", "C", "D", "F", "I", "J", "S", "Z"]) {
				argc++
				i++
			} else if (sig[i] == "L") {
				argc++
				i += (sig[i..-1].indexOf(";"))+1
			} else if (sig[i] == "[") {
				i++
			} else if (sig[i] == ")") {
				i++
				break
			}
		}
		String retType = "??"
		switch (sig[i]) {
			case 'B': retType = "byte"
				break
			case 'C': retType = "char"
				break
			case 'D': retType = "double"
				break
			case 'F': retType = "float"
				break
			case 'I': retType = "int"
				break
			case 'J': retType = "long"
				break
			case 'S': retType = "short"
				break
			case 'Z': retType = "boolean"
				break
			case 'V': retType = "void"
				break
			case 'L': retType = sig[i+2..-2].replace("/", ".")
				break
			case '[': retType = sig[i+1..-1].replace("/", ".")
		}
		Database.instance.methodSigs << "$sig\t$argc\t$retType\n"
	}
}
