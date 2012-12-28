package com.some.locallife.util;

import com.some.locallife.data.ILocalLifeApi;
import com.some.locallife.data.type.LocalType;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

public class TaskManager {
	private Context mContext;
	public TaskManager() {
	}


	public BaseDataTask createTask(BaseDataTask task, final DoTask doTask, final SetData setData) {
		task.setDoTask(doTask);
		task.setSetData(setData);
		return task;
	}


	public class BaseDataTask extends AsyncTask<Void, Void, LocalType> {
		private DoTask mDoTask;
		private SetData mSetData;

		public void setDoTask (DoTask doTask) {
			this.mDoTask = doTask;
		}

		public void setSetData (SetData setData) {
			this.mSetData = setData;
		}

		@Override
		protected LocalType doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			return this.mDoTask.doTask();
		}

		@Override
		protected void onPostExecute(LocalType data) {
			this.mSetData.setData(data);
		}

	}

	public interface DoTask {
		public abstract LocalType doTask();
	}

	public interface SetData {
		public abstract void setData(LocalType data);
	}
}
