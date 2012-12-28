package com.some.locallife.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import android.net.Uri;


public class RemoteResourceManager extends Observable{
	private DiskCache mDiskCache;
	private RemoteResourceFetcher mRemoteResourceFetcher;
	private FetcherObserver mFetcherObserver = new FetcherObserver();

	public RemoteResourceManager(String cacheName) {
		this(new BaseDiskCache("locallife", cacheName));
	}

	public RemoteResourceManager(DiskCache cache) {
		mDiskCache = cache;
		this.mRemoteResourceFetcher = new RemoteResourceFetcher(mDiskCache);
		this.mRemoteResourceFetcher.addObserver(mFetcherObserver);
	}

	public boolean exists(Uri uri) {
		return this.mDiskCache.exists(Uri.encode(uri.toString()));
	}

	public File getFile(Uri uri) {
		return mDiskCache.getFile(Uri.encode(uri.toString()));
	}

	public InputStream getInputStream(Uri uri) throws IOException {
		return this.mDiskCache.getInputStream(Uri.encode(uri.toString()));
	}

	public void request(Uri uri) {
		this.mRemoteResourceFetcher.fetch(uri, Uri.encode(uri.toString()));
	}

	public void invalidate(Uri uri) {
		this.mDiskCache.invalidate(Uri.encode(uri.toString()));
	}

	public void shutdown() {
		this.mRemoteResourceFetcher.shutdown();
		this.mDiskCache.cleanup();
	}

	public void clear() {
		this.mRemoteResourceFetcher.shutdown();
		this.mDiskCache.clear();
	}

	private class FetcherObserver implements Observer {

		@Override
		public void update(Observable observable, Object data) {
			// TODO Auto-generated method stub
			setChanged();
			notifyObservers(data);
		}

	}
}
