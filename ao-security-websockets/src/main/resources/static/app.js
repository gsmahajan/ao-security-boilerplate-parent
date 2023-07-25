
   $('#myButton').click(function() {
  $('#user_wrapper').toggle('slow', function() {
    document.getElementById('user_wrapper').style.display = 'block';
  });
});

   function createNode(element, key, value) {
	elemment.innerHtml = '<div class="profile-info-row"><div class="profile-info-name">'+key+'</div>			<div class="profile-info-value"><span>'+value+'</span></div></div>'
    document.getElementByClassName("profile-user-info").appendChild(document.createElement(element)); // Create the type of element you pass in the parameters
  }

  function append(parent, el) {
    return parent.appendChild(el); // Append the second parameter(element) to the first one
  }



 var urls="http://localhost:8529/_db/_system/cloudseer/peoples/292041";
 var searchId = $("#show-search-text").val();
 if(searchId != null ){
	urls="http://localhost:8529/_db/_system/cloudseer/peoples/"+id
 }


function createNode(element) {
      return document.createElement(element);
  }

  function append(parent, el) {
    return parent.appendChild(el);
  }



  $.getJSON( urls+"?callback=?&format=jsonp&jsonp=?", function( json ) {
  console.log( "JSON Data: " + json.displayName );
 });