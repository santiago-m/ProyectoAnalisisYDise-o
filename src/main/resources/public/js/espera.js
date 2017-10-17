//Establish the WebSocket connection and set up event handlers
var webSocket1 = new WebSocket("ws://" + location.hostname + ":" + location.port + "/wait");
webSocket1.onmessage = function (msg) { redireccion(msg) };
webSocket1.onclose = function () {  };

id("volver").addEventListener("click", function (e) {
	webSocket1.send("borrar")//borrar sala
	location.href= "http://127.0.0.1:4567/menuHost"
});

id("esperar").addEventListener("click", function (e) {
	webSocket1.send("esperar")
});

/*id("volver").addEventListener("click", function() {
	location.href ="http://127.0.0.1:4567/adminMenu";
});*/
id("button").addEventListener("click", function() {
	loadDoc();
});

function loadDoc() {
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
    	del("demo");
    	insert("demo", this.responseText)
    	//document.getElementById("demo").innerHTML = this.responseText;
    }
  };
  xhttp.open("GET", "ajax_info.txt", true);
  xhttp.send();
}

function borrar() {
	webSocket1.send()
}

function del(targetId) {
    id(targetId).innerHTML = "";
}


//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Update the chat-panel, and the list of connected users
function upDate(msg) {
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}