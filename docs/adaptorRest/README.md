
# OCManager Adapter REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_



# 1.文档说明

## 1.1.请求路径  



```
http://{ip}:{port}/{productName}/{versionNum}/{object}/{oper}/
示例: http://127.0.0.1:8080/ocmanager/v2/api/authc/login/
```

- productName: 产品名称(必须遵守)
- versionNum: 版本号(必须遵守)
- object: 对象名称
- oper: 操作

> 路径最后一定需要斜线结束

## 1.2.请求类型
`http`

## 1.3.请求头
```
Content-Type:application/json;charset=UTF-8
Charset:utf-8
```

## 1.4 状态返回
原则上不需要在返回的json数据里面携带状态字段,而是用过状态码来确认是否调用成功。在失败的调用下可以在json数据里面携带errCode和errMsg字段用于描述错误原因。
- 200: 成功
- 501: 失败 (调用失败一定不要使用200，方式某些网页调用导致浏览器缓存)

# 2.接口列表

## 2.1 Authentication APIs

### 2.1.1用户认证（/ocmanager/v2/api/authc/login/） 
	示例：http://127.0.0.1:8080/ocmanager/v2/api/authc/login/
    请求方式：POST

#### 2.1.1.1请求参数

##### 2.1.1.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
username| String | 用户名|是|
password| String | 密码|是| 

#### 2.1.1.2返回参数

##### 2.1.1.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
message| String | 返回的消息|
resCode| int | 响应返回码|200为正常
status | String | 状态|返回登陆状态
token | String | 令牌|

#### 2.1.1.3报文示例

##### 2.1.1.3.1请求报文示例

__request body:__

```
{
    "username": "u1",
    "password": "password1"
}
```

##### 2.1.1.3.2返回报文示例

__response__
```
{
    "message": "Please add token in request header",
    "resCode": 200,
    "status": "Login successful",
    "token": "u1_06834FF564D57A53B88B0A64A02584BE24ED8E2954BBBCB935E88EA777BD77D3"
}
```


### 2.1.2用户注销(/ocmanager/v2/api/authc/logout/username/)

    示例：http://127.0.0.1:8080/ocmanager/v2/api/authc/logout/username/
    请求方式：DELETE



#### 2.1.2.1返回参数

##### 2.1.2.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
resCode| int | 响应返回码|200为正常
status| String | 状态|返回登出状态

#### 2.1.2.2报文示例

##### 2.1.2.2.1请求报文示例

```
 http://127.0.0.1:8080/ocmanager/v2/api/authc/logout/username/
```

##### 2.1.2.2.2返回报文示例

__response__
```
{
    "resCode": 200,
    "status": "Logout successful!"
}
```



### 2.1.3获取认证类型（/ocmanager/v2/api/authc/type/） 
	示例：http://127.0.0.1:8080/ocmanager/v2/api/authc/type/
    请求方式：GET



#### 2.1.3.1返回参数

##### 2.1.3.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
type| int | 认证类型|0为ldap，1为mysql

#### 2.1.3.2报文示例

##### 2.1.3.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/authc/type/
```

##### 2.1.3.2.2返回报文示例

__response__

```
{
    "type": 0
}
```

### 2.1.4 How to use token（/ocmanager/v2/api/user/）

__NOTE: All the API call should add the http request header with the authc token. For example:__

```
'token: admin_2D05DA23B89F65C04646A0330752ED26FE59BF7F451700846872438A2023C6E1'
```
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/
    请求方式：GET


#### 2.1.4.1返回参数

##### 2.1.4.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
createTime|String|创建时间
description|String|描述
email|String|电子邮件
id|String|用户id
password|String|用户密码
phone|String|用户电话
username|String|用户名
platformRoleId|String|平台角色id

#### 2.1.4.2报文示例

##### 2.1.4.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/
```

##### 2.1.4.2.2返回报文示例

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




## 2.2 Users APIs

### 2.2.1获取所有用户（/ocmanager/v2/api/user/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/
    请求方式：GET


#### 2.2.1.1返回参数

##### 2.2.1.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
createTime| String | 创建时间|
description| String| 用户描述| 
email| String |电子邮件|
id| String |用户id|
password| String |密码|
phone| String |电话|
username| String |用户名|

#### 2.2.1.2报文示例

##### 2.2.1.2.1请求报文示例

```
 http://127.0.0.1:8080/ocmanager/v2/api/user/
```

##### 2.2.1.2.2返回报文示例


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

### 2.2.2通过用户id获取单个用户（/ocmanager/v2/api/user/id/{id}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/id/2ef26018-003d-4b2b-b786-0481d4ee9fa8/
    请求方式：GET

#### 2.2.2.1请求参数

##### 2.2.2.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String |用户id|是|

#### 2.2.2.2返回参数

##### 2.2.2.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
createTime| String | 创建时间|
description| String| 用户描述| 
email| String |电子邮件|
id| String |用户id|
password| String |密码|
phone| String |电话|
username| String |用户名|

#### 2.2.2.3报文示例

##### 2.2.2.3.1请求报文示例

```
 http://127.0.0.1:8080/ocmanager/v2/api/user/id/2ef26018-003d-4b2b-b786-0481d4ee9fa8/
```

##### 2.2.2.3.2返回报文示例


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

### 2.2.3通过用户名字获取单个用户（/ocmanager/v2/api/user/name/{userName}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/name/admin/
    请求方式：GET

#### 2.2.3.1请求参数

##### 2.2.3.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
userName| String |用户名|是|

#### 2.2.3.2返回参数

##### 2.2.3.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
createTime| String | 创建时间|
description| String| 用户描述| 
email| String |电子邮件|
id| String |用户id|
password| String |密码|
phone| String |电话|
username| String |用户名|

#### 2.2.3.3报文示例

##### 2.2.3.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/name/admin/ 
```

##### 2.2.3.3.2返回报文示例

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

### 2.2.4创建用户（/ocmanager/v2/api/user/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/
    请求方式：POST

#### 2.2.4.1请求参数

##### 2.2.4.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
username| String |用户名|是|
email| String |电子邮件|否|
description| String| 创建用户描述|否| 
password| String |密码|是|
phone| String |电话|否|

#### 2.2.4.2返回参数

##### 2.2.4.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
createTime| String | 创建时间|
description| String| 创建用户描述| 
email| String |电子邮件|
id| String |用户id|
password| String |密码|
phone| String |电话|
username| String |用户名|


#### 2.2.4.3报文示例

##### 2.2.4.3.1请求报文示例


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

##### 2.2.4.3.2返回报文示例

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


### 2.2.5通过用户id更新用户（/ocmanager/v2/api/user/id/{id}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/id/6afd6428-2468-4069-ac6e-ce5b8b56650e/
    请求方式：PUT

#### 2.2.5.1请求参数

##### 2.2.5.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
description| String| 更新用户描述| 否
email| String |电子邮件|否
phone| String |电话|否
id| String |用户id|是

#### 2.2.5.2返回参数

##### 2.2.5.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
createTime| String | 创建时间|
description| String| 更新用户描述| 
email| String |电子邮件|
id| String |用户id|
password| String |密码|
phone| String |电话|
username| String |用户名|

#### 2.2.5.3报文示例

##### 2.2.5.3.1请求报文示例

__request body:__

```

{
  "description": "createUser001 description 11111 update",
  "email": "createUser001____update@com",
  "phone": "111111111"
}
```

##### 2.2.5.3.2返回报文示例


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

### 2.2.6通过用户名更新用户（/ocmanager/v2/api/user/name/{userName}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/name/createUser001/
    请求方式：PUT

#### 2.2.6.1请求参数

##### 2.2.6.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
description| String| 更新用户描述| 否
email| String |电子邮件|否
phone| String |电话|否
userName| String |用户名|是

#### 2.2.6.2返回参数

##### 2.2.6.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
createTime| String | 创建时间|
description| String| 更新用户描述| 
email| String |电子邮件|
id| String |用户id|
password| String |密码|
phone| String |电话|
username| String |用户名|

#### 2.2.6.3报文示例

##### 2.2.6.3.1请求报文示例

__request body:__

```
{
  "description": "createUser001 description 33333 update",
  "email": "createUser001____update333@com",
  "phone": "33333333"
}
```

##### 2.2.6.3.2返回报文示例

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

### 2.2.7删除用户（/ocmanager/v2/api/user/{id}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/6afd6428-2468-4069-ac6e-ce5b8b56650e/
    请求方式：DELETE

#### 2.2.7.1请求参数

##### 2.2.7.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id| String |用户id|是|

#### 2.2.7.2返回参数

##### 2.2.7.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|--------|------------|
message| String | 返回的消息|返回用户id
resCode| int| 响应返回码|200为正常 
status| String |状态|返回删除用户状态

#### 2.2.7.3报文示例

##### 2.2.7.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/6afd6428-2468-4069-ac6e-ce5b8b56650e/ 
```

##### 2.2.7.3.2返回报文示例

__response:__

```
{
  "message": "6afd6428-2468-4069-ac6e-ce5b8b56650e",
  "resCode": 200,
  "status": "delete user success"
}
```

### 2.2.8修改用户密码（/ocmanager/v2/api/user/{userName}/password/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/username001/password/
    请求方式：PUT

__NOTE:只在enbale mysql 认证后可用__

#### 2.2.8.1请求参数

##### 2.2.8.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
password| String | 用户新密码|是|
userName| String |用户名|是|

#### 2.2.8.2返回参数

##### 2.2.8.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
message| String | 返回的消息|返回用户名
resCode| int|响应返回码|200为正常 
status| String |状态|返回更新密码状态

#### 2.2.8.3报文示例

##### 2.2.8.3.1请求报文示例

__request body:__

```
{
  "password": "1234567890"
}
```

##### 2.2.8.3.2返回报文示例

__response:__

```
{
  "message": "username001",
  "resCode": 200,
  "status": "update user password success"
}
```

### 2.2.9检查用户是否是系统管理员（/ocmanager/v2/api/user/is/admin/{userName}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/is/admin/admin/
    请求方式：GET

#### 2.2.9.1请求参数

##### 2.2.9.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
username| String | 用户名|是|

#### 2.2.9.2返回参数

##### 2.2.9.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
admin| boolean	 | 是否为系统管理员|true为是，false为不是
username| String | 用户名|

#### 2.2.9.3报文示例

##### 2.2.9.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/is/admin/admin/ 
```

##### 2.2.9.3.2返回报文示例

__response:__

```
{
  "admin": true,
  "userName": "admin"
}
```

### 2.2.10通过用户名获取此用户可访问的租户（/ocmanager/v2/api/user/name/{name}/all/tenants/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/name/root/all/tenants/
    请求方式：GET

#### 2.2.10.1请求参数

##### 2.2.10.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
name|String|用户名|是|

#### 2.2.10.2返回参数

##### 2.2.10.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
description|String|租户描述|
id|String|租户id|
level| int|租户级别|内部保留字段
name|String|租户名|
parentId|String|父租户id|

#### 2.2.10.3报文示例

##### 2.2.10.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/name/root/all/tenants/ 
```

##### 2.2.10.3.2返回报文示例

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

### 2.2.11通过用户id获取此用户可访问的租户（/ocmanager/v2/api/user/id/{id}/all/tenants/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/id/111ad1/all/tenants/
    请求方式：GET

#### 2.2.11.1请求参数

##### 2.2.11.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|用户id|是|

#### 2.2.11.2返回参数

##### 2.2.11.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
description|String|租户描述|
id|String|租户id|
level| int|租户级别|内部保留字段
name|String|租户名|
parentId|String|父租户id|

#### 2.2.11.3报文示例

##### 2.2.11.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/id/111ad1/all/tenants/ 
```

##### 2.2.11.3.2返回报文示例

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

### 2.2.12通过用户名和租户id获取该租户中此用户可访问的租户（/ocmanager/v2/api/user/name/{name}/tenant/{tenantId}/children/tenants/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/name/asdasd/tenant/51cadf67-7b37-11e7-aa10-fa163ed7d0ae/children/tenants/
    请求方式：GET

#### 2.2.12.1请求参数

##### 2.2.12.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
tenantId|String|租户id|是|
name|String|用户名|是|

#### 2.2.12.2返回参数

##### 2.2.12.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
description|String|租户描述|
id|String|租户id|
level| int|租户级别|内部保留字段
name|String|租户名|
parentId|String|父租户id|

#### 2.2.12.3报文示例

##### 2.2.12.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/name/asdasd/tenant/51cadf67-7b37-11e7-aa10-fa163ed7d0ae/children/tenants/
```

##### 2.2.12.3.2返回报文示例

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

### 2.2.13通过用户id和租户id获取该租户中此用户可访问的租户（/ocmanager/v2/api/user/id/{id}/tenant/{tenantId}/children/tenants/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/id/1111/tenant/1cadf67/children/tenants/
    请求方式：GET

#### 2.2.13.1请求参数

##### 2.2.13.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
tenantId|String|租户id|是|
id|String|用户id|是|

#### 2.2.13.2返回参数

##### 2.2.13.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
description|String|租户描述|
id|String|租户id|
level| int|租户级别|内部保留字段
name|String|租户名|
parentId|String|父租户id|

#### 2.2.13.3报文示例

##### 2.2.13.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/id/1111/tenant/1cadf67/children/tenants/
```

##### 2.2.13.3.2返回报文示例

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

### 2.2.14获取所有用户以及该用户关联的租户（/ocmanager/v2/api/user/with/tenants/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/with/tenants/
    请求方式：GET


#### 2.2.14.1返回参数

##### 2.2.14.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
description|String|描述|
email|String|邮件
id|String|用户id
password|String|密码
username|String|用户名
urv|array|用户租户权限视图|内容见以下字段
roleId|String|角色id|urv字段
roleName|String|角色名|urv字段
tenantId|String|租户id|urv字段
tenantName|String|租户名|urv字段
userDescription|String|用户描述|urv字段
userId|String|用户id|urv字段

#### 2.2.14.2报文示例

##### 2.2.14.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/with/tenants/ 
```

##### 2.2.14.2.2返回报文示例

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

### 2.2.15根据用户id获取用户以及该用户关联的租户（/ocmanager/v2/api/user/{id}/with/tenants/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/5abbd34c-c5af-42b2-afe1-381363f180fb/with/tenants/
    请求方式：GET

#### 2.2.15.1请求参数

##### 2.2.15.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|用户id|是|

#### 2.2.15.2返回参数

##### 2.2.15.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
description|String|描述|
email|String|邮件
id|String|用户id
password|String|密码
username|String|用户名
urv|array|用户租户权限视图|内容见以下字段
roleId|String|角色id|urv字段
roleName|String|角色名|urv字段
tenantId|String|租户id|urv字段
tenantName|String|租户名称|urv字段
userDescription|String|用户描述|urv字段
userId|String|用户id|urv字段
parentTenantName|String|父租户名|urv字段

#### 2.2.15.3报文示例

##### 2.2.15.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/5abbd34c-c5af-42b2-afe1-381363f180fb/with/tenants/
```

##### 2.2.15.3.2返回报文示例

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

### 2.2.16获取ldap服务其上所有用户（/ocmanager/v2/api/user/ldap/） 

__NOTE:只在enbale ldap 认证后可用__
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/ldap/
    请求方式：GET



#### 2.2.16.1返回参数

##### 2.2.16.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
||array||此处返回的是用户名称列表

#### 2.2.16.2报文示例

##### 2.2.16.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/ldap/ 
```

##### 2.2.16.2.2返回报文示例

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

### 2.2.17根据用户名获取此用户在给定租户下的服务实例授权是否成功（/ocmanager/v2/api/user/name/{userName}/tenant/{tenantId}/assignments/info/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/user/name/zzzz/tenant/abbd34/assignments/info/
    请求方式：GET

#### 2.2.17.1请求参数

##### 2.2.17.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
userName|String|用户名|是
tenantId|String|租户Id|是

#### 2.2.17.2返回参数

##### 2.2.17.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
assignmentStatus|String|授权状态|返回授权状态
instanceName|String|实例名

#### 2.2.17.3报文示例

##### 2.2.17.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/user/name/zzzz/tenant/abbd34/assignments/info/ 
```

##### 2.2.17.3.2返回报文示例


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

## 2.3 Services APIs

### 2.3.1获取所有服务（/ocmanager/v2/api/service/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/service/
    请求方式：GET


#### 2.3.1.1返回参数

##### 2.3.1.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
description|String|服务描述
id|String|服务id
origin|String|来源于哪个service broker
servicename|String|服务名
servicetype|String|服务类型
catogery|String|服务分类（资源、工具、应用）

#### 2.3.1.2报文示例

##### 2.3.1.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/service/ 
```

##### 2.3.1.2.2返回报文示例


__response:__

```
[
  {
    "description": "A Hadoop hbase service broker implementation",
    "id": "d9845ade-9410-4c7f-8689-4e032c1a8450",
    "origin": "ocdp",
    "servicename": "hbase",
    "servicetype": "hbase",
    "catogery": "resource"
  },
  {
    "description": "visualized analysing",
    "id": "ae67d4ba-5c4e-4937-a68b-5b47cfe35699",
    "origin": "open-shift",
    "servicename": "visanalyse",
    "servicetype": "visanalyse",
    "catogery": "tool"
  },
    {
    "description": "spark streaming",
    "id": "ae67d4ba-5c4e-4937-a68b-5b4888888888",
    "origin": "open-shift",
    "servicename": "ocsp",
    "servicetype": "ocsp",
    "catogery": "app"
  },
  ...
]
```

### 2.3.2获取单个服务（/ocmanager/v2/api/service/{id}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/service/d9845ade-9410-4c7f-8689-4e032c1a8450/
    请求方式：GET

#### 2.3.2.1请求参数

##### 2.3.2.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|服务id|是|

#### 2.3.2.2返回参数

##### 2.3.2.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
description|String|服务描述
id|String|服务id
origin|String|来源于哪个service broker
servicename|String|服务名
serivcetype|String|服务类型
catogery|String|服务类别（资源、工具、应用）

#### 2.3.2.3报文示例

##### 2.3.2.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/service/d9845ade-9410-4c7f-8689-4e032c1a8450/ 
```

##### 2.3.2.3.2返回报文示例

__response:__

```
{
  "description": "A Hadoop hbase service broker implementation",
  "id": "d9845ade-9410-4c7f-8689-4e032c1a8450",
  "origin": "ocdp",
  "servicename": "hbase",
  "servicetype": "hbase",
  "catogery": "resource"
}
```

### 2.3.3添加Service broker（/ocmanager/v2/api/service/broker/） 

NOTE: 添加服务,服务是注册在service broker里面的,因此会添加service broker 中注册的所有服务

	示例：http://127.0.0.1:8080/ocmanager/v2/api/service/broker/
    请求方式：POST

#### 2.3.3.1请求参数

##### 2.3.3.1.1基本参数

字段|类型|描述|是否必填|备注|是否常量|
----------|----------------|----|--------|------------|---|
kind|String||是|内部使用字段，使用者可不关心，必须唯一|是
apiVersion|String||是|内部使用字段，使用者可不关心，必须唯一|是
metadata|json|源数据|是|其内容见以下字段|否
name|String|Service broker的名字|是|metadata字段|否
spec|json|指定参数|是|其内容见以下字段|否
url|String|Service broker服务的网址|是|spec字段|否
username|String|Service broker用户名|是|spec字段|否
password|String|Service broker密码|是|spec字段|否

#### 2.3.3.2返回参数

##### 2.3.3.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
kind|String||内部使用字段，使用者可不关心，必须唯一|是
apiVersion|String||内部使用字段，使用者可不关心，必须唯一|是
metadata|json|源数据|其内容见以下字段|否
name|String|Service broker的名字|metadata字段|否
selfLink|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
uid|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
resourceVersion|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
creationTimestamp|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
spec|json|指定参数|其内容见以下字段|否
url|String|Service broker服务的网址|spec字段|否
username|String|Service broker用户名|spec字段|否
password|String|Service broker密码|spec字段|否
status|json|状态|其内容见以下字段|否
phase|String|阶段|（status字段）new表示新添加服务|否

#### 2.3.3.3报文示例

##### 2.3.3.3.1请求报文示例

__request body:__

```
{
  "kind":"ServiceBroker",
  "apiVersion":"v1",
  "metadata":
    {
      "name":"rds9"
    },
  "spec":
    {
      "url":"http://localhost:9900",
      "username":"test",
      "password":"test"
    }
}
```

##### 2.3.3.3.2返回报文示例

__response:__

```
{
  "kind": "ServiceBroker",
  "apiVersion": "v1",
  "metadata": {
    "name": "rds9",
    "selfLink": "/oapi/v1/servicebrokers/rds9",
    "uid": "edf41739-564a-11e7-8f1f-fa163efdbea8",
    "resourceVersion": "17209399",
    "creationTimestamp": "2017-06-21T06:29:07Z"
  },
  "spec": {
    "url": "http://localhost:9900",
    "name": "",
    "username": "test",
    "password": "test"
  },
  "status": {
    "phase": "New"
  }
}
```

### 2.3.4删除Service broker（/ocmanager/v2/api/service/broker/{name}/） 

NOTE： 删除服务,服务是注册在service broker 里面的,因此会删除service broker 中注册的所有服务

	示例：http://127.0.0.1:8080/ocmanager/v2/api/service/broker/111aw2/
    请求方式：DELETE

#### 2.3.4.1请求参数

##### 2.3.4.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
name|String|service broker名字|是|

#### 2.3.4.2返回参数

##### 2.3.4.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
kind|String||内部使用字段，使用者可不关心，必须唯一|是
apiVersion|String||内部使用字段，使用者可不关心，必须唯一|是
metadata|json|源数据|其内容见以下字段|否
name|String|Service broker的名字|metadata字段|否
selfLink|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
uid|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
resourceVersion|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
creationTimestamp|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
deletionTimestamp|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
annotations|json||(metadata字段，其内容见以下字段)|否
ServiceBroker/LastPing|String||(annotations字段)内部使用字段，使用者可不关心，必须唯一|是
ServiceBroker/NewRetryTimes|String||(annotations字段)内部使用字段，使用者可不关心，必须唯一|是
spec|json|指定参数|其内容见以下字段|否
url|String|Service broker服务的网址|spec字段|否
username|String|Service broker用户名|spec字段|否
password|String|Service broker密码|spec字段|否
status|json|状态|其内容见以下字段|否
phase|String|阶段|(status字段)Deleting表示删除服务|否

#### 2.3.4.3报文示例

##### 2.3.4.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/service/broker/111aw2/
```

##### 2.3.4.3.2返回报文示例

__response:__


```
{
  "kind": "ServiceBroker",
  "apiVersion": "v1",
  "metadata": {
    "name": "rds9",
    "selfLink": "/oapi/v1/servicebrokers/rds9",
    "uid": "edf41739-564a-11e7-8f1f-fa163efdbea8",
    "resourceVersion": "17209607",
    "creationTimestamp": "2017-06-21T06:29:07Z",
    "deletionTimestamp": "2017-06-21T06:31:36Z",
    "annotations": {
      "ServiceBroker/LastPing": "1498026648288080254",
      "ServiceBroker/NewRetryTimes": "1"
    }
  },
  "spec": {
    "url": "http://localhost:9900",
    "name": "",
    "username": "test",
    "password": "test"
  },
  "status": {
    "phase": "Deleting"
  }
}
```

### 2.3.5获取Data Foundry服务列表（/ocmanager/v2/api/service/df/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/service/df/
    请求方式：GET


#### 2.3.5.1返回参数

##### 2.3.5.1.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
kind|String||内部使用字段，使用者可不关心，必须唯一|是
apiVersion|String||内部使用字段，使用者可不关心，必须唯一|是
metadata|json|源数据|其内容见以下字段|否
name|String|Service broker的名字|metadata字段|否
selfLink|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
resourceVersion|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
items|array|条目|其内容见以下字段|否
metadata|json|源数据|(items字段，其内容见以下字段)|否
name|String|服务名|metadata字段|否
generateName|String|架构名称|（metadata字段)HBase是hadoop架构的服务|否
namespace|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
uid|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
resourceVersion|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
creationTimestamp|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
labels|json||（metadata字段,其内容见以下字段)内部使用字段，使用者可不关心|否
asiainfo.io/servicebroker|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
spec|json|指定参数|(items字段，其内容见以下字段)|否
name|String|服务名|spec字段|否
id|String|服务id|spec字段|否
description|String|服务描述|spec字段|否
bindable|boolean||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
plan_updateable|boolean||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
tags|array||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
requires|array||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
metadata|json||spec字段,其内容见以下字段|否
displayName|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
documentationUrl|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
imageUrl|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
longDescription|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
providerDisplayName|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
supportUrl|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
plans|array|plan套餐|spec字段,其内容见以下字段|否
name|String|plan名|plans字段|否
id|String|plan的id|plans字段|否
description|String|plan描述|plans字段|否
metadata|json||(plans字段,其内容见以下字段)|否
bullets|array||(metadata字段,其内容见以下字段)|否
Name Space Quota|int|HDFS目录允许创建的最大文件数目|bullet字段|否
Storage Space Quota(GB)|int|HDFS目录的最大存储容量|bullet字段|否
HBase Maximun Tables|int|HBase命名空间允许的最大的表数目|bullet字段|否
HBase Maximun Regions|int|HBase命名空间允许的最大的region数目|bullet字段|否
costs|array||(metadata字段,其内容见以下字段)内部使用字段，使用者可不关心|否
amount|json||(costs字段,其内容见以下字段)内部使用字段，使用者可不关心|否
usd|int||(amount字段)内部使用字段，使用者可不关心，必须唯一|是
unit|String||(costs字段)内部使用字段，使用者可不关心，必须唯一|是
customize|json||(metadata字段,其内容见以下字段)|否
maximumRegionsQuota|json|HBase命名空间允许的最大的region限额列表|customize字段|否
maximumTablesQuota|json|HBase命名空间允许的最大表限额列表|customize字段|否
nameSpaceQuota|json|HDFS目录允许创建的最大文件数限额列表|customize字段|否
storageSpaceQuota|json|HDFS目录的最大存储容量限额列表|customize字段|否
default|int|默认值|maximumRegionsQuota、maximumTablesQuota、nameSpaceQuota、storageSpaceQuota共有字段|否
max|int|最大值|maximumRegionsQuota、maximumTablesQuota、nameSpaceQuota、storageSpaceQuota共有字段|否
price|int||(maximumRegionsQuota、maximumTablesQuota、nameSpaceQuota、storageSpaceQuota共有字段)内部使用字段，使用者可不关心，必须唯一|是
step|int||(maximumRegionsQuota、maximumTablesQuota、nameSpaceQuota、storageSpaceQuota共有字段)内部使用字段，使用者可不关心，必须唯一|是
desc|String|配额描述|maximumRegionsQuota、maximumTablesQuota、nameSpaceQuota、storageSpaceQuota共有字段|否
unit|String|单位，HDFS存储容量单位为GB|storageSpaceQuota字段|否
free|boolean||(plans字段)内部使用字段，使用者可不关心，必须唯一|是
dashboard_client|||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
status|json|状态|(items字段，其内容见以下字段)|否
phase|String|阶段|(status字段)Active表示服务处于启动状态|否
type|String|类型|服务类型|否
catogery|String|类别|服务类别（工具、资源、应用）|否

#### 2.3.5.2报文示例

##### 2.3.5.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/service/df/
```

##### 2.3.5.2.2返回报文示例

__response:__

```
{
  "kind": "BackingServiceList",
  "apiVersion": "v1",
  "metadata": {
    "selfLink": "/oapi/v1/namespaces/openshift/backingservices",
    "resourceVersion": "22588737"
  },
  "items": [
    {
      "metadata": {
        "name": "HBase",
        "generateName": "hadoop",
        "namespace": "openshift",
        "selfLink": "/oapi/v1/namespaces/openshift/backingservices/HBase",
        "uid": "8217a0da-6620-11e7-9586-fa163efdbea8",
        "resourceVersion": "22401880",
        "creationTimestamp": "2017-07-11T10:05:46Z",
        "labels": {
          "asiainfo.io/servicebroker": "hadoop"
        }
      },
      "spec": {
        "name": "HBase",
        "id": "d9845ade-9410-4c7f-8689-4e032c1a8450",
        "description": "HBase是Hadoop的面向列的分布式非关系型数据库。版本：v1.1.2",
        "bindable": false,
        "plan_updateable": false,
        "tags": [
          "hbase",
          "database"
        ],
        "requires": [],
        "metadata": {
          "displayName": "HBase",
          "documentationUrl": "http://hbase.apache.org/",
          "imageUrl": "http://hbase.apache.org/images/hbase_logo_with_orca_large.png",
          "longDescription": "HBase是一个开源的，非关系型的，分布式数据库，类似于Google的BigTable。",
          "providerDisplayName": "Asiainfo",
          "supportUrl": "http://hbase.apache.org/book.html",
          "type": "hbase",
          "catogery": "resource"
        },
        "plans": [
          {
            "name": "shared",
            "id": "f658e391-b7d6-4b72-9e4c-c754e4943ae1",
            "description": "共享HBase实例",
            "metadata": {
              "bullets": [
                "HBase Maximun Tables:10",
                "HBase Maximun Regions:10"
              ],
              "costs": [
                {
                  "amount": {
                    "usd": 0
                  },
                  "unit": "MONTHLY"
                }
              ],
              "displayName": "",
              "customize": {
                "maximumRegionsQuota": {
                  "default": 100,
                  "max": 1000,
                  "price": 10,
                  "step": 10,
                  "desc": "HBase命名空间允许的最大的region数目"
                },
                "maximumTablesQuota": {
                  "default": 10,
                  "max": 100,
                  "price": 10,
                  "step": 10,
                  "desc": "HBase命名空间允许的最大的表数目"
                }
              }
            },
            "free": false
          }
        ],
        "dashboard_client": null
      },
      "status": {
        "phase": "Active"
      }
    },
    {
      "metadata": {
        "name": "HDFS",
        "generateName": "hadoop",
        "namespace": "openshift",
        "selfLink": "/oapi/v1/namespaces/openshift/backingservices/HDFS",
        "uid": "821718e4-6620-11e7-9586-fa163efdbea8",
        "resourceVersion": "22401881",
        "creationTimestamp": "2017-07-11T10:05:46Z",
        "labels": {
          "asiainfo.io/servicebroker": "hadoop"
        }
      },
      "spec": {
        "name": "HDFS",
        "id": "ae67d4ba-5c4e-4937-a68b-5b47cfe356d8",
        "description": "HDFS是Hadoop的分布式文件系统。版本：v2.7.1",
        "bindable": false,
        "plan_updateable": false,
        "tags": [
          "hdfs",
          "storage"
        ],
        "requires": [],
        "metadata": {
          "displayName": "HDFS",
          "documentationUrl": "http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html",
          "imageUrl": "https://hadoop.apache.org/images/hadoop-logo.jpg",
          "longDescription": "Hadoop分布式文件系统(HDFS)是一个的分布式的，可扩展的，轻量级的文件系统。",
          "providerDisplayName": "Asiainfo",
          "type": "hdfs",
          "catogery": "resource",
          "supportUrl": "http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html"
        },
        "plans": [
          {
            "name": "shared",
            "id": "72150b09-1025-4533-8bae-0e04ef68ac13",
            "description": "共享HDFS实例",
            "metadata": {
              "bullets": [
                "Name Space Quota:1000",
                "Storage Space Quota (GB):20"
              ],
              "costs": [
                {
                  "amount": {
                    "usd": 0
                  },
                  "unit": "MONTHLY"
                }
              ],
              "displayName": "",
              "customize": {
                "nameSpaceQuota": {
                  "default": 1000,
                  "max": 100000,
                  "price": 10,
                  "step": 10,
                  "desc": "HDFS目录允许创建的最大文件数目"
                },
                "storageSpaceQuota": {
                  "default": 1024,
                  "max": 102400,
                  "price": 10,
                  "step": 10,
                  "unit": "GB",
                  "desc": "HDFS目录的最大存储容量"
                }
              }
            },
            "free": false
          }
        ],
        "dashboard_client": null
      },
      "status": {
        "phase": "Active"
      }
    },
    ...
  ]
}
```

### 2.3.6获取多租户平台上所有服务实例列表（/ocmanager/v2/api/service/all/instances/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/service/all/instances/
    请求方式：GET


#### 2.3.6.1返回参数

##### 2.3.6.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
id|String|实例id|
instanceName|String|实例名
quota|String|配额|返回Hive数据库的最大存储容量，Yarn队列的最大容量。注意格式
serviceTypeId|String|服务类型id
serviceTypeName|String|服务类型名
status|String|服务实例状态
tenantId|String|租户id
catogery|String|服务类别（工具、资源、应用）

#### 2.3.6.2报文示例

##### 2.3.6.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/service/all/instances/
```

##### 2.3.6.2.2返回报文示例

__response:__


```
[
  {
    "id": "01567a01-8259-11e7-990a-fa163efdbea8",
    "instanceName": "Hive-zhaoyim-1B07F71",
    "quota": "{\"storageSpaceQuota\":\"1024\",\"yarnQueueQuota\":\"10\"}",
    "serviceTypeId": "",
    "serviceTypeName": "Hive",
    "status": "Failure",
    "catogery": "resource",
    "tenantId": "zhaoyim-1502869020"
  },
  {
    "id": "158dcc32-8090-11e7-8e31-fa163efdbea8",
    "instanceName": "HDFS-admin-1502673613",
    "serviceTypeId": "",
    "serviceTypeName": "HDFS",
    "status": "Failure",
    "catogery": "resource",
    "tenantId": "86c62459-7c04-11e7-aa10-fa163ed7d0ae"
  },
  ...
]  
```

### 2.3.7获取指定服务的plan套餐（/ocmanager/v2/api/service/{serviceName}/plan/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/service/HBase/plan/
    请求方式：GET

#### 2.3.7.1请求参数

##### 2.3.7.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
serviceName|String|服务名|是|

#### 2.3.7.2返回参数

##### 2.3.7.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
name|String|plan名||否
id|String|planid||否
description|String|plan描述||否
metadata|json|源数据|其内容见以下字段|否
bullets|array||(metadata字段,其内容见以下字段)|否
HBase Maximun Tables|int|HBase命名空间允许的最大的表数目|bullet字段|否
HBase Maximun Regions|int|HBase命名空间允许的最大的region数目|bullet字段|否
costs|array||(metadata字段,其内容见以下字段)内部使用字段，使用者可不关心|否
amount|json||(costs字段,其内容见以下字段)内部使用字段，使用者可不关心|否
usd|int||(amount字段)内部使用字段，使用者可不关心，必须唯一|是
unit|String||(costs字段)内部使用字段，使用者可不关心，必须唯一|是
displayName|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
customize|json||(metadata字段,其内容见以下字段)|否
maximumRegionsQuota|json|HBase命名空间允许的最大的region限额列表|customize字段|否
maximumTablesQuota|json|HBase命名空间允许的最大表限额列表|customize字段|否
default|int|默认值|maximumRegionsQuota、maximumTablesQuota共有字段|否
max|int|最大值|maximumRegionsQuota、maximumTablesQuota共有字段|否
price|int||(maximumRegionsQuota、maximumTablesQuota共有字段)内部使用字段，使用者可不关心，必须唯一|是
step|int||(maximumRegionsQuota、maximumTablesQuota共有字段共有字段)内部使用字段，使用者可不关心，必须唯一|是
desc|String|配额描述|maximumRegionsQuota、maximumTablesQuota共有字段|否
free|boolean||内部使用字段，使用者可不关心，必须唯一|是

#### 2.3.7.3报文示例

##### 2.3.7.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/service/HBase/plan/
```

##### 2.3.7.3.2返回报文示例

__response:__

```
[
  {
    "name": "shared",
    "id": "f658e391-b7d6-4b72-9e4c-c754e4943ae1",
    "description": "共享HBase实例",
    "metadata": {
      "bullets": [
        "HBase Maximun Tables:10",
        "HBase Maximun Regions:10"
      ],
      "costs": [
        {
          "amount": {
            "usd": 0
          },
          "unit": "MONTHLY"
        }
      ],
      "displayName": "",
      "customize": {
        "maximumRegionsQuota": {
          "default": 100,
          "max": 1000,
          "price": 10,
          "step": 10,
          "desc": "HBase命名空间允许的最大的region数目"
        },
        "maximumTablesQuota": {
          "default": 10,
          "max": 100,
          "price": 10,
          "step": 10,
          "desc": "HBase命名空间允许的最大的表数目"
        }
      }
    },
    "free": false
  }
]
```



## 2.4 Roles APIs

### 2.4.1获取所有服务角色（/ocmanager/v2/api/role/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/role/
    请求方式：GET



#### 2.4.1.1返回参数

##### 2.4.1.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
description|String|服务角色的描述
id|String|服务角色id
permission|String|权限|允许该服务角色具备的权限
rolename|String|服务角色名称

#### 2.4.1.2报文示例

##### 2.4.1.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/role/ 
```

##### 2.4.1.2.2返回报文示例

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

## 2.5 Tenants APIs

### 2.5.1创建租户（/ocmanager/v2/api/tenant/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/
    请求方式：POST

#### 2.5.1.1请求参数

##### 2.5.1.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|租户id|是|
name|String|租户名称|是|
description|String|租户描述|否|
parentId|String|父租户id|是|
quota|String|配额|是|返回HDFS目录允许创建的最大文件数目和HDFS目录的最大存储容量；HBase命名空间允许的最大的region数目和最大的表数目；Hive的最大存储容量和Yarn队列的最大容量；Kafka Topic 的最大存活时间,Kafka Topic 的分区数和Kafka Topic 的每一个分区最大存储容量；spark的Yarn队列的最大容量；MapReduceYarn队列的最大容量。注意格式
dueTime|String|租户有效日期|否|租户生命周期
brokers|List|租户绑定的brokers|是|用于过滤租户的服务列表
#### 2.5.1.2返回参数

##### 2.5.1.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
dataFoundryInfo|String|dataFoundry执行信息|内部使用字段，使用者可不关心，必须唯一|是
databaseInfo|json|数据库信息|其内容见以下字段|否
description|String|租户描述|databaseInfo字段|否
id|String|租户id|databaseInfo字段|否
level|int|租户级别|(databaseInfo字段)内部使用字段，使用者可不关心，必须唯一|是
name|String|租户名称|databaseInfo字段|否
parentId|String|父租户id|databaseInfo字段|否
quota|String|配额|(databaseInfo字段)返回HDFS目录允许创建的最大文件数目和HDFS目录的最大存储容量；HBase命名空间允许的最大的region数目和最大的表数目；Hive的最大存储容量和Yarn队列的最大容量；Kafka Topic 的最大存活时间,Kafka Topic 的分区数和KafkaTopic的每一个分区最大存储容量；spark的Yarn队列的最大容量；MapReduceYarn队列的最大容量。注意格式|否
dueTime|String|租户有效日期|租户生命周期|否
brokers|List|租户绑定的brokers|用于过滤租户的服务列表|否
#### 2.5.1.3报文示例

##### 2.5.1.3.1请求报文示例

__request body:__


```
{
    "id": "00001",
    "name": "test00001",
    "dueTime": "2018-06-15 15:00:00",
    "description": "test00001",
    "parentId": "111",
    "brokers": ["broker1", "broker2", "broker3"],
     "quota":"{\"hdfs\": {\"nameSpaceQuota\": 1,\"storageSpaceQuota\": 1},\"hbase\": {\"maximumTablesQuota\": 1,\"maximumRegionsQuota\": 1},\"hive\": {\"storageSpaceQuota\": 1,\"yarnQueueQuota\": 1},\"mapreduce\": {\"yarnQueueQuota\": 1},\"spark\": {\"yarnQueueQuota\":1},\"kafka\": {\"topicTTL\": 10000, \"topicQuota\":1 ,\"partitionSize\": 1}}"
}

```

##### 2.5.1.3.2返回报文示例



__response:__


```
{
  "dataFoundryInfo": "{\"kind\":\"Project\",\"apiVersion\":\"v1\",\"metadata\":{\"name\":\"00001\",\"selfLink\":\"/oapi/v1/projectrequests/00001\",\"uid\":\"0392f637-9ce7-11e7-b071-fa163efdbea8\",\"resourceVersion\":\"24885069\",\"creationTimestamp\":\"2017-09-19T03:02:46Z\",\"annotations\":{\"openshift.io/description\":\"test00001\",\"openshift.io/display-name\":\"test00001\",\"openshift.io/requester\":\"system:serviceaccount:default:ocm\",\"openshift.io/sa.scc.mcs\":\"s0:c36,c25\",\"openshift.io/sa.scc.supplemental-groups\":\"1001310000/10000\",\"openshift.io/sa.scc.uid-range\":\"1001310000/10000\"}},\"spec\":{\"finalizers\":[\"openshift.io/origin\",\"kubernetes\"]},\"status\":{\"phase\":\"Active\"}}\n",
  "databaseInfo": {
    "description": "test00001",
    "id": "00001",
    "level": 0,
    "dueTime": "2018-06-15 15:00:00",
    "name": "test00001",
    "parentId": "111",
    "brokers": ["broker1", "broker2", "broker3"],
    "quota": "{\"hdfs\": {\"nameSpaceQuota\": 1,\"storageSpaceQuota\": 1},\"hbase\": {\"maximumTablesQuota\": 1,\"maximumRegionsQuota\": 1},\"hive\": {\"storageSpaceQuota\": 1,\"yarnQueueQuota\": 1},\"mapreduce\": {\"yarnQueueQuota\": 1},\"spark\": {\"yarnQueueQuota\":1},\"kafka\": {\"topicTTL\": 10000, \"topicQuota\":1 ,\"partitionSize\": 1}}"
  }
}
```

### 2.5.2更新租户（/ocmanager/v2/api/tenant/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/
    请求方式：PUT


#### 2.5.2.1请求参数

##### 2.5.2.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|租户id|是|
name|String|租户名称|是|
quota|String|配额|是|返回HDFS目录允许创建的最大文件数目和HDFS目录的最大存储容量；HBase命名空间允许的最大的region数目和最大的表数目；Hive的最大存储容量和Yarn队列的最大容量；Kafka Topic 的最大存活时间,Kafka Topic 的分区数和Kafka Topic 的每一个分区最大存储容量；spark的Yarn队列的最大容量；MapReduceYarn队列的最大容量。注意格式

#### 2.5.2.2返回参数

##### 2.5.2.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
dataFoundryInfo|String|dataFoundry执行信息|内部使用字段，使用者可不关心，必须唯一|是
databaseInfo|json|数据库信息|其内容见以下字段|否
description|String|租户描述|databaseInfo字段|否
id|String|租户id|databaseInfo字段|否
level|int|租户级别|(databaseInfo字段)内部使用字段，使用者可不关心，必须唯一|是
name|String|租户名称|databaseInfo字段|否
parentId|String|父租户id|databaseInfo字段|否
quota|String|配额|(databaseInfo字段)返回HDFS目录允许创建的最大文件数目和HDFS目录的最大存储容量；HBase命名空间允许的最大的region数目和最大的表数目；Hive的最大存储容量和Yarn队列的最大容量；Kafka Topic 的最大存活时间,Kafka Topic 的分区数和KafkaTopic的每一个分区最大存储容量；spark的Yarn队列的最大容量；MapReduceYarn队列的最大容量。注意格式|否

#### 2.5.2.3报文示例

##### 2.5.2.3.1请求报文示例

__request body:__

```
{
    "id": "777",
    "description": "test7777",
     "quota":"{\"hdfs\": {\"nameSpaceQuota\": 501,\"storageSpaceQuota\": 4096},\"hbase\": {\"maximumTablesQuota\": 2,\"maximumRegionsQuota\": 2},\"hive\": {\"storageSpaceQuota\": 2,\"yarnQueueQuota\": 2},\"mapreduce\": {\"yarnQueueQuota\": 2},\"spark\": {\"yarnQueueQuota\":2},\"kafka\": {\"topicTTL\": 10000, \"topicQuota\":2 ,\"partitionSize\": 1}}"
}

```

##### 2.5.2.3.2返回报文示例



__response:__

```
{
  "dataFoundryInfo": "no dataFoundryInfo",
  "databaseInfo": {
    "description": "test7777",
    "id": "777",
    "level": 0,
    "name": "test777",
    "parentId": "111",
    "quota": "{\"hdfs\": {\"nameSpaceQuota\": 501,\"storageSpaceQuota\": 4096},\"hbase\": {\"maximumTablesQuota\": 2,\"maximumRegionsQuota\": 2},\"hive\": {\"storageSpaceQuota\": 2,\"yarnQueueQuota\": 2},\"mapreduce\": {\"yarnQueueQuota\": 2},\"spark\": {\"yarnQueueQuota\":2},\"kafka\": {\"topicTTL\": 10000, \"topicQuota\":2 ,\"partitionSize\": 1}}"
  }
}

```


### 2.5.3获取所有租户（/ocmanager/v2/api/tenant/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/
    请求方式：GET


#### 2.5.3.1返回参数

##### 2.5.3.1.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
description|String|租户描述|租户名|否
id|String|租户id||否
level|int|租户级别|内部使用字段，使用者可不关心，必须唯一|是
name|String|租户名称||否
parentId|String|父租户id||否
quota|String|配额|返回HDFS目录允许创建的最大文件数目和HDFS目录的最大存储容量；HBase命名空间允许的最大的region数目和最大的表数目；Hive的最大存储容量和Yarn队列的最大容量；Kafka Topic 的最大存活时间,Kafka Topic 的分区数和KafkaTopic的每一个分区最大存储容量；spark的Yarn队列的最大容量；MapReduceYarn队列的最大容量。注意格式|否

#### 2.5.3.2报文示例

##### 2.5.3.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/
```


##### 2.5.3.2.2返回报文示例

__response:__


```
[
  {
    "description": "test111",
    "id": "111",
    "level": 0,
    "name": "test111",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
    "quota": "{\"hdfs\": {\"nameSpaceQuota\": 5000,\"storageSpaceQuota\": 5000},\"hbase\": {\"maximumTablesQuota\": 5000,\"maximumRegionsQuota\": 5000},\"hive\": {\"storageSpaceQuota\": 5000,\"yarnQueueQuota\": 100},\"mapreduce\": {\"yarnQueueQuota\": 100},\"spark\": {\"yarnQueueQuota\": 100},\"kafka\": {\"topicTTL\": 120960000000,\"topicQuota\": 100,\"partitionSize\": 100}}"
  },
  {
    "description": "test222",
    "id": "222",
    "level": 0,
    "name": "test222",
    "parentId": "111",
    "quota": "{\"hdfs\": {\"nameSpaceQuota\": 200,\"storageSpaceQuota\": 100},\"hbase\": {\"maximumTablesQuota\": 200,\"maximumRegionsQuota\": 100},\"hive\": {\"storageSpaceQuota\": 100,\"yarnQueueQuota\": 5},\"mapreduce\": {\"yarnQueueQuota\": 6},\"spark\": {\"yarnQueueQuota\": 7},\"kafka\": {\"topicTTL\": 604800000,\"topicQuota\": 10,\"partitionSize\": 10}}"
  },
  ...
]
```

### 2.5.4获取单个租户（/ocmanager/v2/api/tenant/{id}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/222/
    请求方式：GET

#### 2.5.4.1请求参数

##### 2.5.4.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|租户id|是

#### 2.5.4.2返回参数

##### 2.5.4.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
description|String|租户描述|租户名|否
id|String|租户id||否
level|int|租户级别|内部使用字段，使用者可不关心，必须唯一|是
name|String|租户名称||否
parentId|String|父租户id||否
quota|String|配额|返回HDFS目录允许创建的最大文件数目和HDFS目录的最大存储容量；HBase命名空间允许的最大的region数目和最大的表数目；Hive的最大存储容量和Yarn队列的最大容量；Kafka Topic 的最大存活时间,Kafka Topic 的分区数和KafkaTopic的每一个分区最大存储容量；spark的Yarn队列的最大容量；MapReduceYarn队列的最大容量。注意格式|否

#### 2.5.4.3报文示例

##### 2.5.4.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/222/
```

##### 2.5.4.3.2返回报文示例


__response:__

```
{
  "description": "test222",
  "id": "222",
  "level": 0,
  "name": "test222",
  "parentId": "111",
  "quota": "{\"hdfs\": {\"nameSpaceQuota\": 200,\"storageSpaceQuota\": 100},\"hbase\": {\"maximumTablesQuota\": 200,\"maximumRegionsQuota\": 100},\"hive\": {\"storageSpaceQuota\": 100,\"yarnQueueQuota\": 5},\"mapreduce\": {\"yarnQueueQuota\": 6},\"spark\": {\"yarnQueueQuota\": 7},\"kafka\": {\"topicTTL\": 604800000,\"topicQuota\": 10,\"partitionSize\": 10}}"
}

```

### 2.5.5获取指定租户的所有子租户（/ocmanager/v2/api/tenant/{id}/children/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/111/children/
    请求方式：GET

#### 2.5.5.1请求参数

##### 2.5.5.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|父租户id|是|

#### 2.5.5.2返回参数

##### 2.5.5.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
description|String|租户描述|租户名|否
id|String|租户id||否
level|int|租户级别|内部使用字段，使用者可不关心，必须唯一|是
name|String|租户名称||否
parentId|String|父租户id||否
quota|String|配额|返回HDFS目录允许创建的最大文件数目和HDFS目录的最大存储容量；HBase命名空间允许的最大的region数目和最大的表数目；Hive的最大存储容量和Yarn队列的最大容量；Kafka Topic 的最大存活时间,Kafka Topic 的分区数和KafkaTopic的每一个分区最大存储容量；spark的Yarn队列的最大容量；MapReduceYarn队列的最大容量。注意格式|否

#### 2.5.5.3报文示例

##### 2.5.5.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/111/children/
```

##### 2.5.5.3.2返回报文示例


__response:__

```
[
  {
    "description": "test222",
    "id": "222",
    "level": 0,
    "name": "test222",
    "parentId": "111",
    "quota": "{\"hdfs\": {\"nameSpaceQuota\": 200,\"storageSpaceQuota\": 100},\"hbase\": {\"maximumTablesQuota\": 200,\"maximumRegionsQuota\": 100},\"hive\": {\"storageSpaceQuota\": 100,\"yarnQueueQuota\": 5},\"mapreduce\": {\"yarnQueueQuota\": 6},\"spark\": {\"yarnQueueQuota\": 7},\"kafka\": {\"topicTTL\": 604800000,\"topicQuota\": 10,\"partitionSize\": 10}}"
  },
  {
    "description": "test6666",
    "id": "333",
    "level": 0,
    "name": "test333",
    "parentId": "111",
    "quota": "{\"hdfs\": {\"nameSpaceQuota\": 1,\"storageSpaceQuota\": 2},\"hbase\": {\"maximumTablesQuota\": 2,\"maximumRegionsQuota\": 2},\"hive\": {\"storageSpaceQuota\": 2,\"yarnQueueQuota\": 2},\"mapreduce\": {\"yarnQueueQuota\": 2},\"spark\": {\"yarnQueueQuota\":2},\"kafka\": {\"topicTTL\": 10000, \"topicQuota\":2 ,\"partitionSize\": 1}}"
  },
  ...
]

```


### 2.5.6在租户下创建服务实例（/ocmanager/v2/api/tenant/{id}/service/instance/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/1111/service/instance/
    请求方式：POST

#### 2.5.6.1请求参数

##### 2.5.6.1.1请求参数

字段|类型|描述|是否必填|备注|是否常量|
----------|----------------|----|--------|------------|---|
id|String|租户id|是
kind|String||是|内部使用字段，使用者可不关心，必须唯一|是
apiVersion|String||是|内部使用字段，使用者可不关心，必须唯一|是
metadata|json|源数据|是|其内容见以下字段|否
name|String|ClusterManage的服务实例名称的名字|是|metadata字段|否
spec|json|指定参数|是|具体内容参见以下字段|否
provisioning|json||是|(spec字段)具体内容参见以下字段,内部使用字段，使用者可不关心|否
backingservice_name|String|backingservice名称|是|(provisioning字段)与backingservice_plan_guid一一对应|否
backingservice_plan_guid|String|backingservice唯一标识符|是|(provisioning字段)与backingservice_plan_guid一一对应|否
parameters|json||是|(provisioning字段,其内容见以下字段)|否
cuzBsiName|String|后端服务的真实名字|是|(parameters字段)|否
maximumRegionsQuota|String|HBase命名空间允许的最大的region数目|是|(parameters字段)|否
maximumTablesQuota|String|Base命名空间允许的最大的表数目|是|(parameters字段)|否

#### 2.5.6.2返回参数

##### 2.5.6.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
kind|String||内部使用字段，使用者可不关心，必须唯一|是
apiVersion|String||内部使用字段，使用者可不关心，必须唯一|是
metadata|json|源数据|其内容见以下字段|否
name|String|ClusterManage的服务实例名称的名字|metadata字段|否
namespace|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
selfLink|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
uid|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
resourceVersion|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
creationTimestamp|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
spec|json|指定参数|其内容见以下字段|否
provisioning|json||(spec字段,具体内容参见以下字段)内部使用字段，使用者可不关心|否
dashboard_url|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
backingservice_name|String|backingservice名称|(provisioning字段)与backingservice_plan_guid一一对应|否
backingservice_plan_guid|String|backingservice唯一标识符|(provisioning字段)与backingservice_plan_guid一一对应|否
backingservice_spec_id|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
backingservice_plan_name|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
parameters|json||(provisioning字段,其内容见以下字段)内部使用字段，使用者可不关心|否
cuzBsiName|String|后端服务的真实名字|(parameters字段)|否
maximumRegionsQuota|String|HBase命名空间允许的最大的region数目|(parameters字段)|否
maximumTablesQuota|String|Base命名空间允许的最大的表数目|(parameters字段)|否
credentials|||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
userprovidedservice|json||(spec字段,其内容见以下字段)内部使用字段，使用者可不关心|否
credentials|||(userprovidedservice字段)内部使用字段，使用者可不关心，必须唯一|是
binding|||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
bound|int||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
instance_id|String|服务实例id|(spec字段)|否
tags|||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
status|json|服务实例状态|其内容见以下字段|否
phase|String|阶段|status字段|否

#### 2.5.6.3报文示例

##### 2.5.6.3.1请求报文示例

__request body:__

```
{
  "kind":"BackingServiceInstance",
  "apiVersion":"v1",
  "metadata":
    {
      "name": "zhaoyim2222"
    },
  "spec":
    {
      "provisioning":
        {
          "backingservice_name":"HBase",
          "backingservice_plan_guid":"f658e391-b7d6-4b72-9e4c-c754e4943ae1",
          "parameters":{"maximumRegionsQuota":"1","maximumTablesQuota":"1", "cuzBsiName": "zhaoyim1234"}
        }
    }
}
```

##### 2.5.6.3.2返回报文示例


__response:__

```
{
  "kind": "BackingServiceInstance",
  "apiVersion": "v1",
  "metadata": {
    "name": "zhaoyim2222",
    "namespace": "admin-1503559648",
    "selfLink": "/oapi/v1/namespaces/admin-1503559648/backingserviceinstances/zhaoyim2222",
    "uid": "9ef21386-8edb-11e7-b3b4-fa163efdbea8",
    "resourceVersion": "22852703",
    "creationTimestamp": "2017-09-01T06:05:57Z"
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "HBase",
      "backingservice_spec_id": "",
      "backingservice_plan_guid": "f658e391-b7d6-4b72-9e4c-c754e4943ae1",
      "backingservice_plan_name": "",
      "parameters": {
        "cuzBsiName": "zhaoyim1234",
        "maximumRegionsQuota": "1",
        "maximumTablesQuota": "1"
      },
      "credentials": null
    },
    "userprovidedservice": {
      "credentials": null
    },
    "binding": null,
    "bound": 0,
    "instance_id": "",
    "tags": null
  },
  "status": {
    "phase": "Provisioning"
  }
}
```

### 2.5.7获取租户下所有服务实例（/ocmanager/v2/api/tenant/{id}/service/instances/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/09367148-c72a-413f-b1de-5a23b566d809/service/instances/
    请求方式：GET

#### 2.5.7.1请求参数

##### 2.5.7.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|租户id|是|

#### 2.5.7.2返回参数

##### 2.5.7.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
id|String|服务实例id
instanceName|String|服务实例名称
quota|String|配额|返回Kafka该服务实例的Topic 的最大存活时间,Topic 的分区数,Topic 的每一个分区最大存储容量信息
serviceTypeId|String|服务类型id
serviceTypeName|String|服务类型名称
status|String|服务实例状态|Unbound表示实例运行异常即已删除
tenantId|String|租户id
category|String|实例分类（资源、工具、应用）

#### 2.5.7.3报文示例

##### 2.5.7.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/09367148-c72a-413f-b1de-5a23b566d809/service/instances/
```

##### 2.5.7.3.2返回报文示例


__response:__


```
[
  {
    "id": "f8be2b3f-8624-11e7-bf73-fa163efdbea8",
    "instanceName": "zhaoyim_kafka_instance777",
    "quota": "{\"instance_id\":\"f8be2b3f-8624-11e7-bf73-fa163efdbea8\",\"partitionSize\":\"10\",\"topicQuota\":\"10\",\"topicTTL\":\"10\"}",
    "serviceTypeId": "",
    "serviceTypeName": "Kafka",
    "category": "resource",
    "status": "Unbound",
    "tenantId": "09367148-c72a-413f-b1de-5a23b566d809"
  },
  ...
]
```

### 2.5.8删除租户下某个服务实例（/ocmanager/v2/api/tenant/{id}/service/instance/{instanceName}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/f8be2b3f-8624-11e7-bf73-fa163efdbea8/service/instance/09367148-c72a-413f-b1de-5a23b566d809/
    请求方式：DELETE

#### 2.5.8.1请求参数

##### 2.5.8.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|租户id|是|
instanceName|String|实例名是|

#### 2.5.8.2返回参数

##### 2.5.8.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
kind|String||内部使用字段，使用者可不关心，必须唯一|是
apiVersion|String||内部使用字段，使用者可不关心，必须唯一|是
metadata|json|源数据|其内容见以下字段|否
name|String|ClusterManage的服务实例名称的名字|metadata字段|否
namespace|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
selfLink|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
uid|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
resourceVersion|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
creationTimestamp|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
deletionTimestamp|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
annotations|json||(metadata字段,其内容见以下字段)内部使用字段，使用者可不关心|否
datafoundry.io/servicebroker|String||(annotations字段)内部使用字段，使用者可不关心，必须唯一|是
spec|json|指定参数|其内容见以下字段|否
provisioning|json||(spec字段,具体内容参见以下字段)内部使用字段，使用者可不关心|否
dashboard_url|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
backingservice_name|String|backingservice名称|(provisioning字段)与backingservice_plan_guid一一对应|否
backingservice_plan_guid|String|backingservice唯一标识符|(provisioning字段)与backingservice_plan_guid一一对应|否
backingservice_spec_id|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
backingservice_plan_name|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
parameters|json||(provisioning字段,其内容见以下字段)|否
instance_id|String|Kafka实例id|parameters字段|否
partitionSize|String|KafkaTopic的每一个分区最大存储容量|parameters字段|否
topicQuota|String|Kafka Topic的分区数|parameters字段|否
topicTTL|String|KafkaTopic的最大存活时间|parameters字段|否
credentials|||(provisioning字段,具体内容参见以下字段)内部使用字段，使用者可不关心|否
ZooKeeper_URI|String|ZooKeeper网址|credentials字段|否
host|String|主机|credentials字段|否
port|String|端口|credentials字段|否
topic|String|Kafka Topic|credentials字段|否
userprovidedservice|json||(spec字段,其内容见以下字段)内部使用字段，使用者可不关心|否
credentials|||(userprovidedservice字段)内部使用字段，使用者可不关心，必须唯一|是
binding|||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
bound|int||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
instance_id|String|实例id|(spec字段)|否
tags|||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
status|json|服务实例状态|其内容见以下字段|否
phase|String|阶段|(status字段)Unbound表示实例运行异常即已删除，bound表示实例运行正常|否
action|String|动作|(status字段)_ToDelete表示删除租户下的某个服务实例|否

#### 2.5.8.3报文示例

##### 2.5.8.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/f8be2b3f-8624-11e7-bf73-fa163efdbea8/service/instance/09367148-c72a-413f-b1de-5a23b566d809/
```

##### 2.5.8.3.2返回报文示例

__response:__


```
{
  "kind": "BackingServiceInstance",
  "apiVersion": "v1",
  "metadata": {
    "name": "zhaoyim_kafka_instance777",
    "namespace": "09367148-c72a-413f-b1de-5a23b566d809",
    "selfLink": "/oapi/v1/namespaces/09367148-c72a-413f-b1de-5a23b566d809/backingserviceinstances/zhaoyim_kafka_instance777",
    "uid": "f8bda5e4-8624-11e7-bf73-fa163efdbea8",
    "resourceVersion": "22590458",
    "creationTimestamp": "2017-08-21T03:58:20Z",
    "deletionTimestamp": "2017-08-21T04:02:13Z",
    "annotations": {
      "datafoundry.io/servicebroker": "hadoop"
    }
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "Kafka",
      "backingservice_spec_id": "7b738c78-d412-422b-ac3e-43a9fc72a4a7",
      "backingservice_plan_guid": "68ee85c2-5b1a-4f51-89e9-5b111c251f0d",
      "backingservice_plan_name": "shared",
      "parameters": {
        "instance_id": "f8be2b3f-8624-11e7-bf73-fa163efdbea8",
        "partitionSize": "10",
        "topicQuota": "10",
        "topicTTL": "10"
      },
      "credentials": {
        "ZooKeeper_URI": "ochadoop111.jcloud.local:2181",
        "host": "",
        "port": "",
        "topic": "oc_f8be2b3f-8624-11e7-bf73-fa163efdbea8"
      }
    },
    "userprovidedservice": {
      "credentials": null
    },
    "binding": null,
    "bound": 0,
    "instance_id": "f8be2b3f-8624-11e7-bf73-fa163efdbea8",
    "tags": null
  },
  "status": {
    "phase": "Unbound",
    "action": "_ToDelete"
  }
}
```

### 2.5.9绑定租户，用户和角色（/ocmanager/v2/api/tenant/{id}/user/role/assignment/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/011ea988-abc2-4267-9215-cacf111716d1/user/role/assignment/
    请求方式：POST

#### 2.5.9.1请求参数

##### 2.5.9.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
userId|String|用户id|是
roleId|String|角色id|是
id|String|用户id|是

#### 2.5.9.2返回参数

##### 2.5.9.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
roleId|String|角色id|
tenantId|String|租户id
userId|String|用户id|

#### 2.5.9.3报文示例

##### 2.5.9.3.1请求报文示例


__request body:__

```
{
    "userId": "011ea988-abc2-4267-9215-cacf111716d1",
    "roleId": "a13dd087-524a-11e7-9dbb-fa163ed7d0ae"
}
```


##### 2.5.9.3.2返回报文示例



__response:__


```
{
  "roleId": "a13dd087-524a-11e7-9dbb-fa163ed7d0ae",
  "tenantId": "09367148-c72a-413f-b1de-5a23b566d809",
  "userId": "011ea988-abc2-4267-9215-cacf111716d1"
}
```

### 2.5.10获取租户下所有用户以及用户角色（/ocmanager/v2/api/tenant/{id}/users/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/09367148-c72a-413f-b1de-5a23b566d809/users/
    请求方式：GET

#### 2.5.10.1请求参数

##### 2.5.10.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|租户id|是|

#### 2.5.10.2返回参数

##### 2.5.10.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
roleId|String|角色id|
roleName|String|角色名称
tenantId|String|租户id
userDescription|String|用户描述
userEmail|String|用户电子邮件
userId|String|用户id|
userName|String|用户名
userPassword|String|用户密码
userPhone|String|用户电话

#### 2.5.10.3报文示例

##### 2.5.10.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/09367148-c72a-413f-b1de-5a23b566d809/users/
```

##### 2.5.10.3.2返回报文示例

__response:__


```
[
  {
    "roleId": "a10170cb-524a-11e7-9dbb-fa163ed7d0ae",
    "roleName": "system.admin",
    "tenantId": "09367148-c72a-413f-b1de-5a23b566d809",
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
    "tenantId": "09367148-c72a-413f-b1de-5a23b566d809",
    "userDescription": "",
    "userEmail": "a1@163.com",
    "userId": "011ea988-abc2-4267-9215-cacf111716d1",
    "userName": "user001",
    "userPassword": "*74B1C21ACE0C2D6B0678A5E503D2A60E8F9651A3"
  },
  ...
]
```

### 2.5.11更新租户中用户的角色（/ocmanager/v2/api/tenant/{id}/user/role/assignment/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/09367148-c72a-413f-b1de-5a23b566d809/user/role/assignment/
    请求方式：PUT

#### 2.5.11.1请求参数

##### 2.5.11.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
String|租户id|是|
userId|String|用户id|是
roleId|String|角色id|是
id|String|租户id|是

#### 2.5.11.2返回参数

##### 2.5.11.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
roleId|String|角色id|
tenantId|String|租户id
userId|String|用户id|

#### 2.5.11.3报文示例

##### 2.5.11.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/09367148-c72a-413f-b1de-5a23b566d809/user/role/assignment/
```

##### 2.5.11.3.2返回报文示例

__response:__


```
{
  "roleId": "a12a84d0-524a-11e7-9dbb-fa163ed7d0ae",
  "tenantId": "09367148-c72a-413f-b1de-5a23b566d809",
  "userId": "011ea988-abc2-4267-9215-cacf111716d1"
}
``` 

### 2.5.12解除租户，用户和角色的绑定（/ocmanager/v2/api/tenant/{id}/user/{userId}/role/assignment/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/09367148-c72a-413f-b1de-5a23b566d809/user/011ea988-abc2-4267-9215-cacf111716d1/role/assignment/
    请求方式：DELETE

#### 2.5.12.1请求参数

##### 2.5.12.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
userId|String|用户id|是|
id|String|租户id|是|

#### 2.5.12.2返回参数

##### 2.5.12.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
message|String|返回的消息|返回用户id
resCode|int|响应返回码|200为正常
status|String|状态|返回解除状态，delete success表示解除成功

#### 2.5.12.3报文示例

##### 2.5.12.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/09367148-c72a-413f-b1de-5a23b566d809/user/011ea988-abc2-4267-9215-cacf111716d1/role/assignment/
```

##### 2.5.12.3.2返回报文示例


__response:__


```
{
  "message": "011ea988-abc2-4267-9215-cacf111716d1",
  "resCode": 200,
  "status": "delete success"
}
``` 

### 2.5.13删除租户（/ocmanager/v2/api/tenant/{id}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/09367148-c72a-413f-b1de-5a23b566d809/
    请求方式：DELETE

#### 2.5.13.1请求参数

##### 2.5.13.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|租户id|是|

#### 2.5.13.2返回参数

##### 2.5.13.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
dataFoundryInfo|String|dataFoundry执行信息|内部使用字段，使用者可不关心，必须唯一|是
databaseInfo|json|数据库信息|其内容见以下字段|否
description|String|租户描述|databaseInfo字段|否
id|String|租户id|databaseInfo字段|否
level|int|租户级别|(databaseInfo字段)内部使用字段，使用者可不关心，必须唯一|是
name|String|租户名称|databaseInfo字段|否
parentId|String|父租户id|databaseInfo字段|否

#### 2.5.13.3报文示例

##### 2.5.13.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/09367148-c72a-413f-b1de-5a23b566d809/
```

##### 2.5.13.3.2返回报文示例


__response:__


```
{
  "dataFoundryInfo": "{\"kind\":\"Status\",\"apiVersion\":\"v1\",\"metadata\":{},\"status\":\"Success\",\"code\":200}\n",
  "databaseInfo": {
    "description": "create tenant 001",
    "id": "09367148-c72a-413f-b1de-5a23b566d809",
    "level": 0,
    "name": "createTenant001",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae"
  }
}
```

### 2.5.14获取服务实例访问信息（/ocmanager/v2/api/tenant/{tenantId}/service/instance/{serviceInstanceName}/access/info/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/zhaoyim-1502764945/service/instance/HDFS-admin-54979FD/access/info/
    请求方式：GET

#### 2.5.14.1请求参数

##### 2.5.14.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
tenantId|String|租户id|是|
serviceInstanceName|String|实例名称|是|

#### 2.5.14.2返回参数

##### 2.5.14.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
kind|String||内部使用字段，使用者可不关心，必须唯一|是
apiVersion|String||内部使用字段，使用者可不关心，必须唯一|是
metadata|json|源数据|其内容见以下字段|否
name|String|ClusterManage的服务实例名称的名字|metadata字段|否
namespace|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
selfLink|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
uid|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
resourceVersion|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
creationTimestamp|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
annotations|json||metadata字段,其内容见以下字段|否
datafoundry.io/servicebroker|String||(annotations字段)内部使用字段，使用者可不关心，必须唯一|是
spec|json|指定参数|(其内容见以下字段)|否
provisioning|json||(spec字段,具体内容参见以下字段)内部使用字段，使用者可不关心|否
dashboard_url|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
backingservice_name|String|backingservice名称|(provisioning字段)与backingservice_plan_guid一一对应|否
backingservice_plan_guid|String|backingservice唯一标识符|(provisioning字段)与backingservice_plan_guid一一对应|否
backingservice_spec_id|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
backingservice_plan_name|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
parameters|json||(provisioning字段,其内容见以下字段)|否
accesses|String|访问权限|(parameters字段)read,write,execute表示可读可写可执行|否
instance_id|String|租户名称|parameters字段|否
nameSpaceQuota|String|HDFS目录允许创建的最大文件数目|parameters字段|否
storageSpaceQuota|String|HDFS目录的最大存储容量|parameters字段|否
user_name|String|用户名|parameters字段|否
credentials|||(provisioning字段,具体内容参见以下字段)内部使用字段，使用者可不关心|否
HDFS Path|String|HDFS路径|credentials字段|否
host|String|主机|credentials字段|否
port|String|端口|credentials字段|否
url|String|HDFS网址|credentials字段|否
userprovidedservice|json||(spec字段,其内容见以下字段)内部使用字段，使用者可不关心|否
credentials|||(userprovidedservice字段)内部使用字段，使用者可不关心，必须唯一|是
binding|||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
bound_time|String||(binding字段)内部使用字段，使用者可不关心，必须唯一|是
bind_uuid|String||(binding字段)内部使用字段，使用者可不关心，必须唯一|是
bind_hadoop_user|String||(binding字段)内部使用字段，使用者可不关心，必须唯一|是
credentials|||(binding字段,具体内容参见以下字段)内部使用字段，使用者可不关心|否
HDFS Path|String|HDFS路径|credentials字段|否
host|String|主机|credentials字段|否
port|String|端口|credentials字段|否
url|String|HDFS网址|credentials字段|否
user_name|String|用户名|credentials字段|否
bound|int||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
instance_id|String|实例id|(spec字段)|否
tags|||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
status|json|服务实例状态|其内容见以下字段|否
phase|String|阶段|(status字段)Unbound表示实例运行异常即删除，bound表示实例运行正常|否

#### 2.5.14.3报文示例

##### 2.5.14.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/zhaoyim-1502764945/service/instance/HDFS-admin-54979FD/access/info/
```

##### 2.5.14.3.2返回报文示例


__response:__


```
{
  "kind": "BackingServiceInstance",
  "apiVersion": "v1",
  "metadata": {
    "name": "HDFS-admin-54979FD",
    "namespace": "zhaoyim-1502764945",
    "selfLink": "/oapi/v1/namespaces/zhaoyim-1502764945/backingserviceinstances/HDFS-admin-54979FD",
    "uid": "4789f551-8629-11e7-bf73-fa163efdbea8",
    "resourceVersion": "22591222",
    "creationTimestamp": "2017-08-21T04:29:11Z",
    "annotations": {
      "datafoundry.io/servicebroker": "hadoop"
    }
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "HDFS",
      "backingservice_spec_id": "ae67d4ba-5c4e-4937-a68b-5b47cfe356d8",
      "backingservice_plan_guid": "72150b09-1025-4533-8bae-0e04ef68ac13",
      "backingservice_plan_name": "shared",
      "parameters": {
        "accesses": "read,write,execute",
        "instance_id": "478a729e-8629-11e7-bf73-fa163efdbea8",
        "nameSpaceQuota": "1000",
        "storageSpaceQuota": "1024",
        "user_name": "user001"
      },
      "credentials": {
        "HDFS Path": "/servicebroker/478a729e-8629-11e7-bf73-fa163efdbea8",
        "host": "ochadoop111",
        "port": "50070",
        "uri": "http://ochadoop111:50070/webhdfs/v1/servicebroker/478a729e-8629-11e7-bf73-fa163efdbea8"
      }
    },
    "userprovidedservice": {
      "credentials": null
    },
    "binding": [
      {
        "bound_time": "2017-08-21T04:32:55Z",
        "bind_uuid": "ccf906dc-8629-11e7-bf73-fa163efdbea8",
        "bind_hadoop_user": "user001",
        "credentials": {
          "HDFS Path": "/servicebroker/478a729e-8629-11e7-bf73-fa163efdbea8",
          "host": "ochadoop111",
          "port": "50070",
          "uri": "http://ochadoop111:50070/webhdfs/v1/servicebroker/478a729e-8629-11e7-bf73-fa163efdbea8",
          "username": "user001"
        }
      }
    ],
    "bound": 1,
    "instance_id": "478a729e-8629-11e7-bf73-fa163efdbea8",
    "tags": null
  },
  "status": {
    "phase": "Bound"
  }
}
```

### 2.5.15更新租户单个服务实例（/ocmanager/v2/api/tenant/{id}/service/instance/{instanceName}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/zhaoyim-1502764945/service/instance/HDFS-admin-54979FD/
    请求方式：PUT

#### 2.5.15.1请求参数

##### 2.5.15.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
id|String|租户id|是|
instanceName|String|实例名|是|
parameters|json|内容见以下字段|是
hiveStorageQuota|String|(parameters字段)Hive数据库的最大存储容量|是
yarnQueueQuota|String|(parameters字段)Yarn队列的最大容量|是

#### 2.5.15.2返回参数

##### 2.5.15.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
kind|String||内部使用字段，使用者可不关心，必须唯一|是
apiVersion|String||内部使用字段，使用者可不关心，必须唯一|是
metadata|json|源数据|其内容见以下字段|否
name|String|ClusterManage的服务实例名称的名字|metadata字段|否
namespace|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
selfLink|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
uid|String||(metadata字段)内部使用字段，使用者可不关心，必须唯一|是
resourceVersion|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
creationTimestamp|String||（metadata字段)内部使用字段，使用者可不关心，必须唯一|是
annotations|json||(metadata字段)其内容见以下字段|否
datafoundry.io/servicebroker|String||(annotations字段)内部使用字段，使用者可不关心，必须唯一|是
spec|json|指定参数|(其内容见以下字段)|否
provisioning|json||(spec字段,具体内容参见以下字段)内部使用字段，使用者可不关心|否
dashboard_url|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
backingservice_name|String|backingservice名称|(provisioning字段)与backingservice_plan_guid一一对应|否
backingservice_plan_guid|String|backingservice唯一标识符|(provisioning字段)与backingservice_plan_guid一一对应|否
backingservice_spec_id|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
backingservice_plan_name|String||(provisioning字段)内部使用字段，使用者可不关心，必须唯一|是
parameters|json||(provisioning字段,其内容见以下字段)|否
hiveStorageQuota|String|Hive数据库的最大存储容量|parameters字段|否
yarnQueueQuota|String|Yarn队列的最大容量|parameters字段|否
credentials|||(provisioning字段,具体内容参见以下字段)内部使用字段，使用者可不关心|否
HDFS Path|String|HDFS路径|credentials字段|否
host|String|主机|credentials字段|否
port|String|端口|credentials字段|否
url|String|HDFS网址|credentials字段|否
userprovidedservice|json||(spec字段,其内容见以下字段)内部使用字段，使用者可不关心|否
credentials|||(userprovidedservice字段)内部使用字段，使用者可不关心，必须唯一|是
binding|||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
bound_time|String||(binding字段)内部使用字段，使用者可不关心，必须唯一|是
bind_uuid|String||(binding字段)内部使用字段，使用者可不关心，必须唯一|是
bind_hadoop_user|String||(binding字段)内部使用字段，使用者可不关心，必须唯一|是
credentials|||(binding字段,具体内容参见以下字段)内部使用字段，使用者可不关心|否
HDFS Path|String|HDFS路径|credentials字段|否
host|String|主机|credentials字段|否
port|String|端口|credentials字段|否
url|String|HDFS网址|credentials字段|否
user_name|String|用户名|credentials字段|否
bound|int||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
instance_id|String|实例id|(spec字段)|否
tags|||(spec字段)内部使用字段，使用者可不关心，必须唯一|是
status|json|服务实例状态|其内容见以下字段|否
phase|String|阶段|(status字段)Unbound表示实例运行异常，bound表示实例运行正常|否
patch|String|补丁|(status字段)Update为更新实例|否

#### 2.5.15.3报文示例

##### 2.5.15.3.1请求报文示例


__request body:__

```
{      
    "parameters": 
        {
            "hiveStorageQuota": "77",
            "yarnQueueQuota": "77"
        }
}
```

##### 2.5.15.3.2返回报文示例


__response:__


```
{
  "kind": "BackingServiceInstance",
  "apiVersion": "v1",
  "metadata": {
    "name": "HDFS-admin-54979FD",
    "namespace": "zhaoyim-1502764945",
    "selfLink": "/oapi/v1/namespaces/zhaoyim-1502764945/backingserviceinstances/HDFS-admin-54979FD",
    "uid": "4789f551-8629-11e7-bf73-fa163efdbea8",
    "resourceVersion": "22591433",
    "creationTimestamp": "2017-08-21T04:29:11Z",
    "annotations": {
      "datafoundry.io/servicebroker": "hadoop"
    }
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "HDFS",
      "backingservice_spec_id": "ae67d4ba-5c4e-4937-a68b-5b47cfe356d8",
      "backingservice_plan_guid": "72150b09-1025-4533-8bae-0e04ef68ac13",
      "backingservice_plan_name": "shared",
      "parameters": {
        "hiveStorageQuota": "77",
        "yarnQueueQuota": "77"
      },
      "credentials": {
        "HDFS Path": "/servicebroker/478a729e-8629-11e7-bf73-fa163efdbea8",
        "host": "ochadoop111",
        "port": "50070",
        "uri": "http://ochadoop111:50070/webhdfs/v1/servicebroker/478a729e-8629-11e7-bf73-fa163efdbea8"
      }
    },
    "userprovidedservice": {
      "credentials": null
    },
    "binding": [
      {
        "bound_time": "2017-08-21T04:32:55Z",
        "bind_uuid": "ccf906dc-8629-11e7-bf73-fa163efdbea8",
        "bind_hadoop_user": "user001",
        "credentials": {
          "HDFS Path": "/servicebroker/478a729e-8629-11e7-bf73-fa163efdbea8",
          "host": "ochadoop111",
          "port": "50070",
          "uri": "http://ochadoop111:50070/webhdfs/v1/servicebroker/478a729e-8629-11e7-bf73-fa163efdbea8",
          "username": "user001"
        }
      }
    ],
    "bound": 1,
    "instance_id": "478a729e-8629-11e7-bf73-fa163efdbea8",
    "tags": null
  },
  "status": {
    "phase": "Bound",
    "patch": "Update"
  }
}
```

### 2.5.16获取角色根据租户和用户名（/ocmanager/v2/api/tenant/{tenantId}/user/{userName}/role/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/zhaoyim-1502764945/user/zhaoyim/role/
    请求方式：GET

#### 2.5.16.1请求参数

##### 2.5.16.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
tenantId|String|租户id|是|
userName|String|用户名称|是|

#### 2.5.16.2返回参数

##### 2.5.16.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
permission|String|该角色权限
roleId|String|角色id
roleName|String|角色名称
tenantId|String|租户id
userId|String|用户id
userName|String|用户名称

#### 2.5.16.3报文示例

##### 2.5.16.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/zhaoyim-1502764945/user/zhaoyim/role/
```

##### 2.5.16.3.2返回报文示例


__response:__

```
{
  "permission": "{createUser: true, updateUser: true, deleteUser: true, addService: true, deleteService: true, grant: true}",
  "roleId": "a10170cb-524a-11e7-9dbb-fa163ed7d0ae",
  "roleName": "system.admin",
  "tenantId": "zhaoyim-1502764945",
  "userId": "8f9adc49-bf64-4358-bb74-ad1a2c657c35",
  "userName": "zhaoyim"
}
```

### 2.5.17获取租户资源使用明细（/ocmanager/v2/api/tenant/{tenantId}/quotas） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/admin-1512542444/quotas
    请求方式：GET

#### 2.5.17.1请求参数

##### 2.5.17.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
tenantId|String|租户id|是|

#### 2.5.17.2返回参数

##### 2.5.17.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
id|String|租户id
services|Array|服务列表	
service|String|服务类型
limitations|JsonObject|服务资源限额
instances|Array|服务实例列表
instanceId|String|服务实例id
usage|Array|服务实例资源使用情况
name|String|服务实例资源名称
size|String|服务实例分配资源大小
used|String|服务实例已使用资源大小
available|String|服务实例空闲资源大小
desc|String|服务实例资源路径


#### 2.5.17.3报文示例

##### 2.5.17.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/admin-1512542444/quotas
```

##### 2.5.17.3.2返回报文示例


__response:__

```
{
    "id": "admin-1512542444",
    "services": [
        {
            "instances": [],
            "limitations": {
                "quotas": {
                    "yarnQueueQuota": 0
                },
                "service": "mapreduce"
            },
            "service": "mapreduce"
        },
        {
            "instances": [
                {
                    "instanceId": "084ad5c0-da51-11e7-988e-fa163efdbea8",
                    "service": "hdfs",
                    "usage": [
                        {
                            "available": "1000",
                            "desc": "/testTenantQuota",
                            "name": "nameSpaceQuota",
                            "size": "1000",
                            "used": "0"
                        },
                        {
                            "available": "1024000000000",
                            "desc": "/testTenantQuota",
                            "name": "storageSpaceQuota",
                            "size": "1024000000000",
                            "used": "0"
                        }
                    ]
                }
            ],
            "limitations": {
                "quotas": {
                    "storageSpaceQuota": 1500,
                    "nameSpaceQuota": 1500
                },
                "service": "hdfs"
            },
            "service": "hdfs"
        },
        {
            "instances": [],
            "limitations": {
                "quotas": {
                    "maximumRegionsQuota": 0,
                    "maximumTablesQuota": 0
                },
                "service": "hbase"
            },
            "service": "hbase"
        },
        {
            "instances": [],
            "limitations": {
                "quotas": {
                    "partitionSize": 0,
                    "topicQuota": 0,
                    "topicTTL": 0
                },
                "service": "kafka"
            },
            "service": "kafka"
        },
        {
            "instances": [],
            "limitations": {
                "quotas": {
                    "yarnQueueQuota": 0
                },
                "service": "spark"
            },
            "service": "spark"
        },
        {
            "instances": [],
            "limitations": {
                "quotas": {
                    "storageSpaceQuota": 0,
                    "yarnQueueQuota": 0
                },
                "service": "hive"
            },
            "service": "hive"
        }
    ]
}
```

### 2.5.18获取租户服务列表（/ocmanager/v2/api/tenant/{tenantId}/services） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/tenant/admin-1512542444/services
    请求方式：GET

#### 2.5.18.1请求参数

##### 2.5.18.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
tenantId|String|租户id|是|

#### 2.5.18.2返回参数

##### 2.5.18.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
description|String|服务描述
id|String|服务id
origin|String|来源于哪个service broker
servicename|String|服务名
servicetype|String|服务类型
catogery|String|服务分类（资源、工具、应用）


#### 2.5.18.3报文示例

##### 2.5.18.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/tenant/admin-1512542444/services
```

##### 2.5.18.3.2返回报文示例

__response:__

```
[
  {
    "description": "A Hadoop hbase service broker implementation",
    "id": "d9845ade-9410-4c7f-8689-4e032c1a8450",
    "origin": "ocdp",
    "servicename": "hbase",
    "servicetype": "hbase",
    "catogery": "resource"
  },
  {
    "description": "visualized analysing",
    "id": "ae67d4ba-5c4e-4937-a68b-5b47cfe35699",
    "origin": "open-shift",
    "servicename": "visanalyse",
    "servicetype": "visanalyse",
    "catogery": "tool"
  },
    {
    "description": "spark streaming",
    "id": "ae67d4ba-5c4e-4937-a68b-5b4888888888",
    "origin": "open-shift",
    "servicename": "ocsp",
    "servicetype": "ocsp",
    "catogery": "app"
  },
  ...
]
```

## 2.6 OCDP service instances quota APIs

### 2.6.1获取大数据平台HDFS服务实例用量（/ocmanager/v2/api/quota/hdfs?path={HDFS Path}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/quota/hdfs?path=/servicebroker/261b8f87-8257-11e7-990a-fa163efdbea8/
    请求方式：GET

#### 2.6.1.1请求参数

##### 2.6.1.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
HDFS Path|String|HDFS路径|是

#### 2.6.1.2返回参数

##### 2.6.1.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
items|array|项|内容见以下字段
available|String|HDFS服务实例可用资源的数量|items字段
desc|String|描述|items字段
name|String|HDFS服务实例用量名称，其中nameSpaceQuota表示HDFS目录允许创建的最大文件数目；storageSpaceQuota表示HDFS目录的最大存储容量|items字段
size|String|HDFS服务实例分配资源的总数|(items字段)对应name
used|String|HDFS服务实例已使用的资源数|(items字段)

#### 2.6.1.3报文示例

##### 2.6.1.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/quota/hdfs?path=/servicebroker/261b8f87-8257-11e7-990a-fa163efdbea8/ 
```

##### 2.6.1.3.2返回报文示例



__response:__

```
{
  "items": [
    {
      "available": "1001",
      "desc": "/servicebroker/261b8f87-8257-11e7-990a-fa163efdbea8",
      "name": "nameSpaceQuota",
      "size": "1001",
      "used": "0"
    },
    {
      "available": "1025000000000",
      "desc": "/servicebroker/261b8f87-8257-11e7-990a-fa163efdbea8",
      "name": "storageSpaceQuota",
      "size": "1025000000000",
      "used": "0"
    }
  ]
}
```

### 2.6.2获取大数据平台Hbase服务实例用量（/ocmanager/v2/api/quota/hbase/{HBaseNameSpace}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/quota/hbase/cc11a764831711e78d91fa163efdbea8/
    请求方式：GET

#### 2.6.2.1请求参数

##### 2.6.2.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
HBaseNameSpace|String|Hbase服务实例名|是

#### 2.6.2.2返回参数

##### 2.6.2.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
items|array|项|内容见以下字段
available|String|Hbase服务实例可用资源的数量|items字段
desc|String|描述|items字段
name|String|Hbase服务实例用量名称，其中maximumRegionsQuota表示HBase命名空间允许的最大的region数目；maximumTablesQuota表示HBase命名空间允许的最大的表数目|items字段
size|String|Hbase服务实例分配资源的总数|(items字段)对应name
used|String|Hbase服务实例已使用的资源数|(items字段)

#### 2.6.2.3报文示例

##### 2.6.2.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/quota/hbase/cc11a764831711e78d91fa163efdbea8/
```

##### 2.6.2.3.2返回报文示例


__response:__

```
{
  "items": [
    {
      "available": "10",
      "desc": "cc11a764831711e78d91fa163efdbea8",
      "name": "maximumTablesQuota",
      "size": "10",
      "used": "0"
    },
    {
      "available": "100",
      "desc": "cc11a764831711e78d91fa163efdbea8",
      "name": "maximumRegionsQuota",
      "size": "100",
      "used": "0"
    }
  ]
}
```

### 2.6.3获取大数据平台kafka服务实例用量（/ocmanager/v2/api/quota/kafka/{topic}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/quota/kafka/oc_ec0fc8e0-8641-11e7-bf73-fa163efdbea8/
    请求方式：GET

#### 2.6.3.1请求参数

##### 2.6.3.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
topic|String|kafka服务实例名|是

#### 2.6.3.2返回参数

##### 2.6.3.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
items|array|项|内容见以下字段
available|String|Kafka服务实例可用资源的数量|items字段
desc|String|描述|items字段
name|String|Kafka服务实例用量名称，其中topicQuota表示KafkaTopic的分区数；partitionSize表示KafkaTopic的每一个分区最大存储容量|items字段
size|String|Kafka服务实例分配资源的总数|(items字段)对应name
used|String|Kafka服务实例已使用的资源数|(items字段)

#### 2.6.3.3报文示例

##### 2.6.3.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/quota/kafka/oc_ec0fc8e0-8641-11e7-bf73-fa163efdbea8/ 
```

##### 2.6.3.3.2返回报文示例


__response:__


```
{
  "items": [
    {
      "available": "0",
      "desc": "oc_ec0fc8e0-8641-11e7-bf73-fa163efdbea8",
      "name": "topicQuota",
      "size": "1",
      "used": "1"
    },
    {
      "available": "-1",
      "desc": "oc_ec0fc8e0-8641-11e7-bf73-fa163efdbea8",
      "name": "partitionSize",
      "size": "-1073741824",
      "used": "0"
    }
  ]
}
```

### 2.6.4获取大数据平台MapReduce服务实例用量（/ocmanager/v2/api/quota/mapreduce/{queuename}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/quota/mapreduce/64647831-1c83-4d09-bdc1-f0494958d8d8/
    请求方式：GET

#### 2.6.4.1请求参数

##### 2.6.4.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
queuename|String|MapReduce的Yarn队列名|是

#### 2.6.4.2返回参数

##### 2.6.4.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
items|array|项|内容见以下字段
available|String|MapReduce服务实例可用资源的数量|items字段
desc|String|描述|items字段
name|String|MapReduce服务实例用量名称，其中yarnQueueQuota表示Yarn队列的最大容量|items字段
size|String|MapReduce服务实例分配资源的总数|(items字段)对应name
used|String|MapReduce服务实例已使用的资源数|(items字段)

#### 2.6.4.3报文示例

##### 2.6.4.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/quota/mapreduce/64647831-1c83-4d09-bdc1-f0494958d8d8/
```

##### 2.6.4.3.2返回报文示例

__response:__


```
{
  "items": [
    {
      "available": "10238",
      "desc": "64647831-1c83-4d09-bdc1-f0494958d8d8",
      "name": "yarnQueueQuota",
      "size": "10238",
      "used": "0"
    }
  ]
}
```

### 2.6.5获取大数据平台Spark服务实例用量（/ocmanager/v2/api/quota/spark/{queuename}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/quota/spark/b798d4da-cccf-4249-8e05-f31deb8baa49/
    请求方式：GET

#### 2.6.5.1请求参数

##### 2.6.5.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
queuename|String|Spark的Yarn队列名|是

#### 2.6.5.2返回参数

##### 2.6.5.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
items|array|项|内容见以下字段
available|String|Spark服务实例可用资源的数量|items字段
desc|String|描述|items字段
name|String|Spark服务实例用量名称，其中yarnQueueQuota表示Yarn队列的最大容量|items字段
size|String|Spark服务实例分配资源的总数|(items字段)对应name
used|String|Spark服务实例已使用的资源数|(items字段)

#### 2.6.5.3报文示例

##### 2.6.5.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/quota/spark/b798d4da-cccf-4249-8e05-f31deb8baa49/
```

##### 2.6.5.3.2返回报文示例

__response:__


```
{
  "items": [
    {
      "available": "10238",
      "desc": "b798d4da-cccf-4249-8e05-f31deb8baa49",
      "name": "yarnQueueQuota",
      "size": "10238",
      "used": "0"
    }
  ]
}
```

### 2.6.6获取大数据平台Hive服务实例用量（/ocmanager/v2/api/quota/hive/{dbname}?queue={queuename}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/quota/hive/1f3aff3d865411e7bf73fa163efdbea8?queue=154157fe-d1b9-4be7-b2e9-92de2969c5a5
    请求方式：GET

#### 2.6.6.1请求参数

##### 2.6.6.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
queuename|String|Hive的Yarn队列名|是
dbname|String|hive数据库名

#### 2.6.6.2返回参数

##### 2.6.6.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
items|array|项|内容见以下字段
available|String|Hive服务实例可用资源的数量|items字段
desc|String|描述|items字段
name|String|Hive服务实例用量名称，其中yarnQueueQuota表示Yarn队列的最大容量;storageSpaceQuota表示Hive数据库的最大存储容量|items字段
size|String|Hive服务实例分配资源的总数|(items字段)对应name
used|String|Hive服务实例已使用的资源数|(items字段)

#### 2.6.6.3报文示例

##### 2.6.6.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/quota/hive/1f3aff3d865411e7bf73fa163efdbea8?queue=154157fe-d1b9-4be7-b2e9-92de2969c5a5
```

##### 2.6.6.3.2返回报文示例

__response:__


```
{
  "items": [
    {
      "available": "1024000000000",
      "desc": "/apps/hive/warehouse/1f3aff3d865411e7bf73fa163efdbea8.db",
      "name": "storageSpaceQuota",
      "size": "1024000000000",
      "used": "0"
    },
    {
      "available": "10238",
      "desc": "154157fe-d1b9-4be7-b2e9-92de2969c5a5",
      "name": "yarnQueueQuota",
      "size": "10238",
      "used": "0"
    }
  ]
}
```

## 2.7 Download cluster hosts file

### 2.7.1下载集群主机列表(hosts 文件)（/ocmanager/v2/api/file/clusterHosts/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/file/clusterHosts/
    请求方式：GET


#### 2.7.1.1报文示例



```
### it should down load the hosts file. for example: you can use curl to download hosts file:
curl -H '{toke key-value}' -o {download path} http://<rest server host >:<rest server port>/ocmanager/v2/api/file/clusterHosts

eg:
curl -H 'token: admin_C805CBA73D3328C8465DC13202FBEA2AC0D341B68D34ED8033E1F81534EE314B' -o /tmp/test/hosts http://10.1.236.95:8080/ocmanager/v2/api/file/clusterHosts
```

## 2.8 Create and download Kerberos keytab and krb5.conf APIs (should configure the KDC server info in the rest server)


### 2.8.1创建Kerberos keytab（/ocmanager/v2/api/kerberos/create/keytab/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/kerberos/create/keytab/
    请求方式：POST

#### 2.8.1.1请求参数

##### 2.8.1.1.1请求参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
krbusername|String|Kerberos keytab名称|是

#### 2.8.1.2返回参数

##### 2.8.1.2.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
message|String|返回的消息
resCode|int|响应返回码|200为正常
status|String|状态|返回创建状态

#### 2.8.1.3报文示例

##### 2.8.1.3.1请求报文示例


__request body:__


```
{
    "krbusername": "zhaoyim"
}
```

##### 2.8.1.3.2返回报文示例




__response:__


```
{
  "message": "zhaoyim.keytab created",
  "resCode": 200,
  "status": "generate keytab successfully!"
}
``` 

### 2.8.2下载Kerberos keytab（/ocmanager/v2/api/kerberos/keytab/{userName}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/kerberos/keytab/zhaoyim/
    请求方式：GET


#### 2.8.2.1报文示例



__response:__

```
### it should down load the keytab file. for example: you can use curl to download the keytab file:
curl -H '{toke key-value}' -o {download path} http://<rest server host >:<rest server port>/ocmanager/v2/api/kerberos/keytab/{userName}

eg:
curl -H 'token: zhaoyim_37205B0412B1F315D54218DABD11A35F50768846069198E609F63F6BCCB7D1CC' -o /tmp/zhaoyim.keytab http://10.1.236.34:8080/ocmanager/v2/api/kerberos/keytab/zhaoyim
```  

### 2.8.3下载krb5.conf（/ocmanager/v2/api/kerberos/krb5/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/kerberos/krb5/
    请求方式：GET


#### 2.8.3.1报文示例


__response:__
```
### it should down load the krb5.conf file. for example: you can use curl to download the keytab file:
curl -H '{toke key-value}' -o {download path} http://<rest server host >:<rest server port>/ocmanager/v2/api/kerberos/krb5

eg:
curl -H 'token: admin_C805CBA73D3328C8465DC13202FBEA2AC0D341B68D34ED8033E1F81534EE314B' -o /tmp/test/krb5.conf http://10.1.236.95:8080/ocmanager/v2/api/kerberos/krb5
```

### 2.8.4 OCM是否开启Kerberos（/ocmanager/v2/api/kerberos/status/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/kerberos/status/
    请求方式：GET


#### 2.8.4.1返回参数

##### 2.8.4.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
ENABLE_KERBEROS|String|OCM是否开启Kerberos|true为已开启

#### 2.8.4.2报文示例

##### 2.8.4.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/kerberos/status/
```

##### 2.8.4.2.2返回报文示例


__response:__


```
{
  "ENABLE_KERBEROS": "true",
}
```  

## 2.9 Get OCManager ldap configuration information APIs

### 2.9.1获取OCM链接ldap的配置信息（/ocmanager/v2/api/ldap/configuration/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/ldap/configuration/
    请求方式：GET


#### 2.9.1.1返回参数

##### 2.9.1.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
LDAP_ADDR|String|LDAP地址
USER_DN_TEMPLATE|String|distinguished name模板

#### 2.9.1.2报文示例

##### 2.9.1.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/ldap/configuration/
```

##### 2.9.1.2.2返回报文示例


__response:__


```
{
  "LDAP_ADDR": "ldap://10.1.236.146:389",
  "USER_DN_TEMPLATE": "uid={0},ou=People,dc=asiainfo,dc=com"
}
```

### 2.9.2 OCM是否开启ldap（/ocmanager/v2/api/ldap/status/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/ldap/status/
    请求方式：GET


#### 2.9.2.1返回参数

##### 2.9.2.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
ENABLE_LDAP|String|OCM是否开启ldap|true为已开启

#### 2.9.2.2报文示例

##### 2.9.2.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/ldap/status/
```

##### 2.9.2.2.2返回报文示例



__response:__


```
{
  "ENABLE_LDAP": "true",
}
```

## 2.10 Get OCManager Ambari configuration and configuration files APIs

### 2.10.1下载ambari yarn cleint configuration files（/ocmanager/v2/api/ambari/yarnclient?filename={filename}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/ambari/yarnclient?filename=yarn/
    请求方式：GET


#### 2.10.1.1报文示例



__response:__
```
### it should down load the ambari yarn cleint configuration files. for example: you can use curl to download the configuration files:
curl -H '{toke key-value}' -o {download path} http://<rest server host >:<rest server port>/ocmanager/v2/api/ambari/yarnclient?filename={filename}

eg:
curl -H 'token: admin_C805CBA73D3328C8465DC13202FBEA2AC0D341B68D34ED8033E1F81534EE314B' -o /tmp/test/yarn.tar.gz http://10.1.236.95:8080/ocmanager/v2/api/ambari/yarnclient?filename=yarn
```

### 2.10.2下载ambari yarn cleint configuration files（/ocmanager/v2/api/ambari/hdfsclient?filename={filename}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/ambari/hdfsclient?filename=hdfs/
    请求方式：GET


#### 2.10.2.1报文示例


__response:__


```
### it should down load the ambari hdfs cleint configuration files. for example: you can use curl to download the configuration files:
curl -H '{toke key-value}' -o {download path} http://<rest server host >:<rest server port>/ocmanager/v2/api/ambari/hdfsclient?filename={filename}

eg:
curl -H 'token: admin_C805CBA73D3328C8465DC13202FBEA2AC0D341B68D34ED8033E1F81534EE314B' -o /tmp/test/hdfs.tar.gz http://10.1.236.95:8080/ocmanager/v2/api/ambari/hdfsclient?filename=hdfs
```

### 2.10.3下载ambari spark cleint configuration files（/ocmanager/v2/api/ambari/sparkclient?filename={filename}/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/ambari/sparkclient?filename=spark/
    请求方式：GET


#### 2.10.3.1报文示例


__response:__
```
### it should down load the ambari spark cleint configuration files. for example: you can use curl to download the configuration files:
curl -H '{toke key-value}' -o {download path} http://<rest server host >:<rest server port>/ocmanager/v2/api/ambari/sparkclient?filename={filename}

eg:
curl -H 'token: admin_C805CBA73D3328C8465DC13202FBEA2AC0D341B68D34ED8033E1F81534EE314B' -o /tmp/test/spark.tar.gz http://10.1.236.95:8080/ocmanager/v2/api/ambari/sparkclient?filename=spark
```

## 2.11 Get OCManager metrics APIs

### 2.11.1获取Kafka serviceName（/ocmanager/v2/api/metrics/kafka/serviceName/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/metrics/kafka/serviceName/
    请求方式：GET


#### 2.11.1.1返回参数

##### 2.11.1.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
oc.kafka.serviceName|String|返回Kafka的serviceName

#### 2.11.1.2报文示例

##### 2.11.1.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/metrics/kafka/serviceName/
```

##### 2.11.1.2.2返回报文示例


__response:__


```
{
    "oc.kafka.serviceName": "ocdp"
}
```

### 2.11.2获取RM连接信息（/ocmanager/v2/api/metrics/resourcemanager/addresses/） 
	
	示例：http://127.0.0.1:8080/ocmanager/v2/api/metrics/resourcemanager/addresses/
    请求方式：GET


#### 2.11.2.1返回参数

##### 2.11.2.1.1基本参数

字段|类型|描述|备注
----------|----------------|----|------------|
RM_ADDR|String|ResourceManager连接地址

#### 2.11.2.2报文示例

##### 2.11.2.2.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/metrics/resourcemanager/addresses/
```

##### 2.11.2.2.2返回报文示例


__response:__
```
{
    "RM_ADDR": "aicloud1.asiainfo.com:8088,aicloud2.asiainfo.com:8088"
}
```

## 2.12 Dashboard Links APIs （此部分为内部使用，外部用户可忽略）
### 2.12.1添加多租户平台首页连接
```
POST /ocmanager/v2/api/dashboard/link
```

__request body:__
```
{
  "blank": true,
  "description": "管理入口",
  "href": "#/console/tenant",
  "imageUrl": "home_tenant",
  "name": "多租户管理平台"
}
```
字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
blank|boolean|是否空白|是|true为空白,false为非空白
description|String|多租户平台首页描述|否
href|String|首页链接|是|
imageUrl|String|图片|是|
name|String|多租户平台名称|是|


__response:__
```
{
  "message": "Add successfully",
  "resCode": 200,
  "status": "successful"
}
``` 
字段|类型|描述|备注
----------|----------------|----|------------|
message|String|返回的消息|添加成功
resCode|int|响应返回码|200为正常
status|String|状态|返回添加状态
### 2.12.2获取多租户平台首页所有连接
```
GET /ocmanager/v2/api/dashboard/link
```

__response:__
```
[
  {
    "blank": true,
    "description": "管理入口",
    "href": "#/console/tenant",
    "id": 15,
    "imageUrl": "home_tenant",
    "name": "多租户管理平台"
  },
  ...
]
```
字段|类型|描述|备注
----------|----------------|----|------------|
blank|boolean|是否空白|true为空白,false为非空白
description|String|多租户平台首页描述
href|String|首页链接|
id|int|链接id
imageUrl|String|图片|
name|String|多租户平台名称|
### 2.12.3获取多租户平台首页连接通过连接名
```
GET /ocmanager/v2/api/dashboard/link/{name}
```

__response:__
```
{
  "blank": true,
  "description": "管理入口",
  "href": "#/console/tenant",
  "id": 15,
  "imageUrl": "home_tenant",
  "name": "多租户管理平台"
}
```
字段|类型|描述|备注
----------|----------------|----|------------|
blank|boolean|是否空白|true为空白,false为非空白
description|String|多租户平台首页描述
href|String|首页链接|
id|int|链接id
imageUrl|String|图片|
name|String|多租户平台名称|

### 2.12.4更新多租户平台首页连接通过id
```
PUT /ocmanager/v2/api/dashboard/link/{id}
```

__request body:__
```
{
  "blank": false,
  "description": "管理入口_change",
  "href": "#/console/tenant/change",
  "imageUrl": "home_tenant_change",
  "name": "多租户管理平台_change"
}
```
字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
blank|boolean|是否空白|是|true为空白,false为非空白
description|String|多租户平台首页描述|否
href|String|首页链接|是|
imageUrl|String|图片|是|
name|String|多租户平台名称|是|


__response:__
```
{
  "message": "Update successfully",
  "resCode": 200,
  "status": "successful"
}
``` 
字段|类型|描述|备注
----------|----------------|----|------------|
message|String|返回的消息|更新成功
resCode|int|响应返回码|200为正常
status|String|状态|返回更新状态
### 2.12.5删除多租户平台首页连接通过连接名
```
DELETE /ocmanager/v2/api/dashboard/link/{id}
```

__response:__
```
{
  "message": "Delete successfully",
  "resCode": 200,
  "status": "successful"
}
```
字段|类型|描述|备注
----------|----------------|----|------------|
message|String|返回的消息|删除成功
resCode|int|响应返回码|200为正常
status|String|状态|返回删除状态

