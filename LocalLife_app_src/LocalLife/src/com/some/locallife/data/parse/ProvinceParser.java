package com.some.locallife.data.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.Province;
import com.some.locallife.util.Util;

public class ProvinceParser extends AbstractParser<Province> {

	@Override
	public Province parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		Util.getData("Province parse==");
		Province obj = new Province();
		if (json.has("id")) {
			obj.setId(json.getString("id"));
		}
		if (json.has("name")) {
			obj.setName(json.getString("name"));
		}
		return obj;
	}

}
