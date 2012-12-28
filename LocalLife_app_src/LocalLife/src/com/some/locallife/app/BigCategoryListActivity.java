package com.some.locallife.app;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.R;
import com.some.locallife.data.LocalLife;
import com.some.locallife.data.type.BigCategory;
import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.ui.widget.BigCategoryListAdapter;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

public class BigCategoryListActivity extends ListActivity {
	private StateHolder mHolder;
	private ArrayAdapter<String> mAdapter;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		//this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		this.setContentView(R.layout.big_category_list_activity);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView) this.findViewById(R.id.titleName);
		tv.setText(R.string.big_category_list_title);
		this.setTitle(R.string.big_category_list_title);
		this.init();

	}

	private void init() {
		this.mAdapter = new ArrayAdapter<String> (this, R.layout.big_category_list_item, R.id.big_category_name);


		//this.getSpinnerContent().setVisibility(View.GONE);
		//sp1.setVisibility(View.GONE);
		this.mHolder = new StateHolder(this);
		this.mHolder.mDataHolder.startTask();
		this.mHolder.mDataHolder.setAdapter(mAdapter);
		this.getListView().setAdapter(mAdapter);
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				String bigCategoryId = BigCategoryListActivity.this.mHolder.mDataHolder.getData().get(arg2).getId();
				String name = BigCategoryListActivity.this.mHolder.mDataHolder.getData().get(arg2).getName();
				android.util.Log.i("MQ","bigCategory clicked==id:"+bigCategoryId+"=="+name);
				Intent intent = new Intent(BigCategoryListActivity.this, CategoryListActivity.class);
				intent.putExtra(CategoryListActivity.EXTRA_BIG_CATEGORY_ID, bigCategoryId);
				BigCategoryListActivity.this.startActivity(intent);
			}});
	}



	private static class DataHolder implements BaseStateHolder {

		private boolean doingTask;
		Group<BigCategory> mData;
		TaskBigCategory mTask;
		private BigCategoryListActivity mActivity;

		ArrayAdapter mAdapter;


		private DataHolder(Context context) {
			mTask = new TaskBigCategory(this);
			this.mTask.setContext(context);
			this.mActivity = (BigCategoryListActivity) context;
		}

		@Override
		public void setData(LocalType data) {
			// TODO Auto-generated method stub
			this.mData = (Group<BigCategory>) data;
			this.doingTask = false;
			for (int i = 0; i < this.mData.size(); i++) {
				android.util.Log.i("MQ","setData====="+this.mData.get(i).getName());
				this.mAdapter.add(this.mData.get(i).getName());
			}
			this.mActivity.setProgressBarIndeterminate(false);
		}

		public void setAdapter(ArrayAdapter adapter) {
			this.mAdapter = adapter;
		}

		public Group<BigCategory> getData() {
			return this.mData;
		}


		@Override
		public void startTask() {
			// TODO Auto-generated method stub
			this.mActivity.setProgressBarIndeterminate(true);
			this.mTask.execute();

		}

		@Override
		public void onStartTask() {
			// TODO Auto-generated method stub
			this.doingTask = true;

		}

		public boolean getTaskState() {
			return this.doingTask;
		}

	}


	private static class StateHolder extends BaseHolder{
		private boolean doingTask = false;

		DataHolder mDataHolder;
		Context mContext;


		public StateHolder (Context context) {
			super(context);
			mDataHolder = new DataHolder(context);
			this.mContext = context;
			this.mConvertViewLayout = R.layout.big_category_list_item;
			this.init();
		}




		@Override
		public void doUpdateView(int position) {
			// TODO Auto-generated method stub

		}

		@Override
		public void doBindView(View convertView) {
			// TODO Auto-generated method stub

		}

		@Override
		public int doGetCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object doGetItem(int position) {
			// TODO Auto-generated method stub
			return this.mDataHolder.getData().get(position);
		}


	}

	private static class TaskBigCategory extends BaseTask {


		public TaskBigCategory(BaseStateHolder holder) {
			super(holder);
			// TODO Auto-generated constructor stub
		}

		@Override
		public LocalType doTask() {
			// TODO Auto-generated method stub
			if (mContext == null) return null;
			LocalLifeApplication localApp = (LocalLifeApplication) this.mContext.getApplicationContext();
			LocalLife local = localApp.getLocalLife();
			try {
				android.util.Log.i("MQ","TASK do task ok");
				return local.getBigCategory();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}

	}



}
