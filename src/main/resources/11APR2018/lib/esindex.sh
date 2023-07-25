#! /bin/bash

#	
#	 ______                  _____                __              __     
#	/\  _  \                /\  __`\             /\ \      __    /\ \    
#	\ \ \L\ \  _____   _____\ \ \/\ \  _ __   ___\ \ \___ /\_\   \_\ \   
#	 \ \  __ \/\ '__`\/\ '__`\ \ \ \ \/\`'__\/'___\ \  _ `\/\ \  /'_` \  
#	  \ \ \/\ \ \ \L\ \ \ \L\ \ \ \_\ \ \ \//\ \__/\ \ \ \ \ \ \/\ \L\ \ 
#	   \ \_\ \_\ \ ,__/\ \ ,__/\ \_____\ \_\\ \____\\ \_\ \_\ \_\ \___,_\
#	    \/_/\/_/\ \ \/  \ \ \/  \/_____/\/_/ \/____/ \/_/\/_/\/_/\/__,_ /
#	             \ \_\   \ \_\                                           
#	              \/_/    \/_/                                           
#	
#
#
# Credit - girish.mahajan@accionlabs.com
#

. ./lib/log.sh

speak "working on elastic search indexes"

function riverRefreshElasticSearch {
rm -rf ./.esindexes/

log_warn "Removing awsecurity_search_users index"

for index in awsecurity_search_users awsecurity_app_metrics; do curl -sS -X DELETE localhost:9200/$index ; done

log_warn "Removing all ES indexes"

# careful
# curl -X DELETE localhost:9200/_all

log_warn "Renew ES index awsecurity_search_users"

arangoexport --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --overwrite true --type csv --collection cloudseer_peoples --fields displayName,logon,badgeId,user_badgecount_alltime,user_vpncount_alltime,empNum,empId,_key --output-directory .esindexes

sed -i "s/empId,_key/empId,dbKey/g" .esindexes/cloudseer_peoples.csv

log_debug "exported people csv sample -> for ES index"

head -4 .esindexes/cloudseer_peoples.csv


log_info "importing into elastic now"

# no real time
cat .esindexes/cloudseer_peoples.csv | /apporchid/solutions/awsecurity/apps/logstash-6.2.2/bin/logstash -e 'input {stdin {}} filter { csv { columns =>[ "displayName", "logon", "badgeId", "alltime_badge_records","alltime_vpn_records","empNum", "empId", "dbKey" ] separator => "," }} filter { mutate { convert => { "badgeId" => "integer" } }} output { elasticsearch { index => "awsecurity_search_users" } stdout { codec => rubydebug {} }}'


# Dashboard Metrics Extracts ( login page )
arangoexport --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --overwrite true --type csv --collection cloudseer_dashboard --fields global_alltime_badgecount,global_alltime_vpncount,global_total_services,global_data_processed_at,_key --output-directory .esindexes

# Import Indexes
cat .esindexes/cloudseer_dashboard.csv | head -2 | tail -1 | /apporchid/solutions/awsecurity/apps/logstash-6.2.2/bin/logstash -e 'input {stdin {}} filter { csv { columns =>[ "global_alltime_badgecount", "global_alltime_vpncount", "global_total_services", "global_data_processed_at", "_key"] separator => "," }} output { elasticsearch { index => "awsecurity_app_metrics" } stdout { codec => rubydebug {} }}'


log_info "ES index back online"

curl -i http://localhost:9200/_cat/indices
echo ""
echo ""
log_info "count of index in awsecurity_search_users"
curl -i http://localhost:9200/_cat/count/awsecurity_search_users
curl -i http://localhost:9200/_cat/count/awsecurity_app_metrics

}

# Begin
riverRefreshElasticSearch

