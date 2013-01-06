package com.some.locallife.app;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.R;
import com.some.locallife.data.LocalLife;
import com.some.locallife.data.error.LocalException;
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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class GroupBuyListActivity extends LoadableListActivity {
	public static final String EXTRA_IS_FAV = "com.some.locallife.GroupBuyListActivity.EXTRA_IS_FAV";

	private BaseDataTask mGroupBuyListTask;
	private LocalLifeApplication mApp;
	private LocalLife mApi;
	private DataHolder mDataHolder;
	private MyHandler mHandler = new MyHandler();
	private GroupBuyListAdapter mAdapter;
	private ViewGroup mLayoutEmpty;
	private boolean runOnce = false;

	private static class DataHolder{
		Group<GroupBuy> mGroupBuys;
		Group<GroupBuy> mFavGroupBuys;
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
				Group<GroupBuy> data;
				if(GroupBuyListActivity.this.isFav) {
					data = GroupBuyListActivity.this.mDataHolder.mFavGroupBuys;
				} else {
					data = GroupBuyListActivity.this.mDataHolder.mGroupBuys;
				}
				GroupBuyListActivity.this.bindData(data);
				break;
			}

		}
	}

	private boolean isFav = false;
	private BaseDataTask mFavGroupBuyTask;

	@Override
	protected void onCreate(Bundle bundle) {
		if (this.getIntent().hasExtra(EXTRA_IS_FAV)) {
			this.isFav = true;
		}
		if(!isFav) {
			requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		}
		super.onCreate(bundle);

		this.mApp = (LocalLifeApplication) this.getApplication();
		this.mApi = this.mApp.getLocalLife();

		if(!this.isFav) {
		//set Activity title
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView) this.findViewById(R.id.titleName);
		tv.setText(R.string.groupbuy_list_activity_title);
		//end
		} else {
			this.setTheme(android.R.style.Theme_NoTitleBar);
		}


		this.mLayoutEmpty = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.groupbuy_list_empty, null);
		Object retained = this.getLastNonConfigurationInstance();
		if(retained != null && retained instanceof DataHolder) {
			this.mDataHolder = (DataHolder) retained;
		} else {
			this.mDataHolder = new DataHolder();
		}
		this.initData();
	}

	private void initData() {
		this.mApp = (LocalLifeApplication) this.getApplication();
		RemoteResourceManager mRrm = this.mApp.getRemoteResourceManager();
		this.mAdapter = new GroupBuyListAdapter(this, mRrm);
		this.getListView().setAdapter(this.mAdapter);
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				GroupBuy groupbuy = (GroupBuy) parent.getAdapter().getItem(position);
				Intent intent = new Intent(GroupBuyListActivity.this, GroupBuyDetailActivity.class);
				intent.putExtra(GroupBuyDetailActivity.EXTRA_GROUP_BUY, groupbuy);
				GroupBuyListActivity.this.startActivity(intent);

			}});

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
							try {
								return GroupBuyListActivity.this.mApi.getGroupBuys();
							} catch (LocalException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
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
							GroupBuyListActivity.this.runOnce = true;
							GroupBuyListActivity.this.mDataHolder.mGroupBuys = (Group<GroupBuy>) data;

						} else {

						}
						Message msg = GroupBuyListActivity.this.mHandler.obtainMessage(MyHandler.MSG_BIND_DATA);
						GroupBuyListActivity.this.mHandler.sendMessage(msg);

					}});
		if(this.mFavGroupBuyTask == null) {
			this.mFavGroupBuyTask = this.mApp.getTaskManager().new BaseDataTask();
			this.mFavGroupBuyTask = this.mApp.getTaskManager().createTask(this.mFavGroupBuyTask,
					new DoTask() {

						@Override
						public LocalType doTask() {
							// TODO Auto-generated method stub
							Message msg = GroupBuyListActivity.this.mHandler.obtainMessage(MyHandler.MSG_START_PROGRESS_BAR);
							GroupBuyListActivity.this.mHandler.sendMessage(msg);
							try {
								return GroupBuyListActivity.this.mApi.getFavGroupBuyList();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (LocalException e) {
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
								GroupBuyListActivity.this.mDataHolder.mFavGroupBuys = (Group<GroupBuy>) data;

							} else {

							}
							Message msg = GroupBuyListActivity.this.mHandler.obtainMessage(MyHandler.MSG_BIND_DATA);
							GroupBuyListActivity.this.mHandler.sendMessage(msg);
						}});
		}
		if(this.mDataHolder.mGroupBuys == null) {
			this.mGroupBuyListTask.execute();
		}
		if(this.mDataHolder.mFavGroupBuys == null) {
			this.mFavGroupBuyTask.execute();
		}

	}

	private void bindData(Group<GroupBuy> data) {
		Util.getData("==bind Data in GroupBuyListActivity===");
		if(data == null) {
			Util.getData("there is no data at all!! in GroupBuyListActivity");
			this.mAdapter.setGroup(new Group<GroupBuy> ());
			this.setEmptyView(this.mLayoutEmpty);
			return;
		} else {
			boolean DEBUG = true;
			if(DEBUG) {
				for(int i = 0; i < data.size(); i++) {
					Util.getData(""+i+"=Discount="+data.get(i).getDiscount());
					Util.getData(""+i+"=Id="+data.get(i).getId());
					Util.getData(""+i+"=Msg="+data.get(i).getMsg());
					Util.getData(""+i+"=NowPrice="+data.get(i).getNowPrice());
					Util.getData(""+i+"=OldPrice="+data.get(i).getOldPrice());
					Util.getData(""+i+"=TeleNum="+data.get(i).getTeleNum());
					Util.getData(""+i+"=SavePrice="+data.get(i).getSavePrice());
				}
				this.mAdapter.setGroup(data);

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
