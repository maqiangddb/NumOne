package com.some.locallife.app;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.some.locallife.data.LocalLife;
import com.some.locallife.data.type.Category;
import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.data.type.Shop;
import com.some.locallife.ui.widget.ShopListAdapter;

public class ShopListActivity extends LoadableListActivity {

	public static final String INTENT_EXTRA_DISTANCE = "com.some.locallife.ShopListActivity.INTENT_EXTRA_DISTANCE";
	public static final String INTENT_EXTRA_SPACE = "com.some.locallife.ShopListActivity.INTENT_EXTRA_SPACE";
	private ShopListHolder mHolder;
	private ShopListAdapter mAdapter;

	String[] mDistanceValues = {
			"500",
			"1000",
			"2000",
			"5000"
	};

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		//this.setTitle(R.string.shop_list_activity_title);
		Object retained = this.getLastNonConfigurationInstance();
		//if (retained != null )
		this.mHolder = new ShopListHolder(this);
		init();



	}

	private void bindAdapter() {
		this.getListView().setAdapter(mAdapter);
	}

	private void init() {
		//this.mAdapter = new ShopListAdapter(this,
		//		((LocalLifeApplication)this.getApplication()).getRemoteResourceManager(), mHolder);
		//this.mHolder.mShopsHolder.setAdapter(mAdapter);
		initUI();
	}

	private void initUI() {
		this.mHolder.getActivityViewHolder().mSpinner1 = (Spinner) findViewById(R.id.filter_spinner_1);
		this.mHolder.getActivityViewHolder().mSpinner2 = (Spinner) findViewById(R.id.filter_spinner_2);
		this.mHolder.getActivityViewHolder().mSpinner3 = (Spinner) findViewById(R.id.filter_spinner_3);
		ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1);
		String[] sortStrings = this.getResources().getStringArray(R.array.sort_show_string);
		for(int i = 0; i < sortStrings.length; i++) {
			adapter.add(sortStrings[i]);
		}
		this.mHolder.mActivityViewHolder.mSpinner3.setAdapter(adapter);
		/*
		this.mHolder.mActivityViewHolder.mSpinner3.setOnItemClickListener(
				new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

					}});
					*/
		if (this.mHolder.ready()) {
		this.filterIntent();
		} else {
			this.mHolder.requestCategorys();
			this.mHolder.requestShops();
		}
	}

	private void filterIntent () {
		Intent intent = this.getIntent();
		if (intent.hasExtra(INTENT_EXTRA_DISTANCE)) {
			this.mHolder.setDistanceFilter(this.mDistanceValues);
		} else if (intent.hasExtra(INTENT_EXTRA_SPACE)) {
			this.mHolder.setSpaceFilter();
		}
	}

	private void ensureUi() {

		String title = this.getString(R.string.shop_list_title_distance);
		this.setTitle(title);
	}

	@Override
	public void onResume() {
		super.onResume();
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
		return null;
	}

	private static class CategoryHolder implements BaseStateHolder {

		Group<Category> mCategory;
		TaskCategory mTask;
		ShopListActivity mActivity;

		private CategoryHolder(Context context) {
			mTask = new TaskCategory(this);
			mTask.setContext(context);
			this.mActivity = (ShopListActivity) context;
		}

		@Override
		public void setData(LocalType data) {
			// TODO Auto-generated method stub
			this.mCategory = (Group<Category>) data;
		}

		public void setGroupCategory(Group<Category> data) {
			this.mCategory = data;
			this.mActivity.setProgressBarIndeterminate(false);
			this.mActivity.filterIntent();
		}

		@Override
		public void startTask() {
			// TODO Auto-generated method stub
			this.mTask.execute();
			this.mActivity.setProgressBarIndeterminate(true);
			this.mActivity.setLoadingView();
		}

		@Override
		public void onStartTask() {
			// TODO Auto-generated method stub

		}

	}

	private static class ShopsHolder implements BaseStateHolder {


		Group<Shop> mShops;
		TaskShops mTask;
		ShopListAdapter mAdapter;
		ShopListActivity mActivity;

		private ShopsHolder(Context context) {
			mTask = new TaskShops(this);
			this.mActivity = (ShopListActivity) context;
			this.mTask.setContext(context);
		}

		public void setAdapter(ShopListAdapter adapter) {
			this.mAdapter = adapter;
		}

		public void setCategoryId(String categoryId) {
			this.mTask.setCategoryId(categoryId);
		}


		@Override
		public void setData(LocalType data) {
			// TODO Auto-generated method stub
			this.mShops = (Group<Shop>) data;
			this.mActivity.bindAdapter();
			this.mActivity.setProgressBarIndeterminateVisibility(false);
		}

		@Override
		public void startTask() {
			// TODO Auto-generated method stub
			this.mActivity.setProgressBarIndeterminateVisibility(true);
			this.mActivity.setLoadingView();
			this.mTask.execute();

		}

		@Override
		public void onStartTask() {
			// TODO Auto-generated method stub

		}


	}

	private static class ShopListHolder extends BaseHolder {

		private ShopListItemViewHolder mItemViewHolder;
		private ShopListActivityFilterViewHolder mActivityViewHolder;
		private ShopsHolder mShopsHolder;
		private CategoryHolder mCategoryHolder;
		private Context mContext;


		private static class ShopListItemViewHolder implements ViewHolder {
			TextView mShopName;
			Button mShopTeleNumBtn;
			ImageView mShopImage;

		}
		private static class ShopListActivityFilterViewHolder implements ViewHolder {
			Spinner mSpinner1;
			Spinner mSpinner2;
			Spinner mSpinner3;
		}

		public ShopListHolder(Context context) {
			super(context);
			this.mContext = context;
			this.mConvertViewLayout = R.layout.shop_list_item;
			this.init();
			this.mItemViewHolder = new ShopListItemViewHolder();
			this.mActivityViewHolder = new ShopListActivityFilterViewHolder();

			this.mShopsHolder = new ShopsHolder(context);
			this.mCategoryHolder = new CategoryHolder(context);

		}

		public ShopListItemViewHolder getItemViewHolder () {
			return this.mItemViewHolder;
		}

		public ShopListActivityFilterViewHolder getActivityViewHolder() {
			return this.mActivityViewHolder;
		}

		public void setDistanceFilter(String[] distanceValues) {
			ArrayAdapter<String> adapter = new ArrayAdapter(mContext,
					android.R.layout.simple_expandable_list_item_1);
			this.mActivityViewHolder.mSpinner1.setAdapter(adapter);
			for(int i=0;i < distanceValues.length; i++) {
				adapter.add(distanceValues[i]);
			}
			this.mActivityViewHolder.mSpinner1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub

				}});

		}

		private boolean ready() {
			if (this.mCategoryHolder.mCategory != null && this.mShopsHolder.mShops != null) {
				return true;
			} else {
				return false;
			}
		}

		public void setSpaceFilter() {
			if (!ready()) return;
			ArrayAdapter<String> adapter = new ArrayAdapter(mContext,
					android.R.layout.simple_expandable_list_item_1);
			this.mActivityViewHolder.mSpinner1.setAdapter(adapter);
			int size = this.mCategoryHolder.mCategory.size();
			for(int i = 0; i < size; i++) {
				String name = this.mCategoryHolder.mCategory.get(i).getName();
				adapter.add(name);
			}
			this.mActivityViewHolder.mSpinner1.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub

				}});

		}

		public void requestCategorys() {
			this.mCategoryHolder.startTask();
		}

		public void requestShops() {
			this.mShopsHolder.startTask();
		}


		@Override
		public void doUpdateView(int position) {
			// TODO Auto-generated method stub
			this.mItemViewHolder.mShopName.setText(
					this.mShopsHolder.mShops.get(position).getName());
			final String teleNum = this.mShopsHolder.mShops.get(position).getTeleNum();
			this.mItemViewHolder.mShopTeleNumBtn.setText(
					teleNum);
			this.mItemViewHolder.mShopTeleNumBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(Intent.ACTION_CALL,
							Uri.parse("tel:"+teleNum));
					ShopListHolder.this.mContext.startActivity(intent);
				}
			});
		}

		@Override
		public void doBindView(View convertView) {
			// TODO Auto-generated method stub
			if (convertView == null) {
				android.util.Log.i("MQ","convertView is null");
				if (this.mConvertView != null) {
					android.util.Log.i("MQ", "mConvertView != null");
					convertView = mConvertView;
				}
				this.mItemViewHolder.mShopName = (TextView) convertView.findViewById(R.id.shop_name);
				this.mItemViewHolder.mShopImage = (ImageView) convertView.findViewById(R.id.shop_picture);
				this.mItemViewHolder.mShopTeleNumBtn = (Button) convertView.findViewById(R.id.shop_tele_num);
			}

		}

		@Override
		public int doGetCount() {
			// TODO Auto-generated method stub
			return this.mShopsHolder.mShops.size();
		}

		@Override
		public Object doGetItem(int position) {
			// TODO Auto-generated method stub
			return this.mShopsHolder.mShops.get(position);
		}



	}

	private static class TaskShops extends BaseTask {
		private String mCategoryId;

		public TaskShops(BaseStateHolder holder) {
			super(holder);
			// TODO Auto-generated constructor stub
		}

		public void setCategoryId(String categoryId) {
			this.mCategoryId = categoryId;
		}

		@Override
		public LocalType doTask() {
			// TODO Auto-generated method stub
			try {
				return this.mApi.getShops(this.mCategoryId);
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
		}

	}

	private static class TaskCategory extends BaseTask {

		String bigCategoryId;

		public TaskCategory(BaseStateHolder holder) {
			super(holder);
			// TODO Auto-generated constructor stub
		}

		public boolean ready() {
			if (this.bigCategoryId == null) {
				return false;
			} else {
				return true;
			}
		}

		@Override
		public LocalType doTask() {
			// TODO Auto-generated method stub
			try {
				return this.mApi.getCategory(bigCategoryId);
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
		}

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