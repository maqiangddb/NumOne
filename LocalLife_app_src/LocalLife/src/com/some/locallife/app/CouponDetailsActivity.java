package com.some.locallife.app;

import java.io.IOException;

import com.some.locallife.R;
import com.some.locallife.data.type.Coupon;
import com.some.locallife.util.RemoteResourceManager;
import com.some.locallife.util.TabsUtil;
import com.some.locallife.util.Util;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class CouponDetailsActivity extends TabActivity {


	public static final String EXTRA_COUPON = "com.some.locallife.CouponDetailsActivity.EXTRA_COUPON";

	private ViewHolder mViewHolder;
	private Coupon mCoupon;

	static class ViewHolder {
		ImageView mImage;
		TextView mMsg;
		TextView mPrice;
		TextView mValue;
	}

	private void bindView() {
		this.mViewHolder = new ViewHolder();
		this.mViewHolder.mImage = (ImageView) this.findViewById(R.id.coupon_image);
		this.mViewHolder.mMsg = (TextView) this.findViewById(R.id.coupon_info);
		this.mViewHolder.mPrice = (TextView) this.findViewById(R.id.coupon_price_text);
		this.mViewHolder.mValue = (TextView) this.findViewById(R.id.coupon_value_text);

	}

	private void initView () {
		this.mViewHolder.mMsg.setText(this.mCoupon.getMsg());
		this.mViewHolder.mPrice.setText(this.mCoupon.getPrice());
		this.mViewHolder.mValue.setText(this.mCoupon.getPageValue());
		Uri photoUri = Uri.parse(this.mCoupon.getImageUrl());
		if (!this.mRrm.exists(photoUri)) {
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

	private RemoteResourceManager mRrm;
	private LocalLifeApplication mApp;

	private static final int COUPON_DETAIL_TAB = 1;
	private static final int COMMENTS_TAB = 2;

	private TabHost mTabHost;

	private void initTabHost() {
		if(this.mTabHost != null) {}
		this.mTabHost = this.getTabHost();

		//add shop detail tab
		TabsUtil.addTab(mTabHost, this.getString(R.string.fav_shop_tab_title), null, COUPON_DETAIL_TAB, R.id.coupon_detail_content);

		//add comments tab
		Intent commentsIntent = new Intent(this, CommentActivity.class);//FavShopActivity.class);
		commentsIntent.putExtra(CommentActivity.EXTRA_IS_COUPON, true);
		TabsUtil.addTab(mTabHost, this.getString(R.string.fav_shop_tab_title), null, COMMENTS_TAB, commentsIntent);

		this.mTabHost.setCurrentTab(this.COUPON_DETAIL_TAB);

	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.coupon_activity);

		this.initTabHost();

		this.mApp = (LocalLifeApplication) this.getApplication();
		this.mRrm = this.mApp.getRemoteResourceManager();

		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView) this.findViewById(R.id.titleName);
		tv.setText(R.string.coupon_detail_activity_title);

		this.bindView();

		Object retained = this.getLastNonConfigurationInstance();
		if(retained != null) {

		} else {

		}

		if (!this.getIntent().hasExtra(EXTRA_COUPON)) {
			Util.getData("in CouponDetailsActivity didn't get Coupon data!!!");
			this.finish();
		}
		this.mCoupon = this.getIntent().getParcelableExtra(EXTRA_COUPON);
		Util.getData("in CouponDetailsActivity get coupon from intent===");
		Util.couponData(mCoupon);

		this.initView();

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.coupon_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId()) {
    	case R.id.comment_item:
    		return true;

    	}
		return super.onOptionsItemSelected(item);

    }

    private void doComment() {

    }

}
