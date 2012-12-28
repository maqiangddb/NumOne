package com.some.locallife.ui.widget;

import com.some.locallife.app.BaseHolder;
import com.some.locallife.app.BaseStateHolder;
import com.some.locallife.util.RemoteResourceManager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DataBaseAdapter extends BaseAdapter {
	private BaseHolder mHolder;

	public DataBaseAdapter(Context context, RemoteResourceManager rrm, BaseHolder holder) {
		this.mHolder = holder;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		this.mHolder.doBindView(convertView);
		this.mHolder.doUpdateView(position);
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mHolder.doGetCount();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mHolder.doGetItem(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
