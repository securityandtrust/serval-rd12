package lu.snt.iot.serval.rn12.karotz;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 11/05/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/



/*
String sk = "87683e1e-c478-4c2a-b46e-f22bd6aca916";
   String ak = "ae5e7b12-bea8-4c5a-80d5-cd88576a95e5";
   String iid = "2b96ef18-b795-4b62-b528-29019c0e84d7";

*/

import fr.gn.karotz.Karotz;
import fr.gn.karotz.actions.MultimediaAction;
import fr.gn.karotz.actions.TextToSpeachAction;
import fr.gn.karotz.msg.ResponseMessage;
import fr.gn.karotz.msg.ServerAnswer;
import fr.gn.karotz.utils.Languages;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Provides({
        @ProvidedPort(type = PortType.MESSAGE, name = "tts")
})
@DictionaryType({
        @DictionaryAttribute(name = "secretKey", optional = false),
        @DictionaryAttribute(name = "applicationKey", optional = false),
        @DictionaryAttribute(name = "installationId", optional = false)
})
@ComponentType
@Library(name = "Serval_RN12")
public class KarotzCmp  extends AbstractComponentType {

    private Karotz karotz;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KarotzCmp.class);
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private Future currentTimer = null;
    private Future pendingMessage = null;


    @Port(name = "tts")
    public void onTtsRequest(Object o) {

        Properties p = (Properties)o;

        logger.info(getName() + " says: "+ (String)p.get("message"));

            sendMessage((String)p.get("message"));


    }

    private void sendMessage(final String message) {
        try {
            if(karotz.initSession()) {

                ServerAnswer answer = karotz.send(new MultimediaAction(karotz.getKernel(), message, Languages.EN));
                if(answer instanceof ResponseMessage) {
                    ResponseMessage response = (ResponseMessage)answer;
                    if(response.getCode() == ResponseMessage.ResponseCode.ERROR) {
                        if(pendingMessage != null && !pendingMessage.isDone()) {
                            pendingMessage.cancel(true);
                        }
                        pendingMessage = executor.schedule(new Runnable() {
                            @Override
                            public void run() {
                                sendMessage(message);
                            }
                        }, 2, TimeUnit.SECONDS);
                    } else {
                        if(currentTimer != null && !currentTimer.isDone()) {
                            currentTimer.cancel(true);
                        }
                        currentTimer = executor.schedule(new Runnable() {
                            @Override
                            public void run() {
                                karotz.closeSession();
                            }
                        }, 20, TimeUnit.SECONDS);
                    }
                } else {
                    logger.error("Unknown server answer:" + answer.getClass().getName());
                }
            } else {
                logger.warn("Couldn't init the session.");
                pendingMessage = executor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(message);
                    }
                }, 10, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            logger.warn("Looks like Internet connection has been lost.\n" +e.getMessage());
        }
    }

    @Start
    public void start(){
        karotz = new Karotz(
                (String)getDictionary().get("applicationKey"),
                (String)getDictionary().get("secretKey"),
                (String)getDictionary().get("installationId"));

    }

    @Stop
    public void stop(){
        if(currentTimer != null) {
            currentTimer.cancel(true);
        }
        if(pendingMessage != null) {
            pendingMessage.cancel(true);
        }
        karotz.closeSession();
        karotz = null;
    }

    @Update
    public void update(){
        stop();
        start();
    }

}
