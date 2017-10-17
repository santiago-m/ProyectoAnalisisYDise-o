//Establish the WebSocket connection and set up event handlers
var webSocket2 = new WebSocket("ws://" + location.hostname + ":" + location.port + "/ho");
webSocket2.onmessage = function (msg) { sendMessage2() };
webSocket2.onclose = function () { alert("WebSocket connection closed") };



//Send a message if it's not empty, then clear the input field
function sendMessage2() {
    console.log("hola3");
    webSocket2.send("hola4");
}