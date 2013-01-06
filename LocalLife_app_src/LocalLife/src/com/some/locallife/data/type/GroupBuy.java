package com.some.locallife.data.type;

import com.some.locallife.util.Util;

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

	public void doDiscount() {
		double nP = Double.parseDouble(nowPrice);
		double oP = Double.parseDouble(oldPrice);
		int d = (int) ((nP / oP)*10);
		this.discount = String.valueOf(d);
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getDiscount() {
		return this.discount;
	}

	public void doSavePrice() {
		double nP = Double.parseDouble(nowPrice);
		double oP = Double.parseDouble(oldPrice);
		int sP = (int) (oP - nP);
		this.savePrice = String.valueOf(sP);
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

	public void setImageUri(String imageUri) {
		this.imageUrl = imageUri;
	}

	public String getImageUri() {
		return this.imageUrl;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		// TODO Auto-generated method stub
		out.writeString(this.id);
		out.writeString(this.discount);
		out.writeString(this.imageUrl);
		out.writeString(this.msg);
		out.writeString(this.nowPrice);
		out.writeString(this.oldPrice);
		out.writeString(this.savePrice);
		out.writeString(this.teleNum);
	}

	private GroupBuy(Parcel in) {
		this.id = in.readString();
		this.discount = in.readString();
		this.imageUrl = in.readString();
		this.msg = in.readString();
		this.nowPrice = in.readString();
		this.oldPrice = in.readString();
		this.savePrice = in.readString();
		this.teleNum = in.readString();
	}

	public GroupBuy() {

	}

	public static final Parcelable.Creator<GroupBuy> CREATOR = new Parcelable.Creator<GroupBuy>() {

		@Override
		public GroupBuy createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new GroupBuy(source);
		}

		@Override
		public GroupBuy[] newArray(int size) {
			// TODO Auto-generated method stub
			return new GroupBuy[size];
		}
	};

}
