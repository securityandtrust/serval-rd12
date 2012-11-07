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

import org.json.JSONException;
import org.json.JSONStringer;
import org.kevoree.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebSocketComponent {

    private Logger logger = LoggerFactory.getLogger(WebSocketComponent.class.getName());
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
        logger.info("Server running at {}", webServer.getUri());

    }

    @Stop
    public void stopServer() {
        webServer.stop();
        webServer = null;
        cSrv = null;
    }

}
