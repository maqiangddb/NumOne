package com.some.locallife.data.parse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.Shop;
import com.some.locallife.util.Util;

public class ShopParser extends AbstractParser<Shop>{

	@Override
	public Shop parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		Util.log("Shop parser parse jsonobject");
		Util.log("json is :"+json);
		Shop obj = new Shop();
		if (json.has("latilongi")) {
			obj.setLatilongi(json.getString("latilongi"));
		}
		if (json.has("id")) {
			obj.setId(json.getString("id"));
		}
		if (json.has("phone")) {
			obj.setTeleNum(json.getString("phone"));
		}
		if (json.has("category")) {
			obj.setCategoryId(json.getString("category"));
		}
		if (json.has("address")) {
			obj.setAddress(json.getString("address"));
		}
		if (json.has("longitude")) {
			obj.setLongitude(json.getString("longitude"));
		}
		if (json.has("latitude")) {
			obj.setLatitude(json.getString("latitude"));
		}
		if (json.has("district")) {
			obj.setDistrictId(json.getString("district"));
		}
		if (json.has("name")) {
			Util.log("pase name :"+json.getString("name"));
			obj.setName(json.getString("name"));
		}
		if (json.has("type")) {
			obj.setType(json.getString("type"));
		}
		if (json.has("average")) {
			obj.setAverage(json.getString("average"));
		}
		if (json.has("images")) {
			//obj.setImageUrls(json.getString("images"));
			Util.log("========================parse shop images "+json.get("images"));
			String[] imgs = null;
			JSONArray jobj = (JSONArray) json.get("images");
			Util.log("length="+jobj.length());
			if (json.get("images") instanceof JSONArray) {
				Util.log("key:images is jsonarray!!!!!");
				JSONArray array = json.getJSONArray("images");
				imgs = new String[array.length()];
				for(int i = 0; i < array.length(); i++) {
					Util.log("="+array.length()+"=="+i+"=="+array.getString(i));
					imgs[i] = array.getString(i);
					Util.log("="+array.length()+"=="+i+"=="+imgs[i]);
					Util.log("imgUrl"+imgs[i]);
				}

				obj.setImageUrls(imgs);

			}
		}
		if (json.has("image")) {
			obj.setImageUrl(json.getString("image"));
		}
		return obj;
	}

}
