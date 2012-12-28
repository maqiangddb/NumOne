package com.some.locallife.data.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.BigCategory;
public class BigCategoryParser extends AbstractParser<BigCategory> {

	@Override
	public BigCategory parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		android.util.Log.i("MQ","BIG category parse===");
		BigCategory obj = new BigCategory();
		if(json.has("id")) {
			android.util.Log.i("MQ","key is id=="+json.getString("id"));
			obj.setId(json.getString("id"));
		}
		if(json.has("name")) {
			android.util.Log.i("MQ","key is name=="+json.getString("name"));
			obj.setName(json.getString("name"));
		}
		return obj;
	}

}
