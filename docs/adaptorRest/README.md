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

8. 修改用户密码
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
    "description": "hdfs description",
    "id": "100",
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
  "description": "hdfs description",
  "id": "100",
  "origin": "ocdp",
  "servicename": "hdfs"
}
```

3. 添加Service broker（添加服务， 服务是注册在service broker 里面的，，因此会添加service broker 中注册的所有服务）
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

4. 删除Service broker（删除服务， 服务是注册在service broker 里面的，因此会删除service broker 中注册的所有服务）
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
    "resourceVersion": "17209829"
  },
  "items": [
    {
      "metadata": {
        "name": "Cassandra",
        "generateName": "etcd",
        "namespace": "openshift",
        "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Cassandra",
        "uid": "38fa4221-cd9f-11e6-b10e-4e10dba0edae",
        "resourceVersion": "6202322",
        "creationTimestamp": "2016-12-29T08:17:22Z",
        "labels": {
          "asiainfo.io/servicebroker": "etcd"
        }
      },
      "spec": {
        "name": "Cassandra",
        "id": "3D7D7D07-D704-4B22-B492-EE5AE5301A55",
        "description": "A Sample Cassandra (v3.4) cluster on Openshift",
        "bindable": true,
        "plan_updateable": false,
        "tags": [
          "cassandra",
          "openshift"
        ],
        "requires": null,
        "metadata": {
          "displayName": "Cassandra",
          "documentationUrl": "https://wiki.apache.org/cassandra/GettingStarted",
          "imageUrl": "https://cassandra.apache.org/media/img/cassandra_logo.png",
          "longDescription": "Managed, highly available cassandra clusters in the cloud.",
          "providerDisplayName": "Asiainfo",
          "supportUrl": "https://cassandra.apache.org/"
        },
        "plans": [
          {
            "name": "standalone",
            "id": "7B7EC041-2090-4ACB-AE0F-E8BDF315A778",
            "description": "HA Cassandra on Openshift",
            "metadata": {
              "bullets": [
                "20 GB of Disk",
                "20 connections"
              ],
              "costs": null,
              "displayName": "Shared and Free",
              "customize": null
            },
            "free": true
          }
        ],
        "dashboard_client": null
      },
      "status": {
        "phase": "Inactive"
      }
    },
    {
      "metadata": {
        "name": "liuxu",
        "generateName": "liuxu",
        "namespace": "openshift",
        "selfLink": "/oapi/v1/namespaces/openshift/backingservices/liuxu",
        "uid": "95468dec-28c4-11e7-9b96-fa163efdbea8",
        "resourceVersion": "7495189",
        "creationTimestamp": "2017-04-24T08:04:04Z",
        "labels": {
          "asiainfo.io/servicebroker": "liuxu"
        }
      },
      "spec": {
        "name": "Greenplum",
        "id": "98E2AFE3-7279-40CA-B04E-74276B3FF4B2",
        "description": "Greenplumæ¯Pivotalå¼æºçMPPæ°æ®åºã",
        "bindable": true,
        "plan_updateable": false,
        "tags": [
          "Greenplum",
          "mpp",
          "database"
        ],
        "requires": null,
        "metadata": {
          "displayName": "Greenplum",
          "documentationUrl": "http://gpdb.docs.pivotal.io",
          "imageUrl": "pub/assets/Greenplum.png",
          "longDescription": "The First Open SourceMassively Parallel Data Warehouse",
          "providerDisplayName": "Asiainfo",
          "supportUrl": "http://greenplum.org"
        },
        "plans": [
          {
            "name": "Experimental",
            "id": "B48A3972-536F-47A6-B04F-A5344F4DC5E0",
            "description": "åç¬Greenplumå®ä¾",
            "metadata": {
              "bullets": [
                "20 GB of Disk",
                "20 connections"
              ],
              "costs": null,
              "displayName": "Shared and Free",
              "customize": null
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


### Service Instance APIs
1. 获取多租户平台所有服务实例
```
GET /ocmanager/v1/api/service/all/instances
```
__response:__
```
[
  {
    "id": "21aeba68-57f2-11e7-9a0f-fa163efdbea8",
    "instanceName": "hive-instance001",
    "quota": "{\"hiveStorageQuota\":\"1024\",\"yarnQueueQuota\":\"10\"}",
    "serviceTypeId": "",
    "serviceTypeName": "Hive",
    "tenantId": "zhaoyim"
  },
    ......
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
    "description": "admin description",
    "id": "1",
    "rolename": "admin"
  },
    ......
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
    "id": "09367148-c72a-413f-b1de-5a23b566d808",
    "name": "createTenant001",
    "description": "create tenant 001",
    "parentId": "f7f281ee-a544-4636-9341-2db50c491b96"
}
```


__response:__
```
{
  "kind": "Project",
  "apiVersion": "v1",
  "metadata": {
    "name": "09367148-c72a-413f-b1de-5a23b566d808",
    "selfLink": "/oapi/v1/projectrequests/09367148-c72a-413f-b1de-5a23b566d808",
    "uid": "d24eaab4-51d5-11e7-9b16-fa163efdbea8",
    "resourceVersion": "16511194",
    "creationTimestamp": "2017-06-15T14:20:45Z",
    "annotations": {
      "openshift.io/description": "create tenant 001",
      "openshift.io/display-name": "createTenant001",
      "openshift.io/requester": "system:serviceaccount:default:ocm",
      "openshift.io/sa.scc.mcs": "s0:c16,c10",
      "openshift.io/sa.scc.supplemental-groups": "1000260000/10000",
      "openshift.io/sa.scc.uid-range": "1000260000/10000"
    }
  },
  "spec": {
    "finalizers": [
      "openshift.io/origin",
      "kubernetes"
    ]
  },
  "status": {
    "phase": "Active"
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
    "description": "child tenant",
    "id": "5a6c16a9-0c85-42da-aec3-8ac1f5532fe1",
    "name": "ChildTenant",
    "parentId": "f7f281ee-a544-4636-9341-2db50c491b96",
    "level": 2,
    "dacpTeamCode": 2
  },
  {
    "description": "root tenant",
    "id": "f7f281ee-a544-4636-9341-2db50c491b96",
    "name": "rootTenant",
    "level": 1,
    "dacpTeamCode": 1
  }
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
  "description": "child tenant",
  "id": "5a6c16a9-0c85-42da-aec3-8ac1f5532fe1",
  "name": "ChildTenant",
  "parentId": "f7f281ee-a544-4636-9341-2db50c491b96",
  "level": 2,
  "dacpTeamCode": 2
}

```

4. 获取租户所有子租户
```
GET /ocmanager/v1/api/tenant/{id}/children
```
__response:__
```
[
  {
    "description": "child tenant",
    "id": "5a6c16a9-0c85-42da-aec3-8ac1f5532fe1",
    "name": "ChildTenant",
    "parentId": "f7f281ee-a544-4636-9341-2db50c491b96",
    "level": 2,
    "dacpTeamCode": 2
  }
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
      "name":"ETCD-instance017"
    },
  "spec":
    {
      "provisioning":
        {
          "backingservice_name":"ETCD",
          "backingservice_plan_guid":"204F8288-F8D9-4806-8661-EB48D94504B3",
          "parameters":{"ETCDStorageQuota":"1024","ETCDQueueQuota":"10"}
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
    "name": "ETCD-instance017",
    "namespace": "zhaoyim",
    "selfLink": "/oapi/v1/namespaces/zhaoyim/backingserviceinstances/ETCD-instance017",
    "uid": "e45783a5-5240-11e7-8905-fa163efdbea8",
    "resourceVersion": "16574723",
    "creationTimestamp": "2017-06-16T03:07:12Z"
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "ETCD",
      "backingservice_spec_id": "",
      "backingservice_plan_guid": "204F8288-F8D9-4806-8661-EB48D94504B3",
      "backingservice_plan_name": "",
      "parameters": null,
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
    "phase": "Provisioning",
    "action": "",
    "last_operation": null
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
    "id": "e45783a5-5240-11e7-8905-fa163efdbea8",
    "instanceName": "ETCD-instance017",
    "serviceTypeId": "",
    "serviceTypeName": "ETCD",
    "tenantId": "zhaoyim"
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
    "name": "ETCD-instance017",
    "namespace": "zhaoyim",
    "selfLink": "/oapi/v1/namespaces/zhaoyim/backingserviceinstances/ETCD-instance017",
    "uid": "e45783a5-5240-11e7-8905-fa163efdbea8",
    "resourceVersion": "16575264",
    "creationTimestamp": "2017-06-16T03:07:12Z",
    "deletionTimestamp": "2017-06-16T03:12:13Z"
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "ETCD",
      "backingservice_spec_id": "",
      "backingservice_plan_guid": "204F8288-F8D9-4806-8661-EB48D94504B3",
      "backingservice_plan_name": "",
      "parameters": null,
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
    "phase": "Provisioning",
    "action": "_ToDelete",
    "last_operation": null
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
    "userId": "user1",
    "roleId": "role1"
}
```


__response:__
```
{
  "roleId": "role1",
  "tenantId": "tenant1",
  "userId": "user1"
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
    "roleId": "r1",
    "roleName": "r1",
    "tenantId": "t1",
    "userDescription": "u1 description",
    "userEmail": "a@163.com",
    "userId": "u1",
    "userName": "u1",
    "userPassword": "passw0rd",
    "userPhone": "123"
  },,
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
    "userId": "user1",
    "roleId": "role2"
}
```


__response:__
```
{
  "roleId": "role2",
  "tenantId": "tenant1",
  "userId": "user1"
}
``` 


11. 解除租户，用户和角色的绑定
```
DELETE /ocmanager/v1/api/tenant/{id}/user/{userId}/role/assignment
```
__response:__
```
{
  "message": "user1",
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
  "kind": "Status",
  "apiVersion": "v1",
  "metadata": {},
  "status": "Success",
  "code": 200
}
```


13. 获取服务实例访问信息（只有服务实例状态是Active的时候才会有访问信息）
```
GET /ocmanager/v1/api/tenant/{tenantId}/service/instance/{serviceInstanceName}/access/info
```
__response:__
```
{
  "kind": "BackingServiceInstance",
  "apiVersion": "v1",
  "metadata": {
    "name": "ETCD-instance014",
    "namespace": "zhaoyim",
    "selfLink": "/oapi/v1/namespaces/zhaoyim/backingserviceinstances/ETCD-instance014",
    "uid": "fae1f410-50ee-11e7-87b1-fa163efdbea8",
    "resourceVersion": "16363936",
    "creationTimestamp": "2017-06-14T10:48:19Z"
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "ETCD",
      "backingservice_spec_id": "",
      "backingservice_plan_guid": "204F8288-F8D9-4806-8661-EB48D94504B3",
      "backingservice_plan_name": "",
      "parameters": null,
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
    "phase": "Provisioning",
    "action": "",
    "last_operation": null
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
            "hiveStorageQuota": "99999",
            "yarnQueueQuota": "88888"
        }
}
```


__response:__
```
{
  "kind": "BackingServiceInstance",
  "apiVersion": "v1",
  "metadata": {
    "name": "hive-instance",
    "namespace": "zhaoyim",
    "selfLink": "/oapi/v1/namespaces/zhaoyim/backingserviceinstances/hive-instance",
    "uid": "f1a993d6-57c8-11e7-9a0f-fa163efdbea8",
    "resourceVersion": "17465724",
    "creationTimestamp": "2017-06-23T04:03:41Z"
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "Hive",
      "backingservice_spec_id": "2ef26018-003d-4b2b-b786-0481d4ee9fa3",
      "backingservice_plan_guid": "aa7e364f-fdbf-4187-b60a-218b6fa398ed",
      "backingservice_plan_name": "shared",
      "parameters": {
            "hiveStorageQuota": "99999",
            "yarnQueueQuota": "88888"
      },
      "credentials": {
        "Hive database": "14ea4d0557c911e79a0ffa163efdbea8",
        "host": "zx-dn-03",
        "password": "e6510d82-2ed3-48f5-8668-b82d572aaac1",
        "port": "10000",
        "uri": "jdbc:hive2://zx-dn-03:10000/14ea4d0557c911e79a0ffa163efdbea8;principal=hive/zx-dn-03@EXAMPLE.COM",
        "username": "zhaoyim@EXAMPLE.COM"
      }
    },
    "userprovidedservice": {
      "credentials": null
    },
    "binding": null,
    "bound": 0,
    "instance_id": "14ea4d05-57c9-11e7-9a0f-fa163efdbea8",
    "tags": null
  },
  "status": {
    "phase": "Unbound",
    "action": "",
    "last_operation": null
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
  "roleId": "r1Id",
  "tenantId": "t1Id",
  "userId": "u1Id",
  "userName": "u1Name"
}
```


### Single Sign on APIs
1. 获取当前用户(所要用户信息需要设置在request header中)
```
GET /ocmanager/v1/api/sso/user
```
__response:__
```
{
  "http_x_proxy_cas_email": "",
  "http_x_proxy_cas_loginname": "user1",
  "http_x_proxy_cas_mobile": "",
  "http_x_proxy_cas_userid": "",
  "http_x_proxy_cas_username": "user1",
  "admin": false,
}
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

