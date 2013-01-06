package com.some.locallife.data.http;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;

import com.some.locallife.data.error.LocalException;
import com.some.locallife.data.parse.Parser;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.util.Util;

public class HttpApiBasic extends AbstractHttpApi {
	private HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {

		@Override
		public void process(final HttpRequest request,final HttpContext context)
				throws HttpException, IOException {
			// TODO Auto-generated method stub
			AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
			CredentialsProvider credsProvider = (CredentialsProvider) context
					.getAttribute(ClientContext.CREDS_PROVIDER);
			HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
			// If not auth scheme has been initialized yet
			if (authState.getAuthScheme() == null) {
				Util.login("getHostName():"+targetHost.getHostName()+"=port:"+targetHost.getPort());
				AuthScope authScope = new AuthScope(targetHost.getHostName(), 80);//targetHost.getPort());
				//Obtain credentials matching the target host
				org.apache.http.auth.Credentials creds = credsProvider.getCredentials(authScope);
				Util.login("Credentials==="+creds);
				// If found, generate BasicScheme preemptively
				if (creds != null) {
					authState.setAuthScheme(new BasicScheme());
					authState.setCredentials(creds);
				} else {
					//creds = new UsernamePasswordCredentials("test", "password");
					//authState.setAuthScheme(new DigestScheme());
					//authState.setCredentials(creds);
				}
			}

		}};

	public HttpApiBasic(DefaultHttpClient httpClient, String clientVersion) {
		super(httpClient, clientVersion);

		//httpClient.addRequestInterceptor(preemptiveAuth, 0);
	}

	public LocalType doHttpRequest(HttpRequestBase httpRequest,
		Parser<? extends LocalType> parser) throws ParseException, IOException, JSONException, LocalException {
		return executeHttpRequest(httpRequest, parser);
	}


}