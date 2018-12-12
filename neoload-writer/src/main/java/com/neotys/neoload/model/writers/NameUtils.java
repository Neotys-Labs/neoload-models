package com.neotys.neoload.model.writers;

public class NameUtils {
	private static final char[] forbiddenChars = { '£', '', '$', '\"', '[', ']', '<', '>', '|', '*', '¤', '?', '§',
			'µ', '#', '`', '@', '^', '²', '°', '¨', '\\'};

	private NameUtils() {}

	/** Replace special characters in a string.
	 * @param name
	 * @return
	 */
	public static String normalize(final String name) {
		String escaped = name;
		
		// replace all special characters
		for (final char c: forbiddenChars) {
			escaped = escaped.replace(c, '_');
		}
		
		return escaped;
	}
}
