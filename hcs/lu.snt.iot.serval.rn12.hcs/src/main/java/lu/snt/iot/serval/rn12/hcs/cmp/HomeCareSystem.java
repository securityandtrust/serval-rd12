package lu.snt.iot.serval.rn12.hcs.cmp;

import lu.snt.iot.serval.rn12.framework.data.Alert;
import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyCallList;
import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyContact;
import lu.snt.iot.serval.rn12.framework.data.id.IdentityRecord;
import lu.snt.iot.serval.rn12.framework.data.msg.EmergencyMessage;
import lu.snt.iot.serval.rn12.hcs.helpers.HcsHelper;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.*;
import org.kevoree.api.Context;
import org.kevoree.api.KevScriptService;
import org.kevoree.api.ModelService;
import org.kevoree.api.Port;
import org.kevoree.api.handler.ModelListener;
import org.kevoree.api.handler.UpdateCallback;
import org.kevoree.log.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

@ComponentType
@Library(name = "Serval_RN12")
public class HomeCareSystem {

    private boolean starting = true;
    private Alert ongoingAlert;
    private List<Alert> pastAlerts;

    @KevoreeInject
    private Context context;

    @KevoreeInject
    private ModelService modelService;

    @KevoreeInject
    private KevScriptService kevScriptService;

    @Output(optional = false)
    protected Port extCom;

    @Output(optional = false)
    protected Port extComNoAck;

    @Output(optional = false)
    protected Port intCom;

    @Output(optional = false)
    protected Port eclProvider;



    public HomeCareSystem() {
        pastAlerts = new ArrayList<Alert>();
    }

    private void store(Alert alert) {
        pastAlerts.add(alert);
    }

    @Input
    public void doorSensor(Object o) {
        if(ongoingAlert != null) {
            if(ongoingAlert.getEmergencyResponder() != null) {
                playOnIntercom("Hello " + ongoingAlert.getEmergencyResponder().getName() + " thank you for coming. I let you take care of " + ongoingAlert.getIdentityRecord().getFirstName());
            }
            ongoingAlert.closeAlert();
            store(ongoingAlert);
            deployProxy(false);

            ongoingAlert = null;
            Log.info("End of Alert.");
        } else {
            Log.debug("Door sensor port activated with no ongoing alert.");
        }
    }

    @Input
    public void needHelp(Object o) {
        if(ongoingAlert != null) {
            playOnIntercom("Dear Annette. An alert is currently being processed.");
        } else {
            Log.info("Beginning of ALERT : " + System.currentTimeMillis());

            IdentityRecord identity = new IdentityRecord();
            identity.setFirstName("Annette");
            identity.setLastName("BECKER");

            //build an alert entry
            ongoingAlert = new Alert(identity);

            playOnIntercom("Dear Annette. I received your request for help. I am processing it.");

            //look for the emergency call list
            Log.debug("Collecting ECL.");
            eclProvider.send(ongoingAlert.getIdentityRecord());
        }

    }

    private void deployProxy(boolean activate) {

        StringBuilder kscript = new StringBuilder();

        //KevScriptEngine engine = kevScriptService.createKevScriptEngine();
        if(activate) {

            kscript.append("include mvn:lu.snt.iot.serval.rn12:lu.snt.iot.serval.rn12.proxy:2.0\n");
            kscript.append("add selenia99.proxy : HaProxy\n");
            Log.debug("Running kevscript:"+kscript.toString());
            modelService.submitScript(kscript.toString(), new UpdateCallback(){
                public void run(Boolean aBoolean) {
                    if(aBoolean) {
                        Log.info("VideoProxy deployed.");
                    } else {
                        Log.error("Error occurred while deploying VideoProxy");
                    }
                }
            });

        } else {
            kscript.append("remove selenia99.proxy\n");
            Log.debug("Running kevscript:"+kscript.toString());
            modelService.submitScript(kscript.toString(), new UpdateCallback(){
                public void run(Boolean aBoolean) {
                    if(aBoolean) {
                        Log.info("VideoProxy Removed.");
                    } else {
                        Log.error("Error occurred while undeploying VideoProxy");
                    }
                }
            });

        }

    }

    @Input
    public void textReceived(Object o) {
        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(ongoingAlert != null) {
                if(ongoingAlert.getEmergencyResponder() == null) {

                    if(HcsHelper.isAnswerPositive(msg)) { //Positive answer, First response
                        ongoingAlert.setEmergencyResponder(msg.getContact());
                        deployProxy(true);
                        sendAccessInformation(msg.getContact());
                    } else {//Negative answer, First response
                        playOnIntercom(msg.getContact().getName()+ " can not come.");
                        askNextContact();
                    }
                } else {//Somebody said yes !

                    if(msg.getContact() == ongoingAlert.getEmergencyResponder()) {
                        //The person who said yes, finally says no
                        if( HcsHelper.isAnswerNegative(msg)) {
                            sendMessageNoAck(msg.getContact(), "Ok. Thank you for telling.");
                            playOnIntercom(msg.getContact().getName()+ " can not come.");
                            ongoingAlert.setEmergencyResponder(null);
                            askNextContact();
                        }
                    } else {
                        //Somebody said yes, somebody else also says yes.
                        if(HcsHelper.isAnswerPositive(msg)) {
                            sendMessageNoAck(msg.getContact(), "Many thanks, but somebody is already taking care of " + ongoingAlert.getIdentityRecord().getFirstName());
                        }
                    }
                }
            } else {
                Log.warn("Received a message but no alert is ongoing." +  msg.getAnswer());
            }
        } else {
            Log.error("Received something but not of expected type. Class:"+o.getClass().getName());
        }
    }

    private void sendAccessInformation(EmergencyContact contact) {
        sendMessageNoAck(contact, "The key is in a safe box on the right of the door. The code is 7546. You can see what is happening on http://gate.sntiotlab.lu:4001");
        playOnIntercom(contact.getName() + " is on the way to come.");
    }


    @Input
    public void ecl(Object o) {

        if(o instanceof EmergencyCallList) {
            if(ongoingAlert != null) {
                ongoingAlert.setEmergencyCallList((EmergencyCallList)o);
                askNextContact();
            } else {
                Log.warn("Emergency call list received while no alert occurred.");
            }
        } else {
            Log.error("Received an object with and unknown format. Class:" + o.getClass().getName(), new IllegalArgumentException());
        }
    }

    private void askNextContact() {
        String message = ongoingAlert.getIdentityRecord().getFirstName();
        message += " " + ongoingAlert.getIdentityRecord().getLastName();
        message += " requires assistance. Can you go and check now ?";
        EmergencyContact contact = ongoingAlert.getEmergencyCallList().getNextContact(ongoingAlert);
        if(contact != null) {
            playOnIntercom("I try to contact " + contact.getName());
            sendMessageAck(contact, message);
        } else {
            playOnIntercom("Annette. I could not find somebody to come. I call the emergency services.");
            Log.info("No more contact in the Emergency Call List. Forwarding the request to Emergency Services.");
        }
    }

    private void sendMessageAck(EmergencyContact contact, String message) {
        if(ongoingAlert != null) {
            if(extCom.getConnectedBindingsSize() > 0 ) {
                EmergencyMessage emergencyMessage = new EmergencyMessage(contact,message);
                ongoingAlert.addCommunication(emergencyMessage);
                extCom.send(emergencyMessage);
            } else {
                Log.error("No external communication mean connected. Cannot send message.");
            }
        } else {
            Log.error("AskNextContact called with no ongoing alert.");
        }
    }

    private void sendMessageNoAck(EmergencyContact contact, String message) {
        if(ongoingAlert != null) {
            if(extComNoAck.getConnectedBindingsSize() > 0 ) {
                EmergencyMessage emergencyMessage = new EmergencyMessage(contact,message);
                ongoingAlert.addCommunication(emergencyMessage);
                extComNoAck.send(emergencyMessage);
            } else {
                Log.error("No external communication mean connected. Cannot send message.");
            }
        } else {
            Log.error("AskNextContact called with no ongoing alert.");
        }
    }

    @Start
    public void start() {

        modelService.registerModelListener(new ModelListener() {
            @Override
            public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
                return true;
            }

            @Override
            public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
                return true;
            }

            @Override
            public boolean afterLocalUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
                return true;
            }

            @Override
            public void modelUpdated() {
                if (starting) {
                    playOnIntercom("Hello. I just want to inform you, that the Home Care System is now ready to operate.");
                    starting = false;
                }
            }

            @Override
            public void preRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void postRollback(ContainerRoot containerRoot, ContainerRoot containerRoot1) {
                //To change body of implemented methods use File | Settings | File Templates.
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
        if(intCom.getConnectedBindingsSize() > 0) {
            intCom.send(internalCom);
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
