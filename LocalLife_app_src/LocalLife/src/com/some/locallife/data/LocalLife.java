package com.some.locallife.data;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.data.type.BigCategory;
import com.some.locallife.data.type.Category;
import com.some.locallife.data.type.City;
import com.some.locallife.data.type.Coupon;
import com.some.locallife.data.type.District;
import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.GroupBuy;
import com.some.locallife.data.type.Province;
import com.some.locallife.data.type.Shop;
import com.some.locallife.data.type.Test;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.WebView;

public class LocalLife implements Parcelable , ILocalLifeApi{

	public static final String LOCALLIFE_API_DOMAIN = "numone.sinaapp.com/api";//http://numone.sinaapp.com/api?action=test

	private LocalLifeHttpApi mLocalApi;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	public LocalLife(LocalLifeHttpApi api) {
		mLocalApi = api;

	}

	public static final LocalLifeHttpApi createHttpApi(String domain, String clientVersion, boolean useOAuth) {
		return new LocalLifeHttpApi(domain, clientVersion, useOAuth);
	}

	public static final LocalLifeHttpApi createHttpApi(String clientVersion, boolean useOAuth) {
		return createHttpApi(LOCALLIFE_API_DOMAIN, clientVersion, useOAuth);

	}

	//api part ==start
	@Override
	public Test getTest() throws ParseException, IOException, JSONException {
		return mLocalApi.getTest();
	}

	@Override
	public Group<BigCategory> getBigCategory () throws ParseException, IOException, JSONException {
		return mLocalApi.getBigCategory();
	}

	@Override
	public Group<Category> getCategory(String bigCategoryId) throws ParseException, IOException, JSONException {
		return mLocalApi.getCategory(bigCategoryId);
	}

	@Override
	public Shop getShop(String shopId) throws ParseException, IOException, JSONException {
		return mLocalApi.getShop(shopId);
	}

	@Override
	public Group<Province> getProvinces() throws ParseException, IOException,
			JSONException {
		// TODO Auto-generated method stub
		return this.mLocalApi.getProvinces();
	}

	@Override
	public Group<City> getCitys(String provinceId) throws ParseException,
			IOException, JSONException {
		// TODO Auto-generated method stub
		return this.mLocalApi.getCitys(provinceId);
	}

	@Override
	public Group<Shop> getShops(String categoryId) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		return this.mLocalApi.getShops(categoryId);
	}


	@Override
	public Group<Shop> getShops(String latitude, String longtitude,
			String distance) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		return this.mLocalApi.getShops(latitude, longtitude, distance);
	}
	//api part ==end

	public static class Location {
		String geolat = null;
		String geolong = null;
		String geohacc = null;
		String geovacc = null;
		String geoalt = null;

		public Location () {

		}

		public Location (final String geolat, final String geolong, final String geohacc,
				final String geovacc, final String geoalt) {
			this.geolat = geolat;
			this.geolong = geolong;
			this.geohacc = geohacc;
			this.geovacc = geovacc;
			this.geoalt = geoalt;
		}

		public Location(final String geolat, final String geolong) {
			this(geolat, geolong, null, null, null);
		}
	}

	@Override
	public Group<Coupon> getCoupons() throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		return this.mLocalApi.getCoupons();
	}

	@Override
	public Group<GroupBuy> getGroupBuys() throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		return this.mLocalApi.getGroupBuys();
	}

	@Override
	public Coupon getCoupon(String couponId) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		return this.mLocalApi.getCoupon(couponId);
	}

	@Override
	public GroupBuy getGroupBuy(String groupBuyId) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		return this.mLocalApi.getGroupBuy(groupBuyId);
	}

	@Override
	public void setCredentials(String user, String password) {
		// TODO Auto-generated method stub
		if (user == null || user.length() == 0 || password == null || password.length() == 0) {

		} else {
			this.mLocalApi.setCredentials(user, password);
		}

	}

	@Override
	public Group<District> getDistricts(String cityId) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		return this.mLocalApi.getDistricts(cityId);
	}





}
