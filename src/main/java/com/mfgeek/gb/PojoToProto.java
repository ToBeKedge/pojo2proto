package com.mfgeek.gb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mfgeek.gb.utils.CollectionMapUtils;
import com.mfgeek.gb.utils.StringUtils;

public class PojoToProto {

	private static final Logger	LOGGER	= LoggerFactory.getLogger(PojoToProto.class);

	private static String				source;
	private static String				target;
	private static String				packageName;
	private static String				javaPackageName;
	private static String				protoPackageName;
	private static String				syntax;

	private static Options			options;

	static {
		syntax = "proto2";

		// create Options object
		options = new Options();

		// add required options
		options.addRequiredOption("i", "input", true, "root directory of the POJO source code");
		options.addRequiredOption("p", "package", true, "specify the package name of the POJO source class");
		options.addRequiredOption("o", "proto-package", true, "specify the package name of the proto file");

		// add options
		options.addOption("h", "help", false, "print help");
		options.addOption("j", "java-package", true, "specify the package name of the generated proto class");
		options.addOption("s", "syntax", true, "protocol buffer syntax (\"proto2\" by default)");
		options.addOption("g", "generated-output", true, "generated proto file output directory");

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		manageCommandLine(args);
		List<ProtoFile> protoFiles = ProtoGenerator.getInstance().parse(source, packageName, protoPackageName, javaPackageName, syntax);

		if (StringUtils.isNotEmpty(target) && CollectionMapUtils.isNotEmptyOrNull(protoFiles)) {
			File out = new File(target);
			if (out.exists() && !out.delete()) {
				LOGGER.info(target + " exist, but cannot be deleted!");
			}
			for (ProtoFile pf : protoFiles) {
				writeTo(target, pf);
			}
		}

	}

	/**
	 * @param args command line arguments
	 */
	private static void manageCommandLine(String[] args) {
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			if (cmd.hasOption("h")) {
				printHelp();
			}
			if (cmd.hasOption("i")) {
				source = cmd.getOptionValue("i");
			}
			if (cmd.hasOption("p")) {
				packageName = cmd.getOptionValue("p");
			}
			if (cmd.hasOption("j")) {
				javaPackageName = cmd.getOptionValue("j");
			}
			if (cmd.hasOption("g")) {
				target = cmd.getOptionValue("g");
			}
			if (cmd.hasOption("o")) {
				protoPackageName = cmd.getOptionValue("o");
			}
			if (cmd.hasOption("s")) {
				syntax = ESyntax.enumOf(cmd.getOptionValue("s")).name();
			}
			if (StringUtils.isEmptyOrNull(source) || StringUtils.isEmptyOrNull(packageName) || StringUtils.isEmptyOrNull(protoPackageName)) {
				printHelp();
			}
		} catch (ParseException e) {
			printHelp();
		}
	}

	/**
	 *
	 */
	private static void printHelp() {
		// automatically generate the help statement
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(PojoToProto.class.getSimpleName(), options);
		System.exit(1);
	}

	private static void writeTo(String dir, ProtoFile pf) {
		File out = new File(dir);
		if (!out.exists()) {
			LOGGER.info(dir + " doesn't exist, will create a new directory with name:[" + dir + "]");
			out.mkdirs();
		}

		if (out.exists() && out.isFile()) {
			String dirName = out.exists() ? dir : (dir + ".2");
			LOGGER.info(dir + " is a file, create another directory with name:[" + dir + ".2]");
			out = new File(dirName);
			out.mkdirs();
		}
		FileWriter writer = null;
		String name = out.getAbsolutePath() + "/" + pf.getName() + ".proto";
		try {
			LOGGER.info("writing proto file: " + name);
			writer = new FileWriter(name, false);
			writer.write(pf.toString());
		} catch (IOException e) {
			LOGGER.error("Unable to write " + name, e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					LOGGER.error("Unable to close writer", e);
				}
			}
		}

	}

	enum ESyntax {

		proto2, //
		proto3; //

		public static final ESyntax enumOf(String pValue) {
			if (StringUtils.isNotEmptyOrNull(pValue)) {
				for (ESyntax type : values()) {
					if (pValue.equals(type.name())) {
						return type;
					}
				}
			}
			return proto2;
		}

	}

}
