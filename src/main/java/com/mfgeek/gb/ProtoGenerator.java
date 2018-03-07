package com.mfgeek.gb;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoGenerator {

	private static final Logger		LOGGER		= LoggerFactory.getLogger(ProtoGenerator.class);

	private static ProtoGenerator	instance	= new ProtoGenerator();

	public static ProtoGenerator getInstance() {
		return instance;
	}

	private static Map<String, String> map = new HashMap<>();
	static {
		map.put("int", "int32");
		map.put("integer", "int32");
		map.put("short", "int32");
		map.put("long", "int64");	// need to manually convert from pojo date value to long value
		map.put("string", "string");
		map.put("float", "float");
		map.put("double", "double");
		map.put("date", "int64");	// convert date value to int64/long TODO
		map.put("boolean", "bool");
		map.put("byte", "int32");
	}

	public List<ProtoFile> parse(String pSource, String pPackage, String pProtoPackageName, String pJavaPackageName, String pSyntax) {
		LOGGER.info("parsing java classes in package:" + pSource);

		List<String> ignores = getIgnores();

		List<ProtoFile> protos = new ArrayList<>();
		File f = new File(pSource, packToDir(pPackage));
		if (f.exists() && f.isDirectory() && f.list().length > 0) {
			List<String> javaFiles = Arrays.asList(f.list());
			LOGGER.info("found " + javaFiles.size() + " java classes in package");

			for (String j : javaFiles) {
				String msg = "processing class " + j;

				// skip **Example.java classes
				if (j.endsWith("Example.java")) {
					LOGGER.info(msg + " -- skipped **Example.java class");
					continue;
				}

				if (ignores.contains(j.substring(0, j.indexOf(".java")))) {
					LOGGER.info(msg + " -- skipped ignored files defined in ignore.txt");
					continue;
				}

				LOGGER.info(msg);
				Class<?> c;
				try {
					c = Class.forName(pPackage + "." + j.substring(0, j.indexOf(".")));

					String name = c.getSimpleName();
					String packageName = c.getPackage().getName();

					ProtoFile pf = new ProtoFile();
					pf.setSyntax(pSyntax);
					if (c.isEnum()) {
						pf.setEnum(true);
						pf.setJavaOuterClassName(name + "Enum");
					} else {
						pf.setJavaOuterClassName(name + "Obj");
					}
					pf.setJavaPackage(pJavaPackageName != null && !pJavaPackageName.isEmpty() ? pJavaPackageName : packageName);
					pf.setName(name);
					pf.setPackageName(pProtoPackageName);

					for (Field field : c.getDeclaredFields()) {
						manageField(javaFiles, pf, field);
					}
					if ((c.getName() + "Key").equals(c.getSuperclass().getName())) {
						for (Field field : c.getSuperclass().getDeclaredFields()) {
							manageField(javaFiles, pf, field);
						}
					}

					protos.add(pf);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

			}
		} else {

		}
		return protos;
	}

	/**
	 * @param javaFiles
	 * @param pf
	 * @param field
	 * @return
	 */
	private void manageField(List<String> javaFiles, ProtoFile pf, Field field) {
		// skip serialVersionUID
		if (field.getName().equals("serialVersionUID")) {
			return;
		}
		Class<?> type = field.getType();
		checkCustomType(javaFiles, pf, type);
		if (checkType(type)) {
			pf.appendField(false, false, getProtoType(type), field.getName(), null);
		} else if (Collection.class.isAssignableFrom(field.getType())) {
			Class<?> parameterizedType = ((Class<?>) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
			checkCustomType(javaFiles, pf, parameterizedType);
			pf.appendField(false, true, getProtoType(parameterizedType), field.getName(), null);
		} else {
			LOGGER.info("WARNING: failed to handle field [" + field.getName() + "] with type [" + type + "]. Skipped.");
		}
	}

	/**
	 * @param pClass
	 * @return
	 */
	private boolean checkType(Class<?> pClass) {
		return map.containsKey(pClass.getSimpleName().toLowerCase());
	}

	/**
	 * @param javaFiles
	 * @param pf
	 * @param pType
	 */
	private void checkCustomType(List<String> javaFiles, ProtoFile pf, Class<?> pClass) {
		String t = pClass.getSimpleName();
		if (javaFiles.contains(t + ".java")) {
			String type = t.toLowerCase();
			pf.appendImport(t + ".proto");
			if (!map.containsKey(type)) {
				map.put(type, t);
			}
		}
	}

	public List<String> getIgnores() {
		List<String> ignoreFiles = new ArrayList<String>();
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("ignore.txt");
			InputStreamReader isr = new InputStreamReader(is);
			if (is != null && isr.ready()) {
				BufferedReader br = new BufferedReader(isr);
				String s;
				while ((s = br.readLine()) != null) {
					ignoreFiles.add(s);
				}
				isr.close();
				is.close();
			} else {
				LOGGER.info("ignore.txt not found");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return ignoreFiles;
	}

	private String getProtoType(Class<?> pClass) {
		return getProtoType(pClass.getSimpleName());
	}

	private String getProtoType(String type) {
		return map.get(type.toLowerCase());
	}

	private String dirToPack(String dir) {
		return dir.replace("/", ".");
	}

	private String packToDir(String packageName) {
		return packageName.replace(".", "/");
	}

}
