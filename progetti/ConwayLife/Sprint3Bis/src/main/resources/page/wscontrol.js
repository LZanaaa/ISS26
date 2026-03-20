/*
wscontrol.js
*/
var pageId = "unknown";
var cmdMsgTemplate = "msg( eval, dispatch, SENDER, lifectrl, CMD, 0 )"
var opened = false
var socketToGui;

function sendCmdToServer(cmd) {
	console.log("sendCmdToServer:" + cmd)
	msg = cmdMsgTemplate.replace("CMD", cmd).replace("SENDER", pageId)
	//addItem("sendCmdToServer: " + msg + " opened=" + opened);		 
	if (opened) socketToGui.send(msg);
}

function initWS() {
	/*1*/
	console.log("initWS | window.location.host=" + window.location.host);
	if (window.location.host == "") socketToGui = new WebSocket("ws://localhost:8080/chat");
	else socketToGui = new WebSocket("ws://" + window.location.host + "/eval");

 /*2*/socketToGui.onopen = () => {
		console.log("initWS | Connesso a eval");
		addItem("initWS | Connesso a chat");
		opened = true;
		sendCmdToServer("ready");
	}

/*3*/socketToGui.onmessage = (event) => {
		console.log("initWS | onmessage:", event.data);

		
		if (event.data === "ROLE:OWNER") {
			document.getElementById("controlsArea").style.display = "block";
			document.getElementById("observerMsg").style.display = "none";
			addItem("Sistema: Sei l'Owner. Hai il controllo del gioco.");
		}
		else if (event.data === "ROLE:OBSERVER") {
			document.getElementById("controlsArea").style.display = "none";
			document.getElementById("observerMsg").style.display = "block";
			addItem("Sistema: Sei un Observer. Sola lettura.");
		}
		// -------------------------------------------

		else if (event.data.startsWith("ID:")) {
			pageId = event.data.split(":")[1];
			addItem("page ID=" + pageId);
		}
		else if (event.data.startsWith("cell(")) { //deve ricevere da caller
			coords = event.data.replace("cell(", "").replace(")", "").split(",");
			updateCellColor(coords[0], coords[1], coords[2])  //In iomap.js
		} else {
			if (event.data != "PING") addItem(event.data);
		}
	}//initWS

 }
	//addItem("Welcome to conwaygui ....");  
	initWS()


