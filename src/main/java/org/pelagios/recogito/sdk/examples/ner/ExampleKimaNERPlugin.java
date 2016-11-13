package org.pelagios.recogito.sdk.examples.ner;

import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import org.pelagios.recogito.sdk.ner.EntityType;
import org.pelagios.recogito.sdk.ner.NERPlugin;
import org.pelagios.recogito.sdk.ner.Entity;


public class ExampleKimaNERPlugin implements NERPlugin {

  private Set<String> _aliases;
  private char [] _prefs = {'ב','מ','ל'};

  public ExampleKimaNERPlugin() {
    try {
        Arrays.sort(_prefs);
        //BufferedReader in = new BufferedReader(new FileReader("nli.txt"));
        InputStream input = getClass().getResourceAsStream("/nli.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String str;

        List<String> list = new ArrayList<String>();
        while((str = in.readLine()) != null){
            list.add(str);
        }
        _aliases = new HashSet<String>(list);
    }
    catch (IOException e) {
        e.printStackTrace();
        _aliases = new HashSet();
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
    List<String> list = new ArrayList<String>();
    list.add("he");

    return list;
  }

  /**
   * For testing and demonstration purposes, the dummy NER plugin
   * reports a 'LOCATION' entity for every uppercase word. Don't consider
   * this a sensible
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

      if (hasWord(nextWord)) {
        phrases.add(new Entity(
        nextWord,
        EntityType.LOCATION,
        runningOffset
        ));
      }

      runningIdx++;
      runningOffset += nextWord.length();
    }

    return phrases;
  }

   public boolean hasWord(String word) {
    // remove punctuation
    word = word.replaceAll("\\p{P}", "");
    if (_aliases.contains(word)) {
        return true;
    }
    else {
        if (word != null && !word.isEmpty()) {
        	char letter = word.charAt(0);
            return  Arrays.binarySearch(_prefs, letter) > -1 && _aliases.contains(word.substring(1));
        }
        else {
            return false;
        }
    }
  }

}
