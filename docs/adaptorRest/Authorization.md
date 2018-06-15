## CM Authentication REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_


### Authentication APIs

1. 用户认证
```
POST ocmanager/v2/api/authc/login

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


2. 用户注销
```
DELETE -H 'token:u1_06834FF564D57A53B88B0A64A02584BE24ED8E2954BBBCB935E88EA777BD77D3' ocmanager/v2/api/authc/logout/username
```
__response__
```
{
    "resCode": 200,
    "status": "Logout successful!"
}
```

3. 获取认证类型 (0:ldap, 1:mysql)
```
GET -H 'token:u1_06834FF564D57A53B88B0A64A02584BE24ED8E2954BBBCB935E88EA777BD77D3' ocmanager/v2/api/authc/type
```
__response__
```
{
    "type": 0
}
```

#### __NOTE: All the API call should add the http request header with the authc token. For example:__
```
'token: admin_2D05DA23B89F65C04646A0330752ED26FE59BF7F451700846872438A2023C6E1'
```
#### How to use token
```
GET -H 'token:u1_06834FF564D57A53B88B0A64A02584BE24ED8E2954BBBCB935E88EA777BD77D3' ocmanager/v2/api/user
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
