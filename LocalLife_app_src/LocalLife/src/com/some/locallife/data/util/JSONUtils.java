package com.some.locallife.data.util;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.parse.Parser;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.util.Util;

public class JSONUtils {


	public static LocalType consume(Parser<? extends LocalType> parser, String content) throws JSONException {
		JSONObject json = new JSONObject(content);
			if (json.has("apiVersion")) {
				Util.log("apiVersion:"+json.get("apiVersion"));
			}
			if (json.has("data")) {
				Util.log("parse data:"+json.get("data"));
				return parser.parse((JSONObject) json.get("data"));
			} else {
				throw new JSONException("no valueable data contained!!");
			}
			/*
			if (key.equals("error")) {

			} else {
				Object obj = json.get(key);
				if (obj instanceof JSONArray) {
					return parser.parse((JSONArray)obj);
				} else if (obj instanceof JSONObject){
					return parser.parse((JSONObject)obj);
				} else {
					Util.log("MQ", "obj:"+obj);
				}
			}
			*/
	}
}