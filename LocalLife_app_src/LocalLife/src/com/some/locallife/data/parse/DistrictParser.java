package com.some.locallife.data.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.City;
import com.some.locallife.data.type.District;
import com.some.locallife.util.Util;

public class DistrictParser extends AbstractParser<District> {

	@Override
	public District parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		Util.getData("District parse==");
		District obj = new District();
		if (json.has("id")) {
			obj.setId(json.getString("id"));
		}
		if (json.has("name")) {
			obj.setName(json.getString("name"));
		}
		if (json.has("city")) {
			obj.setCityId(json.getString("city"));
		}
		return obj;
	}

}
