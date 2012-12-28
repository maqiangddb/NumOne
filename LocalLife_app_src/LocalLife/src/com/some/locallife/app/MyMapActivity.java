package com.some.locallife.app;


import android.os.Bundle;

import com.google.android.maps.MapActivity;
import com.some.locallife.R;

public class MyMapActivity extends MapActivity {

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		this.setContentView(R.layout.my_map_activity);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
