package org.codesimius.jfactor

@Singleton
class Conf {

	String DELIM = ","

	File baseDir = new File("build/out_tmp")

	File opcodes = new File(baseDir, "opcode.csv")
	File allocTypes = new File(baseDir, "alloc-type.csv")
	File labels = new File(baseDir, "label.csv")
	File vars = new File(baseDir, "var.csv")
	File incValues = new File(baseDir, "inc-value.csv")
	File elemTypes = new File(baseDir, "elem-type.csv")
	File invocations = new File(baseDir, "invocation.csv")

	void init() {
		baseDir.mkdirs()
		this.properties.each { prop, val ->
			if(prop in ["metaClass", "class", "instance", "baseDir", "DELIM"]) return
			this[prop].delete()
			this[prop].createNewFile()
		}
		opcodes << ["meth", "idx", "stmt", "opcode", "oper"].join(DELIM) << "\n"
		allocTypes << ["stmt", "type"].join(DELIM) << "\n"
		labels << ["meth", "label", "stmt"].join(DELIM) << "\n"
		vars << ["meth", "var_idx", "var", "name", "type", "start_label", "end_label"].join(DELIM) << "\n"
		incValues << ["stmt", "value", "type"].join(DELIM) << "\n"
		elemTypes << ["array_type", "elem_type"].join(DELIM) << "\n"
		invocations << ["sig", "argc", "ret_type", "qual_name"].join(DELIM) << "\n"
	}

	// File handlers = new File(baseDir, "Handler.facts")
	// File fields = new File(baseDir, "Field.facts")
	// File formals = new File(baseDir, "FORMAL_PARAM.facts")
	// File methodExceptions = new File(baseDir, "MethodDeclaresException.facts")
	// File lineNumbers = new File(baseDir, "LineNumber.facts")
}
