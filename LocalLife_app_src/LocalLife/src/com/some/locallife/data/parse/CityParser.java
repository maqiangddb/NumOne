package com.some.locallife.data.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.City;
import com.some.locallife.data.type.Province;
import com.some.locallife.util.Util;

public class CityParser extends AbstractParser<City> {

	@Override
	public City parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		Util.getData("City parse==");
		City obj = new City();
		if (json.has("id")) {
			obj.setId(json.getString("id"));
		}
		if (json.has("name")) {
			obj.setName(json.getString("name"));
		}
		if (json.has("province")) {
			obj.setProvinceId(json.getString("province"));
		}
		return obj;
	}

}
