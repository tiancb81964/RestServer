topic_name=$1
########## online env ###########
kafka_dir=/hdfs/data*/kafka-logs
hosts="zx-dn-10
zx-dn-11
zx-dn-12
zx-dn-13
zx-dn-14"
#zx-bdi-01
#zx-bdi-02
#zx-bdi-03

#########  test env #############
#kafka_dir=/kafka-logs
#hosts="xianoc1.jcloud.local01
#xianoc1.jcloud.local02
#xianoc1.jcloud.local03"

quotaSum=0
# if the unit is G
#huanSuanG=1048576
# now the unit is B
huanSuanG=1
for i in $hosts
do
	quota=`ssh $i "du -sk ${kafka_dir}/${topic_name}"|awk -F '\t' '{sum+=$1} END {print sum}'`
	let quotaSum=quotaSum+quota
done
let quotaSum=$quotaSum/$huanSuanG
echo $quotaSum
