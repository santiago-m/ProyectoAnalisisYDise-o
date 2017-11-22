//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/multiplayerGame");

webSocket.onopen = function() {
	console.log('started');
	wait(function(){return username}, function() {return hostname}, function() {sendData()});
}

webSocket.onmessage = function(msg) {
	console.log(msg['data']);
	if (msg['data'] == 'Can_Play') {
		console.log('entro');
		setTimeout(function(){ $('#connectGame').submit(); }, 3000);
	}
}
webSocket.onclose = function () {  };

var username;
var hostname;

window.onload = function() {
	var name = document.getElementById('username').textContent;
	var host = document.getElementById('hostname').textContent;
	
	username = name;
	hostname = host;
}

function sendData() {
	console.log("user: " + username);
	console.log("hostname: " + hostname);
	webSocket.send(JSON.stringify({
  		owner: username,
  		hostname: hostname
  	}));
}

function wait(condition1, condition2, callback) {
	console.log(typeof condition1());
	console.log(typeof condition2());
    if (typeof condition1() !== "undefined") {
    	if (typeof condition2() !== "undefined") {
        	callback();
    	}
    	else {
        	setTimeout(function () {
            	wait(condition1, condition2, callback);
        	}, 0);
    	}
    }
    else {
        setTimeout(function () {
            wait(condition1, condition2, callback);
        }, 0)
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