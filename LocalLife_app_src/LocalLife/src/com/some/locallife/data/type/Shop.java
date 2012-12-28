package com.some.locallife.data.type;

import android.os.Parcel;
import android.os.Parcelable;

public class Shop implements LocalType{
	private String id;
	private String type;
	private String name;
	private String latilongi;
	private String latitude;
	private String longitude;
	private String average;
	private String distance;
	private String[] imageUrls;
	private String imageUrl;
	private String commentsId;
	private String teleNum;
	private String address;
	private String price;
	private String categoryId;
	private String districtId;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	public void setImageUrls(String[] imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String[] getImageUrls() {
		return this.imageUrls;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public void setAverage(String average) {
		this.average = average;
	}

	public String getAverage() {
		return this.average;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return this.address;
	}

	public void setCommentsId(String commentsId) {
		this.commentsId = commentsId;
	}

	public String getCommentsId() {
		return this.commentsId;
	}

	public void setTeleNum(String teleNum) {
		this.teleNum = teleNum;
	}

	public String getTeleNum() {
		return this.teleNum;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPrice() {
		return this.price;
	}

	public void setLatilongi(String latilongi) {
		// TODO Auto-generated method stub
		this.latilongi = latilongi;
	}

	public void setLongitude(String longitude) {
		// TODO Auto-generated method stub
		this.longitude = longitude;
	}

	public String getLongitude() {
		return this.longitude;
	}

	public void setLatitude(String latitude) {
		// TODO Auto-generated method stub
		this.latitude = latitude;
	}

	public String getLatitude() {
		return this.latitude;
	}

	public void setDistrictId(String districtId) {
		// TODO Auto-generated method stub
		this.districtId = districtId;
	}

	public String getDistrictId() {
		return this.districtId;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return this.id;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryId() {
		return this.categoryId;
	}

}
