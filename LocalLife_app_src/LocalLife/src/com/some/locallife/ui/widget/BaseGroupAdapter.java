package com.some.locallife.ui.widget;

import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.util.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

abstract class BaseGroupAdapter<T extends LocalType> extends BaseAdapter {
	Group<T> group = null;

	public BaseGroupAdapter(Context context) {

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Util.log("base adapter getcount");
		if(group!= null)
		Util.log("base adapter getCount==="+group.size());
		return (group == null)? 0:group.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return group.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}



	public void setGroup(Group<T> g) {
		group = g;
		if(g != null)
		Util.log("set data to adapter===size:"+g.size());
		this.notifyDataSetInvalidated();
	}



}
