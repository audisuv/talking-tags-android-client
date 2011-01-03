package com.google.android.apps.talkingtags.parsers;

import java.io.IOException;
import java.util.List;

import com.google.android.apps.talkingtags.model.Tag;

/**
 * Interface for parsing raw data into tags.
 * 
 * @author adamconnors
 */
public interface TagParser {
  List<Tag> parseTags(List<String> lines) throws IOException;
}
