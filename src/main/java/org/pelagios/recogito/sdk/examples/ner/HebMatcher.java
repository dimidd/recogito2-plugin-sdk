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
    final static char [] PREFS = {'ב', 'ו', 'ל', 'מ', 'ש'};

	default String hasWord(String word, Map<String, String> aliases, boolean prefixes) {
        word = normalizeWord(word);
        String val;
        if (word == null) {
             return null;
        }
        if ((val = aliases.get(word)) != null) {
            return val;
        }
        
        if (!prefixes) {
        		return null;
        }
        // TODO: Refactor
        char head = word.charAt(0);
        String tail = word.substring(1);
        if (Arrays.binarySearch(PREFS, head) > -1) {
            if ((val = aliases.get(tail)) != null) {
                return val;
            }
            else if (tail.length() > 0) {
                char head2 = tail.charAt(0);
                String tail2 = tail.substring(1);
                if (head == 'ו' || head == 'ש') {
                    if (head2 != head && Arrays.binarySearch(PREFS, head2) > -1) {
                        return aliases.get(tail2);
                    }
                }
            }
        }

        return null;
	}

	public static String normalizeWord(String word) {
        if (word == null || word.isEmpty()) {
             return null;
        }
        // remove punctuation
        word = word.replaceAll("[,.;()\\[\\]{}…?!/:]", "");
        if (word.isEmpty()) {
             return null;
        }
        char first = word.charAt(0);
        if (first == '"' || first == '\'') {
            word = word.substring(1);
        }
        int len = word.length();
        char last = word.charAt(len - 1);
        if (last == '"' || last == '\'') {
            word = word.substring(0, len - 1);
        }

        return word;
    }
}
