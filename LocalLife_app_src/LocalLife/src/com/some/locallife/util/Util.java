package com.some.locallife.util;

import android.R;
import android.annotation.SuppressLint;
import android.widget.TextView;

@SuppressLint({ "ResourceAsColor", "ResourceAsColor" })
public class Util {

	private static TextView mMsgView;

	public static void log(String msg) {
		log("MQ",msg);
	}

	public static void telephone(String msg) {
		log("MQ","telephone==="+msg);
	}

	public static void coupon(String msg) {
		log("MQ","coupon==="+msg);
	}

	public static void httpRequest(String msg) {
		log("MQ","httpRequest==="+msg);
	}

	public static void getData(String msg) {
		log("MQ","getData==="+msg);
	}

	public static void location(String msg) {
		log("location", "location===" + msg);
	}

	public static void log (String tag, String msg) {

		android.util.Log.i(tag, msg);
		msg = tag + "===" + msg + "\n";
		//mMsgView.append(msg);
		//mMsgView.invalidate();
	}

	public static void setView(TextView tv) {
		mMsgView = tv;
	}

	public static TextView getView () {
		return mMsgView;
	}

	public static void testGetData(String msg) {
		android.util.Log.i("GetData", msg);
		msg = msg + "\n";
		mMsgView.append(msg);
	}

	@SuppressLint("ResourceAsColor")
	public static void ok() {
		android.util.Log.v("===","==Success!");
		//mMsgView.setTextColor(R.color.primary_text_light);
		mMsgView.append("==Success!\n");
		//mMsgView.setTextColor(R.color.black);
	}

	public static void error(String msg) {
		android.util.Log.e("<<<","error =="+msg);
		msg = msg + "\n";
		mMsgView.append(msg);
	}

}
