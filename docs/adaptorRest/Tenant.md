## CM Instance REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_

### Tenant APIs
1. 创建租户
```
POST /ocmanager/v2/api/tenant
```
__request body:__
```
{
    "id": "zhaoyim9998877",
    "name": "zhaoyim9998877",
    "description": "test00001",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
"quota": "{\"hive\":{\"storageSpaceQuota\":3000},\"hive_cluster2\":{\"storageSpaceQuota\":3000},\"spark_cluster2\":{\"yarnQueueQuota\":3000},\"hive_testcluster1\":{\"storageSpaceQuota\":3000},\"mapreduce_cluster2\":{\"yarnQueueQuota\":3000},\"storm\":{\"workers\":3000,\"memory\":3000,\"supervisors\":3000},\"mapreduce_testcluster1\":{\"yarnQueueQuota\":3000},\"kafka_cluster2\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs_cluster2\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"anaconda\":{\"cpu\":3000,\"memory\":3000},\"zeppelin\":{\"cpu\":3000,\"memory\":3000},\"hbase_cluster2\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"spark_testcluster1\":{\"yarnQueueQuota\":3000},\"dataiku\":{\"cpu\":3000,\"memory\":3000},\"redis\":{\"memory\":3000,\"nodes\":3000,\"volumeSize\":3000,\"replicas\":3000},\"hbase_testcluster1\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"mapreduce\":{\"yarnQueueQuota\":3000},\"elasticsearch\":{\"volume\":3000,\"cpu\":3000,\"mem\":3000,\"replicas\":3000},\"spark\":{\"yarnQueueQuota\":3000},\"hdfs_testcluster1\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"kafka_testcluster1\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"kafka\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"hbase\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000}}"
}
```

__response:__
```
{
  "cmInfo": {
    "createTime": "2018-06-14 14:53:20.0",
    "description": "test00001",
    "id": "zhaoyim9998877",
    "level": 0,
    "name": "zhaoyim9998877",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
    "quota": "{\"hive\":{\"storageSpaceQuota\":3000},\"hive_cluster2\":{\"storageSpaceQuota\":3000},\"spark_cluster2\":{\"yarnQueueQuota\":3000},\"hive_testcluster1\":{\"storageSpaceQuota\":3000},\"mapreduce_cluster2\":{\"yarnQueueQuota\":3000},\"storm\":{\"workers\":3000,\"memory\":3000,\"supervisors\":3000},\"mapreduce_testcluster1\":{\"yarnQueueQuota\":3000},\"kafka_cluster2\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs_cluster2\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"anaconda\":{\"cpu\":3000,\"memory\":3000},\"zeppelin\":{\"cpu\":3000,\"memory\":3000},\"hbase_cluster2\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"spark_testcluster1\":{\"yarnQueueQuota\":3000},\"dataiku\":{\"cpu\":3000,\"memory\":3000},\"redis\":{\"memory\":3000,\"nodes\":3000,\"volumeSize\":3000,\"replicas\":3000},\"hbase_testcluster1\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"mapreduce\":{\"yarnQueueQuota\":3000},\"elasticsearch\":{\"volume\":3000,\"cpu\":3000,\"mem\":3000,\"replicas\":3000},\"spark\":{\"yarnQueueQuota\":3000},\"hdfs_testcluster1\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"kafka_testcluster1\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"kafka\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"hbase\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000}}",
    "status": "active"
  },
  "osClustersInfo": {
    "application_cluster": "{\"kind\":\"Status\",\"apiVersion\":\"v1\",\"metadata\":{},\"status\":\"Failure\",\"message\":\"project \\\"zhaoyim9998877\\\" already exists\",\"reason\":\"AlreadyExists\",\"details\":{\"name\":\"zhaoyim9998877\",\"kind\":\"project\"},\"code\":409}\n",
    "service_cluster": "{\"kind\":\"Status\",\"apiVersion\":\"v1\",\"metadata\":{},\"status\":\"Failure\",\"message\":\"project \\\"zhaoyim9998877\\\" already exists\",\"reason\":\"AlreadyExists\",\"details\":{\"name\":\"zhaoyim9998877\",\"kind\":\"project\"},\"code\":409}\n"
  }
}
```


2. 更新租户
```
POST /ocmanager/v2/api/tenant
```
__request body:__
```
{
    "id": "zhaoyim9998877",
    "description": "zhaoyim9998877 Changed",
    "dueTime": "2018-10-10",
"quota": "{\"hive\":{\"storageSpaceQuota\":3000},\"hive_cluster2\":{\"storageSpaceQuota\":3000},\"spark_cluster2\":{\"yarnQueueQuota\":3000},\"hive_testcluster1\":{\"storageSpaceQuota\":3000},\"mapreduce_cluster2\":{\"yarnQueueQuota\":3000},\"storm\":{\"workers\":3000,\"memory\":3000,\"supervisors\":3000},\"mapreduce_testcluster1\":{\"yarnQueueQuota\":3000},\"kafka_cluster2\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs_cluster2\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"anaconda\":{\"cpu\":3000,\"memory\":3000},\"zeppelin\":{\"cpu\":3000,\"memory\":3000},\"hbase_cluster2\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"spark_testcluster1\":{\"yarnQueueQuota\":3000},\"dataiku\":{\"cpu\":3000,\"memory\":3000},\"redis\":{\"memory\":3000,\"nodes\":3000,\"volumeSize\":3000,\"replicas\":3000},\"hbase_testcluster1\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"mapreduce\":{\"yarnQueueQuota\":3000},\"elasticsearch\":{\"volume\":3000,\"cpu\":3000,\"mem\":3000,\"replicas\":3000},\"spark\":{\"yarnQueueQuota\":3000},\"hdfs_testcluster1\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"kafka_testcluster1\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"kafka\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"hbase\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000}}"
}
```

__response:__
```
{
  "cmInfo": {
    "description": "zhaoyim9998877 Changed",
    "dueTime": "2018-10-10",
    "id": "zhaoyim9998877",
    "level": 0,
    "name": "zhaoyim9998877",
    "quota": "{\"hive\":{\"storageSpaceQuota\":3000},\"hive_cluster2\":{\"storageSpaceQuota\":3000},\"spark_cluster2\":{\"yarnQueueQuota\":3000},\"hive_testcluster1\":{\"storageSpaceQuota\":3000},\"mapreduce_cluster2\":{\"yarnQueueQuota\":3000},\"storm\":{\"workers\":3000,\"memory\":3000,\"supervisors\":3000},\"mapreduce_testcluster1\":{\"yarnQueueQuota\":3000},\"kafka_cluster2\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs_cluster2\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"anaconda\":{\"cpu\":3000,\"memory\":3000},\"zeppelin\":{\"cpu\":3000,\"memory\":3000},\"hbase_cluster2\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"spark_testcluster1\":{\"yarnQueueQuota\":3000},\"dataiku\":{\"cpu\":3000,\"memory\":3000},\"redis\":{\"memory\":3000,\"nodes\":3000,\"volumeSize\":3000,\"replicas\":3000},\"hbase_testcluster1\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"mapreduce\":{\"yarnQueueQuota\":3000},\"elasticsearch\":{\"volume\":3000,\"cpu\":3000,\"mem\":3000,\"replicas\":3000},\"spark\":{\"yarnQueueQuota\":3000},\"hdfs_testcluster1\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"kafka_testcluster1\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"kafka\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"hbase\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000}}"
  },
  "osClustersInfo": {}
}
```




3. 删除租户
```
DELETE /ocmanager/v2/api/tenant/{tenantId}
``` 

__response:__
```
{
  "cmInfo": {
    "createTime": "2018-06-14 16:45:23.0",
    "description": "test00001",
    "id": "zhaoyim9998877",
    "level": 0,
    "name": "zhaoyim9998877",
    "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
    "quota": "{\"hive\":{\"storageSpaceQuota\":3000},\"hive_cluster2\":{\"storageSpaceQuota\":3000},\"spark_cluster2\":{\"yarnQueueQuota\":3000},\"hive_testcluster1\":{\"storageSpaceQuota\":3000},\"mapreduce_cluster2\":{\"yarnQueueQuota\":3000},\"storm\":{\"workers\":3000,\"memory\":3000,\"supervisors\":3000},\"mapreduce_testcluster1\":{\"yarnQueueQuota\":3000},\"kafka_cluster2\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs_cluster2\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"anaconda\":{\"cpu\":3000,\"memory\":3000},\"zeppelin\":{\"cpu\":3000,\"memory\":3000},\"hbase_cluster2\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"spark_testcluster1\":{\"yarnQueueQuota\":3000},\"dataiku\":{\"cpu\":3000,\"memory\":3000},\"redis\":{\"memory\":3000,\"nodes\":3000,\"volumeSize\":3000,\"replicas\":3000},\"hbase_testcluster1\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"mapreduce\":{\"yarnQueueQuota\":3000},\"elasticsearch\":{\"volume\":3000,\"cpu\":3000,\"mem\":3000,\"replicas\":3000},\"spark\":{\"yarnQueueQuota\":3000},\"hdfs_testcluster1\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"kafka_testcluster1\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"kafka\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"hbase\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000}}",
    "status": "active"
  },
  "osClustersInfo": {
    "application_cluster": "{\"kind\":\"Status\",\"apiVersion\":\"v1\",\"metadata\":{},\"status\":\"Success\",\"code\":200}\n",
    "service_cluster": "{\"kind\":\"Status\",\"apiVersion\":\"v1\",\"metadata\":{},\"status\":\"Success\",\"code\":200}\n"
  }
}
```

4. 获取所有租户
```
GET /ocmanager/v2/api/tenant
```
__response:__
```
[
    {
      "createTime": "2018-06-14 18:02:00.0",
      "description": "test00001",
      "id": "zhaoyim9998877",
      "level": 0,
      "name": "zhaoyim9998877",
      "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
      "quota": "{\"hive\":{\"storageSpaceQuota\":3000},\"hive_cluster2\":{\"storageSpaceQuota\":3000},\"spark_cluster2\":{\"yarnQueueQuota\":3000},\"hive_testcluster1\":{\"storageSpaceQuota\":3000},\"mapreduce_cluster2\":{\"yarnQueueQuota\":3000},\"storm\":{\"workers\":3000,\"memory\":3000,\"supervisors\":3000},\"mapreduce_testcluster1\":{\"yarnQueueQuota\":3000},\"kafka_cluster2\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs_cluster2\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"anaconda\":{\"cpu\":3000,\"memory\":3000},\"zeppelin\":{\"cpu\":3000,\"memory\":3000},\"hbase_cluster2\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"spark_testcluster1\":{\"yarnQueueQuota\":3000},\"dataiku\":{\"cpu\":3000,\"memory\":3000},\"redis\":{\"memory\":3000,\"nodes\":3000,\"volumeSize\":3000,\"replicas\":3000},\"hbase_testcluster1\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"mapreduce\":{\"yarnQueueQuota\":3000},\"elasticsearch\":{\"volume\":3000,\"cpu\":3000,\"mem\":3000,\"replicas\":3000},\"spark\":{\"yarnQueueQuota\":3000},\"hdfs_testcluster1\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"kafka_testcluster1\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"kafka\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"hbase\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000}}",
      "status": "active"
    },
...
]
```


5. 通过租户ID获取租户
```
GET /ocmanager/v2/api/tenant/{tenantId}
```
__response:__
```
{
  "createTime": "2018-06-14 18:02:00.0",
  "description": "test00001",
  "id": "zhaoyim9998877",
  "level": 0,
  "name": "zhaoyim9998877",
  "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
  "quota": "{\"hive\":{\"storageSpaceQuota\":3000},\"hive_cluster2\":{\"storageSpaceQuota\":3000},\"spark_cluster2\":{\"yarnQueueQuota\":3000},\"hive_testcluster1\":{\"storageSpaceQuota\":3000},\"mapreduce_cluster2\":{\"yarnQueueQuota\":3000},\"storm\":{\"workers\":3000,\"memory\":3000,\"supervisors\":3000},\"mapreduce_testcluster1\":{\"yarnQueueQuota\":3000},\"kafka_cluster2\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs_cluster2\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"anaconda\":{\"cpu\":3000,\"memory\":3000},\"zeppelin\":{\"cpu\":3000,\"memory\":3000},\"hbase_cluster2\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"spark_testcluster1\":{\"yarnQueueQuota\":3000},\"dataiku\":{\"cpu\":3000,\"memory\":3000},\"redis\":{\"memory\":3000,\"nodes\":3000,\"volumeSize\":3000,\"replicas\":3000},\"hbase_testcluster1\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000},\"mapreduce\":{\"yarnQueueQuota\":3000},\"elasticsearch\":{\"volume\":3000,\"cpu\":3000,\"mem\":3000,\"replicas\":3000},\"spark\":{\"yarnQueueQuota\":3000},\"hdfs_testcluster1\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"kafka_testcluster1\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"kafka\":{\"topicQuota\":3000,\"topicTTL\":3000,\"partitionSize\":3000},\"hdfs\":{\"nameSpaceQuota\":3000,\"storageSpaceQuota\":3000},\"hbase\":{\"maximumRegionsQuota\":3000,\"maximumTablesQuota\":3000}}",
  "status": "active"
}
```

6. 获取指定租户的所有子租户
```
GET /ocmanager/v2/api/tenant/{tenantId}/children
```

__response:__
```
[
    {
      "createTime": "2018-06-13 12:52:59.0",
      "description": "test7777",
      "id": "000999",
      "level": 0,
      "name": "zhaoyim001",
      "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
      "quota": "{\"hive\":{\"storageSpaceQuota\":1000000},\"hive_cluster2\":{\"storageSpaceQuota\":1000000},\"spark_cluster2\":{\"yarnQueueQuota\":1000000},\"hive_testcluster1\":{\"storageSpaceQuota\":1000000},\"mapreduce_cluster2\":{\"yarnQueueQuota\":1000000},\"storm\":{\"workers\":1000000,\"memory\":1000000,\"supervisors\":1000000},\"mapreduce_testcluster1\":{\"yarnQueueQuota\":1000000},\"kafka_cluster2\":{\"topicQuota\":1000000,\"topicTTL\":1000000,\"partitionSize\":1000000},\"hdfs_cluster2\":{\"nameSpaceQuota\":1000000,\"storageSpaceQuota\":1000000},\"anaconda\":{\"cpu\":1000000,\"memory\":1000000},\"zeppelin\":{\"cpu\":1000000,\"memory\":1000000},\"hbase_cluster2\":{\"maximumRegionsQuota\":1000000,\"maximumTablesQuota\":1000000},\"spark_testcluster1\":{\"yarnQueueQuota\":1000000},\"dataiku\":{\"cpu\":1000000,\"memory\":1000000},\"redis\":{\"memory\":1000000,\"nodes\":1000000,\"volumeSize\":1000000,\"replicas\":1000000},\"hbase_testcluster1\":{\"maximumRegionsQuota\":1000000,\"maximumTablesQuota\":1000000},\"mapreduce\":{\"yarnQueueQuota\":1000000},\"elasticsearch\":{\"volume\":1000000,\"cpu\":1000000,\"mem\":1000000,\"replicas\":1000000},\"spark\":{\"yarnQueueQuota\":1000000},\"hdfs_testcluster1\":{\"nameSpaceQuota\":1000000,\"storageSpaceQuota\":1000000},\"kafka_testcluster1\":{\"topicQuota\":1000000,\"topicTTL\":1000000,\"partitionSize\":1000000},\"kafka\":{\"topicQuota\":1000000,\"topicTTL\":1000000,\"partitionSize\":1000000},\"hdfs\":{\"nameSpaceQuota\":1000000,\"storageSpaceQuota\":1000000},\"hbase\":{\"maximumRegionsQuota\":1000000,\"maximumTablesQuota\":1000000}}",
      "status": "active"
    },
    {
      "createTime": "2018-04-13 10:44:08.0",
      "description": "",
      "dueTime": "2018-06-26 00:00:00.0",
      "id": "admin-1523586795",
      "level": 0,
      "name": "ethanwang",
      "parentId": "ae783b6d-655a-11e7-aa10-fa163ed7d0ae",
      "quota": "{\"hive\":{\"storageSpaceQuota\":0},\"hive_cluster2\":{\"storageSpaceQuota\":0},\"spark_cluster2\":{\"yarnQueueQuota\":0},\"hive_testcluster1\":{\"storageSpaceQuota\":0},\"mapreduce_cluster2\":{\"yarnQueueQuota\":0},\"storm\":{\"workers\":0,\"memory\":0,\"supervisors\":0},\"mapreduce_testcluster1\":{\"yarnQueueQuota\":0},\"kafka_cluster2\":{\"topicQuota\":0,\"topicTTL\":0,\"partitionSize\":0},\"hdfs_cluster2\":{\"nameSpaceQuota\":0,\"storageSpaceQuota\":0},\"anaconda\":{\"cpu\":0,\"memory\":0},\"zeppelin\":{\"cpu\":0,\"memory\":0},\"hbase_cluster2\":{\"maximumRegionsQuota\":0,\"maximumTablesQuota\":0},\"spark_testcluster1\":{\"yarnQueueQuota\":0},\"dataiku\":{\"cpu\":0,\"memory\":0},\"redis\":{\"memory\":0,\"nodes\":0,\"volumeSize\":0,\"replicas\":0},\"hbase_testcluster1\":{\"maximumRegionsQuota\":0,\"maximumTablesQuota\":0},\"mapreduce\":{\"yarnQueueQuota\":0},\"elasticsearch\":{\"volume\":0,\"cpu\":0,\"mem\":0,\"replicas\":0},\"spark\":{\"yarnQueueQuota\":0},\"hdfs_testcluster1\":{\"nameSpaceQuota\":0,\"storageSpaceQuota\":0},\"kafka_testcluster1\":{\"topicQuota\":0,\"topicTTL\":0,\"partitionSize\":0},\"kafka\":{\"topicQuota\":0,\"topicTTL\":0,\"partitionSize\":0},\"hdfs\":{\"nameSpaceQuota\":0,\"storageSpaceQuota\":0},\"hbase\":{\"maximumRegionsQuota\":0,\"maximumTablesQuota\":0}}",
      "status": "active"
    },
    ...
]
```






