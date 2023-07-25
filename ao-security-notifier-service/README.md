# AW - Security - Notifier Service

Powered by
[![AppOrchid](http://www.apporchid.com.php56-19.dfw3-1.websitetestlink.com/wp-content/uploads/2015/12/apporchid-logo.png)](https://nodesource.com/products/nsolid)


Notifier service backed by a amqp queue that receives list of notification to be sent based on severity (severity comes with warning or critical alert)

Notifier uses either self implementation or cloud seer adoption based on endpoint availability

All Put pushes into queue and quit, queue processor either uses cloud seer or a mail service or a webhooks for microsoft teams / slacks depending upon developers implementaiton.

Sample Payload design -
  
`
PUT /notifier-service/notify
{
"severity":"warning",
"processor": "vpn.monitor.spike.connect",
"severityInfo":"vpn connect spike observed with in last 100 connection with threshold of 95",
}
`
Based on severity, app decides to use the channel to inform the team, either a mail or a web hook
==
Allow PUT
Restrict POST / GET / ETC

