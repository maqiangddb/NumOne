package com.some.locallife;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.R;
import com.some.locallife.app.BaseStateHolder;
import com.some.locallife.app.BaseTask;
import com.some.locallife.app.BigCategoryListActivity;
import com.some.locallife.app.CouponListActivity;
import com.some.locallife.app.GroupBuyListActivity;
import com.some.locallife.app.LocalLifeApplication;
import com.some.locallife.app.NewShopListActivity;
import com.some.locallife.app.ShopListActivity;
import com.some.locallife.data.LocalLife;
import com.some.locallife.data.type.BigCategory;
import com.some.locallife.data.type.Category;
import com.some.locallife.data.type.City;
import com.some.locallife.data.type.District;
import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.data.type.Province;
import com.some.locallife.data.type.Test;
import com.some.locallife.util.TaskManager.DoTask;
import com.some.locallife.util.TaskManager.SetData;
import com.some.locallife.util.Util;
import com.some.locallife.util.TaskManager.BaseDataTask;

import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface.OnMultiChoiceClickListener;

public class MainActivity extends Activity {

	private final static int STATE_TEST = 0;
	private final static int STATE_GRID = 1;

	private static final int DLG_BASE = 0;
	private static final int DLG_NO_INTERNET_CONNECT = DLG_BASE + 1;
	private static final int DLG_NO_LOCATION_PROVIDER = DLG_BASE + 2;
	private static final int DLG_CHOSE_PROVINCE = DLG_BASE + 3;
	private static final int DLG_CHOSE_CITY = DLG_BASE + 4;
	private static final int DLG_CATEGORY = DLG_BASE + 3;
	private static final String ACTION_LOCATION_SOURCE_SETTINGS = "android.settings.LOCATION_SOURCE_SETTINGS";
	private static final String ACTION_WIFI_SETTINGS = "android.settings.WIFI_SETTINGS";
	private static final int LOCATION_REQUEST_CODE = 0;
	private static final int WIFI_REQUEST_CODE = 1;


	private DataHolder mDataHolder;

	private int state;

	LocalLife mLocalLife;
	private LocalLifeApplication mApp;

	//add for test
	TextView mTestMsgTV;
	Button mTestBtn;
	Button mCateBtn;
	LinearLayout mTestContent;

	String[] mProvinces;
	String mProvinceId;
	BaseDataTask mProvincesTask;
	String[] mCitys;
	String mCity;
	String mCityId;
	BaseDataTask mCitysTask;

	BaseDataTask mDistrictsTask;

	OnMultiChoiceClickListener mProvinceChoseListener = new OnMultiChoiceClickListener () {

		@Override
		public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			// TODO Auto-generated method stub
			MainActivity.this.mProvinceId = MainActivity.this.mDataHolder.mProvinces.get(which).getId();
		}};

	OnMultiChoiceClickListener mCityChoseListener = new OnMultiChoiceClickListener () {

		@Override
		public void onClick(DialogInterface dialog, int which, boolean isChecked) {
			// TODO Auto-generated method stub
			MainActivity.this.mCity = MainActivity.this.mDataHolder.mCitys.get(which).getName();
			MainActivity.this.mCityId = MainActivity.this.mDataHolder.mCitys.get(which).getId();

			MainActivity.this.refreshTitle();
		}};

	private void refreshTitle() {
		this.mApp.setCityId(this.mCityId);
		this.mApp.setCityName(this.mCity);
		TextView tv = (TextView) this.findViewById(R.id.titleName);
		String title = this.getResources().getString(R.string.main_activity_title) + "-" + this.mCity;
		tv.setText(title);
	}

	private String getProvinceId() {
		if (this.mProvinceId == null) {
			this.mProvinceId = this.mDataHolder.mCitys.get(0).getProvinceId();
		}
		return this.mProvinceId;
	}

	private void initTask () {
		if(this.mProvincesTask == null)
			this.mProvincesTask = this.mApp.getTaskManager().new BaseDataTask();
		if (this.mCitysTask == null)
			this.mCitysTask = this.mApp.getTaskManager().new BaseDataTask();
		if(this.mDistrictsTask == null)
			this.mDistrictsTask = this.mApp.getTaskManager().new BaseDataTask();
		this.mProvincesTask = this.mApp.getTaskManager().createTask(this.mProvincesTask,
				new DoTask() {

					@Override
					public LocalType doTask() {
						// TODO Auto-generated method stub
						//MainActivity.this.createProgressDialog("");
						MainActivity.this.mHandler.sendMessage(
								MainActivity.this.mHandler.obtainMessage(MyHandler.MSG_SHOW_PROGRESS_DLG));
						try {
							Util.getData("get province data===");
							return MainActivity.this.mApp.getLocalLife().getProvinces();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						};
						return null;
					}},
				new SetData() {

					@Override
					public void setData(LocalType data) {
						// TODO Auto-generated method stub
						if(data == null)
							Util.getData("didn't get province data==");
						MainActivity.this.mDataHolder.mProvinces = (Group<Province>) data;
						//MainActivity.this.closeProgressDialog();
						MainActivity.this.mHandler.sendMessage(
								MainActivity.this.mHandler.obtainMessage(MyHandler.MSG_CLOSS_PROGRESS_DLG));
						MainActivity.this.showDialog(MainActivity.DLG_CHOSE_PROVINCE);

					}});
		this.mCitysTask = this.mApp.getTaskManager().createTask(this.mCitysTask,
				new DoTask() {

					@Override
					public LocalType doTask() {
						// TODO Auto-generated method stub
						//MainActivity.this.createProgressDialog("");
						MainActivity.this.mHandler.sendMessage(
								MainActivity.this.mHandler.obtainMessage(MyHandler.MSG_SHOW_PROGRESS_DLG));
						String provinceId = MainActivity.this.getProvinceId();
						try {
							return MainActivity.this.mApp.getLocalLife().getCitys(provinceId);
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
						MainActivity.this.mDataHolder.mCitys = (Group<City>) data;
						//MainActivity.this.closeProgressDialog();
						MainActivity.this.mHandler.sendMessage(
								MainActivity.this.mHandler.obtainMessage(MyHandler.MSG_CLOSS_PROGRESS_DLG));
						MainActivity.this.showDialog(MainActivity.DLG_CHOSE_CITY);
					}});
		this.mDistrictsTask = this.mApp.getTaskManager().createTask(this.mDistrictsTask,
				new DoTask() {

					@Override
					public LocalType doTask() {
						// TODO Auto-generated method stub
						//MainActivity.this.createProgressDialog("");
						MainActivity.this.mHandler.sendMessage(
								MainActivity.this.mHandler.obtainMessage(MyHandler.MSG_SHOW_PROGRESS_DLG));
						String cityId = MainActivity.this.mCityId;
						try {
							return MainActivity.this.mApp.getLocalLife().getDistricts(cityId);
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
						MainActivity.this.mDataHolder.mDistrict = (Group<District>) data;
						MainActivity.this.setDistrictData();
						//MainActivity.this.closeProgressDialog();
						MainActivity.this.mHandler.sendMessage(
								MainActivity.this.mHandler.obtainMessage(MyHandler.MSG_CLOSS_PROGRESS_DLG));
					}});
		if(this.mCityId != null && this.mApp.getDistrictData() == null) {
			this.mDistrictsTask.execute();
		}
	}

	private void setDistrictData() {
		Group<District> data = this.mDataHolder.mDistrict;
		this.mApp.setDistrictData(data);

	}

	//add for grid
	GridView mMainGrid;
	int[] mGridImageId = {
		R.drawable.grid1,
		R.drawable.grid2,
		//R.drawable.grid3,
		R.drawable.grid4,
		R.drawable.grid5
	};
	public final static int GRID_DISTANCE_SHOP = 0;
	public final static int GRID_LOCAL_SHOP = 1;
	public final static int GRID_COUPON = 2;
	public final static int GRID_GROUP_BUY = 3;
	int[] mGridNameId = {
		R.string.grid1_text,
		R.string.grid2_text,
		//R.string.grid3_text,
		R.string.grid4_text,
		R.string.grid5_text
	};

	private SearchLocationObserver mSearchLocationObserver = new SearchLocationObserver();
	private Location mLocation;
	private MyHandler mHandler = new MyHandler(Looper.getMainLooper());


	final class MyHandler extends Handler {
		static final int MSG_SHOW_LIST_ALER_DLG = 1;
		static final int MSG_SHOW_PROGRESS_DLG = 2;
		static final int MSG_CLOSS_PROGRESS_DLG = 3;
		static final int MSG_START_PROVINCE_TASK = 4;
		static final int MSG_START_CITY_TASK = 5;



		public MyHandler(Looper mainLooper) {
			// TODO Auto-generated constructor stub
			super(mainLooper);
		}


		@Override
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_START_PROVINCE_TASK:
				MainActivity.this.mProvincesTask.execute();
				break;

			case MSG_START_CITY_TASK:
				MainActivity.this.mCitysTask.execute();
				break;
			case MSG_SHOW_PROGRESS_DLG:
				MainActivity.this.createProgressDialog("");
				break;
			case MSG_CLOSS_PROGRESS_DLG:
				MainActivity.this.closeProgressDialog();
				break;
			}

		}

	}

	private class SearchLocationObserver implements Observer {

		@Override
		public void update(Observable observable, Object data) {
			// TODO Auto-generated method stub
			Util.location("update location:"+(Location)data);
			mLocation = (Location) data;
			if(mLocation != null) {
				MainActivity.this.setProgressBarIndeterminate(false);
			}

		}

	}

	public class NetStateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo gprs = manager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifi = manager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (!gprs.isConnected() && !wifi.isConnected()) {
				Util.log("no network connection can used!!bad bad bad");

			} else {

			}
		}

	}


	private ProgressDialog mProgressDialog;

	private void createProgressDialog(String msg) {
		this.mProgressDialog = ProgressDialog.show(this, null, msg, true, false);

	}

	private void closeProgressDialog() {
		if(this.mProgressDialog != null) {
			this.mProgressDialog.dismiss();

		}
	}

	private void bindView () {
		//test part
		this.mTestContent = (LinearLayout) findViewById(R.id.test_content);
		this.mTestMsgTV = (TextView) findViewById(R.id.test_msg);
		this.mTestBtn = (Button) findViewById(R.id.test_btn);
		this.mCateBtn = (Button) findViewById(R.id.big_category_test);

		//grid part
		this.mMainGrid = (GridView) findViewById(R.id.main_grid);
	}

	private void initView() {
		this.bindView();
		switch(this.state) {
		case STATE_GRID:
			Util.log("init Grid View");
			this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
			//TextView tv = (TextView) this.findViewById(R.id.titleName);
			//tv.setText(R.string.main_activity_title);
			//this.setTitle(R.string.main_activity_title);
			TextView tv = (TextView) this.findViewById(R.id.titleName);
			String city = "";
			if (this.mCity != null) {
				city = "-" + this.mCity;
			}
			String title = this.getResources().getString(R.string.main_activity_title) + city;
			tv.setText(title);
			this.mTestContent.setVisibility(View.GONE);

			this.mMainGrid.setAdapter(new ListAdapter() {

				@Override
				public int getCount() {
					// TODO Auto-generated method stub
					return mGridImageId.length;
				}

				@Override
				public Object getItem(int position) {
					// TODO Auto-generated method stub
					return mGridImageId[position];
				}

				@Override
				public long getItemId(int position) {
					// TODO Auto-generated method stub
					return position;
				}

				@Override
				public int getItemViewType(int position) {
					// TODO Auto-generated method stub
					return position;
				}

				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {
					// TODO Auto-generated method stub
					RelativeLayout mView;
					if (convertView == null) {
					LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
					mView = (RelativeLayout) inflater.inflate(R.layout.grid_item, null);

					ImageView view = (ImageView) mView.findViewById(R.id.grid_icon);
					/*
					view.setLayoutParams(new GridView.LayoutParams(100, 100));
					view.setAdjustViewBounds(false);
					view.setScaleType(ImageView.ScaleType.CENTER_CROP);
					view.setPadding(8, 8, 8, 8);
					*/
					view.setImageResource(mGridImageId[position]);

					TextView tx = (TextView) mView.findViewById(R.id.grid_name);
					tx.setText(mGridNameId[position]);

					} else {
						mView = (RelativeLayout) convertView;
					}
					return mView;
				}

				@Override
				public int getViewTypeCount() {
					// TODO Auto-generated method stub
					return mGridImageId.length;
				}

				@Override
				public boolean hasStableIds() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean isEmpty() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void registerDataSetObserver(DataSetObserver observer) {
					// TODO Auto-generated method stub

				}

				@Override
				public void unregisterDataSetObserver(DataSetObserver observer) {
					// TODO Auto-generated method stub

				}

				@Override
				public boolean areAllItemsEnabled() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public boolean isEnabled(int position) {
					// TODO Auto-generated method stub
					return true;
				}});
			this.mMainGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					android.util.Log.i("MQ","Grid View item clicked:id:"+position);
					//Util.log("Grid View item clicked:id:"+arg2);
					switch(position) {
					case MainActivity.GRID_DISTANCE_SHOP:
						if(mLocation != null) {
						Intent distanceIntent = new Intent(MainActivity.this, NewShopListActivity.class);
						distanceIntent.putExtra(NewShopListActivity.EXTRA_DISTANCE, "1000");
						if(mLocation != null) {
							distanceIntent.putExtra(NewShopListActivity.EXTRA_LOCATION, mLocation);
						}
						MainActivity.this.startActivity(distanceIntent);
						} else  {
							//Intent cityIntent = new Intent(MainActivity.this, NewShopListActivity.class);
							//cityIntent.putExtra(NewShopListActivity.EXTRA_CITY, true);
							Toast.makeText(MainActivity.this, R.string.no_location_data_hint, Toast.LENGTH_LONG).show();
							MainActivity.this.checkState();
						}
						break;
					case MainActivity.GRID_LOCAL_SHOP:

						Intent bigCategoryIntent = new Intent(MainActivity.this, BigCategoryListActivity.class);
						MainActivity.this.startActivity(bigCategoryIntent);
						break;
					case MainActivity.GRID_COUPON:
						Intent couponIntent = new Intent(MainActivity.this, CouponListActivity.class);
						MainActivity.this.startActivity(couponIntent);
						break;
					case MainActivity.GRID_GROUP_BUY:
						Intent goupbuyIntent = new Intent(MainActivity.this, GroupBuyListActivity.class);
						MainActivity.this.startActivity(goupbuyIntent);
						break;

					}
				}
			});
			break;
		case STATE_TEST:

			this.mMainGrid.setVisibility(View.GONE);
			this.mTestContent.setVisibility(View.VISIBLE);

	        this.mTestBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (Util.getView() == null)
						Util.setView(mTestMsgTV);
					mTestMsgTV.setTextColor(Color.RED);
					Util.log("MQ", "mTestBtn clicked!!");
					mTestMsgTV.setTextColor(MainActivity.this.getResources().getColor(R.color.black));
					Util.ok();
					MainActivity.this.testApi();
				}
			});

	        this.mCateBtn.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MainActivity.this, BigCategoryListActivity.class);
					MainActivity.this.startActivity(intent);
				}
			});

	        this.setTitle("TestActivity");
	        Util.setView(mTestMsgTV);
			break;
		}
	}

	private void startProvinceTask() {
		this.mProvincesTask.execute();
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Util.log("========================on create MainActivity");
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //state = this.STATE_TEST;
        state = this.STATE_GRID;
        setContentView(R.layout.activity_main);
        this.mApp = (LocalLifeApplication) this.getApplication();
        this.mCity = this.mApp.getCityName();
        Util.log("===city:"+this.mCity);
        this.mCityId = this.mApp.getCityId();
        Object retained = this.getLastNonConfigurationInstance();
        if(retained != null && retained instanceof DataHolder) {
        	this.mDataHolder = (DataHolder) retained;
        	this.mApp.setDistrictData(this.mDataHolder.mDistrict);
        } else {
        	this.mDataHolder = new DataHolder();

        }


        mLocalLife = mApp.getLocalLife();

        this.initView();
        this.initTask();

        this.checkState();
    }

    @Override
    public Dialog onCreateDialog(int id, Bundle bundle) {
    	switch (id) {
    	case DLG_NO_INTERNET_CONNECT:
    		return new AlertDialog.Builder(this)
    		.setTitle(R.string.no_internet_connect_title)
    		.setMessage(R.string.dlg_no_internet_msg)
    		.setNegativeButton(R.string.dlg_no_internet_negative_btn_text, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			})
			.setPositiveButton(R.string.dlg_no_internet_positive_btn_text, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					MainActivity.this.startActivityForResult(new Intent(MainActivity.this.ACTION_WIFI_SETTINGS), WIFI_REQUEST_CODE);
				}
			})
			.create();
    	case DLG_NO_LOCATION_PROVIDER:
    		return new AlertDialog.Builder(this)
    		.setTitle(R.string.no_location_provider_title)
    		.setMessage(R.string.dlg_no_location_provider_msg)
    		.setNegativeButton(R.string.dlg_no_location_provider_nagetive_btn_text, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//MainActivity.this.mHandler.sendMessage(
					//		MainActivity.this.mHandler.obtainMessage(MyHandler.MSG_SHOW_PROGRESS_DLG));
					//startProvinceTask();

				}
			})
			.setPositiveButton(R.string.dlg_no_location_provider_positive_btn_text, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					MainActivity.this.startActivityForResult(new Intent(MainActivity.this.ACTION_LOCATION_SOURCE_SETTINGS), LOCATION_REQUEST_CODE);
				}
			})
			.create();
    	case DLG_CHOSE_PROVINCE:
    		String[] provinces = this.getProvinceArray();
    		return new AlertDialog.Builder(this)
    			.setTitle(R.string.chose_province_title)
    			.setMultiChoiceItems(provinces, null, mProvinceChoseListener)
				.setPositiveButton(R.string.chose_province_ok, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						//MainActivity.this.mHandler.sendMessage(
						//		MainActivity.this.mHandler.obtainMessage(MyHandler.MSG_START_CITY_TASK));
						MainActivity.this.mCitysTask.execute();
					}
				})
				.create();
    	case DLG_CHOSE_CITY:
    		String[] citys = this.getCityArray();
    		return new AlertDialog.Builder(this)
			.setTitle(R.string.chose_city_title)
			.setMultiChoiceItems(citys, null, mCityChoseListener)
			.setPositiveButton(R.string.chose_city_ok, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					MainActivity.this.mDistrictsTask.execute();
					MainActivity.this.checkState();
				}
			})
			.create();
    	}
		return null;

    }

    private String[] getProvinceArray() {
    	if (this.mDataHolder.mProvinces == null) {
    		Util.getData("didn't get provinces data in MainActivity!!!bad bad bad");
    		return null;
    	}
    	if (this.mProvinces == null) {
    	int length = this.mDataHolder.mProvinces.size();
    	String[] data = new String[length];
    	for(int i = 0; i < length; i++) {
    		data[i] = this.mDataHolder.mProvinces.get(i).getName();
    	}
    	this.mProvinces = data;
    	}
    	return this.mProvinces;
    }

    private String[] getCityArray() {
    	if (this.mDataHolder.mCitys == null) {
    		return null;
    	}
    	if (this.mCitys == null) {
    		int length = this.mDataHolder.mCitys.size();
    		String[] data = new String[length];
    		for (int i = 0; i < length; i++) {
    			data[i] = this.mDataHolder.mCitys.get(i).getName();
    		}
    		this.mCitys = data;
    	}
    	return this.mCitys;
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
    	return this.mDataHolder;
    }

    @Override
    public void onResume() {
    	super.onResume();
    	//this.mProvincesTask.execute();

    	((LocalLifeApplication)this.getApplication()).requestLocationUpdates(this.mSearchLocationObserver);
    	Util.location("if mlocation is null"+(this.mLocation == null));
    	if (false) {//this.mLocation == null) {
    		boolean ifLocation = ((LocalLifeApplication)this.getApplication()).checkLocationService();
    		if (ifLocation) {
    		Util.location("Location is null  ===requestLocation");
    		this.setProgressBarIndeterminate(true);
    		this.requestLocation();
    		} else {
    			Util.location("there is no Location provider can used");
    			this.startActivityForResult(new Intent(this.ACTION_LOCATION_SOURCE_SETTINGS), LOCATION_REQUEST_CODE);
    		}
    	}
    }

    private void checkState() {
    	//fist check the network connection
    	boolean networkConn = this.mApp.checkNetworkState();
    	if (!networkConn) {
    		// must add some UI suggestion
    		this.removeDialog(this.DLG_NO_INTERNET_CONNECT);
    		this.showDialog(this.DLG_NO_INTERNET_CONNECT);
    		if (false) {//for test
    			this.startActivityForResult(new Intent(this.ACTION_WIFI_SETTINGS), WIFI_REQUEST_CODE);
    		}
    	}

    	//second check the gps location provider
    	if (!networkConn) {//if network connection not ok,do not check location provider
    		return;
    	}
    	if(this.mApp.getCityId() == null) {
    		startProvinceTask();
    		return;
    	}

    	boolean locationProvider = this.mApp.checkLocationService();
    	if (!locationProvider) {
    		//must add some UI suggestion
    		this.removeDialog(this.DLG_NO_LOCATION_PROVIDER);
    		this.showDialog(this.DLG_NO_LOCATION_PROVIDER);
    		if (false) {//for test
    			this.startActivityForResult(new Intent(this.ACTION_LOCATION_SOURCE_SETTINGS), LOCATION_REQUEST_CODE);
    		}
    	}
    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    	Util.location("get result for start location service===request:"+
    			(requestCode )+
    			"===result:"+(resultCode == Activity.RESULT_OK));
    	this.checkState();
    	switch(resultCode) {
    	case Activity.RESULT_OK:
    		break;
    		default:
    			break;
    	}
    }

    @Override
    public void onPause() {
    	super.onPause();
    	((LocalLifeApplication)this.getApplication()).removeLocationUpdates(this.mSearchLocationObserver);
    }

    private void requestLocation() {
    	LocalLifeApplication app = (LocalLifeApplication) this.getApplication();
    	mLocation = app.getLastKnownLocation();
    	if (this.mLocation == null) {
    		Util.location("mLocation still null!!bad bad");
    		return;
    	}
    	String latitude = String.valueOf(mLocation.getLatitude());
    	String longitude = String.valueOf(mLocation.getLongitude());
    	Util.location("location==["+latitude+","+longitude+"]");
    }

    private void testApi() {
    	Util.testGetData("start test api==");
    	Group<BigCategory> mGroupBC = null;
    	try {
    		Util.testGetData("Api getTest");
			this.mLocalLife.getTest();
			Util.ok();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Util.error("ParseException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Util.error("IOException");
			e.printStackTrace();
		} catch (JSONException e) {
			Util.error("JSONException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	try {
    		Util.testGetData("Api getBigCategory");
			mGroupBC = this.mLocalLife.getBigCategory();
			Util.log("data:"+mGroupBC);
			for (int i = 0; i < mGroupBC.size(); i++) {
				android.util.Log.i("MQ","====="+mGroupBC.get(i).getName());
			}
			Util.ok();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Util.error("ParseException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Util.error("IOException");
			e.printStackTrace();
		} catch (JSONException e) {
			Util.error("JSONException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	Group<Category> mGroupC;
    	try {
    		Util.testGetData("Api getCategory");
    		BigCategory bc = mGroupBC.get(0);
			mGroupC = this.mLocalLife.getCategory(bc.getId());
			Util.ok();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			Util.error("ParseException");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Util.error("IOException");
			e.printStackTrace();
		} catch (JSONException e) {
			Util.error("JSONException");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	try {
			this.mLocalLife.getShops("0");
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


    private static class DataHolder {
    	Group<Province> mProvinces;
    	Group<City> mCitys;
    	Group<District> mDistrict;
    }

    private static class TaskProvinces extends BaseTask {

		public TaskProvinces(BaseStateHolder holder) {
			super(holder);
			// TODO Auto-generated constructor stub
		}

		@Override
		public LocalType doTask() {
			// TODO Auto-generated method stub
			try {
				return this.mApi.getProvinces();
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

    private static class ProvinceDataHolder implements BaseStateHolder {

		@Override
		public void setData(LocalType data) {
			// TODO Auto-generated method stub

		}

		@Override
		public void startTask() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTask() {
			// TODO Auto-generated method stub

		}

    }
}
