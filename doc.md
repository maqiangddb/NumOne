data structure
--------------

**big categrory**

- name

**category**

- name

**shop**

- name
- description
- latilongi
- district

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
                "description": "xxx"
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
