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




# LogStash configurations append dummy logs to each logstash inputs

# script by - girish.mahajan@acccionlabs.com


VPN_SOURCE="sample-vpn.csv"
LENEL_SOURCE="sample-lenel.csv"
NETFLOW_STEALTHWATCH_SOURCE="net flow stealthwatch.csv"
SECURITY_CENTER_SOURCE="SecurityCenter data.csv"



#SAMPLE="${ date +%S:%M:%H},Start,taylorzp,8C:C1:21:A1:2F:88,10.1.129.159,RADIUS,,8FC00A38,829472938oiwqreuio,,hershey-dc-pa-vpn01,10.1.128.10,,hsyplpsns001"
VPN_SAMPLE="53:30.0,Stop,reedj1,10:4A:7D:C2:FF:D8,10.1.129.176,RADIUS,User Request,8FC00A48,0a01800a00b790005a785297,29101,hershey-dc-pa-vpn01,10.1.128.10,,hdnplpsns001"

LENEL_SAMPLE="OnGuard 7.2	All Events Over Time	QUERY:  START DATE:  2/1/2018 12:00:00 AM;   END DATE:  2/6/2018 11:59:59 PM;   Daily Time Range of 00:00:00 - 23:59:59;   Badge 172467475	Report Date:  2/6/2018 11:55:53AM Eastern Standard Time	Date/Time	Event	Details	Device	Panel	2/6/2018	 9:33:27AM	Access Granted	172467475 (0): Rao,  SanJay	609-3-01 New Lane 2 Entry -CR	609 NJ - WOODCREST				Total Events:  33	All Events Over Time	OnGuard 7.2	Page	1"


NETFLOW_STEALTHWATCH_SAMPLE=',10.1.69.139,PA-HERSHEY-DTACTR,10.1.70.204,"PA-HERSHEY-DTACTR, DB Servers",52 days 8 hours 38 minutes,Undefined TCP,Undefined TCP (30015/tcp),1.0843737071129708E7,323956645,574183,"Dec 17, 2017 7:31:34 AM",110959348,34.2513,212997297,39923,30015'



SECURITY_CENTER_SAMPLE='103686,RHEL 6 : postgresql (RHSA-2017:2860),Red Hat Local Security Checks,High,10.1.9.29,TCP,0,Yes,00:50:56:86:52:0c,hsyplupts001,,"Plugin Output: 
Remote package installed : postgresql-libs-8.4.20-7.el6
Should be                : postgresql-libs-8.4.20-8.el6_9

NOTE: The vulnerability information above was derived by checking the
package versions of the affected packages from this advisory. This
scan is unable to rely on Red Hats own security checks, which
consider channels and products in their vulnerability determinations.",The remote Red Hat host is missing one or more security updates.,"An update for postgresql is now available for Red Hat Enterprise Linux 6.

Red Hat Product Security has rated this update as having a security impact of Moderate. A Common Vulnerability Scoring System (CVSS) base score, which gives a detailed severity rating, is available for each vulnerability from the CVE link(s) in the References section.

PostgreSQL is an advanced object-relational database management system (DBMS).

Security Fix(es) :

* It was found that authenticating to a PostgreSQL database account with an empty password was possible despite libpqs refusal to send an empty password. A remote attacker could potentially use this flaw to gain access to database accounts with empty passwords. (CVE-2017-7546)

Red Hat would like to thank the PostgreSQL project for reporting this issue. Upstream acknowledges Ben de Graaff, Jelte Fennema, and Jeroen van der Ham as the original reporters.",Update the affected packages.,"https://www.postgresql.org/about/news/1772/
http://rhn.redhat.com/errata/RHSA-2017-2860.html
https://www.redhat.com/security/data/cve/CVE-2017-7546.html",High,I,7.5,6.2,AV:N/AC:L/Au:N/C:P/I:P/A:P/E:F/RL:OF/RC:ND,"p-cpe:/a:redhat:enterprise_linux:postgresql
p-cpe:/a:redhat:enterprise_linux:postgresql-contrib
p-cpe:/a:redhat:enterprise_linux:postgresql-debuginfo
p-cpe:/a:redhat:enterprise_linux:postgresql-devel
p-cpe:/a:redhat:enterprise_linux:postgresql-docs
p-cpe:/a:redhat:enterprise_linux:postgresql-libs
p-cpe:/a:redhat:enterprise_linux:postgresql-plperl
p-cpe:/a:redhat:enterprise_linux:postgresql-plpython
p-cpe:/a:redhat:enterprise_linux:postgresql-pltcl
p-cpe:/a:redhat:enterprise_linux:postgresql-server
p-cpe:/a:redhat:enterprise_linux:postgresql-test
cpe:/o:redhat:enterprise_linux:6",CVE-2017-7546,,"OSVDB #163076,RHSA #2017,IAVB #2017-B-0107","Nov 30, 2017 11:27:46 EST","Nov 30, 2017 11:27:46 EST",N/A,"Oct 5, 2017 12:00:00 EDT","Oct 6, 2017 12:00:00 EDT","Oct 9, 2017 12:00:00 EDT",Exploits are available,,local,Revision: 2.3'



while true; do	
	echo "$(echo $VPN_SAMPLE)" >> $VPN_SOURCE; 
	echo "$(echo "$LENEL_SAMPLE")" >> $LENEL_SOURCE;	
 	echo "$(echo "$NETFLOW_STEALTHWATCH_SAMPLE")" >> "$NETFLOW_STEALTHWATCH_SOURCE";
	echo "$(echo "$SECURITY_CENTER_SAMPLE")" >> "$SECURITY_CENTER_SOURCE";
	sleep 1;
done
