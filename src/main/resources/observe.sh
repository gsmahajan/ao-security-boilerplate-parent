#! /bin/bash -x
#	
#	  ___              _____          _     _     _ 
#	 / _ \            |  _  |        | |   (_)   | |
#	/ /_\ \_ __  _ __ | | | |_ __ ___| |__  _  __| |
#	|  _  | '_ \| '_ \| | | | '__/ __| '_ \| |/ _` |
#	| | | | |_) | |_) \ \_/ / | | (__| | | | | (_| |
#	\_| |_/ .__/| .__/ \___/|_|  \___|_| |_|_|\__,_|
#	      | |   | |                                 
#	      |_|   |_|                                 
#	
# Script by - girish.mahajan@accionlabs.com
#

curl -i localhost:9200/_cluster/health

curl -u elastic:hq9TA5RHMcTKKhkvoOxc http://localhost:9200/_cat/indices

curl -i localhost:5601/

cd /apporchid/solutions/awsecurity/data/14MAR2018

for file in $(ls /apporchid/solutions/awsecurity/data/14MAR2018); do touch "$file"; done

cd ~

ps -eaf|grep java

netstat -tulpn

sleep 15;

tail -f logs/* /apporchid/solutions/awsecurity/apps/*/logs/*


