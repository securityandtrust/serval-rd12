package lu.snt.iot.serval.rn12.hcs.cmp;

import lu.snt.iot.serval.rn12.framework.data.Alert;
import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyCallList;
import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyContact;
import lu.snt.iot.serval.rn12.framework.data.id.IdentityRecord;
import lu.snt.iot.serval.rn12.framework.data.msg.EmergencyMessage;
import lu.snt.iot.serval.rn12.hcs.helpers.HcsHelper;
import org.kevoree.annotation.*;
import org.kevoree.api.service.core.script.KevScriptEngine;
import org.kevoree.api.service.core.script.KevScriptEngineException;
import org.kevoree.framework.MessagePort;
import org.kevoree.framework.service.handler.ModelListenerAdapter;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;


@Provides({
        @ProvidedPort(name = "needHelp", type = PortType.MESSAGE),
        @ProvidedPort(name = "textReceived", type = PortType.MESSAGE),
        @ProvidedPort(name = "phr", type = PortType.MESSAGE),
        @ProvidedPort(name = "ecl", type = PortType.MESSAGE),
        @ProvidedPort(name = "doorSensor", type = PortType.MESSAGE)
})

@Requires({
        @RequiredPort(name="extCom", type=PortType.MESSAGE, optional = false),
        @RequiredPort(name="intCom", type=PortType.MESSAGE, optional = false),
        @RequiredPort(name="eclProvider", type=PortType.MESSAGE, optional = false),
        @RequiredPort(name="phrProvider", type=PortType.MESSAGE, optional = false)
})

@ComponentType
@Library(name = "Serval_RN12")
public class HomeCareSystem extends org.kevoree.framework.AbstractComponentType {

    private Alert ongoingAlert;
    private List<Alert> pastAlerts;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HomeCareSystem.class);

    public HomeCareSystem() {
        pastAlerts = new ArrayList<Alert>();
    }

    private void store(Alert alert) {
        pastAlerts.add(alert);
    }

    @Port(name="doorSensor")
    public void onDoorSensorActivated(Object o) {
        if(ongoingAlert != null) {
            ongoingAlert.closeAlert();

            store(ongoingAlert);
            deployProxy(false);
            ongoingAlert = null;
            logger.info("End of Alert.");
        } else {
            logger.debug("Door sensor port activated with no ongoing alert.");
        }
    }

    @Port(name="needHelp")
    public void onHelpRequestReceived(Object o) {
        logger.info("Beginning of ALERT : " + System.currentTimeMillis());

        IdentityRecord identity = new IdentityRecord();
        identity.setFirstName("Annette");
        identity.setLastName("BECKER");

        //build an alert entry
        ongoingAlert = new Alert(identity);

        playOnIntercom("Annette. I received your request for help. I am processing it.");

        //look for the emergency call list
        logger.debug("Collecting ECL.");
        ((MessagePort)getPortByName("eclProvider")).process(ongoingAlert.getIdentityRecord());
    }

    private void deployProxy(boolean activate) {
        KevScriptEngine engine = getKevScriptEngineFactory().createKevScriptEngine();
        if(activate) {
            engine.append("merge 'mvn:org.kevoree.corelibrary.javase/org.kevoree.library.javase.nodeJS.proxy/1.8.7'");
            engine.append("addComponent input@node0 : NodeJSProxy { ip='192.168.1.216',port='8868',remotePort='80' }");
            try {
                engine.interpretDeploy();
                logger.info("VideoProxy deployed.");
            } catch (KevScriptEngineException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } else {
            engine.append("removeComponent input@node0");
            try {
                engine.interpretDeploy();
                logger.info("VideoProxy removed.");
            } catch (KevScriptEngineException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

    }

    @Port(name="textReceived")
    public void onMessageReceived(Object o) {

        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(ongoingAlert != null) {
                if(ongoingAlert.getEmergencyResponder() == null) {

                    if(HcsHelper.isAnswerPositive(msg)) {
                        ongoingAlert.setEmergencyResponder(msg.getContact());
                        deployProxy(true);
                        sendAccessInformation(msg.getContact());
                    } else {
                        playOnIntercom(msg.getContact().getName()+ " can not come.");
                        askNextContact();
                    }
                } else {
                    playOnIntercom(msg.getContact().getName() + " just said, " + msg.getAnswer());
                }
            } else {
                logger.warn("Received a message but no alert is ongoing." +  msg.getAnswer());
            }
        } else {
            logger.error("Received something but not of expected type. Class:"+o.getClass().getName());
        }
    }

    private void sendAccessInformation(EmergencyContact contact) {
        sendMessage(contact, "The code to get in is: C0369X. You can also see what is happening on http://goo.gl/fusdr");
        playOnIntercom(contact.getName() + " is on the way to come.");
    }


    @Port(name="ecl")
    public void onEclReceived(Object o) {

        if(o instanceof EmergencyCallList) {
            if(ongoingAlert != null) {
                ongoingAlert.setEmergencyCallList((EmergencyCallList)o);
                askNextContact();
            } else {
                logger.warn("Emergency call list received while no alert occurred.");
            }
        } else {
            logger.error("Received an object with and unknown format. Class:" + o.getClass().getName(), new IllegalArgumentException());
        }
    }

    private void askNextContact() {
        String message = ongoingAlert.getIdentityRecord().getFirstName();
        message += " " + ongoingAlert.getIdentityRecord().getLastName();
        message += " requires assistance. Can you go and check now ?";
        EmergencyContact contact = ongoingAlert.getEmergencyCallList().getNextContact(ongoingAlert);
        playOnIntercom("I try to contact " + contact.getName());
        sendMessage(contact,message);
    }

    private void sendMessage(EmergencyContact contact, String message) {
        if(ongoingAlert != null) {
            if(isPortBinded("extCom")) {

                EmergencyMessage emergencyMessage = new EmergencyMessage(contact,message);
                ongoingAlert.addCommunication(emergencyMessage);

                ((MessagePort)getPortByName("extCom")).process(emergencyMessage);

            } else {
                logger.error("No external communication mean connected. Cannot send message.");
            }
        } else {
            logger.error("AskNextContact called with no ongoing alert.");
        }
    }


    @Port(name="phr")
    public void onPhrReceived(Object o) {

    }


    @Start
    public void start() {
        getModelService().registerModelListener(new ModelListenerAdapter() {
            @Override
            public void modelUpdated() {
                playOnIntercom("Hello. I just want to inform you, that the Home Care System is now ready to operate.");
            }
        });

    }

    @Stop
    public void stop() {
        flushAlerts();
    }

    @Update
    public void update() {
        stop();
        start();
    }

    private void playOnIntercom(String message) {
        Properties internalCom = new Properties();
        internalCom.put("message",message);
        if(isPortBinded("intCom")) {
            ((MessagePort)getPortByName("intCom")).process(internalCom);
        }
    }


    private void flushAlerts() {

        try {
            PrintWriter pr = new PrintWriter(new FileOutputStream(new File("alertsFlush" + System.currentTimeMillis() + ".txt"),true));
            if(ongoingAlert != null) {
                pr.println("##ONGOING");
                pr.println(ongoingAlert.toString());
                pr.println("##ONGOING_END");
                pr.println();
            }

            pr.println("##PAST_ALERTS");
            pr.println();
            for(Alert alert : pastAlerts) {
                pr.println(alert.toString());
                pr.println();
            }
            pr.println("##PAST_ALERTS");

            pr.flush();
            pr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
