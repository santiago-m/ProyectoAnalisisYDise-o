$(document).ready(function() {
	var usrname = $('#usrname').html();
	var admin = $('#admin').html();
	var puntaje = $('#puntaje').html();

	document.cookie = "username="+usrname;
	document.cookie = "puntaje="+puntaje;
	if (admin) {
		document.cookie = "admin="+admin;	
	}
	else {
		document.cookie = "admin=false";
	}

	$("#modalSignIn").load("partials/modal-login.html");
	$("#modalSignUp").load("partials/modal-register.html");
	$("#navBar").load("partials/navBar.html");
	$("#profileDiv").load("partials/profile.html");

	$("#viewProfile").click(function(event) {
    event.preventDefault();
    $("#profileModal").modal();
  });
});