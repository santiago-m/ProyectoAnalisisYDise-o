var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/game");
webSocket.onmessage = function (msg) { 
	if (msg['data'] == 'true') {
		status = 'ready';
		nextQuestion();
	}
	else {
		if (cantPlayers == 2) {
			status = 'waiting';
			waitForTurn();
		}
	}};

webSocket.onclose = function () { };

var username;
var idPregunta;
var cantPlayers;
var status;

var pregunta;
var answers;
var answer1;
var answer2;
var answer3;
var answer4;

window.onload = function() {

	$.ajax({                                            
     	url: '/play',    
     	type: 'POST',
     	dataType: "json",
     	async:false,

     	success: function(data) {  

     		console.log(data);

     		username = data["player"];
     		idPregunta = data["ID"];

     		cantPlayers = data["game_"+username];
     		status = data["status_"+username];

     		pregunta = data["pregunta"];
				answer1 = data["opcion 1"];
				answer2 = data["opcion 2"];
				answer3 = data["opcion 3"];
				answer4 = data["opcion 4"];

				answers = [answer1, answer2, answer3, answer4];

				cantOpciones = 0;
				if (answer1 != "") {
					cantOpciones++;
				}
				if (answer2 != "") {
					cantOpciones++;
				}
				if (answer3 != "") {
					cantOpciones++;
				}
				if (answer4 != "") {
					cantOpciones++;
				}
     	}
	});

	webSocket.send(JSON.stringify({
  				username: username
  			}));

	if (status == "waiting") {
		waitForTurn();
	}
	else {

		$('#questionPlace').html('');
		$('#questionPlace').html(pregunta);

		$('#answersPlace').html('');
		for (var i = 0; i < answers.length; i++) {
			if (answers[i] != '') {
				$('#answersPlace').append('<p> <input id="answer'+i+'" type="radio" name="answer" value="'+answers[i]+'"> '+answers[i]+'  </p>');
			}
		}
	}

}

function waitForTurn() {

	$('#questionPlace').html('');
	$('#questionPlace').html('Espere por su turno.');

	$('#answersPlace').html('');
}

function nextQuestion() {
	if (pregunta != null && pregunta != '') {
		$('#questionPlace').html('');
		$('#questionPlace').html(pregunta);

		$('#answersPlace').html('');
		for (var i = 0; i < answers.length; i++) {
			if (answers[i] != '') {
				$('#answersPlace').append('<p> <input id="answer'+i+'" type="radio" name="answer" value="'+answers[i]+'"> '+answers[i]+'  </p>');
			}
		}
	}
	else {
		$.ajax({                                            
     	url: '/play',    
     	type: 'POST',
     	dataType: "json",
     	async:false,

     	success: function(data) {  

     		console.log(data);

     		username = data["player"];
     		idPregunta = data["ID"];

     		pregunta = data["pregunta"];
				answer1 = data["opcion 1"];
				answer2 = data["opcion 2"];
				answer3 = data["opcion 3"];
				answer4 = data["opcion 4"];

				answers = [answer1, answer2, answer3, answer4];

				cantOpciones = 0;
				if (answer1 != "") {
					cantOpciones++;
				}
				if (answer2 != "") {
					cantOpciones++;
				}
				if (answer3 != "") {
					cantOpciones++;
				}
				if (answer4 != "") {
					cantOpciones++;
				}
     		}
		});

		$('#questionPlace').html('');
		$('#questionPlace').html(pregunta);

		$('#answersPlace').html('');
		for (var i = 0; i < answers.length; i++) {
			if (answers[i] != '') {
				$('#answersPlace').append('<p> <input id="answer'+i+'" type="radio" name="answer" value="'+answers[i]+'"> '+answers[i]+'  </p>');
			}
		}	
	}
}

function refreshQuestion(msg) {

	var data = JSON.parse(msg["data"]);

	var answers;
	var answer1;
	var answer2;
	var answer3;
	var answer4;

	pregunta = data["pregunta"];
	answer1 = data["opcion 1"];
	answer2 = data["opcion 2"];
	answer3 = data["opcion 3"];
	answer4 = data["opcion 4"];

	answers = [answer1, answer2, answer3, answer4];

	cantOpciones = 0;
	if (answer1 != "") {
		cantOpciones++;
	}
	if (answer2 != "") {
		cantOpciones++;
	}
	if (answer3 != "") {
		cantOpciones++;
	}
	if (answer4 != "") {
		cantOpciones++;
	}

	$('#questionPlace').html("");
	$('#questionPlace').html(pregunta);

	$('#answersPlace').html("");	

	for (var i = 0; i < answers.length; i++) {
		if (answers[i] != '') {
			$('#answersPlace').append('<p> <input id="answer'+i+'" type="radio" name="answer" value="'+answers[i]+'"> '+answers[i]+'  </p>');
		}
	}

	console.log(data);
}

function sendAnswer() {
	radioAnswers = document.getElementsByName('answer');
	console.log(radioAnswers);
	for (var i = 0; i < radioAnswers.length; i++) {
		actual = $('#answer'+i);
		if (actual.is(':checked')) {
			console.log(actual);

			webSocket.send(JSON.stringify({
  				username: username,
  				idPregunta: idPregunta,
  				answer: actual.val()
			}));
		}
	}
}

function sendMessage(msg) {
	webSocket.send("hello");
}

function getCookie(name) {
	var nameEQ = name + "=";
	//alert(document.cookie);
	var ca = document.cookie.split(';');
	for(var i=0;i < ca.length;i++) {
		var c = ca[i];
		while (c.charAt(0)==' ') c = c.substring(1);
		if (c.indexOf(nameEQ) != -1) return c.substring(nameEQ.length,c.length);
	}
	return null;
} 

