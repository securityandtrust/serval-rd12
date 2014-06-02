
var ws;

function connectWebSocket() {
	
	ws = new WebSocket("ws://hcs.sntiotlab.lu:1337");
	
    ws.onopen = function() {
        var connectionState = $('#connection_state');
        connectionState.css('background-color','#8aff00');
        connectionState.text('ONLINE');
    };

    ws.onmessage = function (evt) {
        var received_msg = evt.data;
        console.log("Received:" + received_msg);
        var json = JSON.parse(received_msg);
        var content = $('#content');
        console.log("Received new log:" + json);

        switch(json.logLevel.toUpperCase()) {
            case "INFO" :
                content.prepend('<div class="info"><u><b>From</b> '+json.logFrom+' <b>on</b> '+json.logDate+' at '+json.logTime+' :</u><br/>'+json.logMessage+'</div>');
            break;
            case "WARN" :
                content.prepend('<div class="warning"><u><b>From</b> '+json.logFrom+' <b>on</b> '+json.logDate+' at '+json.logTime+' :</u><br/>'+json.logMessage+'</div>');
            break;
            case "ERROR" :
                content.prepend('<div class="error"><u><b>From</b> '+json.logFrom+' <b>on</b> '+json.logDate+' at '+json.logTime+' :</u><br/>'+json.logMessage+'</div>');
            break;
            case "DEBUG" :
            case "TRACE" :
                content.prepend('<div class="debug"><u><b>From</b> '+json.logFrom+' <b>on</b> '+json.logDate+' at '+json.logTime+' :</u><br/>'+json.logMessage+'</div>');
            break;
            default:
                content.prepend('<div class="error"><u><b>[['+json.logLevel.toUpperCase()+']] From</b> '+json.logFrom+' <b>on</b> '+json.logDate+' at '+json.logTime+' :</u><br/>'+json.logMessage+'</div>');
                console.log('LogLevel not recognized:' + json.logLevel.toUpperCase());
        }
		
		if ( content.hasChildNodes() )
		{
		    if ( content.childNodes.length >= 200 )
		    {
				for (i = 0; i < 50; i++) 
				{ 
				   content.removeChild( content.lastChild );       
				}
		    } 
		}
		
       // alert("Message is received...");
    };

    ws.onclose = function() {
        var connectionState = $('#connection_state');
        connectionState.css('background-color','#ff004e');
        connectionState.text('OFFLINE');
		console.log('Connection Closed!');
		
	        setTimeout(function() {
	            console.log('Connection Try(Close) !');
	            connectWebSocket();
	        },1000);
	    // websocket is closed.
       // alert("Connection is closed...");
    };

    ws.onerror = function (error) {
        // just in there were some problems with conenction...
        /*
        console.log('Connection Error !');
        setTimeout(function() {
            console.log('Connection Try(Error) !');
            connectWebSocket();
        },1000);
        */
    };
}
