package com.some.locallife.data.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.util.Log;

import com.some.locallife.data.parse.Parser;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.data.util.JSONUtils;
import com.some.locallife.util.Util;


abstract public class AbstractHttpApi implements HttpApi {
	private static final String DEFAULT_CLIENT_VERSION = "";
	private static final String CLIENT_VERSION_HEADER = "User-Agent";
	private static final int TIMEOUT = 60;

	private final DefaultHttpClient mHttpClient;
	private final String mClientVersion;

	public AbstractHttpApi(DefaultHttpClient httpClient, String clientVersion) {
		mHttpClient = httpClient;
		if (clientVersion != null) {
			mClientVersion = clientVersion;
		} else {
			mClientVersion = DEFAULT_CLIENT_VERSION;
		}
	}



	@Override
	public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
		Util.httpRequest(">>>createHttpGet");
		String query = URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
		HttpGet httpGet = new HttpGet(url + "?" + query);
		httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		// TODO Auto-generated method stub
		return httpGet;
	}

	private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
		Util.httpRequest(">>>stripNulls");
		List<NameValuePair> params = new ArrayList<NameValuePair> ();
		for (int i = 0; i< nameValuePairs.length; i++) {
			NameValuePair param = nameValuePairs[i];
			if (param.getValue() != null) {
				Util.httpRequest("Param:"+param);
				params.add(param);
			}
		}
		return params;
	}



	public LocalType executeHttpRequest(HttpRequestBase httpRequest,
		Parser<? extends LocalType> parser) throws ParseException, IOException, JSONException {
		Util.httpRequest("doHttpRequest:"+httpRequest.getURI());
		HttpResponse response = executeHttpRequest(httpRequest);
		Util.httpRequest("execute httpRequest: "+httpRequest.getURI());
		int statusCode = response.getStatusLine().getStatusCode();
		Util.httpRequest("response statusCode:"+statusCode);
		switch(statusCode) {
			case 200:
				String content = EntityUtils.toString(response.getEntity());
				Util.httpRequest("get json so good!!content: "+content);
				return JSONUtils.consume(parser, content);
			case 400:

			case 401:

			case 404:

			case 500:

			default:


		}
		return null;
	}

	public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws ClientProtocolException, IOException {

		mHttpClient.getConnectionManager().closeExpiredConnections();
		return mHttpClient.execute(httpRequest);
	}
/*
	public HttpGet createHttpGet(String url, NameValuePair... nameValuePair) {
		String query = URLEncodedUtils.format(stripNulls(nameValuePair), HTTP.UTF_8);
		HttpGet httpGet = new HttpGet(url + "?" +query);
		httpGet.addHeader(CLIENT_VERSION_HEADER, mClientVersion);
		return httpGet;
	}

	private List<NameValuePair> stripNulls(
			NameValuePair... nameValuePairs) {
		// TODO Auto-generated method stub
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (int i = 0; i < nameValuePairs.length; i++) {
			NameValuePair param = nameValuePairs[i];
			if (param.getValue() != null) {
				params.add(param);
			}
		}
		return params;
	}

	public static DefaultHttpClient creatHttpClient() {
		// TODO Auto-generated method stub
		return null;
	}
	*/


/*
	@Override
	public LocalType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends LocalType> parser) throws ParseException, IOException, JSONException {
		// TODO Auto-generated method stub
		return null;
	}
*/


	@Override
	public String doHttpPost(String url, NameValuePair... nameValuePairs) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public HttpURLConnection createHttpURLConnectionPost(URL url,
			String boundary) {
		// TODO Auto-generated method stub
		return null;
	}



	public static DefaultHttpClient creatHttpClient() {
		// TODO Auto-generated method stub
		final SchemeRegistry supportedSchemes = new SchemeRegistry();
		//Register the "http" protocol scheme, it is required
		//by the default operator to look up socket factories.
		final SocketFactory sf = PlainSocketFactory.getSocketFactory();
		supportedSchemes.register(new Scheme("http", sf, 80));
		supportedSchemes.register(new Scheme("https", SSLSocketFactory.getSocketFactory(),443));
		//Set some client http client parameter defaults.
		final HttpParams httpParams = createHttpParams(TIMEOUT);
		HttpClientParams.setRedirecting(httpParams, false);

		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,
				supportedSchemes);
		return new DefaultHttpClient(ccm, httpParams);
	}

	public static DefaultHttpClient createResourceHttpClient() {
		int timeout = 10;
		final HttpParams httpParams = createHttpParams(timeout);
		final SchemeRegistry supportedSchemes = new SchemeRegistry();

		final SocketFactory sf = PlainSocketFactory.getSocketFactory();
		supportedSchemes.register(new Scheme("http", sf, 80));

		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams,
				supportedSchemes);
		return new DefaultHttpClient(ccm, httpParams);
	}


// Create the default HTTP protocol parameters
	private static HttpParams createHttpParams(int timeout) {
		// TODO Auto-generated method stub
		final HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setStaleCheckingEnabled(params, false);

		HttpConnectionParams.setConnectionTimeout(params, timeout * 1000);
		HttpConnectionParams.setSoTimeout(params, timeout * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);
		return params;
	}
}