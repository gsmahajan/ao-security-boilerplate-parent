# AW - Security - Collector Service

Powered by
[![AppOrchid](http://www.apporchid.com.php56-19.dfw3-1.websitetestlink.com/wp-content/uploads/2015/12/apporchid-logo.png)](https://nodesource.com/products/nsolid)



Any outcomes by rules processing will be transferred to /collector-service/ in order to capture the Activity and Anomolies and decides what to do with them, all flink applications will be communicated to collector service.

Allowed - PUT
Restricted - GET / Post / HEAD etc

PUT  
PUT /collector-service/vpn
PUT /collector-service/lenel
PUT /collector-service/netflow
PUT /collector-service/${rule.source}

Suggested payload -

{
 "name":"${rule.process.name}",
 "value":"##$output"
 "timeIn": "@@LocalDateTime.z",
 "timeOut": "@@LocalDateTime.z",
 "timeWindow":"@@integer",
 "lengthWindow":"@@integer"
 
 "severity":"@@red|@@green|@@orange"
}

Severity - Map to Critical / Warning or an Monitor collect(ion)