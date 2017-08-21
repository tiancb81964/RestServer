## OCManager Adapter REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_


### Authentication APIs

1. 用户认证
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


2. 用户注销
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

3. 获取认证类型 (0:ldap, 1:mysql)
```
GET -H 'token:u1_06834FF564D57A53B88B0A64A02584BE24ED8E2954BBBCB935E88EA777BD77D3' ocmanager/v1/api/authc/type
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



### Users APIs

1. 获取所有用户
```
GET /ocmanager/v1/api/user
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
GET /ocmanager/v1/api/user/id/{id}
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
GET /ocmanager/v1/api/user/name/{userName}
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
POST /ocmanager/v1/api/user
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
PUT /ocmanager/v1/api/user/id/{id}
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
PUT /ocmanager/v1/api/user/name/{userName}
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
DELETE /ocmanager/v1/api/user/{id}
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
PUT /ocmanager/v1/api/user/{userName}/password
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
GET /ocmanager/v1/api/user/is/admin/{userName}
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
GET /ocmanager/v1/api/user/name/{name}/all/tenants
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
GET /ocmanager/v1/api/user/id/{id}/all/tenants
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
GET /ocmanager/v1/api/user/name/{name}/tenant/{tenantId}/children/tenants
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
GET /ocmanager/v1/api/user/id/{id}/tenant/{tenantId}/children/tenants
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
GET /ocmanager/v1/api/user/with/tenants
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
GET /ocmanager/v1/api/user/{id}/with/tenants
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
GET /ocmanager/v1/api/user/ldap
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
GET /ocmanager/v1/api/user/name/{userName}/tenant/{tenantId}/assignments/info
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




### Services APIs
1. 获取所有服务
```
GET /ocmanager/v1/api/service
```
__response:__
```
[
  {
    "description": "A Hadoop hbase service broker implementation",
    "id": "d9845ade-9410-4c7f-8689-4e032c1a8450",
    "origin": "ocdp",
    "servicename": "hbase"
  },
  {
    "description": "A Hadoop hdfs service broker implementation",
    "id": "ae67d4ba-5c4e-4937-a68b-5b47cfe356d8",
    "origin": "ocdp",
    "servicename": "hdfs"
  },
  ...
]
```

2. 获取单个服务
```
GET /ocmanager/v1/api/service/{id}
```
__response:__
```
{
  "description": "A Hadoop hbase service broker implementation",
  "id": "d9845ade-9410-4c7f-8689-4e032c1a8450",
  "origin": "ocdp",
  "servicename": "hbase"
}
```

3. 添加Service broker（添加服务,服务是注册在service broker 里面的,因此会添加service broker 中注册的所有服务）
```
POST /ocmanager/v1/api/service/broker
```
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

4. 删除Service broker（删除服务,服务是注册在service broker 里面的,因此会删除service broker 中注册的所有服务）
```
DELETE /ocmanager/v1/api/service/broker/{name}
```

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


5. 获取Data Foundry服务列表
```
GET /ocmanager/v1/api/service/df
```
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
          "supportUrl": "http://hbase.apache.org/book.html"
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


6. 获取多租户平台上所有服务实例列表
```
GET /ocmanager/v1/api/service/all/instances
```
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
    "tenantId": "zhaoyim-1502869020"
  },
  {
    "id": "158dcc32-8090-11e7-8e31-fa163efdbea8",
    "instanceName": "HDFS-admin-1502673613",
    "serviceTypeId": "",
    "serviceTypeName": "HDFS",
    "status": "Failure",
    "tenantId": "86c62459-7c04-11e7-aa10-fa163ed7d0ae"
  },
  ...
]  
```


7. 获取指定服务的plan套餐
```
GET /ocmanager/v1/api/service/{serviceName}/plan
```
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




### Roles APIs
1. 获取所有服务角色
```
GET /ocmanager/v1/api/role
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


### Tenants APIs
1. 创建租户
```
POST /ocmanager/v1/api/tenant
```

__request body:__
```
{
    "id": "09367148-c72a-413f-b1de-5a23b566d809",
    "name": "createTenant001",
    "description": "create tenant 001",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae"
}
```


__response:__
```
{
  "dataFoundryInfo": "{\"kind\":\"Project\",\"apiVersion\":\"v1\",\"metadata\":{\"name\":\"09367148-c72a-413f-b1de-5a23b566d809\",\"selfLink\":\"/oapi/v1/projectrequests/09367148-c72a-413f-b1de-5a23b566d809\",\"uid\":\"9767c526-8622-11e7-bf73-fa163efdbea8\",\"resourceVersion\":\"22589952\",\"creationTimestamp\":\"2017-08-21T03:41:18Z\",\"annotations\":{\"openshift.io/description\":\"create tenant 001\",\"openshift.io/display-name\":\"createTenant001\",\"openshift.io/requester\":\"system:serviceaccount:default:ocm\",\"openshift.io/sa.scc.mcs\":\"s0:c11,c5\",\"openshift.io/sa.scc.supplemental-groups\":\"1000120000/10000\",\"openshift.io/sa.scc.uid-range\":\"1000120000/10000\"}},\"spec\":{\"finalizers\":[\"openshift.io/origin\",\"kubernetes\"]},\"status\":{\"phase\":\"Active\"}}\n",
  "databaseInfo": {
    "description": "create tenant 001",
    "id": "09367148-c72a-413f-b1de-5a23b566d809",
    "level": 0,
    "name": "createTenant001",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae"
  }
}
``` 



2. 获取所有租户
```
GET /ocmanager/v1/api/tenant
```
__response:__
```
[
  {
    "description": "create tenant 001",
    "id": "09367148-c72a-413f-b1de-5a23b566d809",
    "level": 0,
    "name": "createTenant001",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae"
  },
  {
    "description": "root tenant",
    "id": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
    "level": 1,
    "name": "root.tenant"
  },
  ...
]
```


3. 获取单个租户
```
GET /ocmanager/v1/api/tenant/{id}
```
__response:__
```
{
  "description": "root tenant",
  "id": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
  "level": 1,
  "name": "root.tenant"
}

```

4. 获取指定租户的所有子租户
```
GET /ocmanager/v1/api/tenant/{id}/children
```
__response:__
```
[
  {
    "description": "create tenant 001",
    "id": "09367148-c72a-413f-b1de-5a23b566d809",
    "level": 0,
    "name": "createTenant001",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae"
  },
  {
    "description": "zhaoyimLevel1",
    "id": "51cadf67-7b37-11e7-aa10-fa163ed7d0ae",
    "level": 2,
    "name": "zhaoyimLevel1",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae"
  },
  ...
]

```



5. 在租户下创建服务实例
```
POST /ocmanager/v1/api/tenant/{id}/service/instance
```

__request body:__
```
{
  "kind":"BackingServiceInstance",
  "apiVersion":"v1",
  "metadata":
    {
      "name":"zhaoyim_kafka_instance777"
    },
  "spec":
    {
      "provisioning":
        {
          "backingservice_name":"Kafka",
          "backingservice_plan_guid":"68ee85c2-5b1a-4f51-89e9-5b111c251f0d",
          "parameters":{"partitionSize":"10","topicQuota":"10","topicTTL":"10"}
        }
    }
}
```


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
    "resourceVersion": "22590366",
    "creationTimestamp": "2017-08-21T03:58:20Z"
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "Kafka",
      "backingservice_spec_id": "",
      "backingservice_plan_guid": "68ee85c2-5b1a-4f51-89e9-5b111c251f0d",
      "backingservice_plan_name": "",
      "parameters": {
        "partitionSize": "10",
        "topicQuota": "10",
        "topicTTL": "10"
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


6. 获取租户下所有服务实例
```
GET /ocmanager/v1/api/tenant/{id}/service/instances
```
__response:__
```
[
  {
    "id": "f8be2b3f-8624-11e7-bf73-fa163efdbea8",
    "instanceName": "zhaoyim_kafka_instance777",
    "quota": "{\"instance_id\":\"f8be2b3f-8624-11e7-bf73-fa163efdbea8\",\"partitionSize\":\"10\",\"topicQuota\":\"10\",\"topicTTL\":\"10\"}",
    "serviceTypeId": "",
    "serviceTypeName": "Kafka",
    "status": "Unbound",
    "tenantId": "09367148-c72a-413f-b1de-5a23b566d809"
  },
  ...
]
```


7. 删除租户下某个服务实例
```
DELETE /ocmanager/v1/api/tenant/{id}/service/instance/{instanceName}
```
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


8. 绑定租户，用户和角色
```
POST /ocmanager/v1/api/tenant/{id}/user/role/assignment
```

__request body:__
```
{
    "userId": "011ea988-abc2-4267-9215-cacf111716d1",
    "roleId": "a13dd087-524a-11e7-9dbb-fa163ed7d0ae"
}
```


__response:__
```
{
  "roleId": "a13dd087-524a-11e7-9dbb-fa163ed7d0ae",
  "tenantId": "09367148-c72a-413f-b1de-5a23b566d809",
  "userId": "011ea988-abc2-4267-9215-cacf111716d1"
}
``` 


9. 获取租户下所有用户以及用户角色
```
GET /ocmanager/v1/api/tenant/{id}/users
```
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


10. 更新租户中用户的角色
```
PUT /ocmanager/v1/api/tenant/{id}/user/role/assignment
```

__request body:__
```
{
    "userId": "011ea988-abc2-4267-9215-cacf111716d1",
    "roleId": "a12a84d0-524a-11e7-9dbb-fa163ed7d0ae"
}
```


__response:__
```
{
  "roleId": "a12a84d0-524a-11e7-9dbb-fa163ed7d0ae",
  "tenantId": "09367148-c72a-413f-b1de-5a23b566d809",
  "userId": "011ea988-abc2-4267-9215-cacf111716d1"
}
``` 


11. 解除租户，用户和角色的绑定
```
DELETE /ocmanager/v1/api/tenant/{id}/user/{userId}/role/assignment
```
__response:__
```
{
  "message": "011ea988-abc2-4267-9215-cacf111716d1",
  "resCodel": 200,
  "status": "delete success"
}
``` 


12. 删除租户
```
DELETE /ocmanager/v1/api/tenant/{id}
```
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


13. 获取服务实例访问信息
```
GET /ocmanager/v1/api/tenant/{tenantId}/service/instance/{serviceInstanceName}/access/info
```
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

14. 更新租户单个服务实例
```
PUT /ocmanager/v1/api/tenant/{id}/service/instance/{instanceName}
```

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


15. 获取角色根据租户和用户名
```
GET /ocmanager/v1/api/tenant/{tenantId}/user/{userName}/role
```
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


### OCDP service instances quota APIs
1. 获取大数据平台HDFS服务实例用量
```
GET /ocmanager/v1/api/quota/hdfs?path={HDFS Path}
```
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

2. 获取大数据平台Hbase服务实例用量
```
GET /ocmanager/v1/api/quota/hbase/{HBaseNameSpace}
```
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


3. 获取大数据平台kafka服务实例用量
```
GET /ocmanager/v1/api/quota/kafka/{topic}
```
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


4. 获取大数据平台MapReduce服务实例用量
```
GET /ocmanager/v1/api/quota/mapreduce/{queuename}
```
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


5. 获取大数据平台Spark服务实例用量
```
GET /ocmanager/v1/api/quota/spark/{queuename}
```
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


6. 获取大数据平台Hive服务实例用量
```
GET /ocmanager/v1/api/quota/hive/{dbname}?queue={queuename}
```
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


### Create and download Kerberos keytab APIs (should configure the KDC server info in the rest server)
1. 创建Kerberos keytab
```
POST /ocmanager/v1/api/kerberos/create/keytab
```

__request body:__
```
{
    "krbusername": "zhaoyim",
    "krbpassword": "zhaoyim"
}
```


__response:__
```
{
  "message": "zhaoyim.keytab created",
  "resCodel": 200,
  "status": "generate keytab successfully!"
}
``` 

2. 下载Kerberos keytab
```
GET /ocmanager/v1/api/kerberos/keytab/{userName}
```

__response:__
```
### it should down load the keytab file. for example: you can use curl to download the keytab file:
curl -H '{toke key-value}' -o {download path} http://<rest server host >:<rest server port>/ocmanager/v1/api/kerberos/keytab/{userName}

eg:
curl -H 'token: zhaoyim_37205B0412B1F315D54218DABD11A35F50768846069198E609F63F6BCCB7D1CC' -o /tmp/zhaoyim.keytab http://10.1.236.34:8080/ocmanager/v1/api/kerberos/keytab/zhaoyim
``` 



### Dashboard Links APIs
1. 添加多租户平台首页连接
```
POST /ocmanager/v1/api/dashboard/link
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


__response:__
```
{
  "message": "Add successfully",
  "resCodel": 200,
  "status": "successful"
}
``` 

2. 获取多租户平台首页所有连接
```
GET /ocmanager/v1/api/dashboard/link
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

3. 获取多租户平台首页连接通过连接名
```
GET /ocmanager/v1/api/dashboard/link/{name}
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

4. 更新多租户平台首页连接通过id
```
PUT /ocmanager/v1/api/dashboard/link/{id}
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


__response:__
```
{
  "message": "Update successfully",
  "resCodel": 200,
  "status": "successful"
}
``` 

5. 删除多租户平台首页连接通过连接名
```
DELETE /ocmanager/v1/api/dashboard/link/{id}
```
__response:__
```
{
  "message": "Delete successfully",
  "resCodel": 200,
  "status": "successful"
}
```

