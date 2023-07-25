
function showProfile() {
  $('.form-signin').css({display:"none"});
  $('.user-profile').fadeIn(1000).css({display:"block"});
}

 var urls="http://localhost:8529/_db/_system/cloudseer/peoples/293669";
 
 console.log("searchId == "+$("#show-search-text").val());


$.getJSON( urls, function( json ) {
	console.log(JSON.stringify(json));
 console.log( "Person Name: " + json.displayName );
 var b = '<div class="profile-info-row"><div class="profile-info-name"> Cloudseer Id </div><div class="profile-info-value"><span>'+json.csId+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> Key</div><div class="profile-info-value"><span>'+json._key+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> Logon </div><div class="profile-info-value"><span>'+json.logon+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> Display Name</div><div class="profile-info-value"><span>'+json.displayName+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> Email Address </div><div class="profile-info-value"><span>'+json.emailAddress+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> Employee Number</div><div class="profile-info-value"><span>'+json.empNum+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> Employee Id</div><div class="profile-info-value"><span>'+json.empId+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> BadgeId</div><div class="profile-info-value"><span>'+json.badgeId+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> Employee Since</div><div class="profile-info-value"><span>'+json.since+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> Reporting Manager</div><div class="profile-info-value"><span>'+json.reports_to+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> Division </div><div class="profile-info-value"><span>'+json.fall_under+'</span></div></div>'
+'<div class="profile-info-row"><div class="profile-info-name"> Manages</div><div class="profile-info-value"><span>'+json.manages.forEach(function(item){ return item.name; })+'</span></div></div>';

console.log(b);
 $('#profile-user-info').html();
})