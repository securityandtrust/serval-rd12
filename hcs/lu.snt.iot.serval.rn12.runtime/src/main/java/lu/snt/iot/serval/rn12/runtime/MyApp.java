/**
 * © 2012 - University of Luxebourg - Interdisciplinary Center for Security, Reliability and Trust
 * Author: Gregory NAIN
 */
package lu.snt.iot.serval.rn12.runtime;

/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 30/10/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import org.kevoree.log.Log;
import org.kevoree.platform.standalone.App;

public class MyApp {

    private WebSocketComponent webSocketComponent = new WebSocketComponent();
    private static MyApp INSTANCE = new MyApp();

    public static MyApp getInstance() {
        return INSTANCE;
    }

    public WebSocketComponent getWebSocket() {
        return webSocketComponent;
    }

    public static void main(String[] args) throws Exception {
        MyApp app = MyApp.getInstance();
        app.webSocketComponent.startServer();
        Log.setLogger(new HCSLogger());
        App.main(args);
    }

}
