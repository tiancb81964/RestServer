## CM Instance REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_

### Instance APIs

1. 获取某个租户下所有服务实例
```
GET /ocmanager/v2/api/instance/service/tenant/{tenantId}
```
__response:__
```
[
    {
      "attributes": "{}",
      "category": "service",
      "cuzBsiName": "zhaoyim12345",
      "id": "6c7dae3e-6edd-11e8-ac16-fa163e4dfe45",
      "instanceName": "zhaoyim12345",
      "quota": "{\"cuzBsiName\":\"zhaoyim12345\",\"instance_id\":\"6c7dae3e-6edd-11e8-ac16-fa163e4dfe45\",\"nameSpaceQuota\":\"1\",\"storageSpaceQuota\":\"1\"}",
      "serviceId": "",
      "serviceName": "HDFS",
      "serviceType": "hdfs",
      "status": "Unbound",
      "tenantId": "000999"
    }, 
...
]
```

2. 获取某个租户下所有工具实例
```
GET /ocmanager/v2/api/instance/tool/tenant/{tenantId}
```
__response:__
```
[
    {
      "attributes": "{}",
      "category": "tool",
      "cuzBsiName": "zhaoyim1234567",
      "id": "56b518d4-6edf-11e8-ac16-fa163e4dfe45",
      "instanceName": "zhaoyim1234567",
      "quota": "{\"cuzBsiName\":\"zhaoyim1234567\",\"instance_id\":\"56b518d4-6edf-11e8-ac16-fa163e4dfe45\",\"nameSpaceQuota\":\"1\",\"storageSpaceQuota\":\"1\"}",
      "serviceId": "",
      "serviceName": "HDFS",
      "serviceType": "hdfs",
      "status": "Unbound",
      "tenantId": "000999"
    }
...
]
```

3. 创建实例（服务和工具）
```
POST /ocmanager/v2/api/tenant/{tenantId}/service/instance
```
__request body:__
```
{
  "kind":"BackingServiceInstance",
  "apiVersion":"v1",
  "metadata":
    {
      "name": "zhaoyim1234567"
    },
  "spec":
    {
      "provisioning":
        {
          "backingservice_name":"HDFS",
          "backingservice_plan_guid":"72150b09-1025-4533-8bae-0e04ef68ac13",
          "parameters":{"nameSpaceQuota":"1","storageSpaceQuota":"1", "cuzBsiName": "zhaoyim1234567"}
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
    "name": "zhaoyim123456789",
    "namespace": "000999",
    "selfLink": "/oapi/v1/namespaces/000999/backingserviceinstances/zhaoyim123456789",
    "uid": "d0d2330a-6fc3-11e8-ac16-fa163e4dfe45",
    "resourceVersion": "1223748",
    "creationTimestamp": "2018-06-14T11:12:24Z"
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "HDFS",
      "backingservice_spec_id": "",
      "backingservice_plan_guid": "72150b09-1025-4533-8bae-0e04ef68ac13",
      "backingservice_plan_name": "",
      "parameters": {
        "cuzBsiName": "zhaoyim123456789",
        "nameSpaceQuota": "1",
        "storageSpaceQuota": "1"
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

4. 更新实例（服务和工具）
```
POST /ocmanager/v2/api/tenant/{tenantId}/service/instance/{instanceName}
```

__request body:__
```
{
    "parameters":
        {
            "nameSpaceQuota": "2",
            "storageSpaceQuota": "2"
        }
}
```

__response:__
```
{
  "kind": "BackingServiceInstance",
  "apiVersion": "v1",
  "metadata": {
    "name": "zhaoyim123456789",
    "namespace": "000999",
    "selfLink": "/oapi/v1/namespaces/000999/backingserviceinstances/zhaoyim123456789",
    "uid": "d0d2330a-6fc3-11e8-ac16-fa163e4dfe45",
    "resourceVersion": "1223818",
    "creationTimestamp": "2018-06-14T11:12:24Z",
    "annotations": {
      "datafoundry.io/servicebroker": "cluster61"
    }
  },
  "spec": {
    "provisioning": {
      "dashboard_url": "",
      "backingservice_name": "HDFS",
      "backingservice_spec_id": "ae67d4ba-5c4e-4937-a68b-5b47cfe356d9",
      "backingservice_plan_guid": "72150b09-1025-4533-8bae-0e04ef68ac13",
      "backingservice_plan_name": "shared",
      "parameters": {
        "nameSpaceQuota": "2",
        "storageSpaceQuota": "2"
      },
      "credentials": {
        "HDFS_Path": "zhaoyim123456789",
        "host": "10.1.236.61",
        "port": "50070",
        "uri": "http://10.1.236.61:50070/webhdfs/v1zhaoyim123456789"
      }
    },
    "userprovidedservice": {
      "credentials": null
    },
    "binding": null,
    "bound": 0,
    "instance_id": "d0d2faac-6fc3-11e8-ac16-fa163e4dfe45",
    "tags": null
  },
  "status": {
    "phase": "Unbound",
    "patch": "Update"
  }
}
```

5. 删除实例（服务和工具）
```
DELETE /ocmanager/v2/api/tenant/{tenantId}/service/instance/{instanceName}
```


__response:__
```
{
  "message": "{\"kind\":\"BackingServiceInstance\",\"apiVersion\":\"v1\",\"metadata\":{\"name\":\"zhaoyim123456789\",\"namespace\":\"000999\",\"selfLink\":\"/oapi/v1/namespaces/000999/backingserviceinstances/zhaoyim123456789\",\"uid\":\"d0d2330a-6fc3-11e8-ac16-fa163e4dfe45\",\"resourceVersion\":\"1223853\",\"creationTimestamp\":\"2018-06-14T11:12:24Z\",\"deletionTimestamp\":\"2018-06-14T11:22:00Z\",\"annotations\":{\"datafoundry.io/servicebroker\":\"cluster61\"}},\"spec\":{\"provisioning\":{\"dashboard_url\":\"\",\"backingservice_name\":\"HDFS\",\"backingservice_spec_id\":\"ae67d4ba-5c4e-4937-a68b-5b47cfe356d9\",\"backingservice_plan_guid\":\"72150b09-1025-4533-8bae-0e04ef68ac13\",\"backingservice_plan_name\":\"shared\",\"parameters\":{\"nameSpaceQuota\":\"2\",\"storageSpaceQuota\":\"2\"},\"credentials\":{\"HDFS_Path\":\"zhaoyim123456789\",\"host\":\"10.1.236.61\",\"port\":\"50070\",\"uri\":\"http://10.1.236.61:50070/webhdfs/v1zhaoyim123456789\"}},\"userprovidedservice\":{\"credentials\":null},\"binding\":null,\"bound\":0,\"instance_id\":\"d0d2faac-6fc3-11e8-ac16-fa163e4dfe45\",\"tags\":null},\"status\":{\"phase\":\"Unbound\",\"action\":\"_ToDelete\"}}\n",
  "resCodel": 200,
  "status": "successfully deleted instance"
}
```


6. 获取所有实例（包括服务和工具）
```
GET /ocmanager/v2/api/tenant/{tenantId}/service/instances
```


__response:__
```
    [{
      "attributes": "{}",
      "category": "tool",
      "cuzBsiName": "zhaoyim1234567",
      "id": "56b518d4-6edf-11e8-ac16-fa163e4dfe45",
      "instanceName": "zhaoyim1234567",
      "quota": "{\"cuzBsiName\":\"zhaoyim1234567\",\"instance_id\":\"56b518d4-6edf-11e8-ac16-fa163e4dfe45\",\"nameSpaceQuota\":\"1\",\"storageSpaceQuota\":\"1\"}",
      "serviceId": "",
      "serviceName": "HDFS",
      "serviceType": "hdfs",
      "status": "Unbound",
      "tenantId": "000999"
    }, {
      "attributes": "{}",
      "category": "service",
      "cuzBsiName": "zhaoyim12345",
      "id": "6c7dae3e-6edd-11e8-ac16-fa163e4dfe45",
      "instanceName": "zhaoyim12345",
      "quota": "{\"cuzBsiName\":\"zhaoyim12345\",\"instance_id\":\"6c7dae3e-6edd-11e8-ac16-fa163e4dfe45\",\"nameSpaceQuota\":\"1\",\"storageSpaceQuota\":\"1\"}",
      "serviceId": "",
      "serviceName": "HDFS",
      "serviceType": "hdfs",
      "status": "Unbound",
      "tenantId": "000999"
    },
    ...
]
```

7. 获取实例（服务和工具）访问信息
```
GET /ocmanager/v2/api/tenant/{tenantId}/service/instance/{instanceName}/access/info
```


__response:__
```
TODO
```






