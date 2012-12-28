package com.some.locallife.app;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.R;
import com.some.locallife.data.LocalLife;
import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.GroupBuy;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.ui.widget.GroupBuyListAdapter;
import com.some.locallife.util.RemoteResourceManager;
import com.some.locallife.util.TaskManager.BaseDataTask;
import com.some.locallife.util.TaskManager.DoTask;
import com.some.locallife.util.TaskManager.SetData;
import com.some.locallife.util.Util;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.TextView;

public class GroupBuyListActivity extends LoadableListActivity {

	private BaseDataTask mGroupBuyListTask;
	private LocalLifeApplication mApp;
	private LocalLife mApi;
	private DataHolder mDataHolder;
	private MyHandler mHandler = new MyHandler();
	private GroupBuyListAdapter mAdapter;
	private ViewGroup mLayoutEmpty;

	private static class DataHolder{
		Group<GroupBuy> mGroupBuys;
	}

	final class MyHandler extends Handler {
		public static final int MSG_START_PROGRESS_BAR = 1;
		public static final int MSG_STOP_PROGRESS_BAR = 2;
		public static final int MSG_BIND_DATA = 3;

		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_START_PROGRESS_BAR:
				GroupBuyListActivity.this.setProgressBarIndeterminate(true);
				break;
			case MSG_STOP_PROGRESS_BAR:
				GroupBuyListActivity.this.setProgressBarIndeterminate(false);
				break;
			case MSG_BIND_DATA:
				GroupBuyListActivity.this.bindData();
				break;
			}

		}
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		this.mApp = (LocalLifeApplication) this.getApplication();
		this.mApi = this.mApp.getLocalLife();
		//set Activity title
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView) this.findViewById(R.id.titleName);
		tv.setText(R.string.groupbuy_list_activity_title);
		//end


		this.mLayoutEmpty = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.groupbuy_list_empty, null);
		Object retained = this.getLastNonConfigurationInstance();
		if(retained != null && retained instanceof DataHolder) {
			this.mDataHolder = (DataHolder) retained;
		} else {
			this.mDataHolder = new DataHolder();
		}
	}

	private void initData() {
		this.mApp = (LocalLifeApplication) this.getApplication();
		RemoteResourceManager mRrm = this.mApp.getRemoteResourceManager();
		this.mAdapter = new GroupBuyListAdapter(this, mRrm);
		this.mDataHolder = new DataHolder();
		this.mApi = this.mApp.getLocalLife();
		if(this.mGroupBuyListTask == null)
			this.mGroupBuyListTask = this.mApp.getTaskManager().new BaseDataTask();
		this.mGroupBuyListTask = this.mApp.getTaskManager().createTask(this.mGroupBuyListTask,
				new DoTask() {

					@Override
					public LocalType doTask() {
						// TODO Auto-generated method stub
						Message msg = GroupBuyListActivity.this.mHandler.obtainMessage(MyHandler.MSG_START_PROGRESS_BAR);
						GroupBuyListActivity.this.mHandler.sendMessage(msg);
						try {
							return GroupBuyListActivity.this.mApi.getGroupBuys();
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
				new SetData() {

					@Override
					public void setData(LocalType data) {
						// TODO Auto-generated method stub
						if(data != null) {
							GroupBuyListActivity.this.mDataHolder.mGroupBuys = (Group<GroupBuy>) data;

						} else {

						}
						Message msg = GroupBuyListActivity.this.mHandler.obtainMessage(MyHandler.MSG_BIND_DATA);
						GroupBuyListActivity.this.mHandler.sendMessage(msg);

					}});

	}

	private void bindData() {

		if(this.mDataHolder.mGroupBuys == null) {
			Util.getData("there is no data at all!! in GroupBuyListActivity");
		} else {
			boolean DEBUG = true;
			if(DEBUG) {
				for(int i = 0; i < this.mDataHolder.mGroupBuys.size(); i++) {
					Util.getData(""+i+"=Discount="+this.mDataHolder.mGroupBuys.get(i).getDiscount());
					Util.getData(""+i+"=Id="+this.mDataHolder.mGroupBuys.get(i).getId());
					Util.getData(""+i+"=Msg="+this.mDataHolder.mGroupBuys.get(i).getMsg());
					Util.getData(""+i+"=NowPrice="+this.mDataHolder.mGroupBuys.get(i).getNowPrice());
					Util.getData(""+i+"=OldPrice="+this.mDataHolder.mGroupBuys.get(i).getOldPrice());
					Util.getData(""+i+"=TeleNum="+this.mDataHolder.mGroupBuys.get(i).getTeleNum());
					Util.getData(""+i+"=SavePrice="+this.mDataHolder.mGroupBuys.get(i).getSavePrice());
				}
				this.mAdapter.setGroup(this.mDataHolder.mGroupBuys);
			}
		}
		Message msg = this.mHandler.obtainMessage(MyHandler.MSG_STOP_PROGRESS_BAR);
		this.mHandler.sendMessage(msg);
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

}
