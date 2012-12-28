package com.some.locallife.ui.widget;

import com.some.locallife.data.type.Coupon;
import com.some.locallife.util.RemoteResourceManager;
import com.some.locallife.util.Util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class CouponListAdapter extends BaseGroupAdapter<Coupon> {

	RemoteResourceManager mRrm;
	ViewHolder mViewHolder;

	static class ViewHolder {

	}

	public CouponListAdapter(Context context, RemoteResourceManager rrm) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mRrm = rrm;
	}

	private void initView () {

		Util.coupon("coupon list adapter==getView=initView");
	}

	private void updateView() {

		Util.coupon("coupon list adapter==getView=updateView");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null) {
			initView();
		} else {
			this.mViewHolder = (ViewHolder) convertView.getTag();
		}

		updateView();
		return convertView;
	}

}
