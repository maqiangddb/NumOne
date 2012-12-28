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

public interface ILocalLifeApi {

	abstract public void setCredentials(String user, String password);

	abstract public Test getTest () throws ParseException, IOException, JSONException;

	abstract public Group<BigCategory> getBigCategory() throws ParseException, IOException, JSONException;

	abstract public Group<Category> getCategory(String bigCategoryId) throws ParseException, IOException, JSONException;

	abstract public Group<Coupon> getCoupons() throws ParseException, IOException, JSONException;

	abstract public Coupon getCoupon(String couponId) throws ParseException, IOException, JSONException;

	abstract public Group<GroupBuy> getGroupBuys() throws ParseException, IOException, JSONException;

	abstract public GroupBuy getGroupBuy(String groupBuyId) throws ParseException, IOException, JSONException;

	abstract public Group<Province> getProvinces() throws ParseException, IOException, JSONException;

	abstract public Group<City> getCitys(String provinceId) throws ParseException, IOException, JSONException;

	abstract public Group<District> getDistricts(String cityId) throws ParseException, IOException, JSONException;

	abstract public Shop getShop(String shopId) throws ParseException, IOException,
			JSONException;

	abstract public Group<Shop> getShops(String categoryId) throws ParseException, IOException, JSONException;

	abstract public Group<Shop> getShops(String latitude, String longtitude, String distance) throws ParseException, IOException, JSONException;

}
