#! /bin/bash 
#
#         ___              _____          _     _     _
#        / _ \            |  _  |        | |   (_)   | |
#       / /_\ \_ __  _ __ | | | |_ __ ___| |__  _  __| |
#       |  _  | '_ \| '_ \| | | | '__/ __| '_ \| |/ _` |
#       | | | | |_) | |_) \ \_/ / | | (__| | | | | (_| |
#       \_| |_/ .__/| .__/ \___/|_|  \___|_| |_|_|\__,_|
#             | |   | |
#             |_|   |_|
#
# Script by - girish.mahajan@accionlabs.com
#
#  nohup ./extract_lenels.sh > lenel_forge.csv 2>/dev/null &
#  Script use to map usersinfo with lenel records - bashway
#
# 
IFS=$'\n';

mapfile < badgeIds_userIds_mapping.csv badgeIds_userIds_mapping
#mapfile < users_new.csv users_new
mapfile < users_new_without_DN.csv users_new


for line in $(cat Woodcrest_entrances_and_exits_only_jagan_converted.csv); do 
	badgeid=$(echo $line | cut -f 11,12 -d ',' | sort | uniq | cut -f1 -d' ' | sed -e 's/"//g' | sort | uniq); 


	identity=$(echo "${badgeIds_userIds_mapping[*]}"| grep $badgeid | head -1 | cut -f4 -d',')
	firstName=$(echo "${badgeIds_userIds_mapping[*]}"| grep $badgeid | head -1 | cut -f3 -d',')
	lastName=$(echo "${badgeIds_userIds_mapping[*]}"| grep $badgeid | head -1 | cut -f2 -d',')
	badgeId=$(echo "${badgeIds_userIds_mapping[*]}"| grep $badgeid | head -1 | cut -f1 -d',')
	
	
	empId=$(echo "${users_new[*]}"| grep -i $firstName | grep -i $lastName | head -1 | cut -f4 -d',')
	empNumber=$(echo "${users_new[*]}"| grep -i $firstName | grep -i $lastName | head -1 | cut -f5 -d',')

	accLine=$(echo "${users_new[*]}" | grep -i $firstName | grep -i $lastName | head -1)
	emailId=$(echo "${users_new[*]}" | grep -i $firstName | grep -i $lastName | head -1 | cut -f15 -d',')
	uniqField="$(echo $firstName"#aosignature#"$lastName | sed -e 's/ |  /#/g'| tr '[:upper:]' '[:lower:]')";
	cloudseerId=$(echo $uniqField | md5sum | cut -f1 -d' ')
	
	echo "$cloudseerId,$identity,$firstName,$lastName,$badgeid,$empId,$empNumber,$emailId,$uniqField,$line"
done

