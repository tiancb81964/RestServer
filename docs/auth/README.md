## Authentication 用户认证
### 用户登录
```
POST ocmanager/v1/api/login

```
__request body:__
```
{
    "username": "u1",
    "password": "password1"
}
```
__response__
```
{
"message": "Please add token in request header",
"resCode": 200,
"status": "Login successful",
"token": "u1_06834FF564D57A53B88B0A64A02584BE24ED8E2954BBBCB935E88EA777BD77D3"
}
```

### 使用token
```
GET -H 'token:u1_06834FF564D57A53B88B0A64A02584BE24ED8E2954BBBCB935E88EA777BD77D3' ocmanager/v1/api/user
```
__response__
```
 {
"createTime": "2017-07-27 14:03:29.0",
"description": "System Admin User",
"email": "admin@admin.com",
"id": "2ef26018-003d-4b2b-b786-0481d4ee9fa8",
"password": "*4ACFE3202A5FF5CF467898FC58AAB1D615029441",
"phone": "admin phone number",
"platformRoleId": 1,
"username": "admin"
}
```

## 支持授权的REST API

 * 添加用户
 * 更新用户
 * 删除用户
 *
 * 添加服务
 * 删除服务
 *
 * 给用户授权系统管理员
 * 给用户授权子公司管理员
 * 给用户授权项目管理员
 * 给用户授权团队成员

## 授权方法

所有授权的REST请求在Request Header里面加上当前用户的信息，包括当前用户的tenantId和username信息

```
{
  tenantId: "1",
  username: "zhaoyim"
}
```

如果当前用户没有相应的权限，系统会返回UNAUTHORIZED。
