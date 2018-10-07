package org.pelagios.recogito.sdk.examples.ner;

import java.util.Arrays;
import java.util.Map;

/**
 * Basic matching of Hebrew place names.
 * @author dimidd
 *
 */
public interface HebMatcher {
	// IMPORTANT: Keep it sorted
	final static char [] PREFS = {'ב', 'ו','כ', 'ל', 'מ', 'ש'};
	final static String [] PREFS2 = {
			"וב", "ול", "ומ", "וש",
			"כש",
			"שב", "של", "שמ"
	};
	default String[] hasWord(String word, Map<String, String> aliases, boolean prefixes) {
		word = normalizeWord(word);
		String val;
		if (word == null)
			return null;
		if ((val = aliases.get(word)) != null)
			return new String[]{word, val};

		if (!prefixes)
			return null;
		// TODO: Refactor
		final char head = word.charAt(0);
		final String tail = word.substring(1);
		if (Arrays.binarySearch(PREFS, head) > -1) {
			if ((val = aliases.get(tail)) != null)
				return new String[]{tail, val};
			else if (tail.length() > 0) {
				final String head2 = word.substring(0, 2);
				final String tail2 = tail.substring(1);
				if (Arrays.binarySearch(PREFS2, head2) > -1) {
					val = aliases.get(tail2);
					return new String[]{tail2, val};
				}
			}
		}

		return null;
	}

	public static String normalizeWord(String word) {
		if (word == null || word.isEmpty())
			return null;
		// remove punctuation
		word = word.replaceAll("[,.;()\\[\\]{}…?!/:]", "");
		if (word.isEmpty())
			return null;
		final char first = word.charAt(0);
		if (first == '"' || first == '\'') {
			word = word.substring(1);
		}
		final int len = word.length();
		final char last = word.charAt(len - 1);
		if (last == '"' || last == '\'') {
			word = word.substring(0, len - 1);
		}

		return word;
	}
}
