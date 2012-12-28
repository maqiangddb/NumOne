package com.some.locallife.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;
import android.util.Log;

public class BaseDiskCache implements DiskCache {
	private static final String NOMEDIA = ".nomedia";
	private static final int MIN_FILE_SIZE_IN_BYTES = 100;
	private static final String TAG = "MQ";

	private File mStorageDirectory;

	BaseDiskCache(String dirPath, String name) {
		File baseDirectory = new File(Environment.getExternalStorageDirectory(), dirPath);
		File storageDirectory = new File(baseDirectory, name);
		createDirectory(storageDirectory);
		mStorageDirectory = storageDirectory;
		cleanupSimple();
	}

	private static final void createDirectory(File storageDirectory) {
		if(!storageDirectory.exists()) {
			Log.d(TAG,"Trying to create storageDirectory:"
					+ String.valueOf(storageDirectory.mkdirs()));
            Log.d(TAG, "Trying to create storageDirectory: "
                    + String.valueOf(storageDirectory.mkdirs()));

            Log.d(TAG, "Exists: " + storageDirectory + " "
                    + String.valueOf(storageDirectory.exists()));
            Log.d(TAG, "State: " + Environment.getExternalStorageState());
            Log.d(TAG, "Isdir: " + storageDirectory + " "
                    + String.valueOf(storageDirectory.isDirectory()));
            Log.d(TAG, "Readable: " + storageDirectory + " "
                    + String.valueOf(storageDirectory.canRead()));
            Log.d(TAG, "Writable: " + storageDirectory + " "
                    + String.valueOf(storageDirectory.canWrite()));
            File tmp = storageDirectory.getParentFile();
            Log.d(TAG, "Exists: " + tmp + " " + String.valueOf(tmp.exists()));
            Log.d(TAG, "Isdir: " + tmp + " " + String.valueOf(tmp.isDirectory()));
            Log.d(TAG, "Readable: " + tmp + " " + String.valueOf(tmp.canRead()));
            Log.d(TAG, "Writable: " + tmp + " " + String.valueOf(tmp.canWrite()));
            tmp = tmp.getParentFile();
            Log.d(TAG, "Exists: " + tmp + " " + String.valueOf(tmp.exists()));
            Log.d(TAG, "Isdir: " + tmp + " " + String.valueOf(tmp.isDirectory()));
            Log.d(TAG, "Readable: " + tmp + " " + String.valueOf(tmp.canRead()));
            Log.d(TAG, "Writable: " + tmp + " " + String.valueOf(tmp.canWrite()));
		}

		File nomediaFile = new File(storageDirectory, NOMEDIA);
		if (!nomediaFile.exists()) {
			try {
				Log.d(TAG, "Created file: "+nomediaFile + ""
						+ String.valueOf(nomediaFile.createNewFile()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new IllegalStateException("");
			}
		}

		if (!(storageDirectory.isDirectory() && nomediaFile.exists())) {
			throw new RuntimeException("");
		}
	}

	/*Temporary fix until we rework this disk cache. We delete the first 50 youngest files
	 * if we find the cache has more than 1000 images in it.
	 */
	public void cleanupSimple() {
		final int maxNumFiles = 1000;
		final int numFilesToDelete = 50;

		String[] children = mStorageDirectory.list();
		if (children != null) {
			if (children.length > maxNumFiles) {
				for (int i = children.length - 1, m = i - numFilesToDelete; i > m; i --) {
					File child = new File(mStorageDirectory, children[i]);
					child.delete();
				}
			}
		}
	}

	@Override
	public boolean exists(String key) {
		// TODO Auto-generated method stub
		return getFile(key).exists();
	}

	@Override
	public File getFile(String key) {
		// TODO Auto-generated method stub
		return new File(this.mStorageDirectory.toString()+File.separator+key);
	}

	@Override
	public InputStream getInputStream(String key) throws IOException {
		// TODO Auto-generated method stub
		return new FileInputStream(getFile(key));
	}

	@Override
	public void store(String key, InputStream is) {
		// TODO Auto-generated method stub
		is = new BufferedInputStream(is);
		try {
			OutputStream os = new BufferedOutputStream(new FileOutputStream(getFile(key)));

			byte[] b = new byte[2048];
			int count;
			int total = 0;

			while((count = is.read(b)) > 0) {
				os.write(b, 0, count);
				total += count;
			}
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {}

	}

	@Override
	public void invalidate(String key) {
		// TODO Auto-generated method stub
		getFile(key).delete();
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		String[] children = this.mStorageDirectory.list();
		if (children != null) {
			for(int i = 0; i <children.length; i++) {
				File child = new File(this.mStorageDirectory, children[i]);
				if (!child.equals(new File(this.mStorageDirectory, NOMEDIA))
						&& child.length()<= MIN_FILE_SIZE_IN_BYTES) {
					child.delete();
				}
			}
		}
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		String[] children = this.mStorageDirectory.list();
		if (children != null) {
			for(int i=0; i<children.length; i++) {
				File child = new File(this.mStorageDirectory, children[1]);
				if (!child.equals(new File(this.mStorageDirectory, NOMEDIA))) {
					child.delete();
				}
			}
		}
	}

}
