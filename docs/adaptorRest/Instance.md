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

