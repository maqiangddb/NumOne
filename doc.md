data structure
--------------

**big categrory**

- name

**category**

- name

**shop**

- name 名称
- description 详细描述
- latilongi 经纬度
- district 所在区域
- average 人均消费
- phone 电话

json standard
-------------

**json example for shop**

```json
{
    "data": {
        "kind": "shop",
        "name": "xxx",
        "description": "xxx",
        "imageCount": 2
        "images": [
            "/data/shop/image/a.jpg",
            "/data/shop/image/b.jpg"
        ]
    }
}
```

**json example for shop list**

```json
{
    "data": {
        "kind": "shoplist",
        "totalItems": 6741,
        "startIndex": 1,
        "itemsPerPage": 10,
        "itemCount": 10,
        "items": [
            {
                "name": "xxx",
                "type": "专营门店",
                "latilongi": "+40.6894-074.0447"
                "average": 35,
                "image": "data/shop/thumb/a.jpg"
                // ...
            },
            // ...
        ]
    }
}
```

**json example for error**

```json
{
    "error": {
        "code": 404,
        "message": "can't find"
    }
}
```

**data** and **error** will never show up together.

u can visit this link to see more:
https://github.com/darcyliu/google-styleguide/blob/master/JSONStyleGuide.md

interfaces
----------

url: /api

parameters:

- action
- target

...

**example:**

/api?action=get&target=shoplist&district=futian&count=10

//request JSON in android
// example

private static final REQUEST_URL = "bala bala";
/*
这个parameters是应用传过来的，所以里面已经设过action, target, district, count
等值了。 
*/
private String request(Bundle parameters) {
    parameters.putString("format","JSON");

    if (isSessionKeyValid()) {
        parameters.putString("session_key", accessTokenManager.getSessionKey());
    }

    this.prepareParams(parameters);

    String response = Util.openUrl(REQUEST_URL, "POST", parameters);

    return response;

}

private void prepareParams(Bundle params) {
    params.putString("v","1.0");
    params.putString("call_id", String.valueOf(System.currentTimeMillis()));

    StringBuffer sb = new StringBuffer();
    Set<String> keys = new TreeSet<String>(params.keySet());

    for(String key : keys) {
        sb.append(key);
        sb.append("=");
        sb.append(params.getString(key));
    }
    sb.append(accessTokenManager.getSessionSecret());
    params.putString("sig", Util.md5(sb.toString()));

}

//In Util.java

public static String openUrl(String url, String method, Bundle params) {
    if (method.equals("GET")) {
        url = url + "?" + encodeUrl(params);
    }
    String response = "";
    try {
        HttpURLConnection conn = new URL(url).openConnection();

        conn.setRequestProperty("", "");
        if (!method,equals("GET")) {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.getOutputStream().write(encodeUrl(params).getBytes("UTF-8"));
        }

        InputStream is = null;
        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            is = conn.getInputStream();
        } else {
            is = conn.getErrorStream();
        }
        response =read(is);
        

    } catch (Exception e) {

    }

}
