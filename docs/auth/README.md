## Authentication 用户认证
### 配置认证源
支持mysql和ldap两种认证源, 在server.properties中修改配置
```
oc.server.user.source=ldap/mysql
```

### 配置登陆用户Session信息
登陆用户的Session配置,例如"超时时间"可以通过修改配置文件进行配置, 在ehcache.xml中修改相应配置项
```
#Max elements to retain in memory
maxElementsInMemory="2000"
maxEntriesLocalHeap="2000"
eternal="false"
#Session timeout in seconds
timeToIdleSeconds="1800"
timeToLiveSeconds="0"
maxElementsOnDisk="0"
overflowToDisk="true"
memoryStoreEvictionPolicy="FIFO"
statistics="true"
```

### 用户登录
```
POST ocmanager/v1/api/authc/login

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

### 注销登录
```
DELETE -H 'token:u1_06834FF564D57A53B88B0A64A02584BE24ED8E2954BBBCB935E88EA777BD77D3' ocmanager/v1/api/authc/logout/username
```
__response__
```
{
"resCode": 200,
"status": "Logout successful!"
}
```

### 获取认证源类型(0:ldap, 1:mysql)
```
GET -H 'token:u1_06834FF564D57A53B88B0A64A02584BE24ED8E2954BBBCB935E88EA777BD77D3' ocmanager/v1/api/authc/type
```
__response__
```
{
"type": 0
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
