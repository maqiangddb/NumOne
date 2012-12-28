package com.some.locallife.data.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.Category;
import com.some.locallife.util.Util;

public class CategoryParser extends AbstractParser<Category> {

	@Override
	public Category parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		Util.log("category parse==");
		Category obj = new Category();
		if(json.has("id")) {
			obj.setId(json.getString("id"));
		}
		if(json.has("name")) {
			obj.setName(json.getString("name"));
		}
		if(json.has("BigCategory")) {
			obj.setParentId(json.getString("BigCategory"));
		}
		return obj;
	}

}
