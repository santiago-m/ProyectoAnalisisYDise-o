var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/game");

webSocket.onopen = function() {
	console.log('started');
	wait(function(){return username}, function() {return opponent}, function() {createSession()});
}

webSocket.onmessage = function (msg) {
	console.log(msg);
	var message = msg['data'];

	if (message == 'opponentNotReadyYet') {
		waitForOpponent();
	}
	else if (message == 'opponentReady') {
		opponentReady();
	}
	else {
		message = JSON.parse(message);

		console.log(message['playerState']);
		if (typeof(message['playerState']) == 'undefined') {
			if (message['puedeJugar'] == true) {
				console.log('Recibi true');
				if (typeof(message['puntajeOponente']) == 'undefined') {
					puntaje += 5;
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

					if (suddenDeath) {
						puntaje -= 5;
						nextQuestion();
					}
					else {
						status = 'waiting';
						waitForTurn();
					}
				}
				else {
					nextQuestion();
				}
			}	
		}
		else {
			var state = message['playerState'];

			$('#pointsPlace').html('');
			$('#questionPlace').html('');

			$('#answersPlace').html('');


			if (state == "winner") {
				alert('You have win the game against ' + opponent);
			}
			else if (state == "loser") {
				alert('You have lost the game against ' + opponent);
			}
			else if (state == "draw") {
				alert('You and ' + opponent + ' had a draw in your game!');
			}
			else if (state == "opponentFinished") {
				suddenDeath = true;
				puntajeOponente = message['puntaje_' + opponent];

				nextQuestion();
			}
			else {
				$('#questionPlace').html('Haz respondido las ' + cantMaxPreguntas + 'del juego. \n Juega mientras esperas que tu oponente termine.');
				$('#answersPlace').html('<iframe src="https://funhtml5games.com?embed=spaceinvaders" style="width:800px;height:550px;border:none;" frameborder="0" scrolling="no"></iframe>');				
			}
		}
	}};

webSocket.onclose = function () { };

var username;
var opponent;
var idPregunta;
var cantPlayers;
var status;
var puntaje;
var puntajeOponente;

var cantMaxPreguntas;
var cantPreguntasRespondidas = 0;
var suddenDeath = false;

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
    		cantMaxPreguntas = data["cantPreguntas_"+username];

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

function createSession() {
	console.log("user: " + username);
	console.log("opponent: " + opponent);
	webSocket.send(JSON.stringify({
  		username: username,
  		opponent: opponent
  	}));
}

function wait(condition1, condition2, callback) {
	console.log(typeof condition1());
	console.log(typeof condition2());
    if (typeof condition1() !== "undefined") {
    	if (typeof condition2() !== "undefined") {
        	callback();
    	}
    	else {
        	setTimeout(function () {
            	wait(condition1, condition2, callback);
        	}, 0);
    	}
    }
    else {
        setTimeout(function () {
            wait(condition1, condition2, callback);
        }, 0)
    }
}

function waitForOpponent() {
	$('#pointsPlace').html('');
	$('#questionPlace').html('');
	$('#questionPlace').html('Conectando con el oponente...');

	$('#answersPlace').html('');
}

function opponentReady() {
	if (status == "waiting") {
		waitForTurn();
	}
	else {
		$('#pointsPlace').html('');
		$('#pointsPlace').html(username + ': '+ puntaje + '<br>' + opponent + ': ' + puntajeOponente);
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
	cantPreguntasRespondidas++;
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
	var finished = false;
	if (cantPreguntasRespondidas === cantMaxPreguntas) {
		finished = true;
	}
	radioAnswers = document.getElementsByName('answer');
	console.log(radioAnswers);
	for (var i = 0; i < radioAnswers.length; i++) {
		actual = $('#answer'+i);
		if (actual.is(':checked')) {
			console.log(actual);


			webSocket.send(JSON.stringify({
				finished: finished,
				alone: suddenDeath,
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