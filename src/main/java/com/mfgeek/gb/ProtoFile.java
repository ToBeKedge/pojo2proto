package com.mfgeek.gb;

import java.util.ArrayList;
import java.util.List;

public class ProtoFile {
	private static final String			INDENT							= "\t";
	private String									syntax;
	private String									name;
	private boolean									isEnum							= false;
	private String									javaOuterClassName	= null;
	private String									javaPackage					= null;
	private String									packageName;
	private int											index								= 1;

	private final List<String>			imports							= new ArrayList<>();
	private final List<ProtoField>	fields							= new ArrayList<>();

	public String getSyntax() {
		return this.syntax;
	}

	public void setSyntax(String syntax) {
		this.syntax = syntax;
	}

	public void appendImport(String pImport) {
		this.imports.add(pImport);
	}

	public void appendField(ProtoField field) {
		this.fields.add(field);
	}

	public void appendField(boolean required, boolean repeated, String type, String name, String def) {
		this.fields.add(new ProtoField(required, repeated, type, name, def, this.index++));
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEnum() {
		return this.isEnum;
	}

	public void setEnum(boolean pIsEnum) {
		this.isEnum = pIsEnum;
	}

	public String getJavaOuterClassName() {
		return this.javaOuterClassName;
	}

	public void setJavaOuterClassName(String javaOuterClassName) {
		this.javaOuterClassName = javaOuterClassName;
	}

	public String getJavaPackage() {
		return this.javaPackage;
	}

	public void setJavaPackage(String javaPackage) {
		this.javaPackage = javaPackage;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		if (this.syntax != null && !this.syntax.isEmpty()) {
			appendLine(buf, "syntax = \"" + this.syntax + "\"");
			appendNewLine(buf);
		}
		appendLine(buf, "package " + this.packageName);
		if (!this.isEnum) {
			appendNewLine(buf);
			if (!this.imports.isEmpty()) {
				for (String i : this.imports) {
					appendLine(buf, "import \"" + i + "\"");
				}
			}
		}
		appendNewLine(buf);
		if (this.javaPackage != null && !this.javaPackage.isEmpty()) {
			appendLine(buf, "option java_package = \"" + this.javaPackage + "\"");
		}
		if (this.javaOuterClassName != null && !this.javaOuterClassName.isEmpty()) {
			appendLine(buf, "option java_outer_classname = \"" + this.javaOuterClassName + "\"");
		}
		appendNewLine(buf);
		appendOpenQuote(buf, (this.isEnum ? "enum " : "message ") + this.name);
		appendNewLine(buf);
		for (ProtoField f : this.fields) {
			if (this.isEnum && "value".equals(f.name)) {
				continue;
			}
			appendLine(buf, INDENT + f.toString(this.isEnum));
			appendNewLine(buf);
		}
		appendClosedQuote(buf);
		return buf.toString();
	}

	void appendNewLine(StringBuffer buf) {
		buf.append("\n");
	}

	void appendLine(StringBuffer buf, String str) {
		buf.append(str + ";\n");
	}

	void appendOpenQuote(StringBuffer buf, String str) {
		buf.append(str + " {\n");
	}

	void appendClosedQuote(StringBuffer buf) {
		buf.append("}\n");
	}

	class ProtoField {
		boolean								repeated	= false;
		boolean								required	= false;
		private final String	type;
		private final String	name;
		private final String	defaultValue;
		private final int			index;

		ProtoField(boolean required, boolean repeated, String type, String name, String defaultValue, int in) {
			this.required = required;
			this.repeated = repeated;
			this.type = type;
			this.name = name;
			this.defaultValue = defaultValue;
			this.index = in;
		}

		@Override
		public String toString() {
			return toString(false);
		}

		public String toString(boolean isFieldForEnum) {
			if (isFieldForEnum) {
				return this.name + " = " + (this.index - 1);
			} else {
				return (this.repeated ? "repeated" : (this.required ? "required" : "optional")) + " " + this.type + " " + this.name + " = "
						+ this.index + " " + (this.defaultValue == null ? "" : ("[ " + this.defaultValue + " ]"));
			}
		}
	}

}
