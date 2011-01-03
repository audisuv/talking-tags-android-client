package com.google.android.apps.talkingtags.parsers;

import java.util.ArrayList;

import com.google.android.apps.talkingtags.Config;
import com.google.android.apps.talkingtags.model.TagCollection;
import com.google.android.apps.talkingtags.model.TagCollection.Type;

import junit.framework.TestCase;

public class CollectionParserTest extends TestCase {
  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Config.setTest(true);
  }

  public void testParsesSimpleData() throws Exception {
    String sample = "name: oxtag=resource";
    CollectionParser p = new CollectionParser();
    
    ArrayList<String> lines = new ArrayList<String>();
    lines.add(sample);
    
    ArrayList<TagCollection> cs = p.getCollections(lines);
    assertEquals(1, cs.size());
    TagCollection c = cs.get(0);
    assertEquals("name", c.name);
    assertEquals(Type.OXTAG, c.type);
    assertEquals("resource", c.resource);
  }
}
