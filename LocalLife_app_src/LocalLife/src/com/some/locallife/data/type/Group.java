package com.some.locallife.data.type;

import java.util.ArrayList;

public class Group<T extends LocalType> extends ArrayList<T> implements LocalType {

	private String mType;

	public void setType(String type) {
		// TODO Auto-generated method stub
		this.mType = type;
	}


}
