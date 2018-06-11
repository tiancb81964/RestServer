## CM Workflow REST APIs

__NOTE: All the rest request should set__ _Accept: application/json_ __and__ _Content-Type: application/json_

### bill APIs

1. 获取所有申请工单
```
GET /v2/api/workflow/apply/bill
```
__response:__
```
[
 {
  "applyDate": "2018-06-06 16:03:09.0",
  "applyQuota": "{aa}",
  "applyReason": "bb",
  "applyUser": "zhaoyim",
  "id": "999",
  "status": 0
 }
 ......
]
```

2. 获取一个用户申请的所有工单
```
GET /v2/api/workflow/apply/bill/user/{userName}
```
__response:__
```
[
 {
  "applyDate": "2018-06-06 16:03:09.0",
  "applyQuota": "{aa}",
  "applyReason": "bb",
  "applyUser": "zhaoyim",
  "id": "999",
  "status": 0
 }
 ......
]
```

3. 创建一个工单
```
POST /v2/api/workflow/apply/bill
```
__request body:__
```
{
  "applyQuota": "{fff}",
  "applyReason": "ggg",
  "applyUser": "gsm",
  "id": "888",
  "status": 1
}
```

__response:__
```
{
  "applyDate": "2018-06-08 10:36:01.0",
  "applyQuota": "{fff}",
  "applyReason": "ggg",
  "applyUser": "gsm",
  "id": "888",
  "status": 1
}
```



4. 更新一个工单
```
PUT /v2/api/workflow/apply/bill
```

__request body:__
```
{
  "applyQuota": "{oooo}",
  "applyReason": "ppp",
  "applyUser": "demouser001",
  "id": "888",
  "status": 2
}
```


__response:__
```
{
  "applyDate": "2018-06-08 10:36:01.0",
  "applyQuota": "{oooo}",
  "applyReason": "ppp",
  "applyUser": "demouser001",
  "id": "888",
  "status": 2
}
```


5. 更新一个工单
```
PUT /v2/api/workflow/apply/bill/{id}/status/{status}
```

__request body:__
```
{

}
```


__response:__
```
{
  "applyDate": "2018-06-08 10:36:01.0",
  "applyQuota": "{oooo}",
  "applyReason": "ppp",
  "applyUser": "demouser001",
  "id": "888",
  "status": 777
}
```



6. 删除一个工单
```
DELETE /v2/api/workflow/apply/bill/{id}
``` 

__response:__
```
{
  "message": "888",
  "resCodel": 200,
  "status": "delete bill success"
}
```



### workflow APIs

1. 启动实例（服务和工具）申请流程
```
POST /v2/api/workflow/instance/{tenantId}/start/process
```
__request body:__
```
{
    "assigneeName": "zhaoyim"
}
```

__response:__
```
{
  "processInstanceId": "105001"
}
```


2. 完成实例（服务和工具）申请流程任务
```
POST /v2/api/workflow/instance/complete/task/{taskId}/{flowAction}

NOTE：flowAction=IPApplicantCancel_: means applicant cancel the process
NOTE：flowAction=IPApplicantSubmit_: means applicant complete and go to next task
NOTE：flowAction=null: means tenant admin completethe task

```
__request body:__
```
{
    "assigneeName": "zhaoyim"
}
```

__response:__
```
{
  "message": "taskId: 105005  assignee: zhaoyim",
  "resCodel": 200,
  "status": "complete task success"
}
```


3. 列出任务分配者的所有正在进行的任务

```
GET /v2/api/workflow/common/list/assignee/{assigneeName}/tasks
```
__response:__
```
[
{
  "assignee": "zhaoyim",
  "createTime": "2018-06-08T02:51:04.156Z[UTC]",
  "executionId": "105009",
  "processDefinitionId": "serviceInstanceProcess:1:75004",
  "processInstanceId": "105009",
  "processVariables": {},
  "taskId": "105013",
  "taskName": "tenantMember"
}, {
  "assignee": "zhaoyim",
  "createTime": "2018-06-02T03:03:30.644Z[UTC]",
  "executionId": "17501",
  "processDefinitionId": "test001:2:12504",
  "processInstanceId": "17501",
  "processVariables": {},
  "taskId": "17504",
  "taskName": "step1"
}, 
...
]
```

4. 列出候选者可以处理的所有正在进行的任务

```
GET /v2/api/workflow/common/list/candidate/{candidateName}/tasks
```
__response:__
```
[
{
  "createTime": "2018-06-08T02:59:47.735Z[UTC]",
  "executionId": "105009",
  "processDefinitionId": "serviceInstanceProcess:1:75004",
  "processInstanceId": "105009",
  "processVariables": {},
  "taskId": "105018",
  "taskName": "tenantAdmin"
}, {
  "createTime": "2018-06-05T09:53:45.727Z[UTC]",
  "executionId": "92501",
  "processDefinitionId": "serviceInstanceProcess:1:75004",
  "processInstanceId": "92501",
  "processVariables": {},
  "taskId": "95004",
  "taskName": "tenantAdmin"
},
...
]
```

5. 候选者接受其可处理的任务
```
POST /v2/api/workflow/common/accept/candidate/task/{taskId}
```
__request body:__
```
{
    "assigneeName": "demouser001"
}
```

__response:__
```
{
  "message": "taskId: 105018  assignee: demouser001",
  "resCodel": 200,
  "status": "accept candidate task success"
}
```

6. 启动租户申请流程
```
POST /v2/api/workflow/tenant/{tenantId}/start/process
```
__request body:__
```
{
    "assigneeName": "demouser001"
}
```

__response:__
```
{
  "processInstanceId": "150024"
}
```


7. 完成租户申请流程任务
```
POST /v2/api/workflow/tenant/complete/task/{taskId}/{flowAction}

NOTE：flowAction=TPApplicantCancel_: means applicant cancel the process
NOTE：flowAction=TPApplicantSubmit_: means applicant complete and go to next task
NOTE：flowAction=null: means tenant admin completethe task

```
__request body:__
```
{
    "assigneeName": "demouser001"
}
```

__response:__
```
{
  "message": "taskId: 150029  assignee: demouser001",
  "resCodel": 200,
  "status": "complete task success"
}
```









