package com.some.locallife.app;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.R;
import com.some.locallife.data.LocalLife;
import com.some.locallife.data.type.Coupon;
import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.ui.widget.CouponListAdapter;
import com.some.locallife.util.RemoteResourceManager;
import com.some.locallife.util.TaskManager.BaseDataTask;
import com.some.locallife.util.Util;
import com.some.locallife.util.TaskManager.DoTask;
import com.some.locallife.util.TaskManager.SetData;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

public class CouponListActivity extends LoadableListActivity {

	BaseDataTask mCouponListTask;
	LocalLifeApplication mApp;
	LocalLife mApi;
	DataHolder mDataHolder;
	MyHandler mHandler = new MyHandler(Looper.getMainLooper());
	CouponListAdapter mAdapter;
	private ViewGroup mLayoutEmpty;

	private boolean runOnce = false;
	private boolean running = false;

	final class MyHandler extends Handler {

		static final int MSG_BIND_DATA = 1;

		public MyHandler(Looper mainLooper) {
			// TODO Auto-generated constructor stub
			super(mainLooper);
		}

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_BIND_DATA:
				CouponListActivity.this.bindData();
				break;

			}

		}
	}

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		this.mApp = (LocalLifeApplication) this.getApplication();
		this.mApi = this.mApp.getLocalLife();
		//set Activity title
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView) this.findViewById(R.id.titleName);
		tv.setText(R.string.coupon_list_activity_title);
		//end

		this.mLayoutEmpty = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.coupon_list_empty, null);
		Object retained = this.getLastNonConfigurationInstance();
		if(retained != null && retained instanceof DataHolder) {
			this.mDataHolder = (DataHolder) retained;
		} else {
			this.mDataHolder = new DataHolder();
		}



	}

	//step 1, check if data have received
	private void initData() {

		RemoteResourceManager mRrm = this.mApp.getRemoteResourceManager();
		this.mAdapter = new CouponListAdapter(this, mRrm);


		if(this.mCouponListTask == null)
			this.mCouponListTask = this.mApp.getTaskManager().new BaseDataTask();
		this.mCouponListTask = this.mApp.getTaskManager().createTask(this.mCouponListTask,
				new DoTask(){

			@Override
			public LocalType doTask() {
				// TODO Auto-generated method stub
				CouponListActivity.this.running = true;
				CouponListActivity.this.setLoadingView();
				LocalType data;
				try {
					data = CouponListActivity.this.mApi.getCoupons();
					return data;
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}},
			new SetData(){

				@Override
				public void setData(LocalType data) {
					// TODO Auto-generated method stub
					if (data != null) {
						CouponListActivity.this.runOnce = true;

					} else {
						Util.getData("can not get coupon list data!!! bad bad bad");
						finish();//just for test
					}
					CouponListActivity.this.mDataHolder.mCoupons = (Group<Coupon>) data;
					if(!CouponListActivity.this.mHandler.hasMessages(MyHandler.MSG_BIND_DATA)) {
						Message msg = mHandler.obtainMessage(MyHandler.MSG_BIND_DATA);
						mHandler.sendMessage(msg);
					}
					CouponListActivity.this.running = false;
				}});

		if(!this.runOnce) {
			this.mCouponListTask.execute();
		}

	}

	private void bindData() {
		if (this.mDataHolder.mCoupons == null) {
			Util.getData("there is no data at all!!");
			this.mAdapter.setGroup(new Group<Coupon> ());
			this.setEmptyView(this.mLayoutEmpty);
			return;
		} else {
			boolean DEBUG = true;
			if(DEBUG) {
				for (int i = 0; i < this.mDataHolder.mCoupons.size(); i++) {
					Coupon coupon = this.mDataHolder.mCoupons.get(i);
					Util.getData("coupon data="+i+"=Id="+coupon.getId());
					Util.getData("coupon data="+i+"=ImageUrl="+coupon.getImageUrl());
					Util.getData("coupon data="+i+"=LifeTime="+coupon.getLifeTime());
					Util.getData("coupon data="+i+"=Msg="+coupon.getMsg());
					Util.getData("coupon data="+i+"=PageValue="+coupon.getPageValue());
					Util.getData("coupon data="+i+"=Price="+coupon.getPrice());
					Util.getData("coupon data="+i+"=TeleNum="+coupon.getTeleNum());
				}
				this.mAdapter.setGroup(this.mDataHolder.mCoupons);
				this.getListView().setAdapter(this.mAdapter);
			}
		}

	}

	private void ensureUi() {

	}


	@Override
	public void doOnResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doOnPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object doOnRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		return this.mDataHolder;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doInitSpinner() {
		// TODO Auto-generated method stub

	}

	private static class DataHolder{
		Group<Coupon> mCoupons;
	}

}
