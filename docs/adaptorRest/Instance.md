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
{
  "kind": "BackingServiceInstance",
  "apiVersion": "v1",
  "metadata": {
    "name": "zhaoyim12345678900",
    "namespace": "000999",
    "selfLink": "/oapi/v1/namespaces/000999/backingserviceinstances/zhaoyim12345678900",
    "uid": "936b2ccd-7046-11e8-ac16-fa163e4dfe45",
    "resourceVersion": "1234198",
    "creationTimestamp": "2018-06-15T02:48:25Z",
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
        "accesses": "read,write,execute",
        "cuzBsiName": "zhaoyim12345678900",
        "instance_id": "936c64e7-7046-11e8-ac16-fa163e4dfe45",
        "nameSpaceQuota": "1",
        "storageSpaceQuota": "1",
        "user_name": "gsm"
      },
      "credentials": {
        "HDFS_Path": "zhaoyim12345678900",
        "host": "10.1.236.61",
        "port": "50070",
        "uri": "http://10.1.236.61:50070/webhdfs/v1"
      }
    },
    "userprovidedservice": {
      "credentials": null
    },
    "binding": [{
      "bound_time": "2018-06-15T02:48:30Z",
      "bind_uuid": "955a27f4-7046-11e8-ac16-fa163e4dfe45",
      "bind_hadoop_user": "gsm",
      "credentials": {
        "HDFS_Path": "zhaoyim12345678900",
        "host": "10.1.236.61",
        "port": "50070",
        "uri": "http://10.1.236.61:50070/webhdfs/v1",
        "username": "gsm"
      }
    }],
    "bound": 1,
    "instance_id": "936c64e7-7046-11e8-ac16-fa163e4dfe45",
    "tags": null
  },
  "status": {
    "phase": "Bound"
  }
}
```

8. 获取当前租户可以申请的实例（服务和工具）
```
GET /ocmanager/v2/api/service/access/{tenantId}/services
```

__response:__
```
[
	{
	  "category": "tool",
	  "description": "HBase是Hadoop的面向列的分布式非关系型数据库。版本：v1.1.2",
	  "id": "d9845ade-9410-4c7f-8689-4e032c1a8451",
	  "origin": "cluster61",
	  "serviceType": "hbase",
	  "servicename": "Hbase_cluster61"
	}, {
	  "category": "tool",
	  "description": "HDFS是Hadoop的分布式文件系统。版本：v2.7.1",
	  "id": "ae67d4ba-5c4e-4937-a68b-5b47cfe356d9",
	  "origin": "cluster61",
	  "serviceType": "hdfs",
	  "servicename": "HDFS_cluster61"
	}, {
	  "category": "tool",
	  "description": "Hive是Hadoop的数据仓库。版本：v1.2.1",
	  "id": "2ef26018-003d-4b2b-b786-0481d4ee9fa4",
	  "origin": "cluster61",
	  "serviceType": "hive",
	  "servicename": "Hive_cluster61"
	}, {
	  "category": "tool",
	  "description": "Apache Kafka 是一种支持分布式、高吞吐的消息系统。版本：v0.9.0",
	  "id": "7b738c78-d412-422b-ac3e-43a9fc72a4a8",
	  "origin": "cluster61",
	  "serviceType": "kafka",
	  "servicename": "Kafka_cluster61"
	}, {
	  "category": "tool",
	  "description": "MapReduce是Hadoop的分布式计算框架。版本：v2.7.1",
	  "id": "ae0f2324-27a8-415b-9c7f-64ab6cd88d41",
	  "origin": "cluster61",
	  "serviceType": "mapreduce",
	  "servicename": "MapReduce_cluster61"
	}, {
	  "category": "tool",
	  "description": "Spark是一种的通用并行计算框架。版本：v1.6.0",
	  "id": "d3b9a485-f038-4605-9b9b-29792f5c61d2",
	  "origin": "cluster61",
	  "serviceType": "spark",
	  "servicename": "Spark_cluster61"
	}
]
```


9. 获取服务的详细信息（CM和DF对于服务的综合信息）
```
GET /ocmanager/v2/api/service/df/{serviceName}
```

__response:__
```
{
  "metadata": {
    "name": "Hbase_cluster61",
    "generateName": "cluster61",
    "namespace": "openshift",
    "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Hbase_cluster61",
    "uid": "870864cd-708d-11e8-ac16-fa163e4dfe45",
    "resourceVersion": "1239056",
    "creationTimestamp": "2018-06-15T11:16:19Z",
    "labels": {
      "asiainfo.io/servicebroker": "cluster61"
    }
  },
  "spec": {
    "name": "Hbase_cluster61",
    "id": "d9845ade-9410-4c7f-8689-4e032c1a8451",
    "description": "HBase是Hadoop的面向列的分布式非关系型数据库。版本：v1.1.2",
    "bindable": true,
    "plan_updateable": false,
    "tags": ["hbase", "database"],
    "requires": [],
    "metadata": {
      "category": "tool",
      "displayName": "HBase",
      "documentationUrl": "http://hbase.apache.org/",
      "imageUrl": "http://hbase.apache.org/images/hbase_logo_with_orca_large.png",
      "longDescription": "HBase是一个开源的，非关系型的，分布式数据库，类似于Google的BigTable。",
      "providerDisplayName": "Asiainfo",
      "supportUrl": "http://hbase.apache.org/book.html",
      "type": "hbase"
    },
    "plans": [{
      "name": "shared",
      "id": "f658e391-b7d6-4b72-9e4c-c754e4943ae1",
      "description": "共享HBase实例",
      "metadata": {
        "bullets": ["HBase Maximun Tables:10", "HBase Maximun Regions:10"],
        "costs": [{
          "amount": {
            "usd": 0
          },
          "unit": "MONTHLY"
        }],
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
    }],
    "dashboard_client": null
  },
  "status": {
    "phase": "Active"
  }
}
```



