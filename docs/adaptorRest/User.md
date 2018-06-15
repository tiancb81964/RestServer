## CM User REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_

### User APIs


1. 获取所有用户
```
GET /ocmanager/v2/api/user
```
__response:__
```
[
  {
    "createTime": "2017-08-02 15:15:13.0",
    "description": "System Admin User",
    "email": "admin@admin.com",
    "id": "2ef26018-003d-4b2b-b786-0481d4ee9fa8",
    "password": "*4ACFE3202A5FF5CF467898FC58AAB1D615029441",
    "phone": "admin phone number",
    "username": "admin"
  },
  ...
]
```

2. 通过用户id获取单个用户
```
GET /ocmanager/v2/api/user/id/{userId}
```
__response:__
```
{
  "createTime": "2017-08-02 15:15:13.0",
  "description": "System Admin User",
  "email": "admin@admin.com",
  "id": "2ef26018-003d-4b2b-b786-0481d4ee9fa8",
  "password": "*4ACFE3202A5FF5CF467898FC58AAB1D615029441",
  "phone": "admin phone number",
  "username": "admin"
}
```

3. 通过用户名字获取单个用户
```
GET /ocmanager/v2/api/user/name/{userName}
```
__response:__
```
{
  "createTime": "2017-08-02 15:15:13.0",
  "description": "System Admin User",
  "email": "admin@admin.com",
  "id": "2ef26018-003d-4b2b-b786-0481d4ee9fa8",
  "password": "*4ACFE3202A5FF5CF467898FC58AAB1D615029441",
  "phone": "admin phone number",
  "username": "admin"
}
```

4. 创建用户
```
POST /ocmanager/v2/api/user
```
__request body:__
```
{
    "username": "createUser001",
    "email": "createUser001@com",
    "description": "createUser001 description",
    "password": "createUser001 password",
    "phone": "1234567890"
}
```

__response:__
```
{
  "createTime": "2017-08-18 16:13:13.0",
  "description": "createUser001 description",
  "email": "createUser001@com",
  "id": "6afd6428-2468-4069-ac6e-ce5b8b56650e",
  "password": "*1580C3237E7227E56B4A3304E7D9F8255CA47253",
  "phone": "1234567890",
  "username": "createUser001"
}
```



5. 通过用户id更新用户
```
PUT /ocmanager/v2/api/user/id/{userId}
```

__request body:__
```
{
  "description": "createUser001 description 11111 update",
  "email": "createUser001____update@com",
  "phone": "111111111"
}
```


__response:__
```
{
  "createTime": "2017-08-18 16:13:13.0",
  "description": "createUser001 description 11111 update",
  "email": "createUser001____update@com",
  "id": "6afd6428-2468-4069-ac6e-ce5b8b56650e",
  "password": "*1580C3237E7227E56B4A3304E7D9F8255CA47253",
  "phone": "111111111",
  "username": "createUser001"
}
```

6. 通过用户名更新用户
```
PUT /ocmanager/v2/api/user/name/{userName}
```

__request body:__
```
{
  "description": "createUser001 description 33333 update",
  "email": "createUser001____update333@com",
  "phone": "33333333"
}
```


__response:__
```
{
  "createTime": "2017-08-18 16:13:13.0",
  "description": "createUser001 description 33333 update",
  "email": "createUser001____update333@com",
  "id": "6afd6428-2468-4069-ac6e-ce5b8b56650e",
  "password": "*1580C3237E7227E56B4A3304E7D9F8255CA47253",
  "phone": "33333333",
  "username": "createUser001"
}
```


7. 删除用户
```
DELETE /ocmanager/v2/api/user/{userId}
``` 
__response:__
```
{
  "message": "6afd6428-2468-4069-ac6e-ce5b8b56650e",
  "resCodel": 200,
  "status": "delete user success"
}
```

8. 修改用户密码（__NOTE:只在enbale mysql 认证后可用__）
```
PUT /ocmanager/v2/api/user/{userName}/password
```

__request body:__
```
{
  "password": "1234567890"
}
```


__response:__
```
{
  "message": "username001",
  "resCodel": 200,
  "status": "update user password success"
}
```


9. 检查用户是否是系统管理员
```
GET /ocmanager/v2/api/user/is/admin/{userName}
```
__response:__
```
{
  "admin": true,
  "userName": "admin"
}
```



10. 通过用户名获取次用户可访问的租户
```
GET /ocmanager/v2/api/user/name/{userName}/all/tenants
```
__response:__
```
[
  {
    "description": "root tenant",
    "id": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
    "level": 1,
    "name": "root.tenant"
  },
  {
    "description": "zhaoyimDemo1",
    "id": "admin-1502693686",
    "level": 2,
    "name": "zhaoyimDemo1",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae"
  }
]
```

11. 通过用户id获取次用户可访问的租户
```
GET /ocmanager/v2/api/user/id/{userId}/all/tenants
```
__response:__
```
[
  {
    "description": "root tenant",
    "id": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
    "level": 1,
    "name": "root.tenant"
  },
  {
    "description": "",
    "id": "zhaoyim-1502869020",
    "level": 2,
    "name": "zhaoyimDemo003",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae"
  }
]
```


12. 通过用户名和租户id获取该租户中此用户可访问的租户
```
GET /ocmanager/v2/api/user/name/{userName}/tenant/{tenantId}/children/tenants
```
__response:__
```
[
  {
    "description": "asdasd",
    "id": "51cadf67-7b37-11e7-aa10-fa163ed7d0ae-1502090717",
    "level": 3,
    "name": "asdsad",
    "parentId": "51cadf67-7b37-11e7-aa10-fa163ed7d0ae"
  },
  {
    "description": "zhaoyimLevel2",
    "id": "86c62459-7c04-11e7-aa10-fa163ed7d0ae",
    "level": 3,
    "name": "zhaoyimLevel2",
    "parentId": "51cadf67-7b37-11e7-aa10-fa163ed7d0ae"
  }
]
```


13. 通过用户id和租户id获取该租户中此用户可访问的租户
```
GET /ocmanager/v2/api/user/id/{userId}/tenant/{tenantId}/children/tenants
```
__response:__
```
[
  {
    "description": "asdasd",
    "id": "51cadf67-7b37-11e7-aa10-fa163ed7d0ae-1502090717",
    "level": 3,
    "name": "asdsad",
    "parentId": "51cadf67-7b37-11e7-aa10-fa163ed7d0ae"
  },
  {
    "description": "zhaoyimLevel2",
    "id": "86c62459-7c04-11e7-aa10-fa163ed7d0ae",
    "level": 3,
    "name": "zhaoyimLevel2",
    "parentId": "51cadf67-7b37-11e7-aa10-fa163ed7d0ae"
  }
]
```


14. 获取所有用户以及该用户关联的租户
```
GET /ocmanager/v2/api/user/with/tenants
```
__response:__
```
[
 {
    "description": "",
    "email": "1@2.com",
    "id": "b71eed6f-3032-4c57-97db-312a00bf05f6",
    "password": "",
    "username": "zhaoyimtest",
    "urv": [
      {
        "roleId": "a10170cb-524a-11e7-9dbb-fa163ed7d0ae",
        "roleName": "system.admin",
        "tenantId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
        "tenantName": "root.tenant",
        "userDescription": "",
        "userId": "b71eed6f-3032-4c57-97db-312a00bf05f6",
        "userName": "zhaoyimtest"
      }
    ]
  }
  ...
]
```


15. 根据用户id获取用户以及该用户关联的租户
```
GET /ocmanager/v2/api/user/{userId}/with/tenants
```
__response:__
```
{
  "description": "",
  "email": "a@com",
  "id": "5abbd34c-c5af-42b2-afe1-381363f180fb",
  "password": "*74B1C21ACE0C2D6B0678A5E503D2A60E8F9651A3",
  "username": "user003",
  "urv": [
    {
      "roleId": "a10170cb-524a-11e7-9dbb-fa163ed7d0ae",
      "roleName": "system.admin",
      "tenantId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
      "tenantName": "root.tenant",
      "userDescription": "",
      "userId": "5abbd34c-c5af-42b2-afe1-381363f180fb",
      "userName": "user003"
    },
    {
      "roleId": "a13dd087-524a-11e7-9dbb-fa163ed7d0ae",
      "roleName": "team.member",
      "tenantId": "test009",
      "tenantName": "test009",
      "userDescription": "",
      "userId": "5abbd34c-c5af-42b2-afe1-381363f180fb",
      "userName": "user003",
      "parentTenantName": "root.tenant"
    },
    {
      "roleId": "a13dd087-524a-11e7-9dbb-fa163ed7d0ae",
      "roleName": "team.member",
      "tenantId": "zhaoyim-1502869020",
      "tenantName": "zhaoyimDemo003",
      "userDescription": "",
      "userId": "5abbd34c-c5af-42b2-afe1-381363f180fb",
      "userName": "user003",
      "parentTenantName": "root.tenant"
    }
  ]
}
```


16. 获取ldap服务其上所有用户（__NOTE:只在enbale ldap 认证后可用__）
```
GET /ocmanager/v2/api/user/ldap
```
__response:__
```
[
  "ethanwang",
  "admin",
  "user001",
  "user001",
  "user003",
  "zhaoyimtest",
  ...
]
```



17. 根据用户名获取此用户在给定租户下的服务实例授权是否成功
```
GET /ocmanager/v2/api/user/name/{userName}/tenant/{tenantId}/assignments/info
```
__response:__
```
[
  {
    "assignmentStatus": "Authorization Success",
    "instanceName": "HDFS-zhaoyim-9E700BB"
  }
  {
    "assignmentStatus": "Authorization Success",
    "instanceName": "HDFS-zhaoyim-49D917E"
  }
  ...
]
```