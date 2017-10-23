#！/bin/sh
helpMsg(){
	echo "USAGE: sh $0 clusterName rangerIP rangerUser rangerPassword" 
	echo " e.g.: sh $0 mycluster 127.0.0.1 admin admin" 
}
if [ $# != 4 ] ; then 
helpMsg
exit 1; 
fi 
CLUSTER=$1
IP=$2
USER=$3
PASSWD=$4
curl -i -XPOST http://$USER:$PASSWD@$IP:6080/service/public/v2/api/policy/ -d '{
    "description": "Common policy for OCDP",
    "isAuditEnabled": true,
    "isEnabled": true,
    "name": "common-policy",
    "policyItems": [
        {
            "accesses": [
                {
                    "isAllowed": true,
                    "type": "execute"
                }
            ],
            "conditions": [],
            "delegateAdmin": false,
            "groups": ["public"],
            "users": [
            ]
        }
    ],
    "resources": {
        "path": {
            "isExcludes": false,
            "isRecursive": false,
            "values": [
                "/",
                "/hdp",
                "/hdp/apps",
                "/hdp/apps/*"
            ]
        }
    },
    "service": "'$CLUSTER'_hadoop",
    "version": 1
}'  -H "Content-Type: application/json"
echo ""
echo "Script executed!!!"