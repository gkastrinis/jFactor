package org.codesimius.jfactor

import org.objectweb.asm.ClassReader

class Main extends ClassLoader {
	static void main(String[] args) {
		Database.instance.init()
		try {
			args.each {new Main().loadClass(it) }
		} catch (ReflectiveOperationException e) {
			e.printStackTrace()
		}
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		try {
			def is = new File("build/classes/java/test0/" + name.replace('.', '/') + ".class").newInputStream()
			//def is = getResourceAsStream(name.replace('.', '/') + ".class")
//			def is = getResourceAsStream("B.class")
			new ClassReader(is).accept(new MyClassVisitor(name), 0)
		} catch (Exception e) {
			throw new ClassNotFoundException(name, e)
		}
	}
}