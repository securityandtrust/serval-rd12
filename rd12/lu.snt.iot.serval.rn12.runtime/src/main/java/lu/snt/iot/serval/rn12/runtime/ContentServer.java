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

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContentServer extends BaseWebSocketHandler {

    private List<WebSocketConnection> clients = new ArrayList<WebSocketConnection>();

    @Override
    public void onOpen (WebSocketConnection connection) throws Exception {
        clients.add(connection);
    }

    void sendToAll (final String msg) {
        //messageList.add(msg);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        for (final WebSocketConnection client : clients) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    client.send(msg);
                }
            });
        }
    }

    @Override
    public void onClose (WebSocketConnection connection) throws Exception {
        clients.remove(connection);
    }

}
