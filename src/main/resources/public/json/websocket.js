//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/game");
webSocket.onmessage = function (msg) { print(msg) };
webSocket.onclose = function () { alert("WebSocket connection closed") };

function print(msg) {
	var jsonData = JSON.parse(msg.data);
	console.log(jsonData.sesion);

}

function sendMessage() {
	webSocket.send('hola!');
}