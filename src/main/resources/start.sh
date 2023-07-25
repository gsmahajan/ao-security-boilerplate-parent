#! /bin/bash
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
#
#	mahajag@awsnlsec001 x-pack]$ ./setup-passwords auto
#	Initiating the setup of passwords for reserved users elastic,kibana,logstash_system.
#	The passwords will be randomly generated and printed to the console.
#	Please confirm that you would like to continue [y/N]y
#	
#	
#	Changed password for user kibana
#	PASSWORD kibana = SdGYB3btlbe5WcySPy2b
#	
#	Changed password for user logstash_system
#	PASSWORD logstash_system = dmfaLoDJT0flZ7zqEoVc
#	
#	Changed password for user elastic
#	PASSWORD elastic = hq9TA5RHMcTKKhkvoOxc


HOME_DIR="/apporchid/solutions/awsecurity"
APPS_DIR="$HOME_DIR/apps/"
DATA_DIR="$HOME_DIR/data/14MAR2018"
LOGS_DIR="/home/mahajag/logs/"



function stopAll {
	
	killall java node
	echo "STOPPED" > ~/.status
	rm -rf $LOGS_DIR/
}

function cleaningPlease {
	rm -rf $LOGS_DIR
	mkdir $LOGS_DIR/
	rm -rf $APPS_DIR/logstash-6.2.2/data/	
	rm -rf $APPS_DIR/logstash-6.2.2/logs
	rm -rf $APPS_DIR/logstash-6.2.2/config_vpn
	rm -rf $APPS_DIR/logstash-6.2.2/config_lenel
	rm -rf $APPS_DIR/logstash-6.2.2/config_myaccess
	rm -rf $APPS_DIR/logstash-6.2.2/config_mso
	rm -rf $APPS_DIR/elasticsearch-6.2.2/logs
	rm -rf $APPS_DIR/logstash-6.2.2/data_mso*
	mkdir $APPS_DIR/logstash-6.2.2/logs
	mkdir $APPS_DIR/elasticsearch-6.2.2/logs
	
}


function patchingPlease {
	cp -r $APPS_DIR/logstash-6.2.2/config $APPS_DIR/logstash-6.2.2/config_vpn
	cp -r $APPS_DIR/logstash-6.2.2/config $APPS_DIR/logstash-6.2.2/config_lenel
	cp -r $APPS_DIR/logstash-6.2.2/config $APPS_DIR/logstash-6.2.2/config_myaccess
	cp -r $APPS_DIR/logstash-6.2.2/config $APPS_DIR/logstash-6.2.2/config_mso1
	cp -r $APPS_DIR/logstash-6.2.2/config $APPS_DIR/logstash-6.2.2/config_mso2
	cp -r $APPS_DIR/logstash-6.2.2/config $APPS_DIR/logstash-6.2.2/config_mso3
	cp -r $APPS_DIR/logstash-6.2.2/config $APPS_DIR/logstash-6.2.2/config_mso4
	cp -r $APPS_DIR/logstash-6.2.2/config $APPS_DIR/logstash-6.2.2/config_mso5

	sed -e 's/: data/: data_vpn/g' $APPS_DIR/logstash-6.2.2/config/logstash.yml > $APPS_DIR/logstash-6.2.2/config_vpn/logstash.yml
	sed -e 's/: data/: data_lenel/g' $APPS_DIR/logstash-6.2.2/config/logstash.yml > $APPS_DIR/logstash-6.2.2/config_lenel/logstash.yml
	sed -e 's/: data/: data_myaccess/g' $APPS_DIR/logstash-6.2.2/config/logstash.yml > $APPS_DIR/logstash-6.2.2/config_myaccess/logstash.yml	

	sed -e 's/: data/: data_mso1/g' $APPS_DIR/logstash-6.2.2/config/logstash.yml > $APPS_DIR/logstash-6.2.2/config_mso1/logstash.yml
	sed -e 's/: data/: data_mso2/g' $APPS_DIR/logstash-6.2.2/config/logstash.yml > $APPS_DIR/logstash-6.2.2/config_mso2/logstash.yml
	sed -e 's/: data/: data_mso3/g' $APPS_DIR/logstash-6.2.2/config/logstash.yml > $APPS_DIR/logstash-6.2.2/config_mso3/logstash.yml
	sed -e 's/: data/: data_mso4/g' $APPS_DIR/logstash-6.2.2/config/logstash.yml > $APPS_DIR/logstash-6.2.2/config_mso4/logstash.yml
	sed -e 's/: data/: data_mso5/g' $APPS_DIR/logstash-6.2.2/config/logstash.yml > $APPS_DIR/logstash-6.2.2/config_mso5/logstash.yml
}

function elastic {
	cd $APPS_DIR/elasticsearch-6.2.2/bin/
	nohup ./elasticsearch >> $LOGS_DIR/console_elastic.log 2>&1 &
	sleep 10
	curl  -X DELETE localhost:9200/_template/*awsec*

	curl -X DELETE localhost:9200/awsecurity_vpn
	curl -X DELETE localhost:9200/awsecurity_lenel
	curl -X DELETE localhost:9200/awsecurity_myaccess
	curl -X DELETE localhost:9200/_all


}

function kibana {
	cd $APPS_DIR/kibana-6.2.2-linux-x86_64/bin/
	nohup ./kibana >> $LOGS_DIR/console_kibana.log 2>&1 &
	sleep 5
}

function logstash {
	nohup sh $DATA_DIR/cfglogstash/enroll_templates.sh
	cd $APPS_DIR/logstash-6.2.2/bin/
	nohup ./logstash -f $DATA_DIR/cfglogstash/vpn.conf --path.settings config_vpn --path.data data_vpn --config.reload.automatic >> $LOGS_DIR/console_logstash_vpn.log 2>&1 &
	sleep 10
	nohup ./logstash -f $DATA_DIR/cfglogstash/lenel.conf --path.settings config_lenel --path.data data_lenel --config.reload.automatic >> $LOGS_DIR/console_logstash_lenel.log 2>&1 &
	sleep 10
	
	nohup ./logstash -f $DATA_DIR/cfglogstash/myaccess.conf --path.settings config_myaccess --path.data data_myaccess --config.reload.automatic >> $LOGS_DIR/console_logstash_myaccess.log 2>&1 &
	
	sleep 10;
	nohup ./logstash -f $DATA_DIR/cfglogstash/mso-peoples.conf --path.settings config_mso1 --path.data data_mso_$(cat /proc/sys/kernel/random/uuid ) --config.reload.automatic >> $LOGS_DIR/console_logstash_mso_peoples.log 2>&1 &
	sleep 10
	nohup ./logstash -f $DATA_DIR/cfglogstash/mso-devices.conf --path.settings config_mso2 --path.data data_mso_$(cat /proc/sys/kernel/random/uuid ) --config.reload.automatic >> $LOGS_DIR/console_logstash_mso_devices.log 2>&1 &
	sleep 10
	nohup ./logstash -f $DATA_DIR/cfglogstash/mso-premises.conf --path.settings config_mso3 --path.data data_mso_$(cat /proc/sys/kernel/random/uuid ) --config.reload.automatic >> $LOGS_DIR/console_logstash_mso_premises.log 2>&1 &
	sleep 10
	nohup ./logstash -f $DATA_DIR/cfglogstash/mso-sessions.conf --path.settings config_mso4 --path.data data_mso_$(cat /proc/sys/kernel/random/uuid ) --config.reload.automatic >> $LOGS_DIR/console_logstash_mso_sessions.log 2>&1 &
	sleep 10
	nohup ./logstash -f $DATA_DIR/cfglogstash/mso-gates.conf --path.settings config_mso5 --path.data data_mso_$(cat /proc/sys/kernel/random/uuid ) --config.reload.automatic >> $LOGS_DIR/console_logstash_mso_gates.log 2>&1 &
	sleep 10
}

function houston_lift_off {
echo "STOPPED" > ~/.status

stopAll
cleaningPlease
patchingPlease
elastic
kibana
logstash

sleep 120
echo "RUNNING" > ~/.status

touch $DATA_DIR/*.csv

}

houston_lift_off
