package com.some.locallife.app;

import java.util.Observer;
import java.util.prefs.Preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;

import com.some.locallife.data.LocalLife;
import com.some.locallife.data.location.LifeLocationListener;
import com.some.locallife.data.type.District;
import com.some.locallife.data.type.Group;
import com.some.locallife.preferences.MyPreferences;
import com.some.locallife.util.RemoteResourceManager;
import com.some.locallife.util.TaskManager;
import com.some.locallife.util.Util;

public class LocalLifeApplication extends Application {
	private String mVersion = "1.0";

	private TaskHandler mTaskHandler;
	private HandlerThread mTaskThread;

	private SharedPreferences mPrefs;
	private RemoteResourceManager mRemoteResourceManager;

	private TaskManager mTaskManager;

	private LocalLife mLocalLife;

	private Group<District> mDistricts;


	private LifeLocationListener mLifeLocationListener = new LifeLocationListener();

	public void onCreate() {

		this.mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		this.loadResourceManagers();
		loadLocalLife();

		this.mTaskManager = new TaskManager();

	}

	public void setCityName(String cityName) {
		MyPreferences.setCityName(this.mPrefs, cityName);
	}

	public String getCityName() {
		return MyPreferences.getCityName(mPrefs);
	}

	public void setCityId(String cityId) {
		MyPreferences.setCityId(this.mPrefs, cityId);
	}

	public String getCityId() {
		return MyPreferences.getCityId(mPrefs);
	}

	private void loadResourceManagers() {
		this.mRemoteResourceManager = new RemoteResourceManager("cache");
	}

	private void loadLocalLife() {
		this.mLocalLife = new LocalLife(LocalLife.createHttpApi(mVersion, false));
	}

	public LocalLife getLocalLife() {
		return this.mLocalLife;
	}

	public void setDistrictData(Group<District> data) {
		this.mDistricts = data;
	}

	public Group<District> getDistrictData() {
		return this.mDistricts;
	}

	public TaskManager getTaskManager() {
		return this.mTaskManager;
	}

	public LifeLocationListener requestLocationUpdates(boolean gps) {
		Util.location("requestLocationUpdates==gps:"+gps);
		this.mLifeLocationListener.register(
				(LocationManager) this.getSystemService(Context.LOCATION_SERVICE), gps);
		return this.mLifeLocationListener;
	}

	public LifeLocationListener requestLocationUpdates(Observer observer) {
		Util.location("requestLocationUpdates");
		this.mLifeLocationListener.addObserver(observer);
		this.mLifeLocationListener.register(
				(LocationManager) this.getSystemService(Context.LOCATION_SERVICE), true);
		return this.mLifeLocationListener;
	}

	public boolean checkLocationService() {
		return this.mLifeLocationListener.checkLocationService(
				(LocationManager) this.getSystemService(Context.LOCATION_SERVICE));
	}

	public boolean checkNetworkState() {
		ConnectivityManager manager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo gprs = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (!gprs.isConnected() && !wifi.isConnected()) {
			return false;
		}
		return true;
	}

	private class TaskHandler extends Handler {

		public TaskHandler(Looper looper) {
			super(looper);
		}

		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch(msg.what) {

			}
		}
	}

	public RemoteResourceManager getRemoteResourceManager() {
		// TODO Auto-generated method stub
		return this.mRemoteResourceManager;
	}

	public void removeLocationUpdates(
			Observer observer) {
		// TODO Auto-generated method stub
		this.mLifeLocationListener.deleteObserver(observer);
		this.removeLocationUpdates();
	}

	private void removeLocationUpdates() {
		// TODO Auto-generated method stub
		this.mLifeLocationListener.unregister((LocationManager) this.getSystemService(Context.LOCATION_SERVICE));
	}

	public Location getLastKnownLocation() {
		return this.mLifeLocationListener.getLastKnownLocation();
	}
}