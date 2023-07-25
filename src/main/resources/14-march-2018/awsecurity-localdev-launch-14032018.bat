REM # Downloads Kafka from here - http://www-eu.apache.org/dist/kafka/1.0.1/kafka_2.11-1.0.1.tgz extract in D:\FlinkKafka\
REM # Downloads Flink from here - http://www-us.apache.org/dist/flink/flink-1.4.2/flink-1.4.2-bin-scala_2.11.tgz extract in D:\FlinkKafka\
REM # Downloads ELK 6.2.2 and extract all threes in D:\apporchid\ELK\


D:

rmdir /s /q tmp


cd D:\apporchid\ELK\elasticsearch-6.2.2\

rmdir /s /q data
rmdir /s /q logs

mkdir data
mkdir logs

start "elasticsearch 14-March-2018-data" CMD /c bin\elasticsearch.bat

timeout 15;



cd D:\apporchid\ELK\kibana-6.2.2-windows-x86_64\
start "kibana 14-March-2018-data" CMD /c bin\kibana.bat

timeout 15


start "14-March-2018-data	es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/awsecurity_lenel_data_demo"
start "14-March-2018-data	es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/awsecurity_vpn_demo"
start "14-March-2018-data	es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/awsecurity_netflow_stealthwatch_demo"
start "14-March-2018-data	es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/awsecurity_center_data_demo"
start "14-March-2018-data	es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/awsecurity_vpn"
start "14-March-2018-data	es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/_all"

timeout 15

cd D:\apporchid\ELK\logstash-6.2.2\

rmdir /s /q  lenel_data_
rmdir /s /q  netflow_stealthwatch_data_
rmdir /s /q  security_center_data_
rmdir /s /q  vpn_data_
rmdir /s /q  data

mkdir lenel_data_
mkdir netflow_stealthwatch_data_
mkdir security_center_data_
mkdir vpn_data_
mkdir data

cd D:\apporchid\ELK\logstash-6.2.2\bin\

start "14-March-2018-data	es del" CMD /c powershell Invoke-WebRequest -Method DELETE -Uri "http://localhost:9200/awsecurity_vpn_demo"

start "logstash_vpn 14-March-2018-data" CMD /c logstash.bat -r --debug --node.name logstash1004 --path.data vpn_data_ -f awsecurity-logstash_vpn_14032018.conf 

timeout 15