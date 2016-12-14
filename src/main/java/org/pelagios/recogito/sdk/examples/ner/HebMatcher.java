package org.pelagios.recogito.sdk.examples.ner;
import java.util.Arrays;
import java.util.Collection;

/**
 * Basic matching of Hebrew place names.
 * @author dimidd
 *
 */
public interface HebMatcher {
	// IMPORTANT: Keep it sorted
    final static char [] PREFS = {'ב', 'ו', 'ל', 'מ', 'ש'};

	default boolean hasWord(String word, Collection<String> aliases) {
        word = normalizeWord(word);
        if (word == null) {
             return false;
        }
        if (aliases.contains(word)) {
            return true;
        }
        // TODO: Refactor
        char head = word.charAt(0);
        String tail = word.substring(1);
        if (Arrays.binarySearch(PREFS, head) > -1) {
            if (aliases.contains(tail)) {
                return true;
            }
            else if (tail.length() > 0) {
                char head2 = tail.charAt(0);
                String tail2 = tail.substring(1);
                if (head == 'ו' || head == 'ש') {
                    if (head2 != head && Arrays.binarySearch(PREFS, head2) > -1) {
                        return aliases.contains(tail2);
                    }
                }
            }
        }

        return false;
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
