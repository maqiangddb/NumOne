package com.some.locallife.data.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.Test;

public class TestParser extends AbstractParser<Test> {

	@Override
	public Test parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		Test obj = new Test();
		if (json.has("test")) {
			obj.setValue(json.getString("test"));
		}
		return obj;
	}


}