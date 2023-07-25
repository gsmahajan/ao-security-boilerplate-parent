#! /bin/bash
#
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

function log_warn {
        echo -e `date +%d/%m/%Y-%H:%M:%S ` "\033[33m# WARNING # " $1 "\033[0m" | tee -a $LOGFILE;
}

function log_info {
         echo -e `date +%d/%m/%Y-%H:%M:%S ` "\033[32m# INFO    # " $1 "\033[0m" | tee -a $LOGFILE;
}

function log_debug {
         echo -e `date +%d/%m/%Y-%H:%M:%S ` "\033[32m# INFO    # " $1 "\033[0m" | tee -a $LOGFILE;
}

function log_error {
        echo -e `date +%d/%m/%Y-%H:%M:%S ` "\033[31m# ERROR   # " $1 "\033[0m" | tee -a $LOGFILE;
}

function create_dead_fox_service {
	local ApplicationName="cloudseer";
	log_warn "removing foxx service $ApplicationName";
	curl -i -Ss -X DELETE -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" "http://localhost:8529/_db/_system/_admin/aardvark/foxxes?mount=/$ApplicationName&teardown=true"
	log_warn "creating hollow foxx service $ApplicationName";
	echo $(echo "{\"name\":\"cloudseer\",\"documentCollections\":[ $(for collection in $(cat collections.txt | grep -v "#"); do printf '\"%s\",' $collection; done | sed -e 's/,$//g') ], \"edgeCollections\":[ $( for edge in $(cat edges.txt | grep -v "#"); do printf '\"%s\",' $edge; done | sed -e 's/,$//g') ],\"author\":\"Girish Mahajan\",\"license\":\"Apache 2\",\"description\":\"AppOrchid CloudSeer - Project Security - 2018 &copy; \"}") > foxx-create-cloudseer.json
	curl -i -Ss -X PUT -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" "http://localhost:8529/_db/_system/_admin/aardvark/foxxes/generate?mount=%2F$ApplicationName" --data @foxx-create-cloudseer.json
}

function legacies {
	log_info "fetching all collections from arangodb";
	curl -Ss -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" localhost:8529/_api/collection | python -m json.tool | grep name | grep -v '"_' | sed -e 's/.*: "//g' | sed -e 's/".*//g' > legacies.txt
	cat legacies.txt
}

function wipe_arangodb {
	legacies
	for edge in $(cat edges.txt | grep -v "#"); do  rm -rf $edge.txt; done;
	for collection in $(cat collections.txt | grep -v "#"); do  rm -rf $collection.txt; done;
        for collection in $(cat *s.txt | grep -v "#"); do log_debug "deleting collection $collection"; curl -Ss -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -X DELETE  http://localhost:8529/_api/collection/$collection; done;
}


function truncate_arangodbs {

touch collections.txt
touch unloads.txt
touch edges.txt

log_info "truncate collections";
 
for collection in $(cat collections.txt | grep -v "#"); do log_debug "deleting $collection"; curl -Ss -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -X DELETE  http://localhost:8529/_api/collection/cloudseer_$collection; done;
for collection in $(cat collections.txt | grep -v "#"); do echo "{ \"name\": \"$(echo "cloudseer_$collection")\" }" > $collection.txt; done;
for collection in $(cat collections.txt | grep -v "#"); do log_debug "creating $collection"; curl -Ss -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -XPOST --data @$collection.txt http://localhost:8529/_api/collection ; done;

for edge in $(cat edges.txt | grep -v "#"); do log_debug "deleting $edge"; curl -sS -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -X DELETE  http://localhost:8529/_api/collection/cloudseer_$edge; done;
for edge in $(cat edges.txt | grep -v "#"); do  echo "{ \"name\": \"$(echo "cloudseer_$edge")\", \"type\": 3 }" > $edge.txt; done;
for edge in $(cat edges.txt | grep -v "#"); do log_debug "creating edge - $edge"; curl -sS -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -XPOST --data @$edge.txt http://localhost:8529/_api/collection ; done;

for edge in $(cat unloads.txt | grep -v "#"); do log_debug "unloading $edge"; curl -sS -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -XPUT  http://localhost:8529/_api/collection/cloudseer_$edge/unload; done;

}


function import_content {
	log_warn "Import Collections (5/5)"
	arangoimp --server.username root --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --file active_directory_sample.csv --type csv --collection  cloudseer_source_active_directory_samples --create-collection false
        arangoimp --server.username root --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --file badgeIds_userIds_mapping.csv --type csv --collection  cloudseer_source_badges_users_mappings --create-collection false
        arangoimp --server.username root --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --file jagas_override.csv --type csv --collection  cloudseer_source_lenels --create-collection false

        arangoimp --server.username root --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --file vpn_authentications_sanjay_24_APR_2018/vpn_authentications_2018-09-04_16-36-52.csv --type csv --collection  cloudseer_source_vpns --create-collection false
        arangoimp --server.username root --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --file vpn_authentications_sanjay_24_APR_2018/vpn_authentications_2018-09-04_11-36-14.csv --type csv --collection  cloudseer_source_vpns --create-collection false
        arangoimp --server.username root --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --file users_new.csv --type csv --collection  cloudseer_source_myaccess --create-collection false
}

function cleanup {
	for edge in $(cat edges.txt | grep -v "#"); do  rm -rf $edge.txt; done;
	for collection in $(cat collections.txt | grep -v "#"); do  rm -rf $collection.txt; done;
}


function display_stats { 
	for collection in $(curl -Ss -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" localhost:8529/_api/collection | sed -e 's/name/\nname/g' | grep name | grep -v '"_' |sed -e 's/.*"cloudseer_/cloudseer_/g' | sed -e 's/".*//g'); do echo $(curl -sS -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -X GET  http://localhost:8529/_api/collection/$collection/count | sed -e 's/.*count"://g' | sed -e 's/,.*cloudseer/\t\t\tcloudseer/g'| sed -e 's/"}//g'); done | sed -e 's/ /\t/g' -e 's/".*//g'; 
}

function pre_processor {
	# something to be fix before a process to begin
	log_debug "pre-processor begin";
	rm -rf ./.-0*-.aql
}

function process {
	log_debug "process begin";

	local all=$(cat ./queries-_system-root.json | sed -e 's/"value"/\n"value"/g' | grep "$(echo $prefix)" | grep "\0.*-cloudseer_" | wc -l);

	for count in $(seq 1 $all)
	do 
	  qname=$(echo "$(for string in $(seq -f "%03g" 1 $all); do prefix=$(echo "$string-cloudseer_"); name=$(cat ./queries-_system-root.json | sed -e 's/"value"/\n"value"/g' | grep "$(echo $prefix)" | sed -e 's/.*","parameter.*name":"//g' -e 's/".*//g'); echo "$name"; done | head -$count | tail -1)")
	  aql_query=$(echo "db._query(\`$(for string in $(seq -f "%03g" 1 $all); do prefix=$(echo "$string-cloudseer_"); query=$(cat ./queries-_system-root.json | sed -e 's/"value"/\n"value"/g' | grep "$(echo $prefix)" | sed -e 's/","parameter.*//g'); echo "$query" | sed -e 's/.*":"//g'; done | head -$count | tail -1| sed -e 's/\\n/\n/g' -e 's/\\t/\t/g' -e 's/\\r/\r/g' -e 's/\\"/"/g')\`).getExtra()");
	
  	  log_debug "RUNNING ($count/$all)	<=== $qname ===>";
	  rm -rf fooquery
	  mkdir fooquery
	  echo "$aql_query" | tee ./fooquery/.$count-$qname-$(date +%Y%m%d).aql
	done
}


function export_collections {

        [ ! -d ./export ] && mkdir export || rm -rf ./export/

        for collection in $(curl -Ss -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" localhost:8529/_api/collection | python -m json.tool | grep name | grep -v '"_' | sed -e 's/.*: "//g' | sed -e 's/".*//g'); do arangoexport --collection $collection --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --overwrite true >> /dev/null 2>&1; done
        
	touch custom_export.sh
	chmod 755 ./custom_export.sh
	./custom_export.sh	

        log_warn "EXPORT DIRECTORY = $(pwd)/export/";
	ls $(pwd)/export/
        for each in $(ls $(pwd)/export/); do [ -e "$(pwd)/export/$each" ] && log_debug "$(pwd)/export/$each" || log_error "export collection not found $(pwd)/export/$each"; done
}


function post_processor {

log_warn "Inside postProcessor"

for collection in $(curl -Ss -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" localhost:8529/_api/collection | python -m json.tool | grep name | grep -v '"_' | sed -e 's/.*: "//g' | sed -e 's/".*//g';); do query=$(echo "db._query(\"FOR q in $(echo "$collection") LIMIT 1 RETURN q\")"); log_warn "RUNNING $query"; echo $query | arangosh --quiet --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 ;  done | tee arango_all.out

export_collections

}

function runModel {

	log_info "RUN MODEL BEGIN - FOR $NowDate - EXPECTING DATA FOR HOUR SLOT - MACHINE $(hostname)"
	log_warn "run pre-processing data"
	pre_processor
	log_warn "run processing data"
	process
	log_warn "run post-processing data"
	post_processor
	display_stats
}

function display_banner {
	which filget >> /dev/null 2>&1
	[ 0 == $? ] && 	figlet "AppOrchid $(date +%Y)" || log_info "AppOrchid Inc - 2018 - author: girish.mahajan@accionlabs.com";
 
} 
NowDate=$(date +%Y-%m-%d-%H%M);

function shriharikota_lift_off {
	log_warn "$0 - Lift Off Initiate - $*"
	display_banner
	cd  /apporchid/solutions/awsecurity/data/11APR2018/
	legacies
        wipe_arangodb
	create_dead_fox_service	
 	truncate_arangodbs
	import_content
	cleanup
	runModel
	log_warn "finished. script were started at $NowDate"
}

function foo {
	process

}
# Main
shsriharikota_lift_off IN T minus 10..9..8..7..6..5..4..3..2..1..go
foo
