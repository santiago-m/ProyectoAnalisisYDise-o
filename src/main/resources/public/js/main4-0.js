$(document).ready(function() {
	var usrname = $('#usrname').html();
	var admin = $('#admin').html();

	document.cookie = "username="+usrname;
	if (admin) {
		document.cookie = "admin="+admin;	
	}
	else {
		document.cookie = "admin=false";
	}

	$("#navBar").load("partials/navBar4-0.html");
	$("#opcionesCorcho").load("partials/opcionesCorcho.html");
});