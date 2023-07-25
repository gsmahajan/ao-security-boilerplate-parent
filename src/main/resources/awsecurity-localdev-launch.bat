REM # Downloads Kafka from here - http://www-eu.apache.org/dist/kafka/1.0.1/kafka_2.11-1.0.1.tgz extract in D:\FlinkKafka\
REM # Downloads Flink from here - http://www-us.apache.org/dist/flink/flink-1.4.2/flink-1.4.2-bin-scala_2.11.tgz extract in D:\FlinkKafka\
REM # Downloads ELK 6.2.2 and extract all threes in D:\apporchid\ELK\


D:

rmdir /s /q tmp

cd D:\apporchid\security-workspace\ao-security-boilerplate-parent\src\main\resources

javaapps-topology.jpg
apps-topology.jpg
vms_topology.jpg


REM start "dummy_log_generator" CMD /c "C:\Program Files\Git\git-bash.exe" --login -i -c "/apporchid/security-workspace/ao-security-boilerplate-parent/src/main/resources/samples/append-dummy-log.sh"


rmdir /s /q tmp

cd D:\apporchid\ELK\elasticsearch-6.2.2\

rmdir /s /q data
rmdir /s /q logs

mkdir data
mkdir logs

start "elasticsearch" CMD /c bin\elasticsearch.bat

timeout 10;



cd D:\apporchid\ELK\kibana-6.2.2-windows-x86_64\
start "kibana" CMD /c bin\kibana.bat

timeout 10


cd D:\apporchid\FlinkKafka\kafka_2.11-1.0.1\

REM # start zookeeper
start "zookeeper" CMD /c bin\windows\zookeeper-server-start.bat config/zookeeper.properties

timeout 10

REM # start kafka-server
start "kafka server" CMD /c bin\windows\kafka-server-start.bat config/server.properties

timeout 10

start "kafka" CMD /c bin\windows\kafka-topics.bat --delete --zookeeper localhost:2181  --topic lenelTopic
start "kafka" CMD /c bin\windows\kafka-topics.bat --delete --zookeeper localhost:2181  --topic vpnTopic
start "kafka" CMD /c bin\windows\kafka-topics.bat --delete --zookeeper localhost:2181  --topic stealthWatchTopic
start "kafka" CMD /c bin\windows\kafka-topics.bat --delete --zookeeper localhost:2181  --topic securityCenterTopic
start "kafka" CMD /c bin\windows\kafka-topics.bat --delete --zookeeper localhost:2181  --topic testTopic

start "es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/awsecurity_lenel_data_demo"
start "es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/awsecurity_vpn_demo"
start "es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/awsecurity_netflow_stealthwatch_demo"
start "es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/awsecurity_center_data_demo"



timeout 10
REM # create topic lenelTopic

start "kafka c" CMD /c bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic lenelTopic
start "kafka c" CMD /c bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic vpnTopic
start "kafka c" CMD /c bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic stealthWatchTopic
start "kafka c" CMD /c bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic securityCenterTopic
start "kafka c" CMD /c bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic testTopic

timeout 10

cd D:\apporchid\ELK\logstash-6.2.2\

rmdir /s /q  lenel_data_
rmdir /s /q  netflow_stealthwatch_data_
rmdir /s /q  security_center_data_
rmdir /s /q  vpn_data_

mkdir lenel_data_
mkdir netflow_stealthwatch_data_
mkdir security_center_data_
mkdir vpn_data_


cd D:\apporchid\ELK\logstash-6.2.2\bin\


start "logstash_lenel" CMD /c logstash.bat -r --debug --node.name logstash1001 --path.data lenel_data_ -f awsecurity-logstash_lenel.conf --path.settings config_lenel
timeout 10
start "logstash_netflow-stealthwatch" CMD /c logstash.bat -r --debug --node.name logstash1002 --path.data netflow_stealthwatch_data_ -f awsecurity-logstash_netflow-stealthwatch.conf --path.settings config_netflow_stealthwatch
timeout 10
start "logstash_security_center_data" CMD /c logstash.bat -r --debug --node.name logstash1003 --path.data security_center_data_ -f awsecurity-logstash_security_center_data.conf  --path.settings config_security_center
timeout 10
start "logstash_vpn" CMD /c logstash.bat -r --debug --node.name logstash1004 --path.data vpn_data_ -f awsecurity-logstash_vpn.conf --path.settings config_vpn
timeout 10

cd d:\apporchid\FlinkKafka\flink-1.4.2-bin-scala_2.11\flink-1.4.2\bin\

start "Flink Local" CMD /c D:\apporchid\FlinkKafka\flink-1.4.2-bin-scala_2.11\flink-1.4.2\bin\start-local.bat


cd D:\apporchid\FlinkKafka\kafka_2.11-1.0.1\

bin\windows\kafka-topics.bat --list --zookeeper localhost:2181
timeout 225

chrome http://localhost:5601/app/timelion
chrome chrome-extension://ffmkiejjmecolpfloofpjologoblkegm/elasticsearch-head/index.html


REM ================================= Phase II ========================================


D:

rmdir /s /q tmp

cd D:\apporchid\ELK\elasticsearch-6.2.2\

rmdir /s /q data
rmdir /s /q logs

mkdir data
mkdir logs

start "elasticsearch vanilla" CMD /c bin\elasticsearch.bat

timeout 10;


REM cd D:\apporchid\ArangoDB\
REM rd /Q /S ArangoDB3-3.3.5-1_win64
REM robocopy /MIR /CREATE D:\apporchid\ArangoDB\orig\ArangoDB3-3.3.5-1_win64\ D:\apporchid\ArangoDB\ArangoDB3-3.3.5-1_win64\

cd D:\apporchid\ArangoDB\ArangoDB3-3.3.5-1_win64\

start "arangodb vanilla" CMD /c usr\bin\arangod.exe
timeout 20


start "databot" "%PROGRAMFILES%\Git\git-bash.exe" --cd="D:/apporchid/security-workspace/ao-security-boilerplate-parent/src/main/resources/11APR2018/" ./ingest.sh
