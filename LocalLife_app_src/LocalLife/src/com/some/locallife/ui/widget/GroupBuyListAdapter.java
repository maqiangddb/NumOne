package com.some.locallife.ui.widget;

import java.io.IOException;

import com.some.locallife.R;
import com.some.locallife.data.type.GroupBuy;
import com.some.locallife.util.RemoteResourceManager;
import com.some.locallife.util.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class GroupBuyListAdapter extends BaseGroupAdapter<GroupBuy> {

	private RemoteResourceManager mRrm;
	private ViewHolder mViewHolder;
	private LayoutInflater mInflater;
	private int mLayoutInt;

	static class ViewHolder {
		TextView mMsg;
		ImageView mImage;
		TextView mPrice;
		TextView mOldPrice;
		TextView mDiscount;
		Button mPhoneNum;

	}

	public GroupBuyListAdapter(Context context, RemoteResourceManager rrm) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mRrm = rrm;
		this.mInflater = LayoutInflater.from(context);
		this.mLayoutInt = R.layout.group_list_item;
	}

	private void initView(View convertView) {
		//convertView = this.mInflater.inflate(this.mLayoutInt, null);
		this.mViewHolder = new ViewHolder();

		this.mViewHolder.mMsg = (TextView) convertView.findViewById(R.id.group_buy_description);
		this.mViewHolder.mImage = (ImageView) convertView.findViewById(R.id.item_group_image);
		this.mViewHolder.mPrice = (TextView) convertView.findViewById(R.id.sell_price);
		this.mViewHolder.mOldPrice = (TextView) convertView.findViewById(R.id.item_group_price_text);
		this.mViewHolder.mDiscount = (TextView) convertView.findViewById(R.id.item_discount_text);

		convertView.setTag(this.mViewHolder);
	}

	private void updateView(View convertView, GroupBuy groupBuy) {
		this.mViewHolder.mMsg.setText(groupBuy.getMsg());
		this.mViewHolder.mPrice.setText(groupBuy.getNowPrice());
		this.mViewHolder.mOldPrice.setText(groupBuy.getOldPrice());
		this.mViewHolder.mDiscount.setText(groupBuy.getDiscount()+"折");
		Uri photoUri = Uri.parse(groupBuy.getImageUri());
		if(!this.mRrm.exists(photoUri)) {
			this.mViewHolder.mImage.setImageResource(R.drawable.loading_bg);
			this.mRrm.request(photoUri);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			Bitmap bitmap = BitmapFactory.decodeStream(this.mRrm.getInputStream(photoUri));
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
		if (convertView == null) {
			convertView = this.mInflater.inflate(this.mLayoutInt, null);
			initView(convertView);
		} else {
			this.mViewHolder = (ViewHolder) convertView.getTag();
		}

		GroupBuy groupBuy = (GroupBuy) getItem(position);
		Util.getData("in GroupBuyListAdapter===getView");

		groupBuy.doDiscount();
		groupBuy.doSavePrice();
		Util.groupbuyData(groupBuy);
		updateView(convertView, groupBuy);
		return convertView;
	}

}
