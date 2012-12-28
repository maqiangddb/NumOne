package com.some.locallife.app;

import com.some.locallife.data.type.LocalType;

public interface BaseStateHolder {

	public abstract void setData (LocalType data);

	public abstract void startTask();

	public abstract void onStartTask();

}
