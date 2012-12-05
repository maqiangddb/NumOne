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
