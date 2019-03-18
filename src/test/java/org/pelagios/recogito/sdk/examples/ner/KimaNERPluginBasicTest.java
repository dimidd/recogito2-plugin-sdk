package org.pelagios.recogito.sdk.examples.ner;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.pelagios.recogito.sdk.ner.Entity;
import org.pelagios.recogito.sdk.ner.EntityType;


public class KimaNERPluginBasicTest {

	private final String SAMPLE_TEXT =
			"המהרל מירושלים נולד בפראג והגיע לליטא, פולין ולוורשה שבאירופה שוטין ובתחלה אם אב תוגרמה רוסיא רוסלאנד, זיטאמיר, אדעסא, ענינים שונים";

	private final Entity[] expectedMatches = new Entity[]{
			new Entity("ירושלים", EntityType.LOCATION, 7),
			new Entity("פראג", EntityType.LOCATION, 21),
			new Entity("ליטא", EntityType.LOCATION, 33),
			new Entity("פולין", EntityType.LOCATION, 39),
			new Entity("וורשה", EntityType.LOCATION, 47),
			new Entity("אירופה", EntityType.LOCATION, 55),
			new Entity("תוגרמה", EntityType.LOCATION, 81),
			new Entity("רוסיא", EntityType.LOCATION, 88),
			new Entity("רוסלאנד", EntityType.LOCATION, 94),
			new Entity("זיטאמיר", EntityType.LOCATION, 103),
			new Entity("אדעסא", EntityType.LOCATION, 112)
	};

	@Test
	public void test() {
		final ExampleKimaNERPlugin plugin = new ExampleKimaNERPlugin();
		final List<Entity> entities = plugin.parse(this.SAMPLE_TEXT);

		assertEquals(this.expectedMatches.length, entities.size());
		for (int i = 0; i < this.expectedMatches.length; ++i) {
			assertEquals(this.expectedMatches[i], entities.get(i));
		}

	}

}
