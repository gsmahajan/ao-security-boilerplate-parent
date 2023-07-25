#! /bin/bash

. ./lib/log.sh

#--fields 
#for collection in $(curl -Ss -u "root:14edcd1c2b7ff3b42823eac1eeb5d456" localhost:8529/_api/collection | python -m json.tool | grep name | grep -v '"_' | sed -e 's/.*: "//g' | sed -e 's/".*//g'); do arangoexport --type csv --collection $collection --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --overwrite true >> /dev/null 2>&1; done

#arangoexport --type csv --server.password 14edcd1c2b7ff3b42823eac1eeb5d456 --overwrite true --query "for r in cloudseer_time_buckets COLLECT hour = r.hour RETURN hour"
