package com.some.locallife.data.parse;

import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.Coupon;
import com.some.locallife.util.Util;

public class CouponParser extends AbstractParser<Coupon> {

	@Override
	public Coupon parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		Util.getData("======coupon parse==data is :"+json);
		Coupon obj = new Coupon();
		if(json.has("name")) {

			obj.setMsg(json.getString("name"));
		}
		if(json.has("price")) {
			obj.setPrice(json.getString("price"));
		}
		if(json.has("image")) {
			obj.setImageUrl(json.getString("image"));
		}
		if(json.has("telenum")) {
			obj.setTeleNum(json.getString("telenum"));
		}
		if(json.has("pagevalue")) {
			obj.setPageValue(json.getString("pagevalue"));
		}
		if(json.has("lifetime")) {
			obj.setLifeTime(json.getString("lifetime"));
		}
		if(json.has("id")) {
			obj.setId(json.getString("id"));
		}
		Util.couponData(obj);
		return obj;
	}

}
