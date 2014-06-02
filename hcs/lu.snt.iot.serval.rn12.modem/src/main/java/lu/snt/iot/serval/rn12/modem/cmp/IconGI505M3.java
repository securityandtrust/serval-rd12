/**
 * Project: EnTiMid
 * Copyright: INRIA/IRISA 2011
 */
package lu.snt.iot.serval.rn12.modem.cmp;

import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyContact;
import lu.snt.iot.serval.rn12.framework.data.msg.EmergencyMessage;
import lu.snt.iot.serval.rn12.modem.cmd.*;
import lu.snt.iot.serval.rn12.modem.core.Kernel;
import lu.snt.iot.serval.rn12.modem.utils.SerialConnectionManager;
import org.kevoree.annotation.*;
import org.kevoree.api.Port;
import org.kevoree.log.Log;

import java.util.HashSet;
import java.util.Hashtable;

@Library(name = "Serval_RN12")
@ComponentType
public class IconGI505M3 {

    @Param(optional = false)
    private String pincode = "0000";

    @Param(optional = false)
    private String serialPort = "/dev/ttyHS2";

    @Output(optional = true)
    public Port msgReceived;

    @Output(optional = true)
    public Port ackOut;

    private Hashtable<String,EmergencyMessage> messagesWaitingAck = new Hashtable<String,EmergencyMessage>();
    private HashSet<EmergencyContact> contacts = new HashSet<EmergencyContact>();



    public IconGI505M3() {
        Kernel.setComponent(this);
        Kernel.setCommandManager(new CmdManager());
        Kernel.setSerialConnectionManager(new SerialConnectionManager());
        Kernel.setMessageReceiver(new MessageReceiver());
    }

    @Input
    public void ackIn(Object o) {
        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getPhoneNumber() != null) {
                for(String key : messagesWaitingAck.keySet()) {
                    if(msg.getContact().getPhoneNumber().contains(key)) {
                        messagesWaitingAck.remove(key);
                        Log.debug("Answer for " + msg.getContact().getPhoneNumber() + " received by another mean.");
                        break;
                    }
                }
            }
        }
    }

    @Input
    public void send(Object o) {
        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getPhoneNumber() != null) {
                Kernel.getCommandManager().process(new CMGF());

                CMSG messageCommand = new CMSG();
                messageCommand.setContent(msg.getMessage());
                messageCommand.setPhoneNumber(msg.getContact().getPhoneNumber());
                Kernel.getCommandManager().process(messageCommand);
                contacts.add(msg.getContact());
            } else {
                Log.debug("No Phone number for this contact: " + msg.getContact().toString());
            }
        } else {
            Log.error("Received an object to send, not instance of EmergencyMessage. Class" + o.getClass().getName());
        }
    }


    @Input
    public void sendWithAck(Object o) {

        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getPhoneNumber() != null) {
                Kernel.getCommandManager().process(new CMGF());

                CMSG messageCommand = new CMSG();
                messageCommand.setContent(msg.getMessage());
                messageCommand.setPhoneNumber(msg.getContact().getPhoneNumber());
                Kernel.getCommandManager().process(messageCommand);

                Log.debug("Store " + msg.toString() + " at " + msg.getContact().getPhoneNumber());
                messagesWaitingAck.put(msg.getContact().getPhoneNumber(), msg);
                contacts.add(msg.getContact());
            } else {
                Log.debug("No Phone number for this contact: " + msg.getContact().toString());
            }
        } else {
            Log.error("Received an object to send, not instance of EmergencyMessage. Class" + o.getClass().getName());
        }


    }

    public void messageReceived(CMGR message) {
        Log.debug(message.getDatetime() + " from " + message.getPhoneNumber() + " said " + message.getContent());
        String num = message.getPhoneNumber().replace("\"","");

        EmergencyMessage alert = null;

        for(String key : messagesWaitingAck.keySet()) {
            if(message.getPhoneNumber().contains(key)) {
                alert = messagesWaitingAck.remove(key);
                break;
            }
        }

        if(alert!= null) {
            Log.debug("Found " + alert + " for " + num);

            alert.setAnswer(message.getContent());
            if(msgReceived.getConnectedBindingsSize() > 0 ) {
                if(ackOut.getConnectedBindingsSize() > 0 ) {
                    ackOut.send(alert);
                }
                msgReceived.send(alert);
            } else {
                Log.warn("Sms answer received, but port not binded.");
                Log.warn(alert.toString());
            }
        } else {

            EmergencyContact contact = new EmergencyContact();
            contact.setPhoneNumber(message.getPhoneNumber());

            for(EmergencyContact c : contacts) {
                //try to find an existing contact.
                if(message.getPhoneNumber().contains(c.getPhoneNumber())){
                    contact = c;
                    break;
                }
            }

            EmergencyMessage m = new EmergencyMessage(contact,"");
            m.setAnswer(message.getContent());

            if(msgReceived.getConnectedBindingsSize() > 0) {
                msgReceived.send(m);
            } else {
                Log.warn("Sms received, but port not binded.");
                Log.warn(m.toString());
            }


            Log.warn("No message awaiting answer found for " + message.getPhoneNumber());
        }

    }
/*
    private void setEcho(SerialPort serial, boolean on) {
        try {
            serial.write(("ATE" + (on ? "1" : "0") + "\r\n").getBytes());
            if(!on) {
                logger.debug("Echo:" + readLine());
            }
            logger.debug("Answer:" + readLine());
        } catch (SerialPortException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    */

    public void pinCheck() {
        CPIN pinCommand = new CPIN();
        pinCommand.setPinCode(pincode);
        Kernel.getCommandManager().process(pinCommand);
    }

    @Start
    public void start() {
        update();
        Kernel.getSerialConnectionManager().setSerialPortName(serialPort);
        Kernel.getSerialConnectionManager().initializeSerialConnection();
    }

    @Stop
    public void stop() {
        Kernel.getSerialConnectionManager().closeConnection();
    }

    public void update() {
        if (pincode != null) {
            if (pincode.length() != 4) {
                Log.error("Pin code does not seem to be correct");
            }
            pinCheck();
        }
    }



}
