package com.some.locallife.data.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.GroupBuy;
import com.some.locallife.util.Util;

public class GroupBuyParser extends AbstractParser<GroupBuy> {

	@Override
	public GroupBuy parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		Util.getData("========group buy parse===data is :"+json);
		GroupBuy obj = new GroupBuy();
		if(json.has("id")) {
			obj.setId(json.getString("id"));
		}
		if(json.has("name")) {
			obj.setMsg(json.getString("name"));
		}
		if(json.has("now_price")) {
			obj.setNowPrice(json.getString("now_price"));
		}
		if(json.has("image")) {
			obj.setImageUri(json.getString("image"));
		}
		if(json.has("telenum")) {
			obj.setTeleNum(json.getString("telenum"));
		}
		if(json.has("old_price")) {
			obj.setOldPrice(json.getString("old_price"));
		}
		if(json.has("discount")) {
			obj.setDiscount(json.getString("discount"));
		}
		if(json.has("savePrice")) {
			obj.setSavePrice(json.getString("savePrice"));
		}
		Util.groupbuyData(obj);
		return obj;
	}

}
