package com.some.locallife.data.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.GroupBuy;

public class GroupBuyParser extends AbstractParser<GroupBuy> {

	@Override
	public GroupBuy parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		GroupBuy obj = new GroupBuy();
		if(json.has("id")) {
			obj.setId(json.getString("id"));
		}
		if(json.has("msg")) {
			obj.setId(json.getString("msg"));
		}
		if(json.has("nowprice")) {
			obj.setId(json.getString("nowprice"));
		}
		if(json.has("imageUrl")) {
			obj.setId(json.getString("imageUrl"));
		}
		if(json.has("telenum")) {
			obj.setId(json.getString("telenum"));
		}
		if(json.has("oldprice")) {
			obj.setId(json.getString("oldprice"));
		}
		if(json.has("discount")) {
			obj.setId(json.getString("discount"));
		}
		if(json.has("savePrice")) {
			obj.setId(json.getString("savePrice"));
		}
		return obj;
	}

}
