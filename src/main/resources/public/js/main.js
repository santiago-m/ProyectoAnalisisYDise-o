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

	$("#modalSignIn").load("partials/modal-login.html");
	$("#modalSignUp").load("partials/modal-register.html");
	$("#navBar").load("partials/navBar.html");
});