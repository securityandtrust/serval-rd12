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
import fr.gn.karotz.msg.ResponseMessage;
import fr.gn.karotz.msg.ServerAnswer;
import fr.gn.karotz.utils.Languages;
import org.kevoree.annotation.*;
import org.kevoree.api.Context;
import org.kevoree.log.Log;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@ComponentType
@Library(name = "Serval_RN12")
public class KarotzCmp  {

    private Karotz karotz;
    private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private Future currentTimer = null;
    private Future pendingMessage = null;

    @KevoreeInject
    protected Context context;

    @Param(optional = false)
    protected String secretKey;

    @Param(optional = false)
    protected String applicationKey;

    @Param(optional = false)
    protected String installationId;

    @Input
    public void tts(Object o) {

        Properties p = (Properties)o;

        Log.info(context.getInstanceName() + " says: " + p.get("message"));

        sendMessage((String)p.get("message"));


    }

    private void sendMessage(final String message) {
        sendMessage(message, 0);
    }

    private void sendMessage(final String message, int trynum) {
        if(trynum > 5) {
            return;
        }
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
                                sendMessage(message, trynum+1);
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
                    Log.error("Unknown server answer:" + answer.getClass().getName());
                }
            } else {
                Log.warn("Couldn't init the session.");
                pendingMessage = executor.schedule(new Runnable() {
                    @Override
                    public void run() {
                        sendMessage(message, trynum+1);
                    }
                }, 10, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            Log.warn("Looks like Internet connection has been lost.\n" +e.getMessage());
        }
    }

    @Start
    public void start(){
        karotz = new Karotz(
                applicationKey,
                secretKey,
                installationId);

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
