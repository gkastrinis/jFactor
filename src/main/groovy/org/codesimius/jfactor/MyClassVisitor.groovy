package org.codesimius.jfactor

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class MyClassVisitor extends ClassVisitor {

	String name
	// Allocate here so it is the same set on all visited methods
	Set<String> visitedFlds = [] as Set

	MyClassVisitor(String name) {
		super(Opcodes.ASM9)
		this.name = name
	}

	@Override
	MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		return new MyMethodVisitor(access, name, desc, signature, exceptions, this.name, visitedFlds)
	}
}