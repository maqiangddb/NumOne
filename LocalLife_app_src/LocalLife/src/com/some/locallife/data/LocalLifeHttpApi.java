package com.some.locallife.data;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import com.some.locallife.data.http.AbstractHttpApi;
import com.some.locallife.data.http.HttpApi;
import com.some.locallife.data.http.HttpApiBasic;
import com.some.locallife.data.parse.BigCategoryParser;
import com.some.locallife.data.parse.CategoryParser;
import com.some.locallife.data.parse.CityParser;
import com.some.locallife.data.parse.CouponParser;
import com.some.locallife.data.parse.DistrictParser;
import com.some.locallife.data.parse.GroupBuyParser;
import com.some.locallife.data.parse.GroupParser;
import com.some.locallife.data.parse.ProvinceParser;
import com.some.locallife.data.parse.ShopParser;
import com.some.locallife.data.parse.TestParser;
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

public class LocalLifeHttpApi implements ILocalLifeApi{

	private static final String DATATYPE = ".json";

	private static final String URL_API_TEST = "/test";

	private final DefaultHttpClient mHttpClient = AbstractHttpApi.creatHttpClient();
	private HttpApi mHttpApi;

	//private final DefaultHttpClient mResourceHttpClient = AbstractHttpApi.createResourceHttpClient();

	private final String mApiBaseUrl;
	private final AuthScope mAuthScope;

	public LocalLifeHttpApi (String domain, String clientVersion, boolean useLogin) {
		mApiBaseUrl = "http://" + domain;
		mAuthScope = new AuthScope(domain, 80);

		useLogin = false;//now there is no login part
		if (useLogin) {

		} else {
			mHttpApi = new HttpApiBasic(mHttpClient, clientVersion);
		}
	}

	private String fullUrl(String url) {
		return mApiBaseUrl;// + url +DATATYPE;
	}

	//api part ==start
	@Override
	public Test getTest () throws ParseException, IOException, JSONException {
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(URL_API_TEST),
				new BasicNameValuePair("action","text"));
		return (Test) mHttpApi.doHttpRequest(httpGet, new TestParser());
	}


	@Override
	public Group<BigCategory> getBigCategory() throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind","BigCategory"));
		return (Group<BigCategory>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new BigCategoryParser()));
	}

	@Override
	public Group<Category> getCategory(String bigCategoryId) throws ParseException, IOException, JSONException {
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind","Category"),
				new BasicNameValuePair("bigCategoryId",bigCategoryId));
		return (Group<Category>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new CategoryParser()));
	}

	@Override
	public Shop getShop (String shopId) throws ParseException, IOException, JSONException {
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "Shop"),
				new BasicNameValuePair("id", shopId));
		return (Shop) mHttpApi.doHttpRequest(httpGet, new ShopParser());
	}

	@Override
	public Group<Province> getProvinces () throws ParseException, IOException, JSONException {
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "Province"));
		return (Group<Province>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new ProvinceParser()));
	}

	@Override
	public Group<City> getCitys (String provinceId) throws ParseException, IOException, JSONException {
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "City"),
				new BasicNameValuePair("provinceId", provinceId));
		return (Group<City>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new CityParser()));
	}

	@Override
	public Group<Shop> getShops(String categoryId) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind","ShopList"),
				new BasicNameValuePair("categoryId", categoryId));
		return (Group<Shop>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new ShopParser()));
	}

	@Override
	public Group<Shop> getShops(String latitude, String longtitude,
			String distance) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		String latilongi = latitude+","+longtitude;
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "ShopList"),
				new BasicNameValuePair("distance", distance),
				new BasicNameValuePair("latilongi",latilongi)
				);
		return (Group<Shop>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new ShopParser()));
	}

	//api part ==end

	public class RemoteResourceFetcher {

	}

	@Override
	public Group<Coupon> getCoupons() throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "CouponList"));
		return (Group<Coupon>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new CouponParser()));
	}

	@Override
	public Group<GroupBuy> getGroupBuys() throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "GroupBuyList"));
		return (Group<GroupBuy>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new GroupParser(new GroupBuyParser())));
	}

	@Override
	public Coupon getCoupon(String couponId) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind","Coupon"),
				new BasicNameValuePair("couponId", couponId));
		return (Coupon) this.mHttpApi.doHttpRequest(httpGet, new CouponParser());
	}

	@Override
	public GroupBuy getGroupBuy(String groupBuyId) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "GroupBuy"),
				new BasicNameValuePair("groupBuyId", groupBuyId));
		return (GroupBuy) this.mHttpApi.doHttpRequest(httpGet, new GroupBuyParser());
	}

	@Override
	public void setCredentials(String user, String password) {
		// TODO Auto-generated method stub
		if(user == null || user.length() == 0 || password == null || password.length() == 0) {
			this.mHttpClient.getCredentialsProvider().clear();
		} else {
			this.mHttpClient.getCredentialsProvider().setCredentials(mAuthScope,
					new UsernamePasswordCredentials(user, password));
		}

	}

	@Override
	public Group<District> getDistricts(String cityId) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "District"),
				new BasicNameValuePair("cityId", cityId));
		return  (Group<District>) this.mHttpApi.doHttpRequest(httpGet, new GroupParser( new DistrictParser()));
	}



}