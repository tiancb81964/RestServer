## CM Clusters REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_

### Clusters APIs

### 1获取clusters（/ocmanager/v2/api/clusters） 

	示例：http://127.0.0.1:8080/ocmanager/v2/api/clusters
    请求方式：POST

#### 1.1请求参数

##### 1.1.1基本参数

字段|类型|描述|是否必填|备注|是否常量|
----------|----------------|----|--------|------------|---|

#### 1.2返回参数

##### 1.2.1基本参数

字段|类型|描述|备注|是否常量|
----------|----------------|----|------------|---|

#### 1.3报文示例

##### 1.3.1请求报文示例

##### 1.3.2返回报文示例

__response:__

```
[
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
                "value": ""
            },
            {
                "description": "http ranger url",
                "key": "RANGER_URL",
                "value": ""
            }
        ]
    },
    {
        "cluster_admin": "admin",
        "cluster_id": "2",
        "cluster_name": "cluster_beta",
        "cluster_type": "gbase",
        "cluster_url": "http://10.1.236.111:8080",
        "env": [
            {
                "description": "http ranger url",
                "key": "RANGER_URL",
                "value": ""
            },
            {
                "description": "http ranger url",
                "key": "RANGER_URL",
                "value": ""
            }
        ]
    }
]
```
