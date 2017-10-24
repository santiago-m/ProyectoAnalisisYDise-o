//Establish the WebSocket connection and set up event handlers
var webSocket2 = new WebSocket("ws://" + location.hostname + ":" + location.port + "/search");
webSocket2.onmessage = function (msg) { procesar(msg) };
webSocket2.onclose = function () {  };

/*indow.onload = function() {
 
    webSocket2.send("p_partidas");
 
};*/

$(document).on('click','#cosa2',function(){
  console.log( ( $(this) )[0].value);
  webSocket2.send( ( $(this) )[0].value);
  //location.href= "http://"+ location.hostname + ":"+ location.port +"/selectHost";
});

/*$( document ).ready(function() {
 
    $( "button" ).click(function( event ) {
 
        alert( "Thanks for visiting!" );
 
    });
 
});*/

if (id("refresh") != null) {
  id("refresh").addEventListener("click", function() {
    webSocket2.send("p_partidas");
  });
}

if (id("cosa2") != null) {  // posible solcion con jQuery
  id("cosa2").addEventListener("click", function(){
    console.log((id("cosa2")).value);
    //webSocket2.send();
  });
}

function mandar() {
  webSocket2.send("asd");
}

function procesar(msg) {
  var data = JSON.parse(msg.data);

  console.log(data.length);
  console.log(data);

  tableHtml(data.usuario, data.nombre_partida, data.cPreguntas);
  
  /*if (msg.localeCompare("buscar_partida")) {
    websocket1.send("partida_encontrada");
    location.href= "http://"+ location.hostname + ":"+ location.port +"/playTwoPlayers"

  } else if (msg.localeCompare("listarHost")) {
  }*/

}

// Helper function that creates a html table
function tableHtml(usuario, nombre_partida, cPreguntas) {
  del("partidas"); 
  if (usuario.length > 0) {

    for (var i = usuario.length - 1; i >= 0; i--) {
      insert("partidas",('<tr><td>' + nombre_partida[i] + '</td><td>' + cPreguntas[i] + '</td><td>' + usuario[i] + '</td><td><center><form action="/selectHost" method="POST"><input name="hostName" value="'+ nombre_partida[i] +'" type="hidden"><input class="btn" value="Unite!" type="submit"></form></td></tr>'));
      //<form action="/selectHost" method="post"><input name="hostName" value="'+ nombre_partida[i] +'" type="hidden"><input class="btn" value="Unite!" type="submit"></form></center>
      //insert("partidas",("<tr><th><input name=\"hostName\" value=\" "+ nombre_partida[i] +" \" type=\"hidden\"></th><th>" + usuario[i] + "</th><th><input class=\"btn\" value=\"" + nombre_partida[i] + "\" type=\"submit\"></th></tr>"));
    }
    id("partidas").insertAdjacentHTML("afterbegin", "<tr><th>Partida</th><th>Preguntas</th><th>Usuario</th><th></th></tr>");
  } else {
    insert("partidas", "<strong>No hay partidas para mostrar</strong>")
  }
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