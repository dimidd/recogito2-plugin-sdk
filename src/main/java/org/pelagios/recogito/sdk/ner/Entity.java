package org.pelagios.recogito.sdk.ner;

import java.net.URI;
import java.util.Objects;

/**
 * An instance of a named entity, consisting of the text string, the entity
 * type, and the offset of the entity within the text.
 *
 * @author Rainer Simon <rainer.simon@ait.ac.at>
 */
public class Entity {

	public final String chars;

	public final EntityType entityType;

	public final int charOffset;

	public final URI uri;

	public Entity(String chars, EntityType entityType, int charOffset) {
		this(chars, entityType, charOffset, null);
	}

	public Entity(String chars, EntityType entityType, int charOffset, URI uri) {
		this.chars = chars;
		this.entityType = entityType;
		this.charOffset = charOffset;
		this.uri = uri;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Entity))
			return false;

		final Entity other = (Entity) obj;

		return this.chars.equals(other.chars) &&
				this.entityType.equals(other.entityType) &&
				this.charOffset == other.charOffset;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.chars, this.entityType, this.charOffset);
	}

	@Override
	public String toString() {
		return String.join("|",  new String[]{
				this.chars,
				this.entityType.toString(),
				Integer.toString(this.charOffset),
				this.uri != null ? this.uri.toString() : null
		});
	}

}
