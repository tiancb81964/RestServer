## CM Role REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_

### Role APIs


1. 获取所有CM角色
```
GET /ocmanager/v2/api/role
```
__response:__
```
[
  {
    "description": "system admin is super user, it can create tenant and add users, assign role to user and add services.",
    "id": "a10170cb-524a-11e7-9dbb-fa163ed7d0ae",
    "permission": "{createUser: true, updateUser: true, deleteUser: true, addService: true, deleteService: true, grant: true}",
    "rolename": "system.admin"
  },
  {
    "description": "tenant admin can add uses to the tenant and assign role to user.",
    "id": "a12a84d0-524a-11e7-9dbb-fa163ed7d0ae",
    "permission": "{grant: true}",
    "rolename": "tenant.admin"
  },
  {
    "description": "the user only can read the tenant information that he is in.",
    "id": "a13dd087-524a-11e7-9dbb-fa163ed7d0ae",
    "permission": "{}",
    "rolename": "team.member"
  }
]
```

