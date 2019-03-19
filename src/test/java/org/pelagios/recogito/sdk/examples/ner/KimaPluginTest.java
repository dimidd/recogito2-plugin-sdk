package org.pelagios.recogito.sdk.examples.ner;

import static org.junit.Assert.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import org.pelagios.recogito.sdk.examples.ner.ExampleKimaNERPlugin;
import org.pelagios.recogito.sdk.ner.EntityType;
import org.pelagios.recogito.sdk.ner.Entity;

public class KimaPluginTest {

  @Test
  public void test() throws Exception {
    Path path = Paths.get(getClass().getClassLoader()
      .getResource("kima.txt").toURI());
          
    Stream<String> lines = Files.lines(path);
    String data = lines.collect(Collectors.joining("\n"));
    lines.close();

    ExampleKimaNERPlugin plugin = new ExampleKimaNERPlugin();
    List<Entity> entities = plugin.parse(data);

    System.out.println(entities.toString());

    /*
    assertEquals(3, entities.size());
    assertEquals(firstExpectedMatch, entities.get(0));
    assertEquals(secondExpectedMatch, entities.get(1));
    assertEquals(thirdExpectedMatch, entities.get(2));
    */
  }

}
