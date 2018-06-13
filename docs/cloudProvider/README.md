
# OCManager Cloud Provider REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_


## Baremetal Management

### 1 Physical host provisioning

```
POST /v2/api/cloudprovider/physicalinstance/{tenantId}/provision
```

__request body__

```
{
     "name":"test-3-10",  //物理机名称
     "description":"test-3-10", //物理机描述信息	
     "pool_id": "111", //资源池UUID
     "productId":"00e65b4ffb064fbda14cdfcaa18023b9", //物理机产品UUID	必填字段
     "physicalInstanceImageId":"05a7cb947fad40cf99483b59ca86ec25", //物理机镜像UUID	必填字段
     "vpcId": "111", //VPC UUID
     "domain": "domain_1", //安全域 必填字段
     "securityGroupIds": ["111"], //安全组 UUID	必填字段
     "password":"123456", //root用户密码
}
```

__response__

```
{
     "success": true,
     "message": null,
     "entity": {
       "id": "74b75d0ddf8449f897db20571cd26fb9",
       "userId": "6a81e8c14cf74cb09cbbd5bd1ed0c65e",
       "userName": null,
       "organizationId": "d3701686cc0b4f0da4eead39fa807bd7",
       "organizationName": null,
       "poolId": "03d13aa48350404c85cc872749c0939b",
       "createdAt": "2017-03-12 17:50:12",
       "updatedAt": "2017-03-12 17:50:12",
       "status": 7,
       "type": 16,
       "operateType": "create",
       "adminRemark": null,
       "managerRemark": null,
       "errorMessage": null,
       "metadata": {
         "id": "a4e1f71cba834085a579a4a5eec0ce6b",
         "orderId": "74b75d0ddf8449f897db20571cd26fb9",
         "physicalInstanceId": null,
         "name": "zhao-3-10",
         "description": "zhao-3-10",
         "productId": "00e65b4ffb064fbda14cdfcaa18023b9",
         "productName": null,
         "physicalInstanceImageId": "05a7cb947fad40cf99483b59ca86ec25",
         "imageName": null,
         "businessNetworkIds": [
           "b63750b1-536a-4f10-8715-752d429c754e"
         ],
         "managementNetworkId": null,
         "securityGroupIds": null,
         "keyPairId": null,
         "password": "123456",
         "poolId": "03d13aa48350404c85cc872749c0939b",
         "availabilityZone": null,
         "vpcId": null,
         "vpcName": null,
         "domain": null,
         "organizationId": "d3701686cc0b4f0da4eead39fa807bd7",
         "organizationName": null,
         "businessId": "168cf25d264a446880a6818beefee252",
         "businessName": null,
         "projectId": "06d808f0eac54b3ca054d9846daa6459",
         "projectName": null,
         "createdAt": "2017-03-12 17:50:12",
         "createdBy": "6a81e8c14cf74cb09cbbd5bd1ed0c65e",
         "successed": null,
         "regionName": null,
         "openstackFlavorId": null,
         "openstackImageId": null
       },
       "deleted": false,
       "actual": true,
       "approvalRoute": 4,
       "regionName": null
     }
}
```

### 2 Provisioning status query (?)

```
GET /v2/api/cloudprovider/physicalinstance/{tenantId}/{hostId}/status
```

__response__

```
{
    "id": "bfa9f2c8-abce-4288-9594-335799ae9c95",
    "status": "Done" //三种状态: Provisioning(创建中), Done(创建成功), Failure(创建失败)
}
```

### 3 Physical host list

```
GET /v2/api/cloudprovider/physicalinstance/{tenantId}/list
```

__response__

```
{
  "success": true,
  "message": null,
  "entity": [
    {
      "id": "bfa9f2c8-abce-4288-9594-335799ae9c95",
      "name": null,
      "description": "zhao-3-13",
      "imageId": "d243992f-9c01-4dce-a52f-328cfa0083c0",
      "imageName": null,
      "accessipv4": null,
      "accessipv6": null,
      "configDrive": null,
      "progress": 0,
      "keyName": null,
      "hostId": null,
      "taskState": null,
      "powerState": null,
      "vmState": null,
      "host": null,
      "instanceName": null,
      "hypervisorHostname": null,
      "availabilityZone": null,
      "launchedAt": null,
      "terminatedAt": null,
      "adminPass": "123456",
      "securityGroupId": null,
      "addressId": null,
      "status": null,
      "diskConfig": "MANUAL",
      "osExtendedVolumesAttachedIds": [
        ""
      ],
      "metadataId": null,
      "productId": "00f2b6dd61b34989ba64614979bfd53b",
      "productName": null,
      "poolId": "03d13aa48350404c85cc872749c0939b",
      "regionName": null,
      "organizationId": "d3701686cc0b4f0da4eead39fa807bd7",
      "organizationName": null,
      "orderId": "9b6425f7df17431ebc9b8bfa2b0a82da",
      "businessId": "168cf25d264a446880a6818beefee252",
      "businessName": null,
      "projectId": "06d808f0eac54b3ca054d9846daa6459",
      "projectName": null,
      "vpcId": null,
      "vpcName": null,
      "domain": null,
      "createdBy": "6a81e8c14cf74cb09cbbd5bd1ed0c65e",
      "createdAt": "2017-03-13 10:23:51",
      "updatedAt": null
    },
    {
      "id": "bd32726b-3a58-454d-b52a-cd382767e175",
      "name": null,
      "description": "zhao-3-13",
      "imageId": "d243992f-9c01-4dce-a52f-328cfa0083c0",
      "imageName": null,
      "accessipv4": null,
      "accessipv6": null,
      "configDrive": null,
      "progress": 0,
      "keyName": null,
      "hostId": null,
      "taskState": null,
      "powerState": null,
      "vmState": null,
      "host": null,
      "instanceName": null,
      "hypervisorHostname": null,
      "availabilityZone": null,
      "launchedAt": null,
      "terminatedAt": null,
      "adminPass": "123456",
      "securityGroupId": null,
      "addressId": null,
      "status": null,
      "diskConfig": "MANUAL",
      "osExtendedVolumesAttachedIds": [
        ""
      ],
      "metadataId": null,
      "productId": "00f2b6dd61b34989ba64614979bfd53b",
      "productName": null,
      "poolId": "03d13aa48350404c85cc872749c0939b",
      "regionName": null,
      "organizationId": "d3701686cc0b4f0da4eead39fa807bd7",
      "organizationName": null,
      "orderId": "488d6ad601ab4a8b90c1bb75b02ccdc3",
      "businessId": "168cf25d264a446880a6818beefee252",
      "businessName": null,
      "projectId": "06d808f0eac54b3ca054d9846daa6459",
      "projectName": null,
      "vpcId": null,
      "vpcName": null,
      "domain": null,
      "createdBy": "6a81e8c14cf74cb09cbbd5bd1ed0c65e",
      "createdAt": "2017-03-13 09:59:29",
      "updatedAt": null
    },
    {
      "id": "23a97eb4-305d-4653-bd91-d03fce90244e",
      "name": null,
      "description": "zhao-3-13",
      "imageId": "d243992f-9c01-4dce-a52f-328cfa0083c0",
      "imageName": null,
      "accessipv4": null,
      "accessipv6": null,
      "configDrive": null,
      "progress": 0,
      "keyName": null,
      "hostId": null,
      "taskState": null,
      "powerState": null,
      "vmState": null,
      "host": null,
      "instanceName": null,
      "hypervisorHostname": null,
      "availabilityZone": null,
      "launchedAt": null,
      "terminatedAt": null,
      "adminPass": "123456",
      "securityGroupId": null,
      "addressId": null,
      "status": null,
      "diskConfig": "MANUAL",
      "osExtendedVolumesAttachedIds": [
        ""
      ],
      "metadataId": null,
      "productId": "00f2b6dd61b34989ba64614979bfd53b",
      "productName": null,
      "poolId": "03d13aa48350404c85cc872749c0939b",
      "regionName": null,
      "organizationId": "d3701686cc0b4f0da4eead39fa807bd7",
      "organizationName": null,
      "orderId": "f8b0876ee61441bcb10b9e7518b7f52b",
      "businessId": "168cf25d264a446880a6818beefee252",
      "businessName": null,
      "projectId": "06d808f0eac54b3ca054d9846daa6459",
      "projectName": null,
      "vpcId": null,
      "vpcName": null,
      "domain": null,
      "createdBy": "6a81e8c14cf74cb09cbbd5bd1ed0c65e",
      "createdAt": "2017-03-13 09:55:44",
      "updatedAt": null
    }
  ]
}
```

## Host Flavor Management

### 1 Host flavor list

```
GET /v2/api/cloudprovider/flavor/{tenantId}/list
```

__response__

```
{
  "success": true,
  "message": null,
  "entity": [
    {
      "id": "4219e91f-05a5-43d7-8d8c-d87634efbe92",
      "name": "physical_instance_template_zhao",
      "description": "zhao",
      "ram": 10240,
      "disk": 10,
      "vcpus": 8,
      "osFlvExtData": null,
      "osFlvDisabled": null,
      "swap": 0,
      "rxtxFactor": 1,
      "osFlavorAccess": null,
      "poolId": "7b8f0f5e2fbb4d9aa2d5fd55466d639x",
      "createdAt": "2016-11-11 10:26:58",
      "updatedAt": null,
      "createdBy": null,
      "extraSpecs": [
        {
          "id": null,
          "extraSpecKey": "cpu_arch",
          "extraSpecValue": "x86_64"
        },
        {
          "id": null,
          "extraSpecKey": "capabilities:boot_option",
          "extraSpecValue": "local"
        }
      ]
    },
    {
      "id": "f3fae91b-a8f5-46de-9778-07e76f91971c",
      "name": "physical_instance_template",
      "description": "zhao",
      "ram": 1024,
      "disk": 20480,
      "vcpus": 1,
      "osFlvExtData": null,
      "osFlvDisabled": null,
      "swap": 0,
      "rxtxFactor": 1,
      "osFlavorAccess": null,
      "poolId": "7b8f0f5e2fbb4d9aa2d5fd55466d639x",
      "createdAt": "2016-11-08 12:54:00",
      "updatedAt": null,
      "createdBy": null,
      "extraSpecs": []
    },
    {
      "id": "1",
      "name": "physical_template_1",
      "description": null,
      "ram": 1024,
      "disk": 10240,
      "vcpus": 1,
      "osFlvExtData": null,
      "osFlvDisabled": null,
      "swap": null,
      "rxtxFactor": null,
      "osFlavorAccess": null,
      "poolId": "7b8f0f5e2fbb4d9aa2d5fd55466d639x",
      "createdAt": null,
      "updatedAt": null,
      "createdBy": "1",
      "extraSpecs": []
    },
    {
      "id": "2",
      "name": "physical_template_2",
      "description": null,
      "ram": 2048,
      "disk": 10240,
      "vcpus": 2,
      "osFlvExtData": null,
      "osFlvDisabled": null,
      "swap": null,
      "rxtxFactor": null,
      "osFlavorAccess": null,
      "poolId": "7b8f0f5e2fbb4d9aa2d5fd55466d639x",
      "createdAt": null,
      "updatedAt": null,
      "createdBy": "1",
      "extraSpecs": [
        {
          "id": null,
          "extraSpecKey": "test_key_2",
          "extraSpecValue": "test_value_2"
        }
      ]
    }
  ]
}
```

## Host Image Management

### 1 Host Image list (?)

```
GET /v2/api/cloudprovider/image/{tenantId}/list
```

__response__

```

```

## VPC Management

### 1 VPC Create

```
POST /v2/api/cloudprovider/vpc/{tenantId}/create
```

__request__

```
{
     "organizationId":"a98a944d4f704892a93fe72c56353a0d", //VPC使用部门ID（required = false）。运维创建VPC时，需要指定VPC的使用部门
     "name":"epc的第二个VPC", // VPC名称 （required = true)
     "cidr":"192.168.3.0/24", //私有网络默认网段 （required = true）
     "gateway":"192.168.3.1", // 私有网络默认网关（required = false）没有指定就是默认
     "businessId":"4d3afa9fdd0a4330b71cc3512891df69", // 业务的Id（required = true)
     "projectId":"d6585323e6b0496588f3405bd984c78b", // 项目Id （required = true）
     "domain":"aed318549ff511e6b70cfa163eb66335" //安全域Id（required = true）
  }         
```

__response__

```
{
  "success": true,
  "message": null,
  "entity": {
    "organizationId": "11c763e7ec454fe189eac671d0e7f5f4",
    "userId": "5d693d923337416f98e3a9f364fe2f43",
    "userName": null,
    "organizationId": "a98a944d4f704892a93fe72c56353a0d",
    "organizationName": null,
    "poolId": "7b8f0f5e2fbb4d9aa2d5fd55466d639x",
    "createdAt": "2016-10-27 10:19:37",
    "updatedAt": null,
    "status": 2,
    "type": 21,
    "operateType": "create",
    "adminRemark": null,
    "managerRemark": null,
    "metadata": {
      "id": "a98a944d4f704892a93fe72c56353a0d",
      "id": "4212d80165c44d6d9d3c1063c1191270",
      "deleted": false,
      "orderId": "11c763e7ec454fe189eac671d0e7f5f4",
      "businessId": "4d3afa9fdd0a4330b71cc3512891df69",
      "projectId": "d6585323e6b0496588f3405bd984c78b",
      "createAt": null,
      "successed": false,
      "name": "epc的第二个VPC",
      "networkId": "4f6fa8ed-ef37-4d73-bad6-117c0b4f15dc",
      "vpcId": null,
      "cidr": "192.168.3.0/24",
      "gateway": "192.168.3.1",
      "domain": "DMZ"
    },
    "deleted": false,
    "actual": true,
    "approvalRoute": 2,
    "regionName": null
  }
}
```

### 2 VPC list

```
GET /v2/api/cloudprovider/vpc/{tenantId}/list
```

__response__

```
{
  "success": true,
  "message": null,
  "entity": [
    {
      "id": "eb00a44ec12b453aaa2f9c7f7d8acecb",
      "organizationId": "a98a944d4f704892a93fe72c56353a0d",
      "poolId": "7b8f0f5e2fbb4d9aa2d5fd55466d639x",
      "regionName": "RegionOne",
      "orderId": "80728f91d84e40e0b224f33a4c248b99",
      "userId": "5d693d923337416f98e3a9f364fe2f43",
      "deleted": false,
      "businessId": "4d3afa9fdd0a4330b71cc3512891df69",
      "projectId": "d6585323e6b0496588f3405bd984c78b",
      "productId": null,
      "createAt": "2016-10-27 10:34:31",
      "updateAt": null,
      "name": "我的vpc",
      "externalNetworkId": "4f6fa8ed-ef37-4d73-bad6-117c0b4f15dc",
      "routerResource": {
        "id": "de94c127-c545-48d8-bdde-63f1f15a5bd5",
        "organizationId": null,
        "poolId": null,
        "regionName": null,
        "orderId": null,
        "userId": null,
        "deleted": false,
        "businessId": null,
        "projectId": null,
        "productId": null,
        "createAt": null,
        "updateAt": null,
        "name": "我的vpc的虚拟路由",
        "status": "ACTIVE",
        "networkId": "4f6fa8ed-ef37-4d73-bad6-117c0b4f15dc",
        "subnetId": "f464201d-503d-4b06-b21e-398fc62f38dc",
        "ipAddress": "10.10.0.5",
        "admin": null,
        "hostRoutes": null,
        "firewallId": null,
        "vpcId": null
      },
      "firewallResource": {
        "id": "54dd1dae-f3a9-4aa7-b1bf-8509576678a3",
        "organizationId": null,
        "poolId": null,
        "regionName": null,
        "orderId": null,
        "userId": null,
        "deleted": false,
        "businessId": null,
        "projectId": null,
        "productId": null,
        "createAt": null,
        "updateAt": null,
        "name": "我的vpc的防火墻",
        "status": "PENDING_CREATE",
        "description": "这是我的vpc的防火墙",
        "adminStateUp": true,
        "firewallPolicyId": "1e89c7d9-d56f-4992-b934-8a84a5a0d4d6",
        "policies": null,
        "routers": null,
        "vpcId": null
      },
      "cidr": null,
      "gateway": null,
      "domain": "DMZ"
    },
    {
      "id": "2545207b53504b00a6118575605e883e",
      "organizationId": "a98a944d4f704892a93fe72c56353a0d",
      "poolId": "7b8f0f5e2fbb4d9aa2d5fd55466d639x",
      "regionName": "RegionOne",
      "orderId": "d626f4edc3ad4596a4d9f8255542e7cd",
      "userId": "5d693d923337416f98e3a9f364fe2f43",
      "deleted": false,
      "businessId": "4d3afa9fdd0a4330b71cc3512891df69",
      "projectId": "d6585323e6b0496588f3405bd984c78b",
      "productId": null,
      "createAt": "2016-10-28 15:12:47",
      "updateAt": null,
      "name": "他的VPC",
      "externalNetworkId": "4f6fa8ed-ef37-4d73-bad6-117c0b4f15dc",
      "routerResource": {
        "id": "779702e9-5fe3-43eb-a63c-b510df7abb8a",
        "organizationId": null,
        "poolId": null,
        "regionName": null,
        "orderId": null,
        "userId": null,
        "deleted": false,
        "businessId": null,
        "projectId": null,
        "productId": null,
        "createAt": null,
        "updateAt": null,
        "name": "他的VPC的虚拟路由",
        "status": "ACTIVE",
        "networkId": "4f6fa8ed-ef37-4d73-bad6-117c0b4f15dc",
        "subnetId": "f464201d-503d-4b06-b21e-398fc62f38dc",
        "ipAddress": "10.10.0.6",
        "admin": null,
        "hostRoutes": null,
        "firewallId": null,
        "vpcId": null
      },
      "firewallResource": {
        "id": "92b20dc8-5fb3-41b5-b411-1991891bbac6",
        "organizationId": null,
        "poolId": null,
        "regionName": null,
        "orderId": null,
        "userId": null,
        "deleted": false,
        "businessId": null,
        "projectId": null,
        "productId": null,
        "createAt": null,
        "updateAt": null,
        "name": "他的VPC的防火墻",
        "status": "PENDING_CREATE",
        "description": "这是他的VPC的防火墙",
        "adminStateUp": true,
        "firewallPolicyId": "8d23e168-4cfd-47be-9838-8a2f130939d5",
        "policies": null,
        "routers": null,
        "vpcId": null
      },
      "cidr": null,
      "gateway": null,
      "domain": "DMZ"
    }
  ]
}
```

### 3 VPC update (?)

```
PUT /v2/api/cloudprovider/vpc/{tenantId}/update
```

__resquest__

```
{
}
```

__response__

```
{
}
```

### 4 VPC delete (?)

```
DELETE /v2/api/cloudprovider/vpc/{tenantId}/{vpc_id}
```

__request__

```
{
}
```

__response__

```
{
}
```

## Resource Pool Management

### 1 Resource Pool List (?)

```
GET /v2/api/cloudprovider/pool/{tenantId}/list
```

__response__

```
[
    {
        "id": "7b8f0f5e2fbb4d9aa2d5fd55466d639v",
        "name": "pod_1",
        "description": null,
        "state": 1,
        "type": null,
        "regions": null
    },
    {
        "id": "d3701686cc0b4f0da4eead39fa807bd7",
        "name": "pod_2",
        "description": null,
        "state": 1,
        "type": null,
        "regions": null
    }
]
```

## Cluster Template Management

### 1 Cluster template list (?)

```
GET /v2/api/cloudprovider/clustertemplate/{tenantId}/list
```

__response__

```
[
    {
        "name": "template01",
        "desc": "1.NameNode：建议8核64G，建议2T，以HA方式部署 2.DataNode：主机配置8C32G存储5T，建议99台，支撑495T存储"
    },
    {
        "name": "template02",
        "desc": "1.NameNode：建议8核64G，建议2T，以HA方式部署 2.DataNode：主机配置8C32G存储5T，建议99台，支撑495T存储"
    }
]
```
