package org.pelagios.recogito.sdk.examples.ner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.pelagios.recogito.sdk.ner.Entity;
import org.pelagios.recogito.sdk.ner.EntityType;
import org.pelagios.recogito.sdk.ner.NERPlugin;

public class ExampleKimaNERPlugin implements NERPlugin, HebMatcher {
	private static final Logger LOGGER = Logger.getLogger(ExampleKimaNERPlugin.class.getName());
	private final StreamHandler sh = new StreamHandler(System.err, new SimpleFormatter());
	public static final int MAX_WORDS = 7;
	private HashMap<String, String> _aliases;
	private final char[] _prefs = { 'ב', 'ל', 'מ', 'ש', 'ו' };
	private static final String NAMES_FILE = "nba_1500_1940.tsv";
	private static final int N_MATCHES_TO_LOG = 6;

	public ExampleKimaNERPlugin() {

		try {
			final InputStream input = ClassLoaderUtil.getResourceAsStream(NAMES_FILE, ExampleKimaNERPlugin.class);
			Arrays.sort(this._prefs);
			final BufferedReader in = new BufferedReader(new InputStreamReader(input));
			
			this._aliases = new HashMap<String, String>();
			String str;
			while ((str = in.readLine()) != null) {
				final String[] splitted = str.split("\t");
				this._aliases.put(splitted[0], splitted[1]);
			}
			in.close();
			LOGGER.addHandler(this.sh);
			LOGGER.log( Level.INFO, "KimaNER: succesfully loaded names from {0}", NAMES_FILE);
		} catch (final IOException e) {
			e.printStackTrace();
			this._aliases = new HashMap<String, String>();
		}
	}

	@Override
	public String getName() {
		return "Example Kima NER Plugin";
	}

	@Override
	public String getDescription() {
		return "An attempt to use Kima with the Recogito NER plugin interface.";
	}

	@Override
	public String getOrganization() {
		return "Pelagios Commons";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public List<String> getSupportedLanguages() {
		final List<String> list = new ArrayList<String>();
		list.add("he");

		return list;
	}

	/**
	 * For testing and demonstration purposes, the dummy NER plugin reports a
	 * 'LOCATION' entity for every uppercase word. Don't consider this a sensible
	 */
	@Override
	public List<Entity> parse(String text) {
		final String[] words = text.split("\\s+");
		final List<Entity> phrases = new ArrayList<Entity>();

		if (words.length == 0)
			return phrases;

		int runningIdx = 0;
		int runningOffset = 0;

		while (runningIdx < words.length) {
			final String nextWord = words[runningIdx];
			runningOffset = text.indexOf(nextWord, runningOffset);

			for (int j = 0; j < MAX_WORDS && runningIdx + j < words.length; ++j) {
				final List<String> subWords = Arrays.asList(words).subList(runningIdx, runningIdx + j + 1);
				final String expr = String.join(" ", subWords);
				String gazId = null;
				final String[] gazArr = hasWord(expr, this._aliases, true);
				if (gazArr != null) {
					final String gazExpr = gazArr[0];
					gazId = gazArr[1];
					URI gnUri = null;
					try {
						gnUri = new URI("http://sws.geonames.org/" + gazId);
					} catch (final URISyntaxException e) {
						e.printStackTrace();
					}
					// HebMatcher might have removed prefixes
					int newOffset = runningOffset + expr.indexOf(gazExpr);
					if (newOffset > runningOffset){
						phrases.add(new Entity(gazExpr, EntityType.LOCATION, newOffset, gnUri));
					}
					else {
						phrases.add(new Entity(expr, EntityType.LOCATION, runningOffset, gnUri));
					}
					runningIdx += j;
					break;
				}
			}

			runningIdx++;
			runningOffset += nextWord.length();
		}

		final int maxMatchesToLog = Math.min(phrases.size(), N_MATCHES_TO_LOG);
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
