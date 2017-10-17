//Establish the WebSocket connection and set up event handlers
var webSocket3 = new WebSocket("ws://" + location.hostname + ":" + location.port + "/preguntas");
webSocket3.onmessage = function (msg) { upDate(msg) };
webSocket3.onclose = function () { alert("WebSocket connection closed") };


id("send").addEventListener("click", function () {
    sendMessage3();
});

id("blank").addEventListener("click", function () {
	del("cosa");
});

id("cambiar").addEventListener("keyPress", function (e) {
	if (e.keyCode === 13){
		del("bloque");
		webSocket3.send(e.target.value);
	}
});

function upDate1(msg) {
    if (msg != "hi"){
    	upDate(msg);
    }
}

function del(targetId) {
	//id(targetId).parentNode.removeChild(id(targetId));
    id(targetId).innerHTML = "";
}

//Send a message if it's not empty, then clear the input field
function sendMessage3() {
    console.log("hola5");
    webSocket3.send("hola6");
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Update the chat-panel, and the list of connected users
function upDate(msg) {
	//console.log("solo " + msg);			// este es el objeto
	//console.log(".data " + msg.data);	// hash crudo
    var data = JSON.parse(msg.data);	// lo convertimos a objeto de js
    //console.log("data " + data);		// objeto js
    //console.log("codigoo " + msg.codigoo)	// no es nada
    //webSocket3.send("hi");
    //console.log("hi");
    insert("cosa", data.codigoo);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}