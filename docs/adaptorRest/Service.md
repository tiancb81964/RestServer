## CM Service REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_

### Service APIs


1. 获取所有服务
```
GET /ocmanager/v2/api/service
```
__response:__
```
[
	{
	  "category": "service",
	  "description": "Redis是一个可基于内存亦可持久化的日志型、Key-Value数据库。版本：v2.8",
	  "id": "A54BC117-25E3-4E41-B8F7-14FC314D04D3",
	  "origin": "etcd-34",
	  "serviceType": "Redis",
	  "servicename": "Redis"
	}, {
	  "category": "tool",
	  "description": "Spark是一种的通用并行计算框架。版本：v1.6.0",
	  "id": "d3b9a485-f038-4605-9b9b-29792f5c61d2",
	  "origin": "cluster61",
	  "serviceType": "spark",
	  "servicename": "Spark"
	},
    ...
]
```

2. 获取单个服务
```
GET /ocmanager/v2/api/service/{serviceId}
```
__response:__
```
{
  "category": "tool",
  "description": "Spark是一种的通用并行计算框架。版本：v1.6.0",
  "id": "d3b9a485-f038-4605-9b9b-29792f5c61d0",
  "origin": "cluster116",
  "serviceType": "spark",
  "servicename": "Spark_cluster2"
}
```



3. 获取Data Foundry服务列表
```
GET /ocmanager/v2/api/service/df
```
__response:__
```
{
  "kind": "BackingServiceList",
  "apiVersion": "v1",
  "metadata": {
    "selfLink": "/oapi/v1/namespaces/openshift/backingservices",
    "resourceVersion": "1234789"
  },
  "items": [{
    "metadata": {
      "name": "Anaconda",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Anaconda",
      "uid": "545648a4-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1234414",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "Anaconda",
      "id": "dfc126e9-181a-4d13-a367-f84edfe617ed",
      "description": "Anaconda on Openshift",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["Anaconda", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "Anaconda",
        "documentationUrl": "https://docs.anaconda.com/",
        "imageUrl": "pub/assets/Anaconda.png",
        "longDescription": "Anaconda in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://www.anaconda.com/support/"
      },
      "plans": [{
        "name": "standalone",
        "id": "521a4a06-175a-43e6-b1bc-d9c684f76a0d",
        "description": "Anaconda on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }, {
        "name": "pvc_standalone",
        "id": "a321538f-dee1-4511-93fd-96ade8fee82e",
        "description": "Anaconda on Openshift",
        "metadata": {
          "bullets": ["1 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": {
            "cpu": {
              "default": 1,
              "max": 16,
              "price": 1e+07,
              "step": 0.1,
              "unit": "个",
              "desc": "Anaconda的cpu数量"
            },
            "memory": {
              "default": 2,
              "max": 32,
              "price": 1e+07,
              "step": 0.1,
              "unit": "GB",
              "desc": "Anaconda节点内存设置"
            }
          }
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "Cassandra",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Cassandra",
      "uid": "546a9f80-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1224802",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "Cassandra",
      "id": "3D7D7D07-D704-4B22-B492-EE5AE5301A55",
      "description": "Cassandra是一套开源分布式NoSQL数据库系统。版本：v3.4",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["cassandra", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "Cassandra",
        "documentationUrl": "https://wiki.apache.org/cassandra/GettingStarted",
        "imageUrl": "pub/assets/Cassandra.png",
        "longDescription": "Managed, highly available cassandra clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://cassandra.apache.org/"
      },
      "plans": [{
        "name": "standalone",
        "id": "7B7EC041-2090-4ACB-AE0F-E8BDF315A778",
        "description": "单独Cassandra实例",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "Dataiku",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Dataiku",
      "uid": "5465670c-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1234535",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "Dataiku",
      "id": "e4871703-a37e-427b-bbfc-313c1fbf685f",
      "description": "Dataiku on Openshift",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["Dataiku", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "Dataiku",
        "documentationUrl": "https://www.dataiku.com/learn/",
        "imageUrl": "pub/assets/Dataiku.png",
        "longDescription": "Dataiku in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://www.dataiku.com"
      },
      "plans": [{
        "name": "standalone",
        "id": "3286b8bb-790b-40bc-aeaf-46a0a788f1cc",
        "description": "Dataiku on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": {
            "cpu": {
              "default": 1,
              "max": 16,
              "price": 1e+07,
              "step": 0.1,
              "unit": "个",
              "desc": "Dataiku的cpu数量"
            },
            "memory": {
              "default": 2,
              "max": 32,
              "price": 1e+07,
              "step": 0.1,
              "unit": "GB",
              "desc": "Dataiku节点内存设置"
            }
          }
        },
        "free": true
      }, {
        "name": "volumes_standalone",
        "id": "133F32F2-6E8A-4CD6-A14C-1D400866B6B3",
        "description": "HA Dataiku on Openshift",
        "metadata": {
          "bullets": ["1 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": {
            "cpu": {
              "default": 1,
              "max": 16,
              "price": 1e+07,
              "step": 0.1,
              "unit": "个",
              "desc": "Dataiku的cpu数量"
            },
            "memory": {
              "default": 2,
              "max": 32,
              "price": 1e+07,
              "step": 0.1,
              "unit": "GB",
              "desc": "Dataiku节点内存设置"
            }
          }
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "ETCD",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/ETCD",
      "uid": "545967e5-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1234705",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "ETCD",
      "id": "5E397661-1385-464A-8DB7-9C4DF8CC0662",
      "description": "ETCD是一个高可用的键值存储系统,主要用于共享配置和服务发现。版本：v2.3.0",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["etcd", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "ETCD",
        "documentationUrl": "https://coreos.com/etcd/docs/latest",
        "imageUrl": "pub/assets/ETCD.png",
        "longDescription": "Managed, highly available etcd clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://coreos.com"
      },
      "plans": [{
        "name": "standalone",
        "id": "204F8288-F8D9-4806-8661-EB48D94504B3",
        "description": "单独ETCD实例",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }, {
        "name": "volumes_standalone",
        "id": "256D56C0-B83D-11E6-B227-2714EF851DCA",
        "description": "HA etcd on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "Elasticsearch",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Elasticsearch",
      "uid": "54533e2b-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1232850",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "Elasticsearch",
      "id": "3626D834-BD32-11E6-8C01-F7A6E255AF47",
      "description": "A Sample Elasticsearch (v2.3.0) cluster on Openshift",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["Elasticsearch", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "Elasticsearch",
        "documentationUrl": "https://coreos.com/etcd/docs/latest",
        "imageUrl": "pub/assets/ElasticSearch.png",
        "longDescription": "Managed, highly available etcd clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://coreos.com"
      },
      "plans": [{
        "name": "volumes_standalone",
        "id": "A9537ABE-BD33-11E6-A969-13A2D25B7667",
        "description": "HA Elasticsearch on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }, {
        "name": "Cluster",
        "id": "95cd832a-52a3-11e8-92bf-fa163e3e1b52",
        "description": "Elasticsearch (6.2.2) Cluster on Openshift",
        "metadata": {
          "bullets": ["3 replicas", "20 GB of Disk", "0.5 cpu", "2G memory"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": {
            "cpu": {
              "default": 0.5,
              "max": 2,
              "step": 0.1,
              "unit": "core",
              "desc": "单个实例分配的cpu数量"
            },
            "mem": {
              "default": 2,
              "max": 24,
              "step": 1,
              "unit": "Gi",
              "desc": "单个实例分配的内存数量"
            },
            "replicas": {
              "default": 3,
              "max": 30,
              "step": 1,
              "unit": "nodes",
              "desc": "Elasticsearch集群实例数量"
            },
            "volume": {
              "default": 5,
              "max": 100,
              "step": 1,
              "unit": "GB",
              "desc": "单个实例存储容量设置"
            }
          }
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "HBase",
      "generateName": "cluster61",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/HBase",
      "uid": "3adee069-3ec4-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1234202",
      "creationTimestamp": "2018-04-13T02:41:55Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster61"
      }
    },
    "spec": {
      "name": "HBase",
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
  }, {
    "metadata": {
      "name": "HBase_cluster2",
      "generateName": "cluster116",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/HBase_cluster2",
      "uid": "e797d33c-3e1e-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1203803",
      "creationTimestamp": "2018-04-12T06:58:29Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster116"
      }
    },
    "spec": {
      "name": "HBase_cluster2",
      "id": "d9845ade-9410-4c7f-8689-4e032c1a8449",
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
      "phase": "Inactive"
    }
  }, {
    "metadata": {
      "name": "HBase_testcluster1",
      "generateName": "testcluster001",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/HBase_testcluster1",
      "uid": "e6c3eeb1-49f8-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1012934",
      "creationTimestamp": "2018-04-27T08:56:40Z",
      "labels": {
        "asiainfo.io/servicebroker": "testcluster001"
      }
    },
    "spec": {
      "name": "HBase_testcluster1",
      "id": "d9845ade-9410-4c7f-8689-4e032c1a8111",
      "description": "HBase是Hadoop的面向列的分布式非关系型数据库。版本：v1.1.2",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["hbase", "database"],
      "requires": [],
      "metadata": {
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
  }, {
    "metadata": {
      "name": "HDFS",
      "generateName": "cluster61",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/HDFS",
      "uid": "3ad939db-3ec4-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1234203",
      "creationTimestamp": "2018-04-13T02:41:55Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster61"
      }
    },
    "spec": {
      "name": "HDFS",
      "id": "ae67d4ba-5c4e-4937-a68b-5b47cfe356d9",
      "description": "HDFS是Hadoop的分布式文件系统。版本：v2.7.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["hdfs", "storage"],
      "requires": [],
      "metadata": {
        "category": "tool",
        "displayName": "HDFS",
        "documentationUrl": "http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html",
        "imageUrl": "https://hadoop.apache.org/images/hadoop-logo.jpg",
        "longDescription": "Hadoop分布式文件系统(HDFS)是一个的分布式的，可扩展的，轻量级的文件系统。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html",
        "type": "hdfs"
      },
      "plans": [{
        "name": "shared",
        "id": "72150b09-1025-4533-8bae-0e04ef68ac13",
        "description": "共享HDFS实例",
        "metadata": {
          "bullets": ["Name Space Quota:1000", "Storage Space Quota (GB):20"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
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
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "HDFS_cluster2",
      "generateName": "cluster116",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/HDFS_cluster2",
      "uid": "e796357c-3e1e-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1203804",
      "creationTimestamp": "2018-04-12T06:58:29Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster116"
      }
    },
    "spec": {
      "name": "HDFS_cluster2",
      "id": "ae67d4ba-5c4e-4937-a68b-5b47cfe356d7",
      "description": "HDFS是Hadoop的分布式文件系统。版本：v2.7.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["hdfs", "storage"],
      "requires": [],
      "metadata": {
        "category": "tool",
        "displayName": "HDFS",
        "documentationUrl": "http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html",
        "imageUrl": "https://hadoop.apache.org/images/hadoop-logo.jpg",
        "longDescription": "Hadoop分布式文件系统(HDFS)是一个的分布式的，可扩展的，轻量级的文件系统。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html",
        "type": "hdfs"
      },
      "plans": [{
        "name": "shared",
        "id": "72150b09-1025-4533-8bae-0e04ef68ac13",
        "description": "共享HDFS实例",
        "metadata": {
          "bullets": ["Name Space Quota:1000", "Storage Space Quota (GB):20"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
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
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Inactive"
    }
  }, {
    "metadata": {
      "name": "HDFS_testcluster1",
      "generateName": "testcluster001",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/HDFS_testcluster1",
      "uid": "e6bdf4e8-49f8-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1012935",
      "creationTimestamp": "2018-04-27T08:56:40Z",
      "labels": {
        "asiainfo.io/servicebroker": "testcluster001"
      }
    },
    "spec": {
      "name": "HDFS_testcluster1",
      "id": "ae67d4ba-5c4e-4937-a68b-5b47cfe35111",
      "description": "HDFS是Hadoop的分布式文件系统。版本：v2.7.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["hdfs", "storage"],
      "requires": [],
      "metadata": {
        "displayName": "HDFS",
        "documentationUrl": "http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html",
        "imageUrl": "https://hadoop.apache.org/images/hadoop-logo.jpg",
        "longDescription": "Hadoop分布式文件系统(HDFS)是一个的分布式的，可扩展的，轻量级的文件系统。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/HdfsUserGuide.html",
        "type": "hdfs"
      },
      "plans": [{
        "name": "shared",
        "id": "72150b09-1025-4533-8bae-0e04ef68ac13",
        "description": "共享HDFS实例",
        "metadata": {
          "bullets": ["Name Space Quota:1000", "Storage Space Quota (GB):20"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
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
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "Hive",
      "generateName": "cluster61",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Hive",
      "uid": "3ae8f368-3ec4-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1234204",
      "creationTimestamp": "2018-04-13T02:41:55Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster61"
      }
    },
    "spec": {
      "name": "Hive",
      "id": "2ef26018-003d-4b2b-b786-0481d4ee9fa4",
      "description": "Hive是Hadoop的数据仓库。版本：v1.2.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["hive", "datawarehouse"],
      "requires": [],
      "metadata": {
        "category": "tool",
        "displayName": "Hive",
        "documentationUrl": "http://hive.apache.org/",
        "imageUrl": "https://hive.apache.org/images/hive_logo_medium.jpg",
        "longDescription": "Hive是一个可以通过SQL去读写，管理存储在分布式存储系统上的大规模数据集的数据仓库解决方案。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://cwiki.apache.org/confluence/display/Hive/Home#Home-UserDocumentation",
        "type": "hive"
      },
      "plans": [{
        "name": "shared",
        "id": "aa7e364f-fdbf-4187-b60a-218b6fa398ed",
        "description": "共享Hive实例",
        "metadata": {
          "bullets": ["Storage Space Quota (GB):20"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
          "displayName": "",
          "customize": {
            "storageSpaceQuota": {
              "default": 1,
              "max": 102400,
              "price": 10,
              "step": 10,
              "unit": "GB",
              "desc": "Hive数据库的最大存储容量"
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
  }, {
    "metadata": {
      "name": "Hive_cluster2",
      "generateName": "cluster116",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Hive_cluster2",
      "uid": "e79bd31e-3e1e-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1203805",
      "creationTimestamp": "2018-04-12T06:58:29Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster116"
      }
    },
    "spec": {
      "name": "Hive_cluster2",
      "id": "2ef26018-003d-4b2b-b786-0481d4ee9fa2",
      "description": "Hive是Hadoop的数据仓库。版本：v1.2.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["hive", "datawarehouse"],
      "requires": [],
      "metadata": {
        "category": "tool",
        "displayName": "Hive",
        "documentationUrl": "http://hive.apache.org/",
        "imageUrl": "https://hive.apache.org/images/hive_logo_medium.jpg",
        "longDescription": "Hive是一个可以通过SQL去读写，管理存储在分布式存储系统上的大规模数据集的数据仓库解决方案。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://cwiki.apache.org/confluence/display/Hive/Home#Home-UserDocumentation",
        "type": "hive"
      },
      "plans": [{
        "name": "shared",
        "id": "aa7e364f-fdbf-4187-b60a-218b6fa398ed",
        "description": "共享Hive实例",
        "metadata": {
          "bullets": ["Storage Space Quota (GB):20"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
          "displayName": "",
          "customize": {
            "storageSpaceQuota": {
              "default": 1024,
              "max": 102400,
              "price": 10,
              "step": 10,
              "unit": "GB",
              "desc": "Hive数据库的最大存储容量"
            }
          }
        },
        "free": false
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Inactive"
    }
  }, {
    "metadata": {
      "name": "Hive_testcluster1",
      "generateName": "testcluster001",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Hive_testcluster1",
      "uid": "e6b7c26d-49f8-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1012936",
      "creationTimestamp": "2018-04-27T08:56:40Z",
      "labels": {
        "asiainfo.io/servicebroker": "testcluster001"
      }
    },
    "spec": {
      "name": "Hive_testcluster1",
      "id": "2ef26018-003d-4b2b-b786-0481d4ee9111",
      "description": "Hive是Hadoop的数据仓库。版本：v1.2.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["hive", "datawarehouse"],
      "requires": [],
      "metadata": {
        "displayName": "Hive",
        "documentationUrl": "http://hive.apache.org/",
        "imageUrl": "https://hive.apache.org/images/hive_logo_medium.jpg",
        "longDescription": "Hive是一个可以通过SQL去读写，管理存储在分布式存储系统上的大规模数据集的数据仓库解决方案。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://cwiki.apache.org/confluence/display/Hive/Home#Home-UserDocumentation",
        "type": "hive"
      },
      "plans": [{
        "name": "shared",
        "id": "aa7e364f-fdbf-4187-b60a-218b6fa398ed",
        "description": "共享Hive实例",
        "metadata": {
          "bullets": ["Storage Space Quota (GB):20"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
          "displayName": "",
          "customize": {
            "storageSpaceQuota": {
              "default": 1024,
              "max": 102400,
              "price": 10,
              "step": 10,
              "unit": "GB",
              "desc": "Hive数据库的最大存储容量"
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
  }, {
    "metadata": {
      "name": "Kafka",
      "generateName": "cluster61",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Kafka",
      "uid": "3ad776a8-3ec4-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1234762",
      "creationTimestamp": "2018-04-13T02:41:55Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster61"
      }
    },
    "spec": {
      "name": "Kafka",
      "id": "9972923D-0787-4271-839C-D000BD87E309",
      "description": "Kafka是一种高吞吐量的分布式发布订阅消息系统。版本：v0.9.0",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["kafka", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "Kafka",
        "documentationUrl": "http://kafka.apache.org/documentation.html",
        "imageUrl": "pub/assets/Kafka.png",
        "longDescription": "Managed, highly available kafka clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://kafka.apache.org"
      },
      "plans": [{
        "name": "standalone",
        "id": "9A14FEF4-FB41-4C84-A175-A80492A50875",
        "description": "单独kafka实例",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "Kafka_cluster2",
      "generateName": "cluster116",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Kafka_cluster2",
      "uid": "e7a304fa-3e1e-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1203806",
      "creationTimestamp": "2018-04-12T06:58:29Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster116"
      }
    },
    "spec": {
      "name": "Kafka_cluster2",
      "id": "7b738c78-d412-422b-ac3e-43a9fc72a4a6",
      "description": "Apache Kafka 是一种支持分布式、高吞吐的消息系统。版本：v0.9.0",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["kafka", "streaming"],
      "requires": [],
      "metadata": {
        "category": "tool",
        "displayName": "Kafka",
        "documentationUrl": "https://kafka.apache.org/",
        "imageUrl": "https://spark.apache.org/images/spark-logo.png",
        "longDescription": "Apache Kafka 是一种支持分布式、高吞吐的消息系统。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://kafka.apache.org/documentation/",
        "type": "kafka"
      },
      "plans": [{
        "name": "shared",
        "id": "68ee85c2-5b1a-4f51-89e9-5b111c251f0d",
        "description": "共享Kafka实例",
        "metadata": {
          "bullets": ["topicQuota:1", "partitionSize:1024", "topicTTL:604800000"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
          "displayName": "",
          "customize": {
            "partitionSize": {
              "default": 1024,
              "max": -1,
              "price": 10,
              "step": 10,
              "unit": "GB",
              "desc": "Storage(Bytes) of one partition in a topic."
            },
            "topicQuota": {
              "default": 1,
              "max": -1,
              "price": 10,
              "step": 10,
              "desc": "Number of partitions in a topic."
            },
            "topicTTL": {
              "default": 6.048e+08,
              "max": -1,
              "price": 10,
              "step": 10,
              "unit": "ms",
              "desc": "Topic TTL."
            }
          }
        },
        "free": false
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Inactive"
    }
  }, {
    "metadata": {
      "name": "Kafka_testcluster1",
      "generateName": "testcluster001",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Kafka_testcluster1",
      "uid": "e6bc76d4-49f8-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1012937",
      "creationTimestamp": "2018-04-27T08:56:40Z",
      "labels": {
        "asiainfo.io/servicebroker": "testcluster001"
      }
    },
    "spec": {
      "name": "Kafka_testcluster1",
      "id": "7b738c78-d412-422b-ac3e-43a9fc72a111",
      "description": "Apache Kafka 是一种支持分布式、高吞吐的消息系统。版本：v0.9.0",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["kafka", "streaming"],
      "requires": [],
      "metadata": {
        "displayName": "Kafka",
        "documentationUrl": "https://kafka.apache.org/",
        "imageUrl": "https://spark.apache.org/images/spark-logo.png",
        "longDescription": "Apache Kafka 是一种支持分布式、高吞吐的消息系统。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://kafka.apache.org/documentation/",
        "type": "kafka"
      },
      "plans": [{
        "name": "shared",
        "id": "68ee85c2-5b1a-4f51-89e9-5b111c251f0d",
        "description": "共享Kafka实例",
        "metadata": {
          "bullets": ["topicQuota:1", "partitionSize:1024", "topicTTL:604800000"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
          "displayName": "",
          "customize": {
            "partitionSize": {
              "default": 1024,
              "max": -1,
              "price": 10,
              "step": 10,
              "unit": "GB",
              "desc": "Storage(Bytes) of one partition in a topic."
            },
            "topicQuota": {
              "default": 1,
              "max": -1,
              "price": 10,
              "step": 10,
              "desc": "Number of partitions in a topic."
            },
            "topicTTL": {
              "default": 6.048e+08,
              "max": -1,
              "price": 10,
              "step": 10,
              "unit": "ms",
              "desc": "Topic TTL."
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
  }, {
    "metadata": {
      "name": "Kettle",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Kettle",
      "uid": "54693363-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1224806",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "Kettle",
      "id": "51219A87-E37E-44F5-8E30-4767348D644D",
      "description": "Kettle是一款国外开源的ETL工具，数据抽取高效稳定。版本：v5.0.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["kettle", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "Kettle",
        "documentationUrl": "http://wiki.pentaho.com/display/EAI/Latest+Pentaho+Data+Integration+%28aka+Kettle%29+Documentation",
        "imageUrl": "pub/assets/Kettle.png",
        "longDescription": "Managed, highly available kettle clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://community.pentaho.com/projects/data-integration/"
      },
      "plans": [{
        "name": "standalone",
        "id": "31B428F8-AA3E-4CAC-85A2-7294C7CAA79D",
        "description": "单独kettle实例",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "MapReduce",
      "generateName": "cluster61",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/MapReduce",
      "uid": "3aea0644-3ec4-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1234206",
      "creationTimestamp": "2018-04-13T02:41:55Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster61"
      }
    },
    "spec": {
      "name": "MapReduce",
      "id": "ae0f2324-27a8-415b-9c7f-64ab6cd88d41",
      "description": "MapReduce是Hadoop的分布式计算框架。版本：v2.7.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["MapReduce", "compute engine"],
      "requires": [],
      "metadata": {
        "category": "tool",
        "displayName": "MapReduce",
        "documentationUrl": "http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html ",
        "imageUrl": "https://hadoop.apache.org/images/hadoop-logo.jpg",
        "longDescription": "Hadoop MapReduce是一个可以快速编写能够在大规模集群(数千节点)上处理大规模数据(TB级)的可靠的，容错的应用的软件框架。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html",
        "type": "mapreduce"
      },
      "plans": [{
        "name": "shared",
        "id": "6524c793-0ea5-4456-9a60-ca70271decdc",
        "description": "共享MapReduce实例",
        "metadata": {
          "bullets": ["Yarn Queue Quota (GB):4"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
          "displayName": "",
          "customize": {
            "yarnQueueQuota": {
              "default": 10,
              "max": 100,
              "price": 10,
              "step": 10,
              "unit": "GB",
              "desc": "Yarn队列的最大容量"
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
  }, {
    "metadata": {
      "name": "MapReduce_cluster2",
      "generateName": "cluster116",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/MapReduce_cluster2",
      "uid": "e79d8c0f-3e1e-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1203807",
      "creationTimestamp": "2018-04-12T06:58:29Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster116"
      }
    },
    "spec": {
      "name": "MapReduce_cluster2",
      "id": "ae0f2324-27a8-415b-9c7f-64ab6cd88d39",
      "description": "MapReduce是Hadoop的分布式计算框架。版本：v2.7.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["MapReduce", "compute engine"],
      "requires": [],
      "metadata": {
        "category": "tool",
        "displayName": "MapReduce",
        "documentationUrl": "http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html ",
        "imageUrl": "https://hadoop.apache.org/images/hadoop-logo.jpg",
        "longDescription": "Hadoop MapReduce是一个可以快速编写能够在大规模集群(数千节点)上处理大规模数据(TB级)的可靠的，容错的应用的软件框架。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html",
        "type": "mapreduce"
      },
      "plans": [{
        "name": "shared",
        "id": "6524c793-0ea5-4456-9a60-ca70271decdc",
        "description": "共享MapReduce实例",
        "metadata": {
          "bullets": ["Yarn Queue Quota (GB):4"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
          "displayName": "",
          "customize": {
            "yarnQueueQuota": {
              "default": 10,
              "max": 100,
              "price": 10,
              "step": 10,
              "unit": "GB",
              "desc": "Yarn队列的最大容量"
            }
          }
        },
        "free": false
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Inactive"
    }
  }, {
    "metadata": {
      "name": "MapReduce_testcluster1",
      "generateName": "testcluster001",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/MapReduce_testcluster1",
      "uid": "e6b936c1-49f8-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1012938",
      "creationTimestamp": "2018-04-27T08:56:40Z",
      "labels": {
        "asiainfo.io/servicebroker": "testcluster001"
      }
    },
    "spec": {
      "name": "MapReduce_testcluster1",
      "id": "ae0f2324-27a8-415b-9c7f-64ab6cd88111",
      "description": "MapReduce是Hadoop的分布式计算框架。版本：v2.7.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["MapReduce", "compute engine"],
      "requires": [],
      "metadata": {
        "displayName": "MapReduce",
        "documentationUrl": "http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html ",
        "imageUrl": "https://hadoop.apache.org/images/hadoop-logo.jpg",
        "longDescription": "Hadoop MapReduce是一个可以快速编写能够在大规模集群(数千节点)上处理大规模数据(TB级)的可靠的，容错的应用的软件框架。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html",
        "type": "mapreduce"
      },
      "plans": [{
        "name": "shared",
        "id": "6524c793-0ea5-4456-9a60-ca70271decdc",
        "description": "共享MapReduce实例",
        "metadata": {
          "bullets": ["Yarn Queue Quota (GB):4"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
          "displayName": "",
          "customize": {
            "yarnQueueQuota": {
              "default": 10,
              "max": 100,
              "price": 10,
              "step": 10,
              "unit": "GB",
              "desc": "Yarn队列的最大容量"
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
  }, {
    "metadata": {
      "name": "Mongo",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Mongo",
      "uid": "5463de1c-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1224807",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "Mongo",
      "id": "eac6c8cf-2a63-4120-9e29-30c4b716e37f",
      "description": "A Sample MongoDB cluster on Openshift",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["mongodb", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "MongoDB",
        "documentationUrl": "https://docs.mongodb.com/",
        "imageUrl": "https://webassets.mongodb.com/_com_assets/global/mongodb-logo-white.png",
        "longDescription": "Managed, highly available MongoDB clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://www.mongodb.com/"
      },
      "plans": [{
        "name": "volumes_standalone",
        "id": "3b7fc05d-c630-4c0b-8dda-2cedb271ccb5",
        "description": "HA MongoDB With Volumes on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": false
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "MySQL",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/MySQL",
      "uid": "5457eafb-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1234763",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "MySQL",
      "id": "0f96b0f0-6a25-4018-8225-8f1cd090b1f9",
      "description": "A Sample MySQL cluster on Openshift",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["mysql", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "MySQL",
        "documentationUrl": "https://dev.mysql.com/doc/",
        "imageUrl": "https://labs.mysql.com/common/logos/mysql-logo.svg?v2",
        "longDescription": "Managed, highly available MySQL clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://www.mysql.com/"
      },
      "plans": [{
        "name": "volumes_ha_cluster",
        "id": "5a648266-4a76-4ab3-98ab-fe8933de161a",
        "description": "HA MySQL With Volumes on Openshift",
        "metadata": {
          "bullets": ["1 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": false
      }, {
        "name": "hostpath_ha_cluster",
        "id": "b80b0b7d-5108-4038-b560-67d82e6a43b7",
        "description": "HA MySQL With HostPath Support on Kubernetes",
        "metadata": {
          "bullets": ["1 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": false
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "NiFi",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/NiFi",
      "uid": "546e4de8-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1224809",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "NiFi",
      "id": "BCC10E77-98B6-4EF0-8AFB-E5FD789F712E",
      "description": "NiFi 是一个易于使用、功能强大而且可靠的数据处理和分发系统。版本：v0.6.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["nifi", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "NiFi",
        "documentationUrl": "https://nifi.apache.org/docs.html",
        "imageUrl": "pub/assets/NiFiDrop.png",
        "longDescription": "Managed, highly available nifi clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://nifi.apache.org"
      },
      "plans": [{
        "name": "standalone",
        "id": "4435A93C-6CC9-45F0-AFB0-EA070361DD6A",
        "description": "单独nifi实例",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "PySpider",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/PySpider",
      "uid": "5460882d-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1224810",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "PySpider",
      "id": "c6ed3cb8-d486-4faa-8185-7262183a1113",
      "description": "A Sample PySpider (v0.3.7) cluster on Openshift",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["pyspider", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "PySpider",
        "documentationUrl": "https://docs.pyspider.org",
        "longDescription": "A Powerful Spider(Web Crawler) System in Python.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://github.com/binux/pyspider"
      },
      "plans": [{
        "name": "standalone",
        "id": "1f195802-1642-47e9-be69-9082cc1ceaf5",
        "description": "HA Spider on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "RabbitMQ",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/RabbitMQ",
      "uid": "546bbeb3-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1234418",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "RabbitMQ",
      "id": "86272DCB-86D5-4039-9E05-894436B8E71D",
      "description": "RabbitMQ是流行的开源消息队列系统。版本v3.6.1",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["rabbitmq", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "RabbitMQ",
        "documentationUrl": "https://www.rabbitmq.com/documentation.html",
        "imageUrl": "pub/assets/RabbitMQ.png",
        "longDescription": "Managed, highly available rabbitmq clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://www.rabbitmq.com/"
      },
      "plans": [{
        "name": "standalone",
        "id": "CC5ED8A2-1677-43A0-ADE5-27F713C6F51B",
        "description": "单独rabbitMQ实例",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }, {
        "name": "volumes_standalone",
        "id": "28749ee2-6f30-4967-8311-2bf34f9a5421",
        "description": "HA RabbitMQ With Volumes on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "Redis",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Redis",
      "uid": "54672e2d-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1234761",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "Redis",
      "id": "A54BC117-25E3-4E41-B8F7-14FC314D04D3",
      "description": "Redis是一个可基于内存亦可持久化的日志型、Key-Value数据库。版本：v2.8",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["redis", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "Redis",
        "documentationUrl": "http://redis.io/documentation",
        "imageUrl": "pub/assets/Redis.png",
        "longDescription": "Managed, highly available redis clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://redis.io"
      },
      "plans": [{
        "name": "volumes_cluster",
        "id": "94bcf092-74e7-49b1-add6-314fb2c7bdfb",
        "description": "Redis Cluster With Volumes on Openshift",
        "metadata": {
          "bullets": ["1 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": {
            "memory": {
              "default": 0.1,
              "max": 2,
              "price": 1e+07,
              "step": 0.1,
              "unit": "GB",
              "desc": "Redis集群节点内存设置"
            },
            "nodes": {
              "default": 3,
              "max": 5,
              "price": 1e+08,
              "step": 1,
              "unit": "nodes",
              "desc": "Redis集群node数量"
            },
            "volumeSize": {
              "default": 1,
              "max": 10,
              "price": 1e+07,
              "step": 1,
              "unit": "GB",
              "desc": "Redis挂卷大小设置"
            }
          }
        },
        "free": true
      }, {
        "name": "volumes_cluster_with_replicas",
        "id": "67117486-934f-4f94-bd2d-179ec6e309b4",
        "description": "Redis Cluster Supporting Replicas With Volumes on Openshift",
        "metadata": {
          "bullets": ["1 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": {
            "memory": {
              "default": 0.1,
              "max": 2,
              "price": 1e+07,
              "step": 0.1,
              "unit": "GB",
              "desc": "Redis集群节点内存设置"
            },
            "nodes": {
              "default": 3,
              "max": 5,
              "price": 1e+08,
              "step": 1,
              "unit": "nodes",
              "desc": "Redis集群master node数量"
            },
            "replicas": {
              "default": 1,
              "max": 3,
              "price": 1e+08,
              "step": 1,
              "unit": "replicas",
              "desc": "Redis集群slaves/master数量"
            },
            "volumeSize": {
              "default": 1,
              "max": 10,
              "price": 1e+07,
              "step": 1,
              "unit": "GB",
              "desc": "Redis挂卷大小设置"
            }
          }
        },
        "free": true
      }, {
        "name": "volumes_single",
        "id": "6c97104b-2d7d-44f9-a053-5c8d018d25a6",
        "description": "Redis Single Master With Volumes on Openshift",
        "metadata": {
          "bullets": ["1 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "Spark",
      "generateName": "cluster61",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Spark",
      "uid": "3aaf0537-3ec4-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1234760",
      "creationTimestamp": "2018-04-13T02:41:55Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster61"
      }
    },
    "spec": {
      "name": "Spark",
      "id": "0674A0E3-5EE4-425C-BE43-5A61EB3F52A6",
      "description": "A Sample Spark (v1.5.2) cluster on Openshift with one worker node",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["spark", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "Spark",
        "documentationUrl": "http://spark.apache.org/docs/latest/",
        "imageUrl": "http://spark.apache.org/images/spark-logo-trademark.png",
        "longDescription": "Managed, highly available Spark clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://spark.apache.org"
      },
      "plans": [{
        "name": "Three_Workers",
        "id": "65242C9B-C266-4F1D-A28B-98B1A2043A84",
        "description": "HA Spark on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "three worker nodes"],
          "costs": null,
          "displayName": "High Available",
          "customize": null
        },
        "free": false
      }, {
        "name": "One_Worker",
        "id": "CB00754C-11FF-444F-8419-9AA9B1E04970",
        "description": "Spark on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "one worker node"],
          "costs": null,
          "displayName": "Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "Spark_cluster2",
      "generateName": "cluster116",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Spark_cluster2",
      "uid": "e7a11846-3e1e-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1203808",
      "creationTimestamp": "2018-04-12T06:58:29Z",
      "labels": {
        "asiainfo.io/servicebroker": "cluster116"
      }
    },
    "spec": {
      "name": "Spark_cluster2",
      "id": "d3b9a485-f038-4605-9b9b-29792f5c61d0",
      "description": "Spark是一种的通用并行计算框架。版本：v1.6.0",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["spark", "compute engine"],
      "requires": [],
      "metadata": {
        "category": "tool",
        "displayName": "Spark",
        "documentationUrl": "http://spark.apache.org/",
        "imageUrl": "https://spark.apache.org/images/spark-logo.png",
        "longDescription": "Apache Spark是一个快速的，通用性的集群计算系统。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://spark.apache.org/docs/1.6.0/",
        "type": "spark"
      },
      "plans": [{
        "name": "shared",
        "id": "5c3d471d-f94a-4bb8-b340-f783f3c15ba1",
        "description": "共享Spark实例",
        "metadata": {
          "bullets": ["Yarn Queue Quota (GB):4"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
          "displayName": "",
          "customize": {
            "yarnQueueQuota": {
              "default": 10,
              "max": 100,
              "price": 10,
              "step": 10,
              "unit": "GB",
              "desc": "Yarn队列的最大容量"
            }
          }
        },
        "free": false
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Inactive"
    }
  }, {
    "metadata": {
      "name": "Spark_testcluster1",
      "generateName": "testcluster001",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Spark_testcluster1",
      "uid": "e6ba6f2f-49f8-11e8-a558-fa163e4dfe45",
      "resourceVersion": "1012939",
      "creationTimestamp": "2018-04-27T08:56:40Z",
      "labels": {
        "asiainfo.io/servicebroker": "testcluster001"
      }
    },
    "spec": {
      "name": "Spark_testcluster1",
      "id": "d3b9a485-f038-4605-9b9b-29792f5c6111",
      "description": "Spark是一种的通用并行计算框架。版本：v1.6.0",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["spark", "compute engine"],
      "requires": [],
      "metadata": {
        "displayName": "Spark",
        "documentationUrl": "http://spark.apache.org/",
        "imageUrl": "https://spark.apache.org/images/spark-logo.png",
        "longDescription": "Apache Spark是一个快速的，通用性的集群计算系统。",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://spark.apache.org/docs/1.6.0/",
        "type": "spark"
      },
      "plans": [{
        "name": "shared",
        "id": "5c3d471d-f94a-4bb8-b340-f783f3c15ba1",
        "description": "共享Spark实例",
        "metadata": {
          "bullets": ["Yarn Queue Quota (GB):4"],
          "costs": [{
            "amount": {
              "usd": 0
            },
            "unit": "MONTHLY"
          }],
          "displayName": "",
          "customize": {
            "yarnQueueQuota": {
              "default": 10,
              "max": 100,
              "price": 10,
              "step": 10,
              "unit": "GB",
              "desc": "Yarn队列的最大容量"
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
  }, {
    "metadata": {
      "name": "Storm",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Storm",
      "uid": "546d26a4-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1233901",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "Storm",
      "id": "6555DBC1-E6BC-4F0D-8948-E1B5DF6BD596",
      "description": "Storm为分布式实时计算框架。版本：v0.9.2",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["storm", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "Storm",
        "documentationUrl": "https://storm.apache.org/releases/1.0.1/index.html",
        "imageUrl": "pub/assets/Storm.png",
        "longDescription": "Managed, highly available storm clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://storm.apache.org/"
      },
      "plans": [{
        "name": "standalone",
        "id": "D0B82741-770A-463C-818F-6E99862367DF",
        "description": "单独Storm实例",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }, {
        "name": "external_standalone",
        "id": "ef12ed9a-87f5-11e7-b949-0fc03853e5ec",
        "description": "HA Storm on Openshift exposed to external",
        "metadata": {
          "bullets": ["1 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": {
            "memory": {
              "default": 0.5,
              "max": 32,
              "price": 1e+07,
              "step": 0.1,
              "unit": "GB",
              "desc": "Storm集群supervisor节点内存设置"
            },
            "supervisors": {
              "default": 2,
              "max": 10,
              "price": 1e+08,
              "step": 1,
              "unit": "个",
              "desc": "Storm集群supervisor数量"
            },
            "workers": {
              "default": 4,
              "max": 30,
              "price": 1e+07,
              "step": 1,
              "unit": "个",
              "desc": "每个supervisor上的worker数量"
            }
          }
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "TensorFlow",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/TensorFlow",
      "uid": "546f7523-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1224814",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "TensorFlow",
      "id": "2DD1363D-8768-4DAA-BDC3-FB29C10C4A8C",
      "description": "谷歌基于DistBelief研发的第二代AI学习系统。版本：v0.8.0",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["tensorflow", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "TensorFlow",
        "documentationUrl": "https://www.tensorflow.org/get_started",
        "imageUrl": "pub/assets/TensorFlow.png",
        "longDescription": "Managed, highly available tensorflow clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://www.tensorflow.org/"
      },
      "plans": [{
        "name": "standalone",
        "id": "BE1CAAF2-CAB7-4B56-AEB4-2A3111225D50",
        "description": "单独tensorflow实例",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "Zeppelin",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/Zeppelin",
      "uid": "bcb81db1-5e5d-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1224815",
      "creationTimestamp": "2018-05-23T07:48:52Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "Zeppelin",
      "id": "A326EF4F-74D0-4B60-9CA0-CAED94D7E50F",
      "description": "Zeppelin on Openshift",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["Zeppelin", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "Zeppelin",
        "documentationUrl": "http://zeppelin.apache.org/docs/0.7.3/",
        "imageUrl": "pub/assets/Zeppelin.png",
        "longDescription": "Zeppelin in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "http://zeppelin.apache.org"
      },
      "plans": [{
        "name": "standalone",
        "id": "F344A7E1-2E49-4BA4-8BEF-42FFCC7AEB14",
        "description": "Zeppelin on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": {
            "cpu": {
              "default": 1,
              "max": 16,
              "price": 1e+07,
              "step": 0.1,
              "unit": "个",
              "desc": "Zeppelin的cpu数量"
            },
            "memory": {
              "default": 2,
              "max": 32,
              "price": 1e+07,
              "step": 0.1,
              "unit": "GB",
              "desc": "Zeppelin节点内存设置"
            }
          }
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }, {
    "metadata": {
      "name": "ZooKeeper",
      "generateName": "etcd-34",
      "namespace": "openshift",
      "selfLink": "/oapi/v1/namespaces/openshift/backingservices/ZooKeeper",
      "uid": "5450cb9c-58d5-11e8-ac16-fa163e4dfe45",
      "resourceVersion": "1234021",
      "creationTimestamp": "2018-05-16T06:49:50Z",
      "labels": {
        "asiainfo.io/servicebroker": "etcd-34"
      }
    },
    "spec": {
      "name": "ZooKeeper",
      "id": "FA780A47-4AB2-4035-8638-287538B13416",
      "description": "ZooKeeper是一个分布式的，开放源码的分布式应用程序协调服务。版本：v3.4.8",
      "bindable": true,
      "plan_updateable": false,
      "tags": ["zookeeper", "openshift"],
      "requires": null,
      "metadata": {
        "displayName": "ZooKeeper",
        "documentationUrl": "https://zookeeper.apache.org/doc/trunk",
        "imageUrl": "pub/assets/ZooKeeper.png",
        "longDescription": "Managed, highly available zookeeper clusters in the cloud.",
        "providerDisplayName": "Asiainfo",
        "supportUrl": "https://zookeeper.apache.org/"
      },
      "plans": [{
        "name": "standalone",
        "id": "221F1338-96C1-4135-A865-9CDA4C12A185",
        "description": "单独zookeeper实例",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }, {
        "name": "volumes_standalone",
        "id": "dffc3555-eb18-4c7b-ac56-bd326b19dcd0",
        "description": "HA ZooKeeper With Volumes on Openshift",
        "metadata": {
          "bullets": ["20 GB of Disk", "20 connections"],
          "costs": null,
          "displayName": "Shared and Free",
          "customize": null
        },
        "free": true
      }],
      "dashboard_client": null
    },
    "status": {
      "phase": "Active"
    }
  }]
}
```










