$(document).ready(function() {

	var estadoLogin = $('#estadoLogin').html();
	var estadoRegistro = $('#estadoRegistro').html();
	

	var usrname = $('#usrname').html();
	var admin = $('#admin').html();
	var puntaje = $('#puntaje').html();

	var canAnswer = $('#canAnswer').html();

	$('#playMultiBtn').click(function(e) {
		if (canAnswer != "false") {
			$('#playMultiForm').submit();
		}
		else {
			alert('No tiene mas preguntas disponibles para responder. Intente otro dia');
		}
	});
	$('#playAloneBtn').click(function(e) {
		if (canAnswer != "false") {
			$('#playAloneForm').submit();
		}
		else {
			alert('No tiene mas preguntas disponibles para responder. Intente otro dia');
		}
	});

	if (admin == 'true') {
		document.cookie = "admin="+admin;	
	}
	else {
		document.cookie = "admin=false";
	}
	
	if (!getCookie('username') || getCookie('username') == 'undefined') {
		document.cookie = "username="+usrname;		
	}
	if (!getCookie('puntaje') || getCookie('puntaje') == 'undefined') {
		document.cookie = "puntaje="+puntaje;
	}

	if ((estadoLogin != "") && (estadoLogin != undefined)) {
		alert(estadoLogin);
	}

	if ((estadoRegistro != "") && (estadoRegistro != undefined)) {
		alert(estadoRegistro);
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