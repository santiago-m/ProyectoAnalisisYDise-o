var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/game");

var username;
var opponent;
var idPregunta;
var cantPlayers;
var status;
var puntaje;
var puntajeOponente;

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
    		opponent = data["opponent"];
    		idPregunta = data["ID"];

    		cantPlayers = data["game_"+username];
    		status = data["status_"+username];

	    	puntaje = data["puntaje_"+username];
    		puntajeOponente = data["puntaje_"+opponent];

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
}

	webSocket.onmessage = function (msg) {
		var message = JSON.parse(msg['data']);
		console.log(message['puedeJugar']);
		console.log(message['puntajeOponente']);
		console.log(message['puntajeOponente'] == undefined);

		if (message['puedeJugar'] == true) {
			console.log('Recibi true');
			if (message['puntajeOponente'] == undefined) {
				puntaje += 5;
				updatePoints();
			}
			else {
				puntajeOponente = message["puntajeOponente"];
			}

			status = 'ready';
			nextQuestion();
		}
		else {
			console.log('Recibi false');
			if (cantPlayers == 2) {
				status = 'waiting';
				waitForTurn();
			}
			else {
				nextQuestion();
			}
		}};

	webSocket.onclose = function () { wsReady = false; };
}

function updatePoints() {
	$('#pointsPlace').html('');
	if (cantPlayers == 2) {
		$('#pointsPlace').html(username + ': '+ puntaje + '<br>' + opponent + ': ' + puntajeOponente);
	}
	else {
		$('#pointsPlace').html(username + ': '+ puntaje);	
	}
}

function waitForTurn() {
	$('#pointsPlace').html('');
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
	$('#pointsPlace').html('');
	$('#pointsPlace').html(username + ': '+ puntaje + '<br>' + opponent + ': ' + puntajeOponente);
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
	pregunta = null;
	radioAnswers = document.getElementsByName('answer');
	console.log(radioAnswers);
	for (var i = 0; i < radioAnswers.length; i++) {
		actual = $('#answer'+i);
		if (actual.is(':checked')) {
			console.log(actual);

			webSocket.send(JSON.stringify({
				cantPlayers: cantPlayers,
  				username: username,
  				opponent: opponent,
  				puntaje: puntaje,
  				idPregunta: idPregunta,
  				answer: actual.val()
			}));
		}
	}
}

function sendMessage(msg) {
	webSocket.send("hello");
}
