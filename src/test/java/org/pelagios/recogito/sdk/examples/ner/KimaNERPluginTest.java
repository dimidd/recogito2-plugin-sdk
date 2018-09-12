package org.pelagios.recogito.sdk.examples.ner;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.pelagios.recogito.sdk.ner.Entity;
import org.pelagios.recogito.sdk.ner.EntityType;

public class KimaNERPluginTest {

	private final String SAMPLE_TEXT =
			"המהרל מירושלים נולד בפראג והגיע לליטא, פולין ולוורשה שבאירופה";

	private final Entity firstExpectedMatch = new Entity("ירושלים", EntityType.LOCATION, 6);
	private final Entity secondExpectedMatch = new Entity("פראג", EntityType.LOCATION, 20);

	@Test
	public void test() {
		final ExampleKimaNERPlugin plugin = new ExampleKimaNERPlugin();
		final List<Entity> entities = plugin.parse(this.SAMPLE_TEXT);

		assertEquals(6, entities.size());
		assertEquals(this.firstExpectedMatch, entities.get(0));
		assertEquals(this.secondExpectedMatch, entities.get(1));
	}

}