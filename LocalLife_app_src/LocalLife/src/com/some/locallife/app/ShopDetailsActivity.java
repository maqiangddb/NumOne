package com.some.locallife.app;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.R;
import com.some.locallife.data.type.Shop;
import com.some.locallife.util.RemoteResourceManager;
import com.some.locallife.util.Util;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopDetailsActivity extends Activity {

	protected static final String EXTRA_SHOP_PARCEL = "com.some.locallife.app.ShopDetailsActivity.EXTRA_SHOP_PARCEL";

	protected static final String EXTRA_SHOP_ID = "com.some.locallife.app.ShopDetailsActivity.EXTRA_SHOP_ID";

	private StateHolder mStateHolder;
	private RemoteResourceManager mRrm;
	private RemoteResourceManagerObserver mResourcesObserver;
	private Handler mHandler;
	private ViewHolder mViewHolder;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.shop_activity_new);



		Object retained = this.getLastNonConfigurationInstance();
		if(retained != null) {
			this.mStateHolder = (StateHolder) retained;
			this.mStateHolder.setActivity(this);

		}else {
			this.mStateHolder = new StateHolder();
			if(this.getIntent().hasExtra(EXTRA_SHOP_ID)) {
				String mShopId = this.getIntent().getStringExtra(EXTRA_SHOP_ID);
				this.mStateHolder.setShopId(mShopId);
			}
		}
		this.mHandler = new Handler();
		this.mRrm = ((LocalLifeApplication)this.getApplication()).getRemoteResourceManager();
		this.mResourcesObserver = new RemoteResourceManagerObserver();
		this.mRrm.addObserver(this.mResourcesObserver);
		ensureUi();

		if(!this.mStateHolder.getIsRunning() && !this.mStateHolder.getRanOnce()) {
			this.mStateHolder.startTaskShopDetails(this, this.mStateHolder.getShopId());
		}

		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView)this.findViewById(R.id.titleName);
		tv.setText(R.string.shop_detail_title);
		//this.setTitle(R.string.shop_detail_title);
	}

	@Override
	public Object onRetainNonConfigurationInstance() {
		this.mStateHolder.setActivity(null);
		return this.mStateHolder;
	}


	private void ensureUiImage(Shop shop) {
		// TODO Auto-generated method stub
		ViewGroup imageGroup = (ViewGroup) findViewById(R.id.imageLayout);
		this.mViewHolder.mImg1.setVisibility(View.GONE);
		this.mViewHolder.mImg2.setVisibility(View.GONE);
		this.mViewHolder.mImg3.setVisibility(View.GONE);

		if(shop == null || shop.getImageUrls() == null) {
			imageGroup.setVisibility(View.GONE);
			return;
		}
		imageGroup.setVisibility(View.VISIBLE);
		ImageView[] ims = {this.mViewHolder.mImg1, this.mViewHolder.mImg2, this.mViewHolder.mImg3};
		String[] images = shop.getImageUrls();
		TextView imgNum = (TextView) this.findViewById(R.id.picNum);
		String numText = String.valueOf(images.length);
		imgNum.setText(numText);
		if (images.length < 3) {
			this.mViewHolder.mMoreImgBtn.setVisibility(View.GONE);
		}
		for(int i=0; i < images.length; i++) {
			Util.log("get image==="+images[i]);
			Uri uriImage = Uri.parse(images[i]);
			if(this.mRrm.exists(uriImage)) {
				try {

					Bitmap bitmap = BitmapFactory.decodeStream(this.mRrm.getInputStream(uriImage));
					ims[i].setVisibility(View.VISIBLE);
					ims[i].setImageBitmap(bitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//setShopImageMissing(ims[i], shop);
					Util.log("this image get error:url:"+uriImage);
					ims[i].setVisibility(View.GONE);
				}
			} else {
				this.mRrm.request(uriImage);
				Util.log("Shop image missing!!!!"+uriImage);
				setShopImageMissing(ims[i], shop);
			}
		}
	}

	private void setShopImageMissing(ImageView view, Shop shop) {
		Util.log("Shop image missing!!!!");
	}

	private static class StateHolder {

		private Shop mShop;
		private ShopDetailsTask mTaskShopDetails;
		private String mShopId;
		private boolean mIsRanOnce;
		private boolean mIsRunning;
		private ShopDetailsActivity mActivity;


		public StateHolder() {

		}

		public void startTaskShopDetails(
				ShopDetailsActivity activity, String shopId) {
			// TODO Auto-generated method stub
			if (!this.mIsRunning) {
				this.mIsRunning = true;
				this.mTaskShopDetails = new ShopDetailsTask(activity, shopId);
				this.mTaskShopDetails.execute(shopId);
			}

		}

		public String getShopId() {
			// TODO Auto-generated method stub
			return this.mShopId;
		}

		public boolean getIsRunning() {
			// TODO Auto-generated method stub
			return this.mIsRunning;
		}

		public boolean getRanOnce() {
			// TODO Auto-generated method stub
			return this.mIsRanOnce;
		}

		public void setShopId(String mShopId) {
			// TODO Auto-generated method stub
			this.mShopId = mShopId;
		}

		public void setActivity(ShopDetailsActivity activity) {
			// TODO Auto-generated method stub
			this.mActivity = activity;
		}

		public Shop getShop() {
			// TODO Auto-generated method stub
			return this.mShop;
		}

		public void setIsRunningTask(boolean b) {
			// TODO Auto-generated method stub
			this.mIsRunning = b;
		}

		public void setRanOnce(boolean b) {
			// TODO Auto-generated method stub
			this.mIsRanOnce = b;
		}

		public void setShop(Shop shop) {
			// TODO Auto-generated method stub
			this.mShop = shop;
		}
	}

	private class RemoteResourceManagerObserver implements Observer {

		@Override
		public void update(Observable observable, Object data) {
			// TODO Auto-generated method stub
			ShopDetailsActivity.this.mHandler.post(mRunnableUpdateShopImage);
		}

	}

	private Runnable mRunnableUpdateShopImage = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ensureUiImage(ShopDetailsActivity.this.mStateHolder.getShop());
		}

		};

	private static class ShopDetailsTask extends AsyncTask<String, Void, Shop> {

		private ShopDetailsActivity mActivity;
		private Exception mReason;

		private String mShopId;

		public ShopDetailsTask(ShopDetailsActivity activity, String shopId) {
			this.mActivity = activity;
			this.mShopId = shopId;
		}

		@Override
		protected Shop doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				return ((LocalLifeApplication)this.mActivity.getApplication()).getLocalLife().getShop(mShopId);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.mReason = e;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.mReason = e;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				this.mReason = e;
			}

			return null;
		}

		@Override
		protected void onPreExecute() {
			this.mActivity.ensureUi();
		}

		@Override
		protected void onPostExecute(Shop shop) {
			if (this.mActivity != null) {
				this.mActivity.onShopDetailsTaskComplete(shop, this.mReason);
			}
		}

		@Override
		protected void onCancelled() {
			if (this.mActivity != null) {
				this.mActivity.onShopDetailsTaskComplete(null, this.mReason);
			}
		}

		public void setActivity(ShopDetailsActivity activity) {
			this.mActivity = activity;
		}

	}

	private void onShopDetailsTaskComplete(Shop shop, Exception ex) {
		// TODO Auto-generated method stub
		boolean DEBUG = true;
		if(DEBUG && shop != null) {
			Util.log("=address:"+shop.getAddress());
			Util.log("=average:"+shop.getAverage());
			Util.log("=commentsid:"+shop.getCommentsId());
			Util.log("=id:"+shop.getId());
			Util.log("=imageurl:"+shop.getImageUrl());
			Util.log("=name:"+shop.getName());
			Util.log("=price:"+shop.getPrice());
			Util.log("=teleNum:"+shop.getTeleNum());
			Util.log("=type:"+shop.getType());
			Util.log("=imageUrls:"+shop.getImageUrls());

		}
		this.mStateHolder.setIsRunningTask(false);
		this.mStateHolder.setRanOnce(true);
		if(shop != null) {
			this.mStateHolder.setShop(shop);
		}else if (ex != null) {
			android.util.Log.e("MQ","an exception happened on get shop details!!");
		} else {
			android.util.Log.e("MQ","A surprising new error has occurred!!");
		}


		ensureUi();

	}

	public void ensureUi() {
		// TODO Auto-generated method stub
		if(this.mViewHolder == null) {
			this.mViewHolder = new ViewHolder();

		this.mViewHolder.mInfoBtn = (Button) this.findViewById(R.id.shop_info);
		this.mViewHolder.mCommentBtn = (Button) this.findViewById(R.id.shop_comment);
		this.mViewHolder.mName = (TextView) this.findViewById(R.id.shopName);
		this.mViewHolder.mAveragePercent = (TextView) this.findViewById(R.id.comebackPercent);
		this.mViewHolder.mAverageCount = (TextView) this.findViewById(R.id.commentCount);
		this.mViewHolder.mPrice = (TextView) this.findViewById(R.id.perPrice);
		this.mViewHolder.mAddress = (TextView) this.findViewById(R.id.address);
		this.mViewHolder.mDistance = (TextView) this.findViewById(R.id.distance);
		this.mViewHolder.mPhone = (TextView) this.findViewById(R.id.tel);
		this.mViewHolder.mPhoneBtn = (Button) this.findViewById(R.id.telButton);

		this.mViewHolder.mMsgBtn = (Button) this.findViewById(R.id.msgButton);
		this.mViewHolder.mMapBtn = (Button) this.findViewById(R.id.mapButton);
		this.mViewHolder.mImg1 = (ImageView) this.findViewById(R.id.image1);
		this.mViewHolder.mImg2 = (ImageView) this.findViewById(R.id.image2);
		this.mViewHolder.mImg3 = (ImageView) this.findViewById(R.id.image3);
		this.mViewHolder.mMoreImgBtn = (Button) this.findViewById(R.id.moreButton);
		this.mViewHolder.mGroupBuyContainer = this.findViewById(R.id.tuanInfo);
		this.mViewHolder.mCouponContainer = this.findViewById(R.id.couponInfo);
		}
		Shop shop = this.mStateHolder.getShop();
		if(shop != null)
			updateView(shop);

		this.ensureUiImage(shop);
	}

	private void updateView(final Shop shop) {
		this.mViewHolder.mAddress.setText(shop.getAddress());
		this.mViewHolder.mAverageCount.setText(shop.getCommentsId());
		this.mViewHolder.mAveragePercent.setText(shop.getAverage());
		//this.mViewHolder.mDistance.setText(shop.getDistance);
		this.mViewHolder.mName.setText(shop.getName());
		this.mViewHolder.mPhone.setText(shop.getTeleNum());
		this.mViewHolder.mPrice.setText(shop.getPrice());
		this.mViewHolder.mPhoneBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+shop.getTeleNum()));
				ShopDetailsActivity.this.startActivity(intent);
			}
		});
	}


	static class ViewHolder {
		Button mInfoBtn;
		Button mCommentBtn;
		TextView mName;
		TextView mAveragePercent;
		TextView mAverageCount;
		TextView mPrice;
		TextView mAddress;
		TextView mDistance;
		TextView mPhone;
		Button mPhoneBtn;
		Button mMsgBtn;
		Button mMapBtn;
		ImageView mImg1;
		ImageView mImg2;
		ImageView mImg3;
		Button mMoreImgBtn;
		View mGroupBuyContainer;
		View mCouponContainer;
	}

}