$(document).ready(function() {

	var estadoLogin = $('#estadoLogin').html();

	var usrname = $('#usrname').html();
	var admin = $('#admin').html();
	var puntaje = $('#puntaje').html();

	if (!getCookie('admin')) {
		if (admin) {
			document.cookie = "admin="+admin;	
		}
		else {
			document.cookie = "admin=false";
		}
	}
	if (!getCookie('username')) {
		document.cookie = "username="+usrname;		
	}
	if (!getCookie('puntaje')) {
		document.cookie = "puntaje="+puntaje;
	}

	if ((estadoLogin != "") && (estadoLogin != undefined)) {
		alert('Usuario o Contraseña incorrectos. Por favor intente nuevamente');
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

$(window).on("unload", function(e) {
    deleteAllCookies();
});

function getCookie(name) {
  var value = "; " + document.cookie;
  var parts = value.split("; " + name + "=");
  if (parts.length == 2) return parts.pop().split(";").shift();
}

function delete_cookie( name ) {
  document.cookie = name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function deleteAllCookies() {
  delete_cookie('username');
  delete_cookie('puntaje');
  delete_cookie('admin');
}