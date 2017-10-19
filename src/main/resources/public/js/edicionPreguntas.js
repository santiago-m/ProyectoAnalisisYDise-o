//Establish the WebSocket connection and set up event handlers
var webSocket4 = new WebSocket("ws://" + location.hostname + ":" + location.port + "/edicionPreguntas");
webSocket4.onmessage = function (msg) { upDate(msg) };
webSocket4.onclose = function () { };


// Helper function for send the content for searching
id("cambiar").addEventListener("keypress", function (e) {
	if (e.keyCode === 13) {
		webSocket4.send(e.target.value);
	}
});

// Update the options to display
function upDate(msg) {
    var data = JSON.parse(msg.data);
    toHtml(data.id, data.pregunta);
}

// Creates an html content from the lists
function toHtml (id, pregunta/*, correcta, mal1, mal2, mal3, activa*/){ // <-- Comments for future ((paused ATM))
    for (var i = id.length - 1; i >= 0; i--) {
        insert("bloque", ("<input type=\"radio\" name=\"opciones\" value=\"" + id[i] + "\"/>"+ pregunta[i] + "<br>") );
    }
    insert("bloque", ("<input id=\"submit\" name=\"submit\" type=\"submit\" class=\"btn btn-default\" value=\"Cambiar\" />"));
}

// Helper function for selecting element by id ((From chat example))
function id(id) {
    return document.getElementById(id);
}

// Helper function for inserting HTML as the first child of an element ((From chat example))
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("beforeend", message);
}

//Helper function for cleaning HTML
function del(targetId) {
    id(targetId).innerHTML = "";
}