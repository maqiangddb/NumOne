package com.some.locallife.ui.widget;

import com.some.locallife.data.type.GroupBuy;
import com.some.locallife.util.RemoteResourceManager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class GroupBuyListAdapter extends BaseGroupAdapter<GroupBuy> {

	private RemoteResourceManager mRrm;
	private ViewHolder mViewHolder;

	static class ViewHolder {

	}

	public GroupBuyListAdapter(Context context, RemoteResourceManager rrm) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mRrm = rrm;
	}

	private void initView(View convertView) {

	}

	private void updateView(View convertView, GroupBuy groupBuy) {

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			initView(convertView);
		} else {
			this.mViewHolder = (ViewHolder) convertView.getTag();
		}

		GroupBuy groupBuy = (GroupBuy) getItem(position);
		updateView(convertView, groupBuy);
		return convertView;
	}

}
