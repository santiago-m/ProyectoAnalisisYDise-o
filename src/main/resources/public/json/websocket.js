
//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/game");
webSocket.onmessage = function (msg) { };
webSocket.onclose = function () { };

function closeWS() {
	console.log("HOLA! Cerrando");
	webSocket.close();
	webSocket.send("HOLA!");
}

function startPlaying() {
	 $.ajax({                                            
     	url: '/play',    
     	type: 'GET',
     	async:false,
     	success: function(data) {
     		location.href="/play"
     	}
     	/*success: function(data) {   
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
			
			window.location.href = "http://"+ location.hostname + ":" + location.port + "/play";
     	}*/
    });
}
/*
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
*/