//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/multiplayerGame");
webSocket.onmessage = function(msg) {}

var WebSocket2 = new WebSocket("ws://" + location.hostname + ":" + location.port + "/search");
WebSocket2.onopen = function() {
  $('#refresh').click();
}
WebSocket2.onmessage = function (msg) { procesar(msg); };

if (id("refresh") != null) {
  id("refresh").addEventListener("click", function() {
    WebSocket2.send("p_partidas");
  });
}

// Function that process the message
function procesar(msg) {
  var data = JSON.parse(msg.data);
  tableHtml(data.usuario, data.nombre_partida, data.cPreguntas);
}

// Helper function that creates a html table
function tableHtml(usuario, nombre_partida, cPreguntas) {
  del_div("partidas");
  if (usuario.length > 0) {

    for (var i = usuario.length - 1; i >= 0; i--) {
      insert("partidas",('<tr><td>' + nombre_partida[i] + '</td><td>' + cPreguntas[i] + '</td><td>' + usuario[i] + '</td><td><center><form id="connectForm" action="/selectHost" method="POST"><input name="hostName" value="'+ nombre_partida[i] +'" type="hidden"><input class="btn" value="Unite!" onclick="javascript:joinHost(\''+ nombre_partida[i] +'\')"></form></td></tr>'));
      //                      // Primera columna               // Segunda columna            // Tercera Columna         // Cuarta columna
    }
    id("partidas").insertAdjacentHTML("afterbegin", "<tr><th>Partida</th><th>Preguntas</th><th>Creador</th><th></th></tr>");
  } else {
    insert("partidas", "<strong>No hay partidas para mostrar</strong>")
  }
}

//
function joinHost(hostname) {
  webSocket.send(JSON.stringify({
    hostname: hostname
  }));
  setTimeout(function(){ $('#connectForm').submit(); }, 1000);
}

// Helper function that clean the div
function del_div(targetId) {
  id(targetId).innerHTML = "";
}

// Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
  id(targetId).insertAdjacentHTML("afterbegin", message);
}

// Helper function for selecting element by id
function id(id) {
  return document.getElementById(id);
}
