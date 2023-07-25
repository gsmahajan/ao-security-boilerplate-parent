# .bashrc

# Source global definitions
if [ -f /etc/bashrc ]; then
	. /etc/bashrc
fi

# Uncomment the following line if you don't like systemctl's auto-paging feature:
# export SYSTEMD_PAGER=

# User specific aliases and functions
export HISTTIMEFORMAT="%F	%T	"
#sudo chown -R mahajag:root /apporchid/


alias observe='sh ~/observe.sh'
alias relaunch='/apporchid/solutions/awsecurity/apps/start.sh'
alias launch='/apporchid/solutions/awsecurity/apps/start.sh'
alias stop='/apporchid/solutions/awsecurity/apps/stop.sh'
alias status='sh ~/status.sh'
alias ingest="sh /apporchid/solutions/awsecurity/data/14MAR2018/ingest.sh"

function clean_elastic_search_indexes {
curl -X DELETE localhost:9200/awsecurity_myaccess
curl -X DELETE localhost:9200/awsecurity_lenel
curl -X DELETE localhost:9200/awsecurity_vpn

curl  -X DELETE localhost:9200/_template/*awsec*
#courtesy - ramayan
curl -X DELETE localhost:9200/_all
}


function clean_arango_collections {
for collection in vpn_servers peoples premises sessions badging_stations creates laptops owns locations are_in; do curl -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -X DELETE  http://localhost:8529/_api/collection/awsecurity_$collection; done


}

function truncate_arangodbs {

for collection in vpn_servers peoples premises sessions badging_stations laptops locations; do curl -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -X DELETE  http://localhost:8529/_api/collection/awsecurity_$collection; done
for collection in vpn_servers peoples premises sessions badging_stations laptops locations; do  echo "{ \"name\": \"$(echo "awsecurity_$collection")\" }" > $collection.txt; done
for collection in vpn_servers peoples premises sessions badging_stations laptops locations; do curl -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -XPOST --data @$collection.txt http://localhost:8529/_api/collection ; done
for collection in vpn_servers peoples premises sessions badging_stations laptops locations; do  rm -rf $collection.txt; done


for collection in creates owns are_in; do curl -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -X DELETE  http://localhost:8529/_api/collection/awsecurity_$collection; done
for collection in creates owns are_in; do  echo "{ \"name\": \"$(echo "awsecurity_$collection")\", \"type\": 3 }" > $collection.txt; done
for collection in creates owns are_in; do curl -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -XPOST --data @$collection.txt http://localhost:8529/_api/collection ; done
for collection in creates owns are_in; do  rm -rf $collection.txt; done


# unload few

for collection in premises locations; do curl -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" -XPUT  http://localhost:8529/_api/collection/awsecurity_$collection/unload; done
}
