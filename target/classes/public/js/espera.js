//Establish the WebSocket connection and set up event handlers
var webSocket1 = new WebSocket("ws://" + location.hostname + ":" + location.port + "/wait");
webSocket1.onmessage = function (msg) { redireccion(msg) };
webSocket1.onclose = function () {  };

id("volver").addEventListener("click", function (e) {
	webSocket1.send("borrar")//borrar sala
	location.href= "http://"+ location.hostname + ":"+ location.port +"/menuHost"
});

function redireccion(msg) {
  //if (msg.localeCompare("partida_encontrada") ) {
    location.href= "http://"+ location.hostname + ":"+ location.port +"/playTwoPlayers"
  //} /*else if (msg == "") {

  //}*/
  console.log("hi!");
  
}

function del(targetId) {
    id(targetId).innerHTML = "";
}

//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}