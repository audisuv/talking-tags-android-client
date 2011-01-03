/**
 * 
 */
package com.google.android.apps.talkingtags.listadapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.apps.talkingtags.R;
import com.google.android.apps.talkingtags.model.Tag;

/**
 * @author adamconnors
 */
public class TagListAdapter extends BaseAdapter {
  
  private List<Tag> tags;
  private final Context ctx;
  
  public TagListAdapter(Context ctx) {
    this.ctx = ctx;
  }
  
  public void setTags(List<Tag> tags) {
    this.tags = tags;
  }
  
  @Override
  public int getCount() {
    return tags.size();
  }

  @Override
  public Object getItem(int pos) {
    return tags.get(pos);
  }

  @Override
  public long getItemId(int pos) {
    return tags.get(pos).tagId.hashCode();
  }

  @Override
  public View getView(int pos, View view, ViewGroup parent) {
    if (view == null) {
      view = LayoutInflater.from(ctx).inflate(R.layout.list_item_tag_collection,
          parent, false);
    }
    bindView(pos, view);
    return view;
  }

  private void bindView(int pos, View view) {
    Tag item = tags.get(pos);
    TextView title = (TextView) view.findViewById(R.id.text1);
    TextView sub = (TextView) view.findViewById(R.id.text2);
    title.setText(item.tagId);
    sub.setText(item.title);
  }
}
