package com.some.locallife.app;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.R;
import com.some.locallife.data.LocalLife;
import com.some.locallife.data.type.Category;
import com.some.locallife.data.type.District;
import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.util.TaskManager.BaseDataTask;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CategoryListActivity extends ListActivity {
	public static final String EXTRA_BIG_CATEGORY_ID = "com.some.locallife.app.CategoryListActivity.EXTRA_BIG_CATEGORY_ID";
	private StateHolder mHolder;
	private ArrayAdapter<String> mAdapter;
	private Group<District> mDistricts;
	private BaseDataTask mDistrictsTask;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		//this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		this.setContentView(R.layout.category_list_activity);
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView)this.findViewById(R.id.titleName);
		tv.setText(R.string.category_list_title);
		this.setTitle(R.string.category_list_title);
		this.init();
	}

	private String[] getCategorys() {
		int size = this.mHolder.mDataHolder.mData.size();
		String[] data = new String[size];
		for (int i = 0; i < size;i++) {
			data[i] = this.mHolder.mDataHolder.mData.get(i).getName();
		}
		return data;
	}

	private String[] getCategoryIds() {
		String[] data = new String[this.mHolder.mDataHolder.mData.size()];
		for (int i = 0; i < this.mHolder.mDataHolder.mData.size();i++) {
			data[i] = this.mHolder.mDataHolder.mData.get(i).getId();
		}
		return data;
	}

	private void init() {
		this.mAdapter = new ArrayAdapter<String> (this, R.layout.category_list_item, R.id.category_name);
		if (this.getIntent().hasExtra(EXTRA_BIG_CATEGORY_ID)) {
		this.mHolder = new StateHolder(this,
				this.getIntent().getStringExtra(EXTRA_BIG_CATEGORY_ID));
		} else {
			android.util.Log.e("MQ","missing big category id===error");
			this.finish();
		}
		this.mHolder.mDataHolder.startTask();
		this.mHolder.mDataHolder.setAdapter(this.mAdapter);
		this.getListView().setAdapter(mAdapter);
		this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int id,
					long arg3) {
				// TODO Auto-generated method stub
				Category category = CategoryListActivity.this.mHolder.mDataHolder.mData.get(id);
				String categoryId = category.getId();
				String categoryName = category.getName();
				String bigCategoryId = category.getParentId();
				String[] categorys = CategoryListActivity.this.getCategorys();
				String[] categoryIds = CategoryListActivity.this.getCategoryIds();
				android.util.Log.i("MQ","category clicked====id:"+categoryId+"=="+categoryName);
				Intent intent = new Intent(CategoryListActivity.this, NewShopListActivity.class);
				intent.putExtra(NewShopListActivity.EXTRA_CATEGORY_ID, categoryId);
				intent.putExtra(NewShopListActivity.EXTRA_BIG_CATEGORY_ID, bigCategoryId);
				intent.putExtra(NewShopListActivity.EXTRA_CATEGORYS, categorys);
				intent.putExtra(NewShopListActivity.EXTRA_CATEGORY_IDS, categoryIds);
				CategoryListActivity.this.startActivity(intent);
			}});

	}

	private static class DataHolder implements BaseStateHolder {

		private Group<Category> mData;
		private TaskCategorys mTask;
		private CategoryListActivity mActivity;
		ArrayAdapter mAdapter;
		private boolean doingTask;

		public DataHolder (Context context,String bigCategoryId) {
			this.mActivity = (CategoryListActivity) context;
			this.mTask = new TaskCategorys(this, bigCategoryId);
			this.mTask.setContext(context);
		}

		public void setAdapter(ArrayAdapter<String> adapter) {
			// TODO Auto-generated method stub
			this.mAdapter = adapter;
		}

		@Override
		public void setData(LocalType data) {
			// TODO Auto-generated method stub
			this.mData = (Group<Category>) data;
			this.doingTask = false;
			for (int i = 0; i< this.mData.size(); i++) {
				this.mAdapter.add(this.mData.get(i).getName());
			}
			this.mActivity.setProgressBarIndeterminate(false);
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

		}

	}

	private static class StateHolder extends BaseHolder {
		Context mContext;
		DataHolder mDataHolder;

		public StateHolder(Context context, String bigCategoryId) {
			super(context);
			// TODO Auto-generated constructor stub
			this.mContext = context;
			this.mDataHolder = new DataHolder(context, bigCategoryId);
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
			return null;
		}

	}

	private static class TaskCategorys extends BaseTask {

		private String mBigCategoryId;

		public TaskCategorys(BaseStateHolder holder, String id) {
			super(holder);
			// TODO Auto-generated constructor stub
			this.mBigCategoryId = id;
		}

		@Override
		public LocalType doTask() {
			// TODO Auto-generated method stub
			if (mContext == null) {
				android.util.Log.e("MQ","category task going to be run, but there is no Context!!");
				return null;
				}
			LocalLifeApplication localApp = (LocalLifeApplication) this.mContext.getApplicationContext();
			LocalLife local = localApp.getLocalLife();
			try {
				return local.getCategory(this.mBigCategoryId);
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
