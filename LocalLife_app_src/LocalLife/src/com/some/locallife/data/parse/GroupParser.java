package com.some.locallife.data.parse;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.some.locallife.data.type.Group;
import com.some.locallife.data.type.LocalType;
import com.some.locallife.util.Util;

public class GroupParser extends AbstractParser<Group>{

	private Parser<? extends LocalType> mSubParser;

	public GroupParser(Parser<? extends LocalType> subParser) {
		mSubParser = subParser;
	}

	public Group<LocalType> parse(JSONObject json) throws JSONException {
		Util.log(">>"+GroupParser.class.getName()+"==parse JSONObject");
		Group<LocalType> group = new Group<LocalType>();

		Iterator<String> it = (Iterator<String>)json.keys();
		while(it.hasNext()) {
			String key = it.next();
			Util.log("key:"+key);
			if (key.equals("kind")) {
				group.setType(json.getString(key));
			} else {
				Object obj = json.get(key);
				Util.log("key=2:"+key);
				if (obj instanceof JSONArray) {
					Util.log("this is JSONArray===");
					parse(group, (JSONArray)obj);
				} else {

				}
			}
		}
		return group;
	}

	@Override
	public Group parse(JSONArray array) throws JSONException {
		// TODO Auto-generated method stub
		Group<LocalType> group = new Group<LocalType>();
		parse(group, array);
		return group;
	}

	private void parse(Group group, JSONArray array) throws JSONException {
		Util.log("pase JSONArray===");
		for (int i = 0, m = array.length(); i < m; i++) {
			Object element = array.get(i);
			LocalType item = null;
			if (element instanceof JSONArray) {
				android.util.Log.i("MQ","SUB PARSER pase jsonarray");
				item = mSubParser.parse((JSONArray) element);
			} else {
				android.util.Log.i("MQ","SUB PARSER pase jsonobject");
				item = mSubParser.parse((JSONObject)element);
			}

			group.add(item);
		}
	}

}
