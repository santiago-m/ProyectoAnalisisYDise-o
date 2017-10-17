//Establish the WebSocket connection and set up event handlers
var webSocket4 = new WebSocket("ws://" + location.hostname + ":" + location.port + "/edicionPreguntas");
webSocket4.onmessage = function (msg) { upDate(msg) };
webSocket4.onclose = function () { };

id("cambiar").addEventListener("keypress", function (e) {
	if (e.keyCode === 13){
		//del("bloque");
		webSocket4.send(e.target.value);
	}
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

function del(targetId) {
    id(targetId).innerHTML = "";
}


//Helper function for inserting HTML as the first child of an element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("beforeend", message);
}

//Update the chat-panel, and the list of connected users
function upDate(msg) {
    var data = JSON.parse(msg.data);	// lo convertimos a objeto de js
    console.log(data);			

    // ahora acceder al arreglo con los indices
    var primero = data.pregunta[0];
    var prim = data.id[0];
    var segundo = data.pregunta[1];
    var seg = data.id[1];

    console.log(primero);
    console.log(segundo);

    insert("bloque", ("<input type=\"radio\" name=\"opciones\" value=\"" + prim + "\"/>"+ primero + "<br>"));
    insert("bloque", ("<input type=\"radio\" name=\"opciones\" value=\"" + seg + "\"/>"+ segundo + "<br>"));
}

//Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}