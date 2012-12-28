package com.some.locallife.data.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.json.JSONException;

import com.some.locallife.data.parse.Parser;
import com.some.locallife.data.type.LocalType;

public interface HttpApi {
	abstract public LocalType doHttpRequest(HttpRequestBase httpRequest,
			Parser<? extends LocalType> parser) throws ParseException, IOException, JSONException;

	abstract public String doHttpPost(String url, NameValuePair... nameValuePairs);

	abstract public HttpGet createHttpGet(String url, NameValuePair... nameValuePairs);

	abstract public HttpPost createHttpPost(String url, NameValuePair... nameValuePairs);

	abstract public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary);
}