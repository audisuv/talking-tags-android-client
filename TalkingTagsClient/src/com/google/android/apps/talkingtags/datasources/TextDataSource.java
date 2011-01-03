package com.google.android.apps.talkingtags.datasources;

import java.io.IOException;
import java.util.List;

/**
 * Abstracts away a source of text data (lines) so the DataFetchers can be isolated from the
 * network.
 * 
 * @author adamconnors
 */
public interface TextDataSource {
  List<String> getData(String resourcename) throws IOException;
}
