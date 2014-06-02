/**
 * © 2012 - University of Luxebourg - Interdisciplinary Center for Security, Reliability and Trust
 * Author: Gregory NAIN
 */

package lu.snt.iot.serval.rn12.runtime;
/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 29/10/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import org.kevoree.annotation.*;
import org.kevoree.log.Log;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

public class WebSocketComponent {

   private WebServer webServer = null;
    private ContentServer cSrv = null;

    public void handleMessage(String msg) {
        cSrv.sendToAll(msg);
    }

    @Start
    public void startServer() {

        cSrv = new ContentServer();
        webServer = WebServers.createWebServer(1337)
                .add("/", cSrv);
        webServer.start();
        Log.info("Server running at {}", webServer.getUri());

    }

    @Stop
    public void stopServer() {
        webServer.stop();
        webServer = null;
        cSrv = null;
    }

}
