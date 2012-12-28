package com.some.locallife.data.location;

import java.util.Date;
import java.util.List;
import java.util.Observable;

import com.some.locallife.util.Util;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class LifeLocationListener extends Observable implements LocationListener {

	private static final long LOCATION_UPDATE_MAX_DELTA_THRESHOLD = 1000 * 60 * 5;
	private static final long LOCATION_UPDATE_MIN_TIME = 0;
	private static final long LOCATION_UPDATE_MIN_DISTANCE = 0;

	private static final long SLOW_LOCATION_UPDATE_MIN_TIME = 1000*60*5;
	private static final long SLOW_LOCATION_UPDATE_MIN_DISTANCE = 50;
	private Location mLastLocation;

	public LifeLocationListener() {
		super();
	}

	synchronized public void onBestLocationChanged(Location location) {
		Util.location( "onBestLocationChanged: " + location);
		mLastLocation = location;
		setChanged();
		notifyObservers(location);
	}

	public void updateLocation(Location location) {

		if (location != null && mLastLocation == null) {
			Util.log("==","updateLocation: null last location");
			onBestLocationChanged(location);
			return;
		} else if (location == null) {
			return;
		}

		long now = new Date().getTime();
		long locationUpdateDelta = now - location.getTime();
		long lastLocationUpdateDelta = now - mLastLocation.getTime();
		boolean locationIsInTimeThreshold = locationUpdateDelta <= LOCATION_UPDATE_MAX_DELTA_THRESHOLD;
		boolean lastLocationIsInTimeThreshold = lastLocationUpdateDelta <= LOCATION_UPDATE_MAX_DELTA_THRESHOLD;
		boolean locationIsMostRecent = locationUpdateDelta <= lastLocationUpdateDelta;

		boolean accuracyComparable = location.hasAccuracy() || mLastLocation.hasAccuracy();
		boolean locationIsMostAccurate = false;
		if(accuracyComparable) {
			if (location.hasAccuracy() && !mLastLocation.hasAccuracy()) {
				locationIsMostAccurate = true;
			} else if (!location.hasAccuracy() && mLastLocation.hasAccuracy()) {
				locationIsMostAccurate = false;
			} else {
				locationIsMostAccurate = location.getAccuracy() <= mLastLocation.getAccuracy();
			}
		}

		if (accuracyComparable && locationIsMostAccurate && locationIsInTimeThreshold) {
			onBestLocationChanged(location);
		} else if (locationIsInTimeThreshold && !lastLocationIsInTimeThreshold) {
			onBestLocationChanged(location);
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Util.location("onLocationChanged==="+location);
		updateLocation(location);
	}

	@Override
	public void onStatusChanged(String paramString, int paramInt,
			Bundle paramBundle) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String paramString) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String paramString) {
		// TODO Auto-generated method stub

	}

	public void register(LocationManager locationManager, boolean gps) {
		long updateMinTime = SLOW_LOCATION_UPDATE_MIN_TIME;
		long updateMinDistance = this.SLOW_LOCATION_UPDATE_MIN_DISTANCE;
		if(gps) {
			updateMinTime = LOCATION_UPDATE_MIN_TIME;
			updateMinDistance = LOCATION_UPDATE_MIN_DISTANCE;
		}
		List<String> providers = locationManager.getProviders(true);
		int providersCount = providers.size();
		Util.location("location provider count:"+providersCount);
		for(int i = 0; i < providersCount; i++) {
			String providerName = providers.get(i);
			Util.location("name:"+providerName);
			if (locationManager.isProviderEnabled(providerName)) {
				Util.location(""+providerName+"is enabled==");
				updateLocation(locationManager.getLastKnownLocation(providerName));
			}
			//Only register with GPS if we've explicitly allowed it
			if (gps || !LocationManager.GPS_PROVIDER.equals(providerName)) {
				Util.location(""+providerName+"====requestLocationUpdates==");
				locationManager.requestLocationUpdates(providerName, updateMinTime,
						updateMinDistance, this);
			}
		}
	}

	public boolean checkLocationService(LocationManager locationManager) {
		Util.location("checkLocationService ");
		List<String> providers = locationManager.getProviders(true);
		if (providers.size() == 0) {
			Util.location("No location Providers can use!!!!!bad bad bad!");
			return false;
		}
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			Util.location("gps location provider is not enabled!!!bad bad bad");
			return false;
		}
		return true;
	}

	public void unregister(LocationManager locationManager) {
		locationManager.removeUpdates(this);
	}

	synchronized public Location getLastKnownLocation() {
		// TODO Auto-generated method stub
		return this.mLastLocation;
	}

}
