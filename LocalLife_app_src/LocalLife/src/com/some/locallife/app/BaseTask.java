package com.some.locallife.app;

import com.some.locallife.data.LocalLife;
import com.some.locallife.data.type.LocalType;

import android.content.Context;
import android.os.AsyncTask;

public abstract class BaseTask extends AsyncTask<Void, Void, LocalType> {

	private BaseStateHolder mHolder;
	protected Context mContext;
	protected LocalLife mApi;

	public BaseTask(BaseStateHolder holder) {
		this.mHolder = holder;
	}

	@Override
	protected LocalType doInBackground(Void... params) {
		// TODO Auto-generated method stub
		return doTask();
	}

	public void setContext(Context context) {
		this.mContext = context;
		LocalLifeApplication app = (LocalLifeApplication) this.mContext.getApplicationContext();
		this.mApi = app.getLocalLife();

	}

	public abstract LocalType doTask();

	@Override
	protected void onPreExecute() {
		mHolder.onStartTask();
	}

	@Override
	protected void onPostExecute(LocalType data) {
		android.util.Log.i("MQ","ON POST EXECUTE");
		mHolder.setData(data);
	}


}
