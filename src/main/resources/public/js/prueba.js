//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/hi");
webSocket.onmessage = function (msg) { sendMessage() };
webSocket.onclose = function () { alert("WebSocket connection closed") };



//Send a message if it's not empty, then clear the input field
function sendMessage() {
    console.log("hola1");
    webSocket.send("hola2");
}