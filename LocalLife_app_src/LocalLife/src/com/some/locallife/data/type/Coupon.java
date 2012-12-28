package com.some.locallife.data.type;

import android.os.Parcel;
import android.os.Parcelable;

import com.some.locallife.data.parse.AbstractParser;

public class Coupon implements LocalType, Parcelable {
	private String msg;
	private String price;
	private String imageUrl;
	private String teleNum;
	private String pageValue;
	private String lifeTime;
	private String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice() {
		return this.price;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setTeleNum(String teleNum) {
		this.teleNum = teleNum;
	}

	public String getTeleNum() {
		return this.teleNum;
	}

	public void setPageValue(String pageValue) {
		this.pageValue = pageValue;
	}

	public String getPageValue() {
		return this.pageValue;
	}

	public void setLifeTime(String lifeTime) {
		this.lifeTime = lifeTime;
	}

	public String getLifeTime() {
		return this.lifeTime;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(this.imageUrl);
		out.writeString(this.lifeTime);
		out.writeString(this.msg);
		out.writeString(this.pageValue);
		out.writeString(this.price);
		out.writeString(this.teleNum);
	}

}
