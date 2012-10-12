package lu.snt.iot.serval.rn12.hcs.cmp;

import org.kevoree.annotation.*;
import org.kevoree.api.service.core.script.KevScriptEngine;
import org.kevoree.api.service.core.script.KevScriptEngineException;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.kevoree.framework.service.handler.ModelListenerAdapter;
import org.slf4j.LoggerFactory;

import javax.swing.*;
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

    private Map<String, Object> ongoingAlert;
    private List<Map<String, Object>> pastAlerts;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(HomeCareSystem.class);

    public HomeCareSystem() {
        ongoingAlert = new Hashtable<String, Object>();
        pastAlerts = new ArrayList<Map<String, Object>>();
    }

    private void store(Map<String, Object> alert) {
        pastAlerts.add(alert);
    }

    @Port(name="doorSensor")
    public void onDoorSensorActivated(Object o) {
        if(ongoingAlert != null) {
            ongoingAlert.put("time.event.end", System.currentTimeMillis());

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

        //build an alert entry
        Map<String, Object> alert =  new Hashtable<String, Object>();
        alert.put("time.event.start", System.currentTimeMillis());
        alert.put("patient.name.last", "BECKER");
        alert.put("patient.name.first", "Annette");
        ongoingAlert = alert;

        //Prepare a internal communication for the user
        Properties internalCom = new Properties();
        internalCom.put("message","Annette. I received your request for help. I am processing it.");
        if(isPortBinded("intCom")) {
            ((MessagePort)getPortByName("intCom")).process(internalCom);
        }

        //look for the emergency call list
        logger.debug("HCS::FallDetection::Hashtable:" + alert.toString() + " size:" + alert.size());
        ((MessagePort)getPortByName("eclProvider")).process(alert);
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
        Map<String, Object> alert = (Map<String,Object>)o;

        if(alert.containsKey("text.id")) {
            int textId = (Integer)alert.get("text.id");
            int newTextId = textId+1;
            logger.debug("HCS::MessageReceived: msg->" + (String)alert.get("text." + textId + ".response"));
            if(!alert.containsKey("ecl.accepted")) {

                if(((String)alert.get("text." + textId + ".response")).toLowerCase().contains("yes")) {

                    deployProxy(true);

                    alert.put("text.id",newTextId);
                    alert.put("text."+newTextId+".name",alert.get("text." + textId + ".name"));
                    alert.put("text."+newTextId+".number",alert.get("text."+textId+".number"));
                    if(alert.containsKey("text."+textId+".xmpp")) {
                        alert.put("text."+newTextId+".xmpp",alert.get("text."+textId+".xmpp"));
                    }
                    alert.put("text."+newTextId+".content", "The code to get in is: C0369X. You can also see what is happening on http://goo.gl/fusdr");
                    alert.put("ecl.accepted",textId);
                    ((MessagePort)getPortByName("extCom")).process(alert);

                    Properties internalCom = new Properties();
                    internalCom.put("message",alert.get("text." + textId + ".name") + " is on the way to come.");
                    if(isPortBinded("intCom")) {
                        ((MessagePort)getPortByName("intCom")).process(internalCom);
                    }

                } else if(((String)alert.get("text." + textId + ".response")).toLowerCase().contains("no")) {

                    Map<String, String> ecl = (Hashtable<String, String>) alert.get("ecl");
                    //send a message to the first contact
                    alert.put("text.id",newTextId);
                    alert.put("text."+newTextId+".name",alert.get("ecl." + newTextId +".name"));
                    alert.put("text."+newTextId+".number",alert.get("ecl." + newTextId + ".number"));
                    if(alert.containsKey("ecl."+newTextId+".xmpp")) {
                        alert.put("text."+newTextId+".xmpp",alert.get("ecl."+newTextId+".xmpp"));
                    }
                    alert.put("text."+newTextId+".content", alert.get("patient.name.first") + " " + alert.get("patient.name.last") + " requires assistance. Can you go and check now ?");
                    ((MessagePort)getPortByName("extCom")).process(alert);

                    Properties internalCom = new Properties();
                    internalCom.put("message",alert.get("text." + textId + ".name") + " can not come. I contact " + alert.get("ecl." + newTextId +".name"));
                    if(isPortBinded("intCom")) {
                        ((MessagePort)getPortByName("intCom")).process(internalCom);
                    }

                } else {

                    //JOptionPane.showMessageDialog(null, "Cannot get information from the answer. Trying next contact.", "Warning" ,JOptionPane.WARNING_MESSAGE);
                    Map<String, String> ecl = (Hashtable<String, String>) alert.get("ecl");
                    //send a message to the first contact
                    alert.put("text.id",newTextId);
                    alert.put("text."+newTextId+".name",alert.get("ecl." + newTextId +".name"));
                    alert.put("text."+newTextId+".number",alert.get("ecl." + newTextId + ".number"));
                    if(alert.containsKey("ecl."+newTextId+".xmpp")) {
                        alert.put("text."+newTextId+".xmpp",alert.get("ecl."+newTextId+".xmpp"));
                    }
                    alert.put("text."+newTextId+".content", alert.get("patient.name.first") + " " + alert.get("patient.name.last") + " requires assistance. Can you go and check now ?");
                    ((MessagePort)getPortByName("extCom")).process(alert);
                }
            }
            ongoingAlert = alert;
        }
    }

    @Port(name="ecl")
    public void onEclReceived(Object o) {
        //Receipt of the emergency call list
        Map<String, Object> alert =  (Map<String, Object>)o;
        logger.debug("HCS::EclReceived::Hashtable:" + alert.toString() + " size:" + alert.size());
        //send a message to the first contact
        alert.put("text.id",0);
        alert.put("text.0.name",alert.get("ecl.0.name"));
        alert.put("text.0.number",alert.get("ecl.0.number"));
        if(alert.containsKey("ecl.0.xmpp")) {
            alert.put("text.0.xmpp",alert.get("ecl.0.xmpp"));
        }
        alert.put("text.0.content", alert.get("patient.name.first") + " " + alert.get("patient.name.last") + " requires assistance. Can you go and check now ?");
        ((MessagePort)getPortByName("extCom")).process(alert);
        ongoingAlert = alert;

        //Prepare a internal communication for the user
        Properties internalCom = new Properties();
        internalCom.put("message","I try to contact " + alert.get("ecl.0.name"));
        if(isPortBinded("intCom")) {
            ((MessagePort)getPortByName("intCom")).process(internalCom);
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
                Properties internalCom = new Properties();
                internalCom.put("message","Hello. I just want to inform you, that the Home Care System is now ready to operate.");
                if(isPortBinded("intCom")) {
                    ((MessagePort)getPortByName("intCom")).process(internalCom);
                }
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


    private void flushAlerts() {

        try {
            PrintWriter pr = new PrintWriter(new FileOutputStream(new File("alertsFlush" + System.currentTimeMillis() + ".txt"),true));
            if(ongoingAlert != null) {
                pr.println("##ONGOING");
                for(Map.Entry e : ongoingAlert.entrySet()) {
                    pr.println(e.getKey().toString() + "\t\t= " + e.getValue().toString());
                }
                pr.println("##ONGOING_END");
                pr.println();
            }

            for(Map<String, Object> alert : pastAlerts) {
                pr.println("##PAST_ALERT_" + alert.get("time.event.start"));
                for(Map.Entry e : alert.entrySet()) {
                    pr.println(e.getKey().toString() + "\t\t= " + e.getValue().toString());
                }
                pr.println("##PAST_ALERT_" + alert.get("time.event.start") + "_END");
                pr.println();
            }

            pr.flush();
            pr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


}
