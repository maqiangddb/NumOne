data structure
--------------

**category**

- name

**big categrory**

- name

**shop**

- name
- description
- latilongi
- district

json standard
-------------
```json
{
    "apiVersion": "1.0",
    "data": {
        "kind": "shoplist",
        "totalItems": 6741,
        "startIndex": 1,
        "itemsPerPage": 10,
        "itemCount": 10,
        "items": [
            // ...
        ]
    },
    "error": {
        "code": 404,
        "message": "can't find"
    }
}
```

interfaces
----------

url: /api

parameters:

- action
- target

...

**example:**

/api?action=get&target=shoplist&district=futian&count=10
