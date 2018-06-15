## CM Binding REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_

### Binding APIs




1. 绑定租户，用户和角色
```
POST /ocmanager/v2/api/tenant/{tenantId}/user/role/assignment
```

__request body:__
```
{
    "userId": "3b7a31f5-b95b-4396-bbb2-c2d8e488d1ee",
    "roleId": "a12a84d0-524a-11e7-9dbb-fa163ed7d0ae"
}
```

__response:__
```
{
  "roleId": "a12a84d0-524a-11e7-9dbb-fa163ed7d0ae",
  "tenantId": "000999",
  "userId": "3b7a31f5-b95b-4396-bbb2-c2d8e488d1ee"
}
```


2. 更新租户中用户的角色
```
PUT /ocmanager/v2/api/tenant/{tenantId}/user/role/assignment
```

__request body:__
```
{
    "userId": "3b7a31f5-b95b-4396-bbb2-c2d8e488d1ee",
    "roleId": "a13dd087-524a-11e7-9dbb-fa163ed7d0ae"
}
```

__response:__
```
{
  "roleId": "a13dd087-524a-11e7-9dbb-fa163ed7d0ae",
  "tenantId": "000999",
  "userId": "3b7a31f5-b95b-4396-bbb2-c2d8e488d1ee"
}
```

3. 解除租户，用户和角色绑定
```
DELETE /ocmanager/v2/api/tenant/{tenantId}/user/{userId}/role/assignment
```

__response:__
```
{
  "message": "3b7a31f5-b95b-4396-bbb2-c2d8e488d1ee",
  "resCodel": 200,
  "status": "delete success"
}
```

4. 获取租户下所有用户和角色信息
```
GET /ocmanager/v2/api/tenant/{tenantId}/users
```

__response:__
```
[
    {
      "roleId": "a10170cb-524a-11e7-9dbb-fa163ed7d0ae",
      "roleName": "system.admin",
      "tenantId": "000999",
      "userDescription": "System Admin User",
      "userEmail": "admin@admin.com",
      "userId": "2ef26018-003d-4b2b-b786-0481d4ee9fa8",
      "userName": "admin",
      "userPassword": "*4ACFE3202A5FF5CF467898FC58AAB1D615029441",
      "userPhone": "admin phone number"
    }, 
    {
      "roleId": "a13dd087-524a-11e7-9dbb-fa163ed7d0ae",
      "roleName": "team.member",
      "tenantId": "000999",
      "userDescription": "",
      "userEmail": "guosm@asiainfo.com",
      "userId": "3b7a31f5-b95b-4396-bbb2-c2d8e488d1ee",
      "userName": "gsm",
      "userPassword": ""
    }
    ...
]
```






