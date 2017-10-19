//Establish the WebSocket connection and set up event handlers
var webSocket2 = new WebSocket("ws://" + location.hostname + ":" + location.port + "/search");
webSocket2.onmessage = function () { procesar() };
webSocket2.onclose = function () {  };

function procesar() {
  //if (msg.localeCompare("")) {
    websocket1.send("partida_encontrada");
    location.href= "http://"+ location.hostname + ":"+ location.port +"/playTwoPlayers"

  //} else if (msg.localeCompare("listarHost")) {


  //}
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