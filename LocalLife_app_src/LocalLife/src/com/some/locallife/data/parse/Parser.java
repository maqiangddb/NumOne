package com.some.locallife.data.parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.LocalType;

public interface Parser<T extends LocalType> {

	public abstract T parse(JSONObject json) throws JSONException;
	public Group parse(JSONArray array) throws JSONException;

}
