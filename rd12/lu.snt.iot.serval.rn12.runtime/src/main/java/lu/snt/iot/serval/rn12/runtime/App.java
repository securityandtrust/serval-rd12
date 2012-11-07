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

import ch.qos.logback.classic.LoggerContext;
import org.slf4j.LoggerFactory;

public class App extends org.kevoree.platform.standalone.App {

    private WebSocketComponent webSocketComponent = new WebSocketComponent();
    private static App INSTANCE = new App();

    public static App getInstance() {
        return INSTANCE;
    }

    public WebSocketComponent getWebSocket() {
        return webSocketComponent;
    }

    public void start() {

        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        CustomLogbackAppender myAppender = new CustomLogbackAppender();
        myAppender.setContext(root.getLoggerContext());
        myAppender.start();
        root.addAppender(myAppender);

        webSocketComponent.startServer();

        try {
            super.start();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void main(String[] args) {
        App app = App.getInstance();

        app.start();
    }

}
