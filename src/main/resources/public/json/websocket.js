

//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/game");
webSocket.onmessage = function (msg) { print(msg) };
webSocket.onclose = function () { alert("WebSocket connection closed") };
webSocket.sendMessage("username: "+getCookie("username"))

function print(msg) {
	var jsonData = JSON.parse(msg.data);
	console.log(jsonData.sesion);

}

function sendMessage(msg) {
	webSocket.send(msg);
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