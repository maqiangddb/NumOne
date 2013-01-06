package com.some.locallife.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import com.some.locallife.data.error.LocalException;
import com.some.locallife.data.http.AbstractHttpApi;
import com.some.locallife.data.http.HttpApi;
import com.some.locallife.data.http.HttpApiBasic;
import com.some.locallife.data.parse.BigCategoryParser;
import com.some.locallife.data.parse.CategoryParser;
import com.some.locallife.data.parse.CityParser;
import com.some.locallife.data.parse.CommentParser;
import com.some.locallife.data.parse.CouponParser;
import com.some.locallife.data.parse.DistrictParser;
import com.some.locallife.data.parse.GroupBuyParser;
import com.some.locallife.data.parse.GroupParser;
import com.some.locallife.data.parse.ProvinceParser;
import com.some.locallife.data.parse.ResponseParser;
import com.some.locallife.data.parse.ShopParser;
import com.some.locallife.data.parse.TestParser;
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
import com.some.locallife.util.Util;

public class LocalLifeHttpApi implements ILocalLifeApi{

	private static final String DATATYPE = ".json";

	private static final String URL_API_TEST = "/test";

	private final DefaultHttpClient mHttpClient = AbstractHttpApi.creatHttpClient();
	private HttpApi mHttpApi;

	//private final DefaultHttpClient mResourceHttpClient = AbstractHttpApi.createResourceHttpClient();

	private final String mApiBaseUrl;
	private final AuthScope mAuthScope;

	public LocalLifeHttpApi (String domain, String clientVersion, boolean useLogin) {
		mApiBaseUrl = "http://" + domain + "/api";
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
	public Test getTest () throws ParseException, IOException, JSONException, LocalException {
		Util.login("===2"+this.mHttpClient.getCredentialsProvider().getCredentials(new AuthScope("numone.sinaapp.com", 80)));
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(URL_API_TEST),
				new BasicNameValuePair("kind","FavShop"));
		return (Test) mHttpApi.doHttpRequest(httpGet, new TestParser());
	}


	@Override
	public Group<BigCategory> getBigCategory() throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		Util.httpRequest("hasCredentials:"+this.hasCredentials());
		Util.login("schemes:"+this.mHttpClient.getAuthSchemes().getSchemeNames());
		Util.login("getCredentials=="+this.mHttpClient.getCredentialsProvider().getCredentials(mAuthScope));
		Util.login("mHttpClient:"+mHttpClient);
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind","BigCategory"));
		Util.login("getCredentials=2="+this.mHttpClient.getCredentialsProvider().getCredentials(mAuthScope));
		Util.login("mHttpClient:"+mHttpClient);
		return (Group<BigCategory>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new BigCategoryParser()));
	}

	@Override
	public Group<Category> getCategory(String bigCategoryId) throws ParseException, IOException, JSONException, LocalException {
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind","Category"),
				new BasicNameValuePair("bigCategoryId",bigCategoryId));
		return (Group<Category>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new CategoryParser()));
	}

	@Override
	public Shop getShop (String shopId) throws ParseException, IOException, JSONException, LocalException {
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "Shop"),
				new BasicNameValuePair("id", shopId));
		return (Shop) mHttpApi.doHttpRequest(httpGet, new ShopParser());
	}

	@Override
	public Group<Province> getProvinces () throws ParseException, IOException, JSONException, LocalException {
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "Province"));
		return (Group<Province>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new ProvinceParser()));
	}

	@Override
	public Group<City> getCitys (String provinceId) throws ParseException, IOException, JSONException, LocalException {
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "City"),
				new BasicNameValuePair("provinceId", provinceId));
		return (Group<City>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new CityParser()));
	}

	@Override
	public Group<Shop> getShops(String categoryId) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind","ShopList"),
				new BasicNameValuePair("categoryId", categoryId));
		return (Group<Shop>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new ShopParser()));
	}

	@Override
	public Group<Shop> getShops(String latitude, String longtitude,
			String distance) throws ParseException, IOException, JSONException, LocalException {
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
	public Group<Coupon> getCoupons() throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "CouponList"));
		return (Group<Coupon>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new CouponParser()));
	}

	@Override
	public Group<GroupBuy> getGroupBuys() throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "GroupBuyList"));
		return (Group<GroupBuy>) mHttpApi.doHttpRequest(httpGet, new GroupParser(new GroupBuyParser()));
	}

	@Override
	public Coupon getCoupon(String couponId) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind","Coupon"),
				new BasicNameValuePair("couponId", couponId));
		return (Coupon) this.mHttpApi.doHttpRequest(httpGet, new CouponParser());
	}

	@Override
	public GroupBuy getGroupBuy(String groupBuyId) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "GroupBuy"),
				new BasicNameValuePair("groupBuyId", groupBuyId));
		return (GroupBuy) this.mHttpApi.doHttpRequest(httpGet, new GroupBuyParser());
	}

	@Override
	public void setCredentials(String user, String password) {
		// TODO Auto-generated method stub
		Util.login("in LocalLifeHttpApi===setCredentials==host:"+this.mAuthScope.getHost()+"==port:"+this.mAuthScope.getPort());
		UsernamePasswordCredentials upc = new UsernamePasswordCredentials(user, password);
		BasicCredentialsProvider bcp = new BasicCredentialsProvider();
		bcp.setCredentials(mAuthScope, upc);
		this.mHttpClient.setCredentialsProvider(bcp);
		/*
		if(user == null || user.length() == 0 || password == null || password.length() == 0) {
			this.mHttpClient.getCredentialsProvider().clear();
		} else {
			Util.login("in LocalLifeHttpApi===setCredentials=="+user+","+password);
			this.mHttpClient.getCredentialsProvider().setCredentials(mAuthScope,
					new UsernamePasswordCredentials(user, password));
		}
		*/

	}

	public void clearCredentials() {
		this.mHttpClient.getCredentialsProvider().clear();
	}

	public void testFunction() throws ClientProtocolException, IOException {
		// 1. 获取并设置url地址，一个字符串变量，一个URL对象

		String urlStr ="http://<host>:<port>/data.json";

		URL url= new URL(urlStr);

		// 2. 创建一个密码证书，(UsernamePasswordCredentials类)

		  String username="foo";

		  String password="bar";

		  UsernamePasswordCredentials upc = new UsernamePasswordCredentials(username, password);

		// 3. 设置认证范围 (AuthScore类)

		String strRealm = "<mydomain>";

		String strHost = url.getHost();

		int iPort = url.getPort();

		AuthScope as = new AuthScope(strHost, iPort, strRealm);

		// 4. 创建认证提供者(BasicCredentials类) ，基于as和upc

		BasicCredentialsProvider bcp=new BasicCredentialsProvider();

		bcp.setCredentials(as, upc);

		// 5. 创建Http客户端(DefaultHttpClient类)

		DefaultHttpClient client=new DefaultHttpClient();

		// 6. 为此客户端设置认证提供者

		client.setCredentialsProvider(bcp);

		// 7. 创建一个get 方法(HttpGet类)，基于所访问的url地址

		HttpGet hg= new HttpGet(urlStr);

		// 8. 执行get方法并返回 response

		HttpResponse  hr = client.execute(hg);

		// 9. 从response中取回数据，使用InputStreamReader读取Response的Entity：

		        String line=null;

		        StringBuilder builder = new StringBuilder();

		        BufferedReader reader = new BufferedReader(new InputStreamReader(hr.getEntity().getContent() ));

		        while((line = reader.readLine()) != null)  builder.append(line);

		        String strContent = builder.toString();
	}

	public boolean hasCredentials() {
		return this.mHttpClient.getCredentialsProvider().getCredentials(mAuthScope) != null;
	}

	@Override
	public Group<District> getDistricts(String cityId) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "District"),
				new BasicNameValuePair("cityId", cityId));
		return  (Group<District>) this.mHttpApi.doHttpRequest(httpGet, new GroupParser( new DistrictParser()));
	}

	@Override
	public LocalType postLogcat(String msg) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpPost httpPost = this.mHttpApi.createHttpPost(fullUrl(null),
				new BasicNameValuePair("kind", "Logcat"),
				new BasicNameValuePair("text", msg));
		return this.mHttpApi.doHttpRequest(httpPost, new ResponseParser());
	}

	@Override
	public Group<Shop> getFavShopList() throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		Util.httpRequest("hasCredentials:"+this.hasCredentials());
		Util.login("schemes:"+this.mHttpClient.getAuthSchemes().getSchemeNames());
		Util.login("getCredentials=="+this.mHttpClient.getCredentialsProvider().getCredentials(mAuthScope));
		Util.login("mHttpClient:"+mHttpClient);
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "FavShop"));
		/*
		try {
        System.out.println("executing request:" + httpGet.getRequestLine());
        HttpResponse response = this.mHttpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        System.out.println("----------------------------------------");
        System.out.println(response.getStatusLine());
        if (entity != null) {
            System.out.println("Response content length: " + entity.getContentLength());
            byte b[] = new byte[(int) entity.getContentLength()];
            entity.getContent().read(b);
            System.out.println("Content: " + new String(b));;
        }
        //EntityUtils.consume(entity);
    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    return null;
		*/



		return (Group<Shop>) this.mHttpApi.doHttpRequest(httpGet, new GroupParser(new ShopParser()));
	}

	@Override
	public Group<Coupon> getFavCouponList() throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "FavCoupon"));
		return (Group<Coupon>) this.mHttpApi.doHttpRequest(httpGet, new GroupParser(new CouponParser()));
	}

	@Override
	public Group<GroupBuy> getFavGroupBuyList() throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "FavGroupBuy"));
		return (Group<GroupBuy>) this.mHttpApi.doHttpRequest(httpGet, new GroupParser(new GroupBuyParser()));
	}

	@Override
	public Group<Comment> getShopComments(String shopId) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "ShopComment"),
				new BasicNameValuePair("id", shopId)
		);
		return (Group<Comment>) this.mHttpApi.doHttpRequest(httpGet, new GroupParser(new CommentParser()));
	}

	@Override
	public Response postShopComment(String shopId, String msg) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("action","comment"),
				new BasicNameValuePair("kind", "Shop"),
				new BasicNameValuePair("id", shopId),
				new BasicNameValuePair("content", msg)
		);
		return (Response) this.mHttpApi.doHttpRequest(httpGet, new ResponseParser());
	}

	@Override
	public Group<Comment> getCouponComments(String couponId) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "CouponComment"),
				new BasicNameValuePair("id", couponId)
		);
		return (Group<Comment>) this.mHttpApi.doHttpRequest(httpGet, new GroupParser(new CommentParser()));
	}

	@Override
	public Response postCouponComment(String couponId, String msg) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("action","comment"),
				new BasicNameValuePair("kind", "Coupon"),
				new BasicNameValuePair("id", couponId),
				new BasicNameValuePair("content", msg)
		);
		return (Response) this.mHttpApi.doHttpRequest(httpGet, new ResponseParser());
	}

	@Override
	public Group<Comment> getGroupBuyComments(String groupBuyId) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("kind", "GroupBuyComment"),
				new BasicNameValuePair("id", groupBuyId)
		);
		return (Group<Comment>) this.mHttpApi.doHttpRequest(httpGet, new GroupParser(new CommentParser()));
	}

	@Override
	public Response postGroupBuyComment(String groupBuyId, String msg) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("action","comment"),
				new BasicNameValuePair("kind", "GroupBuy"),
				new BasicNameValuePair("id", groupBuyId),
				new BasicNameValuePair("content", msg)
		);
		return (Response) this.mHttpApi.doHttpRequest(httpGet, new ResponseParser());
	}

	@Override
	public Response favShop(String shopId) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("action","fav"),
				new BasicNameValuePair("kind", "Shop"),
				new BasicNameValuePair("id", shopId)
				);
		return (Response) this.mHttpApi.doHttpRequest(httpGet, new ResponseParser());
	}

	@Override
	public Response favCoupon(String couponId) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("action","fav"),
				new BasicNameValuePair("kind", "Coupon"),
				new BasicNameValuePair("id", couponId)
				);
		return (Response) this.mHttpApi.doHttpRequest(httpGet, new ResponseParser());
	}

	@Override
	public Response favGroupBuy(String groupBuyId) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("action","fav"),
				new BasicNameValuePair("kind", "GroupBuy"),
				new BasicNameValuePair("id", groupBuyId)
				);
		return (Response) this.mHttpApi.doHttpRequest(httpGet, new ResponseParser());
	}

	@Override
	public Response register(String user, String password) throws ParseException, IOException, JSONException, LocalException {
		// TODO Auto-generated method stub
		HttpGet httpGet = this.mHttpApi.createHttpGet(fullUrl(null),
				new BasicNameValuePair("action", "register"),
				new BasicNameValuePair("username", user),
				new BasicNameValuePair("password", password)
		);
		return (Response) this.mHttpApi.doHttpRequest(httpGet, new ResponseParser());
	}



}