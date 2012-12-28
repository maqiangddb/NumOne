package com.some.locallife.data;

import android.content.Context;
import android.location.LocationManager;

public class LocationDataManager {
	Context mContext;
	LocationManager mLocationManager;
	
	public LocationDataManager (Context context) {
		mContext = context;
	}
	
	private void init() {
		this.mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
	}

}
