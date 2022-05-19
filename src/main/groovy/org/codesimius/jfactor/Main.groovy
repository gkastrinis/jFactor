package org.codesimius.jfactor

import org.objectweb.asm.ClassReader

import java.util.jar.JarFile

Conf.instance.init()

args.each {
	def jarFile = new JarFile(it)
	jarFile.entries().each { entry ->
		if (!entry.name.endsWith(".class")) return
		def name = entry.name[0..-7]
		new ClassReader(jarFile.getInputStream(entry)).accept(new MyClassVisitor(name), 0)
	}
}