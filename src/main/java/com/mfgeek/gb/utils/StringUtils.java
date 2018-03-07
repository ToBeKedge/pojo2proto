package com.mfgeek.gb.utils;

import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h3>DESCRIPTION</h3>
 * util call for string.
 * <h3>IMPLEMENTATION</h3>
 *
 * <h3>USAGE</h3>
 *
 * @author VincentSi
 *
 */
public final class StringUtils {

	private static final Logger	LOGGER	= LoggerFactory.getLogger(StringUtils.class);

	/**
	 *
	 */
	private StringUtils() {

	}

	/**
	 * Remove accent from a string.
	 *
	 * @param pString the string to normalize
	 * @return the string normalized
	 */
	public static String unAccent(String pString) {
		String temp = Normalizer.normalize(pString, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("");
	}

	/**
	 * @param pString the string to check
	 * @return <code>true</code> if null or emtpy, <code>false</code> otherwise
	 */
	public static boolean isEmptyOrNull(String pString) {
		return pString == null || pString.isEmpty();
	}

	/**
	 * @param pString the string to check
	 * @return <code>true</code> if NOT null or emtpy, <code>false</code> otherwise
	 */
	public static boolean isNotEmptyOrNull(String pString) {
		return !isEmptyOrNull(pString);
	}

	/**
	 * Check if a string is empty.
	 *
	 * @param pString the string to check
	 * @return if the string is empty
	 */
	public static boolean isEmpty(String pString) {
		return isEmptyOrNull(pString);
	}

	/**
	 * Check if a string is NOT empty.
	 *
	 * @param pString the string to check
	 * @return if the string is NOT empty
	 */
	public static boolean isNotEmpty(String pString) {
		return isNotEmptyOrNull(pString);
	}

	/**
	 * Return the position of the nth occurrence of the given sub string in the string.
	 *
	 * @param pStr the string
	 * @param pSubStr the substring
	 * @param pN the number of occurrence
	 * @return the position if found, -1 otherwise
	 */
	public static int nthOccurrence(String pStr, String pSubStr, int pN) {
		int pos = pStr.indexOf(pSubStr, 0);
		while (pN-- > 1 && pos != -1) {
			pos = pStr.indexOf(pSubStr, pos + 1);
		}
		return pos;
	}

	/**
	 *
	 * @param pString convert String UTF8 to ISO-8859-1
	 * @return the String in ISO-8859-1 encoding, if error, return the given String from input without encoding
	 */
	public static String stringUTF8toISO(String pString) {
		try {
			return new String(pString.getBytes(), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			LOGGER.error("Can not convert String " + pString + " in ISO-8859-1 encoding. Use given String.");
			return pString;
		}
	}

}
