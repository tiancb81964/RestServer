#！/bin/sh
CURRENT_ABS_PATH=$(pwd)

INSTANCEPROCESSNAME=InstanceProcess
INSTANCEPROCESSPATH=${CURRENT_ABS_PATH%/*}/workflow/InstanceProcess.bpmn

curl -XPOST -i -k http://localhost:9090/ocmanager/v2/api/workflow/common/deployment/process -d '{
    "processName": "'"$INSTANCEPROCESSNAME"'","bpmnPath": "'"$INSTANCEPROCESSPATH"'"
}' -H 'Content-Type: application/json'


TENANTPROCESSNAME=TenantProcess
TENANTPROCESSPATH=${CURRENT_ABS_PATH%/*}/workflow/TenantProcess.bpmn

curl -XPOST -i -k http://localhost:9090/ocmanager/v2/api/workflow/common/deployment/process -d '{
    "processName": "'"$TENANTPROCESSNAME"'","bpmnPath": "'"$TENANTPROCESSPATH"'"
}' -H 'Content-Type: application/json'