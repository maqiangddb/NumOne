package com.some.locallife.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.some.locallife.data.type.LocalType;
import com.some.locallife.util.Util;

public abstract class BaseHolder {
	protected int mConvertViewLayout;
	protected View mConvertView;
	protected Context mContext;

	public interface ViewHolder {}

	public BaseHolder(Context context) {
		Util.log("init shop list holder===2");
		this.mContext = context;
		//this.doBindView(mConvertView);
	}

	protected void init() {
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mConvertView = inflater.inflate(this.mConvertViewLayout, null);
		android.util.Log.i("MQ","in BaseHolder init() if null:"+(mConvertView == null));
	}

	abstract public void doUpdateView(int position);

	abstract public void doBindView(View convertView);

	abstract public int doGetCount();

	abstract public Object doGetItem(int position);




}



