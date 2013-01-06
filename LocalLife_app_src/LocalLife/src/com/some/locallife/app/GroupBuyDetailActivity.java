package com.some.locallife.app;

import java.io.IOException;

import com.some.locallife.R;
import com.some.locallife.data.type.GroupBuy;
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
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

public class GroupBuyDetailActivity extends TabActivity {


	public static final String EXTRA_GROUP_BUY = "com.some.locallife.GroupBuyDetailActivity.EXTRA_GROUP_BUY";

	private ViewHolder mViewHolder;
	private GroupBuy mGroup;

	static class ViewHolder {
		ImageView mImage;
		TextView mMsg;
		TextView mPrice;
		TextView mOldPrice;
		TextView mDiscount;
		TextView mSave;
	}

	private void bindView () {
		this.mViewHolder = new ViewHolder();
		this.mViewHolder.mImage = (ImageView) this.findViewById(R.id.group_image);
		this.mViewHolder.mMsg = (TextView) this.findViewById(R.id.group_msg);
		this.mViewHolder.mPrice = (TextView) this.findViewById(R.id.group_price_text);
		this.mViewHolder.mOldPrice = (TextView) this.findViewById(R.id.group_old_price_text);
		this.mViewHolder.mDiscount = (TextView) this.findViewById(R.id.group_discount_text);
		this.mViewHolder.mSave = (TextView) this.findViewById(R.id.group_save_text);
	}

	private void initView () {

		this.mViewHolder.mMsg.setText(this.mGroup.getMsg());
		this.mViewHolder.mPrice.setText(this.mGroup.getNowPrice());
		this.mViewHolder.mOldPrice.setText(this.mGroup.getOldPrice());
		this.mViewHolder.mDiscount.setText(this.mGroup.getDiscount());
		this.mViewHolder.mSave.setText(this.mGroup.getSavePrice());


		Uri photoUri = Uri.parse(this.mGroup.getImageUri());
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

	private static final int GROUPBUY_DETAIL_TAB = 1;
	private static final int COMMENTS_TAB = 2;

	private TabHost mTabHost;

	private void initTabHost() {
		if(this.mTabHost != null) {}
		this.mTabHost = this.getTabHost();

		//add shop detail tab
		TabsUtil.addTab(mTabHost, this.getString(R.string.fav_shop_tab_title), null, GROUPBUY_DETAIL_TAB, R.id.group_detail_content);

		//add comments tab
		Intent commentsIntent = new Intent(this, CommentActivity.class);//FavShopActivity.class);
		commentsIntent.putExtra(CommentActivity.EXTRA_IS_GROUPBUY, true);
		TabsUtil.addTab(mTabHost, this.getString(R.string.fav_shop_tab_title), null, COMMENTS_TAB, commentsIntent);

		this.mTabHost.setCurrentTab(this.GROUPBUY_DETAIL_TAB);

	}

	@Override
	public void onCreate (Bundle bundle) {
		super.onCreate(bundle);
		this.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.mApp = (LocalLifeApplication) this.getApplication();
		this.mRrm = this.mApp.getRemoteResourceManager();

		this.setContentView(R.layout.group_activity);
		this.initTabHost();
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView) this.findViewById(R.id.titleName);
		tv.setText(R.string.group_buy_detail_activity_title);

		this.bindView();

		if (!this.getIntent().hasExtra(EXTRA_GROUP_BUY)) {
			Util.getData("in GroupBuyDetailActivity didn't get GroupBuy data!!!");
			this.finish();
		}
		this.mGroup = this.getIntent().getParcelableExtra(EXTRA_GROUP_BUY);
		Util.getData("in GroupBuyDetailActivity===get groupbuy from intent===");
		Util.groupbuyData(mGroup);
		this.initView();

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.groupbuy_detail_menu, menu);
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
