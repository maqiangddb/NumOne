package com.some.locallife.ui.widget;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.some.locallife.app.BaseHolder;
import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.Shop;
import com.some.locallife.util.RemoteResourceManager;
import com.some.locallife.util.Util;
import com.some.locallife.R;


public class ShopListAdapter extends BaseGroupAdapter<Shop> implements ObservableAdapter {


	private LayoutInflater mInflater;
	private int mLayoutToInflate;
	private RemoteResourceManager mRrm;
	private RemoteResourceManagerObserver mResourcesObserver;
	private Handler mHandler = new Handler();

	private Set<String> mLaunchedPhotoFetches;
	private int mLoadedPhotoIndex;
	private Map<String, String> mCachedTimestamps;
	private Context mContext;

	private Runnable mUpdatePhoto = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ShopListAdapter.this.notifyDataSetChanged();
		}

	};

	public ShopListAdapter(Context context, RemoteResourceManager rrm) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mInflater = LayoutInflater.from(context);
		this.mLayoutToInflate = R.layout.shop_list_item_new;
		this.mRrm = rrm;
		this.mResourcesObserver = new RemoteResourceManagerObserver();
		this.mLaunchedPhotoFetches = new HashSet<String> ();

		this.mRrm.addObserver(mResourcesObserver);

		this.mLoadedPhotoIndex = 0;
		this.mCachedTimestamps = new HashMap<String, String>();
		this.mContext = context;
	}

	public ShopListAdapter(Context context, int layoutResource) {
		super(context);
		this.mInflater = LayoutInflater.from(context);
		this.mLayoutToInflate = layoutResource;
		this.mContext = context;
	}

	public void removeItem(int position) {
		this.group.remove(position);
		this.notifyDataSetInvalidated();
	}

	@Override
	public void setGroup(Group<Shop> g) {
		super.setGroup(g);
		this.mLoadedPhotoIndex = 0;
		this.mHandler.postDelayed(mUpdatePhoto, 10L);

		this.mCachedTimestamps.clear();

	}

	private void initViewHolder(ViewHolder holder, View convertView) {
		android.util.Log.e("MQ","didn't initViewHolder===");
		holder = new ViewHolder();
		convertView = this.mInflater.inflate(mLayoutToInflate, null);
		holder.mShopAverageCount = (TextView) convertView.findViewById(R.id.commentCountText);
		holder.mShopAveragePercent = (TextView) convertView.findViewById(R.id.goodCommentCount);
		holder.mShopDistance = (TextView) convertView.findViewById(R.id.distance);
		holder.mShopName = (TextView) convertView.findViewById(R.id.shopName);
		holder.mShopPhone = (Button) convertView.findViewById(R.id.shop_tele_num);
		holder.mShopPhoto = (ImageView) convertView.findViewById(R.id.Storepicture);
		holder.mShopPrice = (TextView) convertView.findViewById(R.id.perPrice);
		holder.mShopType = (TextView) convertView.findViewById(R.id.categoryName);

		convertView.setTag(holder);
	}

	private void updateView(ViewHolder holder, Shop shop) {
		holder.mShopAverageCount.setText(shop.getCommentsId());
		holder.mShopPhone.setText(shop.getTeleNum());
		final Shop data = shop;
		holder.mShopPhone.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+data.getTeleNum()));
				mContext.startActivity(intent);
			}
		});
		holder.mShopPrice.setText(shop.getPrice());
		holder.mShopType.setText(shop.getType());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;

		if(convertView == null) {
			holder = new ViewHolder();
			convertView = this.mInflater.inflate(mLayoutToInflate, null);
			holder.mShopAverageCount = (TextView) convertView.findViewById(R.id.commentCountText);
			holder.mShopAveragePercent = (TextView) convertView.findViewById(R.id.goodCommentCount);
			holder.mShopDistance = (TextView) convertView.findViewById(R.id.distance);
			holder.mShopName = (TextView) convertView.findViewById(R.id.shopName);
			holder.mShopPhone = (Button) convertView.findViewById(R.id.tele_btn);
			holder.mShopPhoto = (ImageView) convertView.findViewById(R.id.Storepicture);
			holder.mShopPrice = (TextView) convertView.findViewById(R.id.perPrice);
			holder.mShopType = (TextView) convertView.findViewById(R.id.categoryName);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Shop shop = (Shop) getItem(position);
		Uri photoUri = Uri.parse(shop.getImageUrl());
			Util.log("build list item name:"+shop.getName());
		if(!this.mRrm.exists(photoUri)) {
			this.mLaunchedPhotoFetches.add(shop.getId());
			holder.mShopPhoto.setImageResource(R.drawable.loading_bg);
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
			holder.mShopPhoto.setImageBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			holder.mShopPhoto.setImageResource(R.drawable.loading_fail_4_3);
			e.printStackTrace();

		}

		holder.mShopName.setText(shop.getName());
		updateView(holder, shop);

		return convertView;
	}

	@Override
	public void removeObserver() {
		// TODO Auto-generated method stub
		this.mHandler.removeCallbacks(mUpdatePhoto);
		this.mRrm.deleteObserver(mResourcesObserver);
	}


	private class RemoteResourceManagerObserver implements Observer {

		@Override
		public void update(Observable observable, Object data) {
			// TODO Auto-generated method stub

		}

	}

	static class ViewHolder {
		ImageView mShopPhoto;
		TextView mShopName;
		Button mShopPhone;
		TextView mShopAveragePercent;
		TextView mShopAverageCount;
		TextView mShopPrice;
		TextView mShopDistance;
		TextView mShopType;
	}

}