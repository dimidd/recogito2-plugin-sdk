package org.pelagios.recogito.sdk.ner;

import java.util.Objects;

public class Phrase {
	
  public final String chars;
  
  public final EntityType entityType;
  
  public final int charOffset;
	
  public Phrase(String chars, EntityType entityType, int charOffset) {
	this.chars = chars;
	this.entityType = entityType;
    this.charOffset = charOffset;
  }
  
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof Phrase))
      return false;
    
    Phrase other = (Phrase) obj;
    return chars.equals(other.chars) && 
      entityType.equals(other.entityType) && 
      charOffset == other.charOffset;
  }
  
  @Override
  public int hashCode() {
	return Objects.hash(chars, entityType, charOffset);
  }
  
  public String toString() {
    return "[" + chars + "|" + entityType.toString() + "|" + charOffset + "]";
  }
	
}