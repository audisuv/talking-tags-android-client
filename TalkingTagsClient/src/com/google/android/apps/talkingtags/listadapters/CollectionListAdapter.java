package com.google.android.apps.talkingtags.listadapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.apps.talkingtags.R;
import com.google.android.apps.talkingtags.model.TagCollection;
import com.google.android.apps.talkingtags.persistence.Store;

public class CollectionListAdapter extends BaseAdapter {
  
  private final Store<TagCollection> store;
  private final Context ctx;
  
  public CollectionListAdapter(Context ctx, Store<TagCollection> store) {
    this.ctx = ctx;
    this.store = store;
  }
  
  @Override
  public int getCount() {
    return store.size();
  }

  @Override
  public Object getItem(int pos) {
    return store.read(pos);
  }

  @Override
  public long getItemId(int pos) {
    return store.read(pos).name.hashCode();
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
    TagCollection item = store.read(pos);
    TextView title = (TextView) view.findViewById(R.id.text1);
    TextView sub = (TextView) view.findViewById(R.id.text2);
    title.setText(item.name);
    sub.setText(item.resource);
  }
}
