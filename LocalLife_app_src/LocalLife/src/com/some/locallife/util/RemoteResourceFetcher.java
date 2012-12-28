package com.some.locallife.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.GZIPInputStream;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.net.Uri;

public class RemoteResourceFetcher extends Observable {



	private DiskCache mResourceCache;
	private ExecutorService mExecutor;

	private HttpClient mHttpClient;

	private ConcurrentHashMap<Request, Callable<Request>> mActiveRequestsMap = new ConcurrentHashMap<Request, Callable<Request>>();

	public RemoteResourceFetcher(DiskCache cache) {
		// TODO Auto-generated constructor stub
		this.mResourceCache = cache;
		this.mHttpClient = createHttpClient();
		this.mExecutor = Executors.newCachedThreadPool();
	}

	public void notifyObservers(Object data) {
		setChanged();
		super.notifyObservers(data);
	}

	public static final DefaultHttpClient createHttpClient() {
		HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setStaleCheckingEnabled(params, false);

		HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
		HttpConnectionParams.setSoTimeout(params, 10 * 1000);
		HttpConnectionParams.setSocketBufferSize(params, 8192);

		final SchemeRegistry supportedSchemes = new SchemeRegistry();

		final SocketFactory sf = PlainSocketFactory.getSocketFactory();
		supportedSchemes.register(new Scheme("http", sf, 80));

		final ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
				supportedSchemes);
		return new DefaultHttpClient(ccm, params);
	}

	private static class Request {

		Uri uri;
		String hash;

		public Request(Uri requestUri, String requestHash) {
			uri = requestUri;
			hash = requestHash;
		}

		public int hashCode() {
			return hash.hashCode();
		}
	}

	public void shutdown() {
		// TODO Auto-generated method stub

	}

	private Callable<Request> newRequestCall (final Request request) {
		return new Callable<Request>() {

			@Override
			public Request call() throws Exception {
				// TODO Auto-generated method stub
				HttpGet httpGet = new HttpGet(request.uri.toString());
				httpGet.addHeader("Accept-Encoding", "gzip");
				HttpResponse response = mHttpClient.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream is = getUngzippedContent(entity);
				mResourceCache.store(request.hash, is);
				return null;
			}};
	}

	public static InputStream getUngzippedContent(HttpEntity entity) throws IllegalStateException, IOException {
		InputStream responseStream = entity.getContent();
		if (responseStream == null) {
			return responseStream;
		}
		Header header = entity.getContentEncoding();
		if (header == null) {
			return responseStream;
		}
		String contentEncoding = header.getValue();
		if (contentEncoding == null) {
			return responseStream;
		}
		if (contentEncoding.contains("gzip")) {
			responseStream = new GZIPInputStream(responseStream);
		}
		return responseStream;
	}

	public Future<Request> fetch(Uri uri, String hash) {
		// TODO Auto-generated method stub

		Request request = new Request(uri, hash);
		synchronized(this.mActiveRequestsMap) {
			Callable<Request> fetcher = newRequestCall(request);
			if(this.mActiveRequestsMap.putIfAbsent(request, fetcher) == null) {
				return this.mExecutor.submit(fetcher);
			} else {

			}
		}
		return null;
	}
}
