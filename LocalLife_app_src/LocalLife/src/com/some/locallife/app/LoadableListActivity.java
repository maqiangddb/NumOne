package com.some.locallife.app;

import java.util.ArrayList;

import com.some.locallife.R;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.util.Util;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public abstract class LoadableListActivity extends ListActivity implements AdapterView.OnItemSelectedListener{

	protected View mSpinnerContentView;

	protected Spinner mSp1;
	protected Spinner mSp2;
	protected Spinner mSp3;

	private ArrayList<String> mSp1Array;
	private ArrayList<String> mSp3Array;
	private ArrayList<String> mSp2Array;

	@Override
	protected void onCreate (Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.setContentView(R.layout.loadable_list_activity);

		this.initSpinner();

		this.mSpinnerContentView = findViewById(R.id.spinner_content);
		this.getListView().setDividerHeight(0);

	}

	private void initSpinner () {
		mSp1 = (Spinner) this.findViewById(R.id.filter_spinner_1);
		mSp2 = (Spinner) this.findViewById(R.id.filter_spinner_2);
		mSp3 = (Spinner) this.findViewById(R.id.filter_spinner_3);
		doInitSpinner();
		this.mSp1.setOnItemSelectedListener(this);
		this.mSp2.setOnItemSelectedListener(this);
		this.mSp3.setOnItemSelectedListener(this);
		mSp3.setVisibility(View.GONE);
	}

	protected abstract void doInitSpinner();

	public void setTitle(int id) {
		this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.main_title);
		TextView tv = (TextView)this.findViewById(R.id.titleName);
		tv.setText(id);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.doOnResume();
	}

	abstract public void doOnResume();

	@Override
	protected void onPause() {
		super.onPause();
		this.doOnPause();
	}

	abstract public void doOnPause();

	@Override
	public Object onRetainNonConfigurationInstance() {
		return this.doOnRetainNonConfigurationInstance();
	}

	abstract public Object doOnRetainNonConfigurationInstance();



	public void setEmptyView(View view) {
		LinearLayout parent = (LinearLayout) findViewById(R.id.loadableListHolder);

		parent.getChildAt(0).setVisibility(View.GONE);
		if (parent.getChildCount() > 1) {
			parent.removeViewAt(1);
		}
		parent.addView(view);
	}

	public void setLoadingView() {
		LinearLayout parent = (LinearLayout) findViewById(R.id.loadableListHolder);

		if (parent.getChildCount() > 1) {
			parent.removeViewAt(1);
		}

		parent.getChildAt(0).setVisibility(View.VISIBLE);
	}

	public int getNoSearchResultsStringId() {
		return 0;
	}

	public View getSpinnerContent () {
		return findViewById(R.id.spinner_content);
	}



}
