package org.pelagios.recogito.sdk.examples.ner;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.StreamHandler;
import java.util.logging.SimpleFormatter;

import org.pelagios.recogito.sdk.ner.EntityType;
import org.pelagios.recogito.sdk.ner.NERPlugin;
import org.pelagios.recogito.sdk.ner.Entity;

public class ExampleKimaNERPlugin implements NERPlugin, HebMatcher {
	private static final Logger LOGGER = Logger.getLogger(ExampleKimaNERPlugin.class.getName());
	private StreamHandler sh = new StreamHandler(System.err, new SimpleFormatter());
	public static final int MAX_WORDS = 7;
	private HashMap<String, String> _aliases;
	private char[] _prefs = { 'ב', 'ל', 'מ', 'ש', 'ו' };
	private static final String NAMES_FILE = "/kima_gn.tsv";
	private static final int N_MATCHES_TO_LOG = 6;

	public ExampleKimaNERPlugin() {

		try {
			Arrays.sort(_prefs);
			InputStream input = getClass().getResourceAsStream(NAMES_FILE);
			BufferedReader in = new BufferedReader(new InputStreamReader(input));
			String str;
			_aliases = new HashMap<String, String>();

			while ((str = in.readLine()) != null) {
				String[] splitted = str.split("\t");
				_aliases.put(splitted[0], splitted[1]);
			}
			in.close();
			LOGGER.addHandler(sh);
			LOGGER.log( Level.INFO, "KimaNER: succesfully loaded names from '{0}'", NAMES_FILE);
		} catch (IOException e) {
			e.printStackTrace();
			_aliases = new HashMap<String, String>();
		}
	}

	public String getName() {
		return "Example Kima NER Plugin";
	}

	public String getDescription() {
		return "An attempt to use Kima with the Recogito NER plugin interface.";
	}

	public String getOrganization() {
		return "Pelagios Commons";
	}

	public String getVersion() {
		return "1.0";
	}

	@Override
	public List<String> getSupportedLanguages() {
		List<String> list = new ArrayList<String>();
		list.add("he");

		return list;
	}

	/**
	 * For testing and demonstration purposes, the dummy NER plugin reports a
	 * 'LOCATION' entity for every uppercase word. Don't consider this a sensible
	 */
	@Override
	public List<Entity> parse(String text) {
		String[] words = text.split("\\s+");
		List<Entity> phrases = new ArrayList<Entity>();

		if (words.length == 0)
			return phrases;

		int runningIdx = 0;
		int runningOffset = 0;

		while (runningIdx < words.length) {
			String nextWord = words[runningIdx];
			runningOffset = text.indexOf(nextWord, runningOffset);

			for (int j = 0; j < MAX_WORDS && runningIdx + j < words.length; ++j) {
				List<String> subWords = Arrays.asList(words).subList(runningIdx, runningIdx + j + 1);
				String expr = String.join(" ", subWords);
				String gazId = null;
				if ((gazId = hasWord(expr, _aliases, true)) != null) {
					URI gnUri = null;
					try {
						gnUri = new URI("http://sws.geonames.org/" + gazId);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
					phrases.add(new Entity(expr, EntityType.LOCATION, runningOffset, gnUri));
					runningIdx += j;
					break;
				}
			}

			runningIdx++;
			runningOffset += nextWord.length();
		}

		int maxMatchesToLog = Math.min(phrases.size(), N_MATCHES_TO_LOG);
		LOGGER.log(
				Level.INFO,
				 "KimaNER: found {0} mathces. The first {1}:",
				 new Object[] {phrases.size(), maxMatchesToLog}
		);
		for (int i = 0; i < maxMatchesToLog; ++i) {
			LOGGER.log(
				Level.INFO,
				"{0}. {1}",
				new Object[] {i + 1, phrases.get(i).toString()}
			);
		}
		return phrases;
	}
}
