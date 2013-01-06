package com.some.locallife.ui.widget;

import java.io.IOException;

import com.some.locallife.R;
import com.some.locallife.data.type.Coupon;
import com.some.locallife.util.RemoteResourceManager;
import com.some.locallife.util.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CouponListAdapter extends BaseGroupAdapter<Coupon> {

	RemoteResourceManager mRrm;
	ViewHolder mViewHolder;
	private int mLayoutId;
	private LayoutInflater mInflater;

	static class ViewHolder {
		ImageView mImage;
		TextView mMsg;
		TextView mPrice;


	}

	public CouponListAdapter(Context context, RemoteResourceManager rrm) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mRrm = rrm;
		this.mLayoutId = R.layout.coupon_list_item;
		this.mInflater = LayoutInflater.from(context);
	}

	private void initView (View convertView) {
		//convertView = this.mInflater.inflate(mLayoutId, null);
		this.mViewHolder = new ViewHolder();

		Util.coupon("coupon list adapter==getView=initView");
		this.mViewHolder.mMsg = (TextView) convertView.findViewById(R.id.coupon_description);
		this.mViewHolder.mImage = (ImageView) convertView.findViewById(R.id.coupon_image);
		this.mViewHolder.mPrice = (TextView) convertView.findViewById(R.id.coupon_price_text);

		convertView.setTag(this.mViewHolder);
	}

	private void updateView(Coupon coupon) {

		Util.coupon("coupon list adapter==getView=updateView");
		this.mViewHolder.mMsg.setText(coupon.getMsg());
		this.mViewHolder.mPrice.setText(coupon.getPrice());
		Uri photoUri = Uri.parse(coupon.getImageUrl());
		if(this.mRrm.exists(photoUri)) {
			this.mViewHolder.mImage.setImageResource(R.drawable.loading_bg);
			this.mRrm.request(photoUri);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		Bitmap bitmap;
		try {
			bitmap = BitmapFactory.decodeStream(this.mRrm.getInputStream(photoUri));
			this.mViewHolder.mImage.setImageBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.mViewHolder.mImage.setImageResource(R.drawable.loading_fail_4_3);
			e.printStackTrace();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null) {
			convertView = this.mInflater.inflate(mLayoutId, null);
			initView(convertView);
		} else {
			this.mViewHolder = (ViewHolder) convertView.getTag();
		}
		Coupon coupon = (Coupon) this.getItem(position);

		updateView(coupon);
		return convertView;
	}

}
