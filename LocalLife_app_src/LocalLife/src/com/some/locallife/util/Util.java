package com.some.locallife.util;

import com.some.locallife.data.type.Coupon;
import com.some.locallife.data.type.GroupBuy;

import android.R;
import android.annotation.SuppressLint;
import android.widget.TextView;

@SuppressLint({ "ResourceAsColor", "ResourceAsColor" })
public class Util {

	private static TextView mMsgView;

	public static void log(String msg) {
		log("MQ",msg);
	}

	public static void login(String msg) {
		log("MQ", "login===" + msg);
	}

	public static void telephone(String msg) {
		log("MQ","telephone==="+msg);
	}

	public static void coupon(String msg) {
		log("MQ","coupon==="+msg);
	}

	public static void couponData(Coupon coupon) {

			Util.getData("coupon data==Id="+coupon.getId());
			Util.getData("coupon data==ImageUrl="+coupon.getImageUrl());
			Util.getData("coupon data==LifeTime="+coupon.getLifeTime());
			Util.getData("coupon data==Msg="+coupon.getMsg());
			Util.getData("coupon data==PageValue="+coupon.getPageValue());
			Util.getData("coupon data==Price="+coupon.getPrice());
			Util.getData("coupon data==TeleNum="+coupon.getTeleNum());

	}

	public static void groupbuyData(GroupBuy group) {
		Util.getData("=Discount="+group.getDiscount());
		Util.getData("=Id="+group.getId());
		Util.getData("=Msg="+group.getMsg());
		Util.getData("=NowPrice="+group.getNowPrice());
		Util.getData("=OldPrice="+group.getOldPrice());
		Util.getData("=TeleNum="+group.getTeleNum());
		Util.getData("=SavePrice="+group.getSavePrice());
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
