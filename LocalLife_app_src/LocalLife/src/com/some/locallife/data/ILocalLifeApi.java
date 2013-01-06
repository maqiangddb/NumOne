package com.some.locallife.data;

import java.io.IOException;

import org.apache.http.ParseException;
import org.json.JSONException;

import com.some.locallife.data.error.LocalException;
import com.some.locallife.data.type.BigCategory;
import com.some.locallife.data.type.Category;
import com.some.locallife.data.type.City;
import com.some.locallife.data.type.Comment;
import com.some.locallife.data.type.Coupon;
import com.some.locallife.data.type.District;
import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.GroupBuy;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.data.type.Province;
import com.some.locallife.data.type.Response;
import com.some.locallife.data.type.Shop;
import com.some.locallife.data.type.Test;

public interface ILocalLifeApi {

	abstract public void setCredentials(String user, String password);

	abstract public Test getTest () throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<BigCategory> getBigCategory() throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<Category> getCategory(String bigCategoryId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<Coupon> getCoupons() throws ParseException, IOException, JSONException, LocalException;

	abstract public Coupon getCoupon(String couponId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<GroupBuy> getGroupBuys() throws ParseException, IOException, JSONException, LocalException;

	abstract public GroupBuy getGroupBuy(String groupBuyId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<Province> getProvinces() throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<City> getCitys(String provinceId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<District> getDistricts(String cityId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Shop getShop(String shopId) throws ParseException, IOException,
			JSONException, LocalException;

	abstract public Group<Shop> getShops(String categoryId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<Shop> getShops(String latitude, String longtitude, String distance) throws ParseException, IOException, JSONException, LocalException;

	abstract public LocalType postLogcat(String msg) throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<Shop> getFavShopList() throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<Coupon> getFavCouponList() throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<GroupBuy> getFavGroupBuyList() throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<Comment> getShopComments(String shopId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Response postShopComment(String shopId, String msg) throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<Comment> getCouponComments(String couponId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Response postCouponComment(String couponId, String msg) throws ParseException, IOException, JSONException, LocalException;

	abstract public Group<Comment> getGroupBuyComments(String groupBuyId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Response postGroupBuyComment(String groupBuyId, String msg) throws ParseException, IOException, JSONException, LocalException;

	abstract public Response favShop(String shopId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Response favCoupon(String couponId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Response favGroupBuy(String groupBuyId) throws ParseException, IOException, JSONException, LocalException;

	abstract public Response register(String user, String password) throws ParseException, IOException, JSONException, LocalException;
}
