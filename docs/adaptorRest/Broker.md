## CM Brokers REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_

### Brokers APIs

### 1创建broker dc（/ocmanager/v2/api/brokers/dc） 

	示例：http://127.0.0.1:8080/ocmanager/v2/api/brokers/dc?clustername=mycluster
    请求方式：POST

#### 1.1请求参数

##### 1.1.1基本参数

字段|类型|描述|是否必填|备注|是否常量|
----------|----------------|----|--------|------------|---|
clustername|String||是|目标集群名|否

#### 1.2返回参数

##### 1.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|

#### 1.3报文示例

##### 1.3.1请求报文示例

__request:__

```
{
    "cluster_admin": "admin",
    "cluster_id": "1",
    "cluster_name": "cluster_alpha",
    "cluster_type": "hadoop",
    "cluster_url": "http://10.1.236.111:8080",
    "env": [
        {
            "description": "http ranger url",
            "key": "RANGER_URL",
            "value": "http://192.168.1.1:6080"
        },
        {
            "description": "http ranger url",
            "key": "RANGER_URL",
            "value": "http://192.168.1.1:6080"
        }
    ]
}
```

##### 1.3.2返回报文示例

__response:__

```
{
    "apiVersion": "v1",
    "kind": "DeploymentConfig",
    "metadata": {
        "annotations": {
            "dadafoundry.io/create-by": "clustermanager",
            "openshift.io/generated-by": "OpenShiftWebConsole"
        },
        "creationTimestamp": "2018-06-05T02:33:13Z",
        "generation": 3,
        "labels": {
            "app": "cm-broker"
        },
        "name": "cm-broker",
        "namespace": "southbase",
        "resourceVersion": "9200111",
        "selfLink": "/oapi/v1/namespaces/southbase/deploymentconfigs/cm-broker",
        "uid": "cb8ea774-6868-11e8-ae4e-fa163ef134de"
    },
    "spec": {
        "replicas": 1,
        "selector": {
            "app": "cm-broker",
            "deploymentconfig": "cm-broker"
        },
        "strategy": {
            "activeDeadlineSeconds": 21600,
            "resources": {},
            "rollingParams": {
                "intervalSeconds": 1,
                "maxSurge": "25%",
                "maxUnavailable": "25%",
                "timeoutSeconds": 600,
                "updatePeriodSeconds": 1
            },
            "type": "Rolling"
        },
        "template": {
            "metadata": {
                "annotations": {
                    "openshift.io/generated-by": "OpenShiftWebConsole"
                },
                "creationTimestamp": null,
                "labels": {
                    "app": "cm-broker",
                    "deploymentconfig": "cm-broker"
                }
            },
            "spec": {
                "containers": [
                    {
                        "env": [
                            {
                                "name": "ADAPTER_API_SERVER",
                                "value": "http://10.1.236.60:9090"
                            },
                            {
                                "name": "SVCAMOUNT_API_SERVER",
                                "value": "http://svc-amount2.cloud.prd.asiainfo.com"
                            }
                        ],
                        "image": "docker-registry.default.svc:5000/southbase/cm-broker@sha256:8f0b437a91bed1ab44cfdda6b989debc078dfba8a2013ef38e5a824dff42afd7",
                        "imagePullPolicy": "IfNotPresent",
                        "name": "cm-broker",
                        "ports": [
                            {
                                "containerPort": 9000,
                                "protocol": "TCP"
                            }
                        ],
                        "resources": {},
                        "terminationMessagePath": "/dev/termination-log",
                        "terminationMessagePolicy": "File"
                    }
                ],
                "dnsPolicy": "ClusterFirst",
                "restartPolicy": "Always",
                "schedulerName": "default-scheduler",
                "securityContext": {},
                "terminationGracePeriodSeconds": 30
            }
        },
        "test": false,
        "triggers": [
            {
                "type": "ConfigChange"
            }
        ]
    },
    "status": {
        "availableReplicas": 1,
        "conditions": [
            {
                "lastTransitionTime": "2018-06-05T02:34:59Z",
                "lastUpdateTime": "2018-06-05T02:34:59Z",
                "message": "Deployment config has minimum availability.",
                "status": "True",
                "type": "Available"
            },
            {
                "lastTransitionTime": "2018-06-05T03:28:27Z",
                "lastUpdateTime": "2018-06-05T03:28:29Z",
                "message": "replication controller \"cm-broker-2\" successfully rolled out",
                "reason": "NewReplicationControllerAvailable",
                "status": "True",
                "type": "Progressing"
            }
        ],
        "details": {
            "causes": [
                {
                    "type": "ConfigChange"
                }
            ],
            "message": "config change"
        },
        "latestVersion": 2,
        "observedGeneration": 3,
        "readyReplicas": 1,
        "replicas": 1,
        "unavailableReplicas": 0,
        "updatedReplicas": 1
    }
}
```

### 2创建broker svc（/ocmanager/v2/api/brokers/svc） 

NOTE: 添加服务,服务是注册在service broker里面的,因此会添加service broker 中注册的所有服务

	示例：http://127.0.0.1:8080/ocmanager/v2/api/brokers/svc?dcname=dcname
    请求方式：POST

#### 2.1请求参数

##### 2.1.1基本参数

字段|类型|描述|是否必填|备注|是否常量|
----------|----------------|----|--------|------------|---|
dcname|String|dcname|是|dcname|否

#### 2.2返回参数

##### 2.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|

#### 2.3报文示例

##### 2.3.1请求报文示例

##### 2.3.2返回报文示例

__response:__

```
{
    "apiVersion": "v1",
    "kind": "Service",
    "metadata": {
        "annotations": {
            "dadafoundry.io/create-by": "clustermanager"
        },
        "creationTimestamp": "2018-06-05T02:33:13Z",
        "labels": {
            "app": "cm-broker"
        },
        "name": "cm-broker",
        "namespace": "southbase",
        "resourceVersion": "9194792",
        "selfLink": "/api/v1/namespaces/southbase/services/cm-broker",
        "uid": "cb7f1b68-6868-11e8-ae4e-fa163ef134de"
    },
    "spec": {
        "clusterIP": "172.25.247.231",
        "ports": [
            {
                "name": "9000-tcp",
                "port": 9000,
                "protocol": "TCP",
                "targetPort": 9000
            }
        ],
        "selector": {
            "app": "cm-broker",
            "deploymentconfig": "cm-broker"
        },
        "sessionAffinity": "None",
        "type": "ClusterIP"
    },
    "status": {
        "loadBalancer": {}
    }
}
```

### 3创建 broker router（/ocmanager/v2/api/brokers/router） 

NOTE: 启动/实例化指定broker

	示例：http://127.0.0.1:8080/ocmanager/v2/api/service/brokers/router?svcname=svcname
    请求方式：POST

#### 3.1请求参数

##### 3.1.1基本参数

字段|类型|描述|是否必填|备注|是否常量|
----------|----------------|----|--------|------------|---|

#### 3.2返回参数

##### 3.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|

#### 3.3报文示例

##### 3.3.1请求报文示例

##### 3.3.2返回报文示例

__response:__

```
{
    "apiVersion": "v1",
    "kind": "Route",
    "metadata": {
        "creationTimestamp": "2018-06-05T02:35:50Z",
        "name": "cm-broker",
        "namespace": "southbase",
        "resourceVersion": "9195063",
        "selfLink": "/oapi/v1/namespaces/southbase/routes/cm-broker",
        "uid": "28f36555-6869-11e8-ae4e-fa163ef134de"
    },
    "spec": {
        "host": "cm.southbase.prd.dataos.io",
        "port": {
            "targetPort": "9000-tcp"
        },
        "tls": {
            "insecureEdgeTerminationPolicy": "Redirect",
            "termination": "edge"
        },
        "to": {
            "kind": "Service",
            "name": "cm-broker",
            "weight": 50
        },
        "wildcardPolicy": "None"
    },
    "status": {
        "ingress": [
            {
                "conditions": [
                    {
                        "lastTransitionTime": "2018-06-05T02:35:50Z",
                        "status": "True",
                        "type": "Admitted"
                    }
                ],
                "host": "cm.southbase.prd.dataos.io",
                "routerName": "router",
                "wildcardPolicy": "None"
            }
        ]
    }
}
```

### 4实例化broker dc（/ocmanager/v2/api/brokers/{id}/dc/instantiate） 

	示例：http://127.0.0.1:8080/ocmanager/v2/api/brokers/123456/dc/instantiate
    请求方式：PUT

#### 4.1请求参数

##### 4.1.1基本参数

#### 4.2返回参数

##### 4.2.1基本参数

#### 4.3报文示例

##### 4.3.1请求报文示例

##### 4.3.2返回报文示例

__response:__

```
{
    "apiVersion": "v1",
    "kind": "DeploymentConfig",
    "metadata": {
        "annotations": {
            "dadafoundry.io/create-by": "clustermanager",
            "openshift.io/generated-by": "OpenShiftWebConsole"
        },
        "creationTimestamp": "2018-06-05T02:33:13Z",
        "generation": 3,
        "labels": {
            "app": "cm-broker"
        },
        "name": "cm-broker",
        "namespace": "southbase",
        "resourceVersion": "9200111",
        "selfLink": "/oapi/v1/namespaces/southbase/deploymentconfigs/cm-broker",
        "uid": "cb8ea774-6868-11e8-ae4e-fa163ef134de"
    },
    "spec": {
        "replicas": 1,
        "selector": {
            "app": "cm-broker",
            "deploymentconfig": "cm-broker"
        },
        "strategy": {
            "activeDeadlineSeconds": 21600,
            "resources": {},
            "rollingParams": {
                "intervalSeconds": 1,
                "maxSurge": "25%",
                "maxUnavailable": "25%",
                "timeoutSeconds": 600,
                "updatePeriodSeconds": 1
            },
            "type": "Rolling"
        },
        "template": {
            "metadata": {
                "annotations": {
                    "openshift.io/generated-by": "OpenShiftWebConsole"
                },
                "creationTimestamp": null,
                "labels": {
                    "app": "cm-broker",
                    "deploymentconfig": "cm-broker"
                }
            },
            "spec": {
                "containers": [
                    {
                        "env": [
                            {
                                "name": "ADAPTER_API_SERVER",
                                "value": "http://10.1.236.60:9090"
                            },
                            {
                                "name": "SVCAMOUNT_API_SERVER",
                                "value": "http://svc-amount2.cloud.prd.asiainfo.com"
                            }
                        ],
                        "image": "docker-registry.default.svc:5000/southbase/cm-broker@sha256:8f0b437a91bed1ab44cfdda6b989debc078dfba8a2013ef38e5a824dff42afd7",
                        "imagePullPolicy": "IfNotPresent",
                        "name": "cm-broker",
                        "ports": [
                            {
                                "containerPort": 9000,
                                "protocol": "TCP"
                            }
                        ],
                        "resources": {},
                        "terminationMessagePath": "/dev/termination-log",
                        "terminationMessagePolicy": "File"
                    }
                ],
                "dnsPolicy": "ClusterFirst",
                "restartPolicy": "Always",
                "schedulerName": "default-scheduler",
                "securityContext": {},
                "terminationGracePeriodSeconds": 30
            }
        },
        "test": false,
        "triggers": [
            {
                "type": "ConfigChange"
            }
        ]
    },
    "status": {
        "availableReplicas": 1,
        "conditions": [
            {
                "lastTransitionTime": "2018-06-05T02:34:59Z",
                "lastUpdateTime": "2018-06-05T02:34:59Z",
                "message": "Deployment config has minimum availability.",
                "status": "True",
                "type": "Available"
            },
            {
                "lastTransitionTime": "2018-06-05T03:28:27Z",
                "lastUpdateTime": "2018-06-05T03:28:29Z",
                "message": "replication controller \"cm-broker-2\" successfully rolled out",
                "reason": "NewReplicationControllerAvailable",
                "status": "True",
                "type": "Progressing"
            }
        ],
        "details": {
            "causes": [
                {
                    "type": "ConfigChange"
                }
            ],
            "message": "config change"
        },
        "latestVersion": 2,
        "observedGeneration": 3,
        "readyReplicas": 1,
        "replicas": 1,
        "unavailableReplicas": 0,
        "updatedReplicas": 1
    }
}
```

### 5注册broker（/ocmanager/v2/api/brokers/register） 

	示例：http://127.0.0.1:8080/ocmanager/v2/api/brokers/register
    请求方式：PUT

#### 5.1请求参数

##### 5.1.1基本参数

#### 5.2返回参数

##### 5.2.1基本参数

#### 5.3报文示例

##### 5.3.1请求报文示例

__request:__

```
{
  "kind":"ServiceBroker",
  "apiVersion":"v1",
  "metadata":
    {
      "name":"brokername"
    },
  "spec":
    {
      "url":"https://cm.southbase.prd.dataos.io",
      "username":"admin",
      "password":"admin"
    }
}
```

##### 5.3.2返回报文示例

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
    "url": "https://cm.southbase.prd.dataos.io",
    "name": "",
    "username": "admin",
    "password": "admin"
  },
  "status": {
    "phase": "New"
  }
}
```

### 6更新Service broker dc（/ocmanager/v2/api/brokers/{id}/dc） 

	示例：http://127.0.0.1:8080/ocmanager/v2/api/service/brokers/123456/dc
    请求方式：PUT

#### 6.1请求参数

##### 6.1.1基本参数

字段|类型|描述|是否必填|备注|是否常量|
----------|----------------|----|--------|------------|---|
id|String|broker id|是|broker id|否
env|List|dc环境变量|dc环境变量|否

#### 6.2返回参数

##### 6.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|
env|List|dc环境变量|dc环境变量|否

#### 6.3报文示例

##### 6.3.1请求报文示例

__request body:__

```
{
    "kind": "DeploymentConfig",
    "apiVersion": "v1",
    "metadata": {
        "name": "cm-broker-${id}",
        "labels": {
            "app": "cm-broker"
        },
        "annotations": {
            "dadafoundry.io/create-by": "clustermanager"
        }
    },
    "spec": {
        "replicas": 1,
        "selector": {
            "app": "cm-broker",
            "deploymentconfig": "cm-broker"
        },
        "template": {
            "metadata": {
                "labels": {
                    "app": "cm-broker",
                    "deploymentconfig": "cm-broker"
                }
            },
            "spec": {
                "containers": [
                    {
                        "name": "cm-broker",
                        "image": "${docker-image-url}",
                        "ports": [
                            {
                                "containerPort": 9000,
                                "protocol": "TCP"
                            }
                        ],
                        "env": [
                            {
                                "name": "ENV_ALPHA",
                                "value": "VALUE"
                            },
                            {
                                "name": "ENV_BETA",
                                "value": "VALUE"
                            }
                        ]
                    }
                ]
            }
        }
    }
}
 
```

##### 6.3.2返回报文示例

__response:__

```
{
    "apiVersion": "v1",
    "kind": "DeploymentConfig",
    "metadata": {
        "annotations": {
            "dadafoundry.io/create-by": "chaizs",
            "openshift.io/generated-by": "OpenShiftWebConsole"
        },
        "creationTimestamp": "2018-06-05T02:33:13Z",
        "generation": 3,
        "labels": {
            "app": "cm-broker"
        },
        "name": "cm-broker",
        "namespace": "southbase",
        "resourceVersion": "9200111",
        "selfLink": "/oapi/v1/namespaces/southbase/deploymentconfigs/cm-broker",
        "uid": "cb8ea774-6868-11e8-ae4e-fa163ef134de"
    },
    "spec": {
        "replicas": 1,
        "selector": {
            "app": "cm-broker",
            "deploymentconfig": "cm-broker"
        },
        "strategy": {
            "activeDeadlineSeconds": 21600,
            "resources": {},
            "rollingParams": {
                "intervalSeconds": 1,
                "maxSurge": "25%",
                "maxUnavailable": "25%",
                "timeoutSeconds": 600,
                "updatePeriodSeconds": 1
            },
            "type": "Rolling"
        },
        "template": {
            "metadata": {
                "annotations": {
                    "openshift.io/generated-by": "OpenShiftWebConsole"
                },
                "creationTimestamp": null,
                "labels": {
                    "app": "cm-broker",
                    "deploymentconfig": "cm-broker"
                }
            },
            "spec": {
                "containers": [
                    {
                        "env": [
                            {
                                "name": "ADAPTER_API_SERVER",
                                "value": "http://10.1.236.60:9090"
                            },
                            {
                                "name": "SVCAMOUNT_API_SERVER",
                                "value": "http://svc-amount2.cloud.prd.asiainfo.com"
                            }
                        ],
                        "image": "docker-registry.default.svc:5000/southbase/cm-broker",
                        "imagePullPolicy": "IfNotPresent",
                        "name": "cm-broker",
                        "ports": [
                            {
                                "containerPort": 9000,
                                "protocol": "TCP"
                            }
                        ],
                        "resources": {},
                        "terminationMessagePath": "/dev/termination-log",
                        "terminationMessagePolicy": "File"
                    }
                ],
                "dnsPolicy": "ClusterFirst",
                "restartPolicy": "Always",
                "schedulerName": "default-scheduler",
                "securityContext": {},
                "terminationGracePeriodSeconds": 30
            }
        },
        "test": false,
        "triggers": [
            {
                "type": "ConfigChange"
            }
        ]
    },
    "status": {
        "availableReplicas": 1,
        "conditions": [
            {
                "lastTransitionTime": "2018-06-05T02:34:59Z",
                "lastUpdateTime": "2018-06-05T02:34:59Z",
                "message": "Deployment config has minimum availability.",
                "status": "True",
                "type": "Available"
            },
            {
                "lastTransitionTime": "2018-06-05T03:28:27Z",
                "lastUpdateTime": "2018-06-05T03:28:29Z",
                "message": "replication controller \"cm-broker-2\" successfully rolled out",
                "reason": "NewReplicationControllerAvailable",
                "status": "True",
                "type": "Progressing"
            }
        ],
        "details": {
            "causes": [
                {
                    "type": "ConfigChange"
                }
            ],
            "message": "config change"
        },
        "latestVersion": 2,
        "observedGeneration": 3,
        "readyReplicas": 1,
        "replicas": 1,
        "unavailableReplicas": 0,
        "updatedReplicas": 1
    }
}

```

### 7删除Service broker（/ocmanager/v2/api/service/brokers/{name}/） 

NOTE： 删除服务,服务是注册在service broker 里面的,因此会删除service broker 中注册的所有服务

	示例：http://127.0.0.1:8080/ocmanager/v2/api/service/brokers/111aw2/
    请求方式：DELETE

#### 7.1请求参数

##### 7.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|
name|String|service broker名字|是|

#### 7.2返回参数

##### 7.2.1基本参数

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

#### 7.3报文示例

##### 7.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/service/brokers/111aw2/
```

##### 7.3.2返回报文示例

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

### 8获取broker（/ocmanager/v2/api/brokers/） 

NOTE： 删除服务,服务是注册在service broker 里面的,因此会删除service broker 中注册的所有服务

	示例：http://127.0.0.1:8080/ocmanager/v2/api/brokers?clustername=cluster1
    请求方式：GET

#### 8.1请求参数

##### 8.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|

#### 8.2返回参数

##### 8.2.1基本参数


#### 8.3报文示例

##### 8.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/brokers?clustername=cluster1
```

##### 8.3.2返回报文示例

__response:__

```
[
    {
        "binded_cluster": "cluster1",
        "broker_id": "1",
        "broker_image": "http://myimage.com",
        "broker_name": "broker1",
        "broker_url": "https://mybroker.com",
        "dc_name": "cm-broker-123456"
    }
]
```

### 9获取broker dc（/ocmanager/v2/api/brokers/{id}/dc） 

NOTE： 删除服务,服务是注册在service broker 里面的,因此会删除service broker 中注册的所有服务

	示例：http://127.0.0.1:8080/ocmanager/v2/api/brokers/123456/dc
    请求方式：GET

#### 9.1请求参数

##### 9.1.1基本参数

字段|类型|描述|是否必填|备注
----------|----------------|----|--------|------------|

#### 9.2返回参数

##### 9.2.1基本参数


#### 9.3报文示例

##### 9.3.1请求报文示例

```
http://127.0.0.1:8080/ocmanager/v2/api/brokers/123456/dc
```

##### 9.3.2返回报文示例

__response:__

```
{
    "apiVersion": "v1",
    "kind": "DeploymentConfig",
    "metadata": {
        "annotations": {
            "dadafoundry.io/create-by": "clustermanager",
            "openshift.io/generated-by": "OpenShiftWebConsole"
        },
        "creationTimestamp": "2018-06-05T02:33:13Z",
        "generation": 3,
        "labels": {
            "app": "cm-broker"
        },
        "name": "cm-broker",
        "namespace": "southbase",
        "resourceVersion": "9200111",
        "selfLink": "/oapi/v1/namespaces/southbase/deploymentconfigs/cm-broker",
        "uid": "cb8ea774-6868-11e8-ae4e-fa163ef134de"
    },
    "spec": {
        "replicas": 1,
        "selector": {
            "app": "cm-broker",
            "deploymentconfig": "cm-broker"
        },
        "strategy": {
            "activeDeadlineSeconds": 21600,
            "resources": {},
            "rollingParams": {
                "intervalSeconds": 1,
                "maxSurge": "25%",
                "maxUnavailable": "25%",
                "timeoutSeconds": 600,
                "updatePeriodSeconds": 1
            },
            "type": "Rolling"
        },
        "template": {
            "metadata": {
                "annotations": {
                    "openshift.io/generated-by": "OpenShiftWebConsole"
                },
                "creationTimestamp": null,
                "labels": {
                    "app": "cm-broker",
                    "deploymentconfig": "cm-broker"
                }
            },
            "spec": {
                "containers": [
                    {
                        "env": [
                            {
                                "name": "ADAPTER_API_SERVER",
                                "value": "http://10.1.236.60:9090"
                            },
                            {
                                "name": "SVCAMOUNT_API_SERVER",
                                "value": "http://svc-amount2.cloud.prd.asiainfo.com"
                            }
                        ],
                        "image": "docker-registry.default.svc:5000/southbase/cm-broker@sha256:8f0b437a91bed1ab44cfdda6b989debc078dfba8a2013ef38e5a824dff42afd7",
                        "imagePullPolicy": "IfNotPresent",
                        "name": "cm-broker",
                        "ports": [
                            {
                                "containerPort": 9000,
                                "protocol": "TCP"
                            }
                        ],
                        "resources": {},
                        "terminationMessagePath": "/dev/termination-log",
                        "terminationMessagePolicy": "File"
                    }
                ],
                "dnsPolicy": "ClusterFirst",
                "restartPolicy": "Always",
                "schedulerName": "default-scheduler",
                "securityContext": {},
                "terminationGracePeriodSeconds": 30
            }
        },
        "test": false,
        "triggers": [
            {
                "type": "ConfigChange"
            }
        ]
    },
    "status": {
        "availableReplicas": 1,
        "conditions": [
            {
                "lastTransitionTime": "2018-06-05T02:34:59Z",
                "lastUpdateTime": "2018-06-05T02:34:59Z",
                "message": "Deployment config has minimum availability.",
                "status": "True",
                "type": "Available"
            },
            {
                "lastTransitionTime": "2018-06-05T03:28:27Z",
                "lastUpdateTime": "2018-06-05T03:28:29Z",
                "message": "replication controller \"cm-broker-2\" successfully rolled out",
                "reason": "NewReplicationControllerAvailable",
                "status": "True",
                "type": "Progressing"
            }
        ],
        "details": {
            "causes": [
                {
                    "type": "ConfigChange"
                }
            ],
            "message": "config change"
        },
        "latestVersion": 2,
        "observedGeneration": 3,
        "readyReplicas": 1,
        "replicas": 1,
        "unavailableReplicas": 0,
        "updatedReplicas": 1
    }
}
```
