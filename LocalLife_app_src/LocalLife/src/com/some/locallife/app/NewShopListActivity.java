package com.some.locallife.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.R;
import com.some.locallife.data.LocalLife;
import com.some.locallife.data.type.Category;
import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.data.type.Shop;
import com.some.locallife.ui.widget.ShopListAdapter;
import com.some.locallife.util.TaskManager.DoTask;
import com.some.locallife.util.TaskManager.SetData;
import com.some.locallife.util.Util;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class NewShopListActivity extends LoadableListActivity implements AdapterView.OnItemSelectedListener{

	public static final String EXTRA_CATEGORY_ID = "com.some.locallife.NewShopListActivity.EXTRA_CATEGORY_ID";
	public static final String EXTRA_BIG_CATEGORY_ID = "com.some.locallife.NewShopListActivity.EXTRA_BIG_CATEGORY_ID";
	public static final String EXTRA_CATEGORYS = "com.some.locallife.NewShopListActivity.EXTRA_CATEGORYS";
	public static final String EXTRA_CATEGORY_IDS = "com.some.locallife.NewShopListActivity.EXTRA_CATEGORY_IDS";
	public static final String EXTRA_DISTANCE = "com.some.locallife.NewShopListActivity.EXTRA_DISTANCE";
	public static final String EXTRA_LOCATION = "com.some.locallife.NewShopListActivity.EXTRA_LOCATION";
	private StateHolder mStateHolder;
	private SearchLocationObserver mSearchLocationObserver = new SearchLocationObserver();
	private ViewGroup mLayoutEmpty;

	private ShopListAdapter mListAdapter;
	private boolean useLocation;
	private String mDistance;

/*
	private Spinner mSp1;
	private Spinner mSp2;
	private Spinner mSp3;
*/
	private ArrayList<String> mSp1Array ;
	private ArrayList<String> mSp3Array ;
	private ArrayList<String> mSp2Array ;

	private String[] mCategorys;
	private String[] mDistricts;
	private String[] mCategoryIds;

	private LocalLifeApplication mApp;



	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		//first of all, register receivers
		this.mApp = (LocalLifeApplication) this.getApplication();
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView) this.findViewById(R.id.titleName);
		tv.setText(R.string.shop_list_activity_title);
		//this.setTitle(R.string.shop_list_activity_title);
		Object retained = this.getLastNonConfigurationInstance();
		if(retained != null && retained instanceof StateHolder) {
			this.mStateHolder = (StateHolder) retained;
			this.mStateHolder.setActivity(this);
		} else {
				if (this.getIntent().hasExtra(EXTRA_CATEGORY_ID)) {
					android.util.Log.i("MQ","start shop list==get CategoryId:"+
							this.getIntent().getStringExtra(EXTRA_CATEGORY_ID));
					this.mStateHolder = new StateHolder(
							this.getIntent().getStringExtra(EXTRA_CATEGORY_ID),
							this.getIntent().getStringExtra(EXTRA_BIG_CATEGORY_ID));
					this.mCategoryIds = this.getIntent().getStringArrayExtra(EXTRA_CATEGORY_IDS);
					this.mCategorys = this.getIntent().getStringArrayExtra(EXTRA_CATEGORYS);
					this.useLocation = false;
				} else if (this.getIntent().hasExtra(EXTRA_DISTANCE)) {
					this.mStateHolder = new StateHolder(
							null, null);
					this.mDistance = this.getIntent().getStringExtra(EXTRA_DISTANCE);
					this.useLocation = true;
				}
		}


		this.mLayoutEmpty = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.shop_list_empty, null);
		if(!this.mStateHolder.getIsRunningTask())
		this.mStateHolder.startTaskShops(this, this.useLocation);
		this.ensureUi();
		this.initSpinnerUI();
	}

	@Override
	public void doOnResume() {
		// TODO Auto-generated method stub
		((LocalLifeApplication)this.getApplication()).requestLocationUpdates(mSearchLocationObserver);
	}

	private void initSpinnerUI () {
		initSpinnerArray();
		ArrayAdapter<String> spa1 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, mSp1Array);
		this.mSp1.setAdapter(spa1);
		if(this.useLocation) {
			this.mSp1.setSelection(1);//default is 1000
		} else {

		}

		if(!this.useLocation) {
		ArrayAdapter<String> spa2 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, mSp2Array);
		this.mSp2.setAdapter(spa2);
		} else {

		}

		//ArrayAdapter<String> spa3 = new ArrayAdapter<String> (this, android.R.layout.simple_spinner_item, mSp3Array);
		//this.mSp1.setAdapter(spa3);
	}

	private void initSpinnerArray() {
		if (this.useLocation) {
			this.mSp2.setVisibility(View.GONE);
			this.mSp1Array = new ArrayList<String>(
					Arrays.asList(
							this.getResources().getStringArray(R.array.distance_array)
							)
							);

		} else {

			String[] data = this.getDistrictArray();
			this.mSp1Array = new ArrayList<String> (Arrays.asList(data));
			this.mSp2Array = new ArrayList<String>(
					Arrays.asList(this.getCategoryArray())
					);
		}


		this.mSp3Array = new ArrayList<String>(
				Arrays.asList(this.getResources().getStringArray(R.array.sort_string))
				);
	}

	private String[] getCategoryArray() {
		if (this.mCategorys != null) {
			return this.mCategorys;
		} else {
			return null;
		}

	}

	private String[] getDistrictArray() {
    	if (this.mApp.getDistrictData() == null) {
    		Util.getData("didn't get district data in NewShopListActivity!!!bad bad bad");
    		return null;
    	}
    	if (this.mDistricts == null) {
    	int length = this.mApp.getDistrictData().size();
    	String[] data = new String[length];
    	for(int i = 0; i < length; i++) {
    		data[i] = this.mApp.getDistrictData().get(i).getName();
    	}
    	this.mDistricts = data;

    	}
    	return this.mDistricts;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//unregisterReceiver
	}

	@Override
	public void doOnPause() {
		// TODO Auto-generated method stub
		((LocalLifeApplication)this.getApplication()).removeLocationUpdates(this.mSearchLocationObserver);
		if (this.isFinishing()) {
			this.mStateHolder.cancelTasks();
			this.mListAdapter.removeObserver();
		}

	}

	@Override
	public Object doOnRetainNonConfigurationInstance() {
		// TODO Auto-generated method stub
		this.mStateHolder.setActivity(null);
		return mStateHolder;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);

	}

	private void ensureUi() {
		this.mListAdapter = new ShopListAdapter(this,
				((LocalLifeApplication)this.getApplication()).getRemoteResourceManager());
		this.mListAdapter.setGroup(this.mStateHolder.getShops());

		ListView listView = this.getListView();
		listView.setAdapter(mListAdapter);
		listView.setSmoothScrollbarEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Shop shop = (Shop) parent.getAdapter().getItem(position);
				String shopId = shop.getId();
				Intent intent = new Intent(NewShopListActivity.this, ShopDetailsActivity.class);
				intent.putExtra(ShopDetailsActivity.EXTRA_SHOP_ID, shopId);
				NewShopListActivity.this.startActivity(intent);

			}});
		if(this.mStateHolder.getIsRunningTask()) {
			this.setLoadingView();
		} else if (this.mStateHolder.getFetchedShopsOnce() && this.mStateHolder.getShops().size() == 0) {
			this.setEmptyView(this.mLayoutEmpty);
		}

		this.setTitle(this.getString(R.string.shop_list_activity_title));
	}


	private static class StateHolder {

		Group<Shop> mShops;
		String mCategoryId;

		private boolean mFetchedOnce;
		private boolean mIsRunningTask;

		private TaskShops mTaskShops;

		public StateHolder(String category, String bigCategory) {
			this.mCategoryId = category;
		}

		public void setCategoryId(String categoryId) {
			this.mCategoryId = categoryId;
		}

		public void setActivity(NewShopListActivity activity) {
			// TODO Auto-generated method stub
			if (this.mTaskShops != null) {
				this.mTaskShops.setActivity(activity);
			}
		}

		public void startTaskShops(NewShopListActivity activity,boolean useLocation) {
			// TODO Auto-generated method stub
			Util.getData("start task==="+useLocation);
			this.mIsRunningTask = true;
			this.mTaskShops = new TaskShops(activity, useLocation);

			this.mTaskShops.execute(this.mCategoryId);
		}

		public void cancelTasks() {
			// TODO Auto-generated method stub
			if(this.mTaskShops != null) {
				this.mTaskShops.setActivity(null);
				this.mTaskShops.cancel(true);
			}
		}

		public boolean getIsRunningTask() {
			// TODO Auto-generated method stub
			return this.mIsRunningTask;
		}

		public boolean getFetchedShopsOnce() {
			// TODO Auto-generated method stub
			return this.mFetchedOnce;
		}

		public Group<Shop> getShops() {
			// TODO Auto-generated method stub
			return mShops;
		}

		public void setShops(Group<Shop> shops) {
			// TODO Auto-generated method stub
			this.mShops = shops;
		}

		public void setIsRunningFriendsTask(boolean b) {
			// TODO Auto-generated method stub
			this.mIsRunningTask = b;
		}

		public void setFetchedFriendsOnce(boolean b) {
			// TODO Auto-generated method stub
			this.mFetchedOnce = b;
		}

	}

	private class SearchLocationObserver implements Observer {

		@Override
		public void update(Observable observable, Object data) {
			// TODO Auto-generated method stub

		}

	}

	private static class TaskShops extends AsyncTask<String, Void, Group<Shop>> {
		private NewShopListActivity mActivity;
		private Exception mReason;
		private boolean useLocation;
		private String mReverseGeoLoc;

		public TaskShops(NewShopListActivity activity, boolean useLocation) {
			this.mActivity = activity;
			this.useLocation = useLocation;
		}

		public void setActivity(NewShopListActivity activity) {
			this.mActivity = activity;
		}

		@Override
		protected void onPreExecute() {
			this.mActivity.setLoadingView();
		}

		@Override
		public void onPostExecute(Group<Shop> shops) {
			if(this.mActivity != null) {
				this.mActivity.onShopsTaskComplete(shops, mReason);
			}
		}

		@Override
		protected void onCancelled() {
			if(this.mActivity != null) {
				this.mActivity.onShopsTaskComplete(null, mReason);
			}
		}

		@Override
		protected Group<Shop> doInBackground(String... params) {
			// TODO Auto-generated method stub
			LocalLifeApplication app = (LocalLifeApplication) this.mActivity.getApplication();
			LocalLife api = app.getLocalLife();
			Util.log("shoplist task do=="+useLocation);
			try {
				if(this.useLocation) {
					Util.log("1");
					Location location = app.getLastKnownLocation();
					Util.log("2");
					if (location == null) {
						//throw new Exception("Didn't have location data!!!");
					}

					Util.log("3");
					String latitude = null;
					String longitude = null;
					if (location != null) {
					mReverseGeoLoc = getGeocode(this.mActivity, location);
					latitude = String.valueOf(location.getLatitude());
					longitude = String.valueOf(location.getLongitude());
					}
					String distance = this.mActivity.getDistance();
					Util.log("start get shoplist task===latitude:"+latitude+"=longitude:"+longitude+"=distance:"+distance);
					return api.getShops(latitude, longitude, distance);
				} else {
					Util.log("did not use location");
					Group<Shop> data = api.getShops(params[0]);
					Util.log("data=="+data.size()+"item1:name:"+data.get(0).getName());
					for(int i=0; i<data.size(); i++) {
						Util.log("item"+i+"==name"+data.get(i).getName());
					}
				return data;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				this.mReason = e;
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				this.mReason = e;
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				this.mReason = e;
				e.printStackTrace();
			} catch (Exception e) {

			}
			return null;
		}

		private String getGeocode(Context context,
				Location location) {
			// TODO Auto-generated method stub
			Geocoder geocoded = new Geocoder(context, Locale.getDefault());
			List<Address> addresses;
			try {
				addresses = geocoded.getFromLocation(
						location.getLatitude(), location.getLongitude(), 1);
				if (addresses.size() > 0) {
					Address address = addresses.get(0);

					StringBuilder sb = new StringBuilder(128);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			return null;
		}
	}

	public void onShopsTaskComplete(Group<Shop> shops, Exception mReason) {
		boolean DEBUG = true;
		if(DEBUG && shops != null) {
			for(int i=0; i<shops.size(); i++) {
				Shop shop = shops.get(i);
				Util.log("="+i+"=address:"+shop.getAddress());
				Util.log("="+i+"=average:"+shop.getAverage());
				Util.log("="+i+"=commentsid:"+shop.getCommentsId());
				Util.log("="+i+"=id:"+shop.getId());
				Util.log("="+i+"=imageurl:"+shop.getImageUrl());
				Util.log("="+i+"=name:"+shop.getName());
				Util.log("="+i+"=price:"+shop.getPrice());
				Util.log("="+i+"=teleNum:"+shop.getTeleNum());
				Util.log("="+i+"=type:"+shop.getType());
				Util.log("="+i+"=imageUrls:"+shop.getImageUrls());
			}
		}
		// TODO Auto-generated method stub
		this.mListAdapter.removeObserver();
		this.mListAdapter = new ShopListAdapter(this,
				((LocalLifeApplication)this.getApplication()).getRemoteResourceManager());
		if (shops != null) {
			Util.log("get data frome holder===size:"+shops.size());
			this.mStateHolder.setShops(shops);
			this.mListAdapter.setGroup(mStateHolder.getShops());
			this.getListView().setAdapter(this.mListAdapter);
		}
		else {
			this.mStateHolder.setShops(new Group<Shop>());
			this.mListAdapter.setGroup(this.mStateHolder.getShops());
			this.getListView().setAdapter(mListAdapter);
			android.util.Log.e("MQ","request data failed=="+mReason);
		}
		this.mStateHolder.setIsRunningFriendsTask(false);
		this.mStateHolder.setFetchedFriendsOnce(true);

		if (this.mStateHolder.getShops().size() == 0) {
			this.setEmptyView(this.mLayoutEmpty);
		}

	}

	public String getDistance() {
		// TODO Auto-generated method stub
		return this.mDistance;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == this.mSp1) {
			Util.log("mSp1 clicked on item="+arg2);
			if(this.useLocation) {
				this.onDistanceSpinnerClick(arg2, arg3);
			} else {
				this.onDistrictSpinnerClick(arg2, arg3);
			}
		} else if (arg0 == this.mSp2) {
			Util.log("mSp2 clicked on item="+arg2);
			this.onCategorySpinnerClick(arg2, arg3);
		} else if (arg0 == this.mSp3) {
			Util.log("mSp3 clicked on item="+arg2);
		}

	}

	private void onDistanceSpinnerClick(int position, long id) {
		String[] distances = this.getResources().getStringArray(R.array.distance_array);
		this.mDistance = distances[position];
		this.mStateHolder.startTaskShops(this, this.useLocation);
	}

	private void onDistrictSpinnerClick(int position, long id) {
		String[] districts = this.getDistrictArray();

	}

	private void onCategorySpinnerClick(int position, long id) {
		this.mStateHolder.setCategoryId(this.mCategoryIds[position]);
		this.mStateHolder.startTaskShops(this, this.useLocation);
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
