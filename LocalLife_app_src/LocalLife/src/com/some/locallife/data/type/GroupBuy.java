package com.some.locallife.data.type;

import android.os.Parcel;
import android.os.Parcelable;

public class GroupBuy implements LocalType, Parcelable{
	private String msg;
	private String imageUrl;
	private String nowPrice;
	private String oldPrice;
	private String discount;
	private String savePrice;
	private String teleNum;
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

	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}

	public String getNowPrice() {
		return this.nowPrice;
	}

	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}

	public String getOldPrice() {
		return this.oldPrice;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getDiscount() {
		return this.discount;
	}

	public void setSavePrice(String savePrice) {
		this.savePrice = savePrice;
	}

	public String getSavePrice() {
		return this.savePrice;
	}

	public void setTeleNum(String teleNum) {
		this.teleNum = teleNum;
	}

	public String getTeleNum() {
		return this.teleNum;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(this.discount);
		out.writeString(this.imageUrl);
		out.writeString(this.msg);
		out.writeString(this.nowPrice);
		out.writeString(this.oldPrice);
		out.writeString(this.savePrice);
		out.writeString(this.teleNum);
	}

}
