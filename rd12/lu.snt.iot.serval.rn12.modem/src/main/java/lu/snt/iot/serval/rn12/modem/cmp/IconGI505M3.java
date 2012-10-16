/**
 * Project: EnTiMid
 * Copyright: INRIA/IRISA 2011
 */
package lu.snt.iot.serval.rn12.modem.cmp;

import lu.snt.iot.serval.rn12.framework.data.msg.EmergencyMessage;
import lu.snt.iot.serval.rn12.modem.cmd.*;
import lu.snt.iot.serval.rn12.modem.core.Kernel;
import lu.snt.iot.serval.rn12.modem.utils.SerialConnectionManager;
import org.kevoree.annotation.*;
import org.kevoree.extra.kserial.SerialPort.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingQueue;

@Library(name = "Serval_RN12")
@DictionaryType({
        @DictionaryAttribute(name = "pincode", optional = false),
        @DictionaryAttribute(name = "serialPort", optional = false)
})
@Provides({
        @ProvidedPort(name="sendWithAck", type = PortType.MESSAGE),
        @ProvidedPort(name="send", type = PortType.MESSAGE)
})

@Requires({
        @RequiredPort(name = "msgReceived", type = PortType.MESSAGE, optional = true)
})
@ComponentType
public class IconGI505M3 extends AbstractComponentType {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(IconGI505M3.class);
    private String pin = "0000";

    //private String phoneNum;
    private String serialPortName = "/dev/ttyHS2";

    private Hashtable<String,EmergencyMessage> messagesWaitingAck = new Hashtable<String,EmergencyMessage>();

    public IconGI505M3() {
        Kernel.setComponent(this);
        Kernel.setCommandManager(new CmdManager());
        Kernel.setSerialConnectionManager(new SerialConnectionManager());
        Kernel.setMessageReceiver(new MessageReceiver());
    }



    @Ports({@Port(name="sendWithAck"),@Port(name="send")})
    public void sendSMS(Object o) {


        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getPhoneNumber() != null) {
                Kernel.getCommandManager().process(new CMGF());

                CMSG messageCommand = new CMSG();
                messageCommand.setContent(msg.getMessage());
                messageCommand.setPhoneNumber(msg.getContact().getPhoneNumber());
                Kernel.getCommandManager().process(messageCommand);

                logger.debug("Store " + msg.toString() + " at " + msg.getContact().getPhoneNumber());
                messagesWaitingAck.put(msg.getContact().getPhoneNumber(), msg);
            } else {
                logger.debug("No Phone number for this contact: " + msg.getContact().toString());
            }
        } else {
            logger.error("Received an object to send, not instance of EmergencyMessage. Class" + o.getClass().getName());
        }


    }

    public void messageReceived(CMGR message) {
        logger.debug(message.getDatetime() + " from " + message.getPhoneNumber() + " said " + message.getContent());
        String num = message.getPhoneNumber().replace("\"","");

        EmergencyMessage alert = null;

        for(String key : messagesWaitingAck.keySet()) {
            if(message.getPhoneNumber().contains(key)) {
                alert = messagesWaitingAck.remove(key);
                break;
            }
        }

        if(alert!= null) {
            logger.debug("Found " + alert + " for " + num);
            MessagePort answer = (MessagePort) getPortByName("msgReceived");
            alert.setAnswer(message.getContent());
            if(isPortBinded("msgReceived")) {
                answer.process(alert);
            } else {
                logger.warn("Sms answer received, but port not binded.");
                logger.warn(alert.toString());
            }
        } else {
            logger.warn("No message awaiting answer found for " + message.getPhoneNumber());
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
        pinCommand.setPinCode(pin);
        Kernel.getCommandManager().process(pinCommand);
    }

    @Start
    public void start() {
        update();
        Kernel.getSerialConnectionManager().setSerialPortName(serialPortName);
        Kernel.getSerialConnectionManager().initializeSerialConnection();
    }

    @Stop
    public void stop() {
        Kernel.getSerialConnectionManager().closeConnection();
    }

    public void update() {
        Object o;
        if ((o = getDictionary().get("pincode")) != null) {
            pin = o.toString();
            if (pin.length() != 4) {
                logger.error("Pin code does seem to be correct");
            }
            pinCheck();
        }
        /*
        if ((o = getDictionary().get("phonenumber")) != null) {
            phoneNum = o.toString();
        }
        */
        if ((o = getDictionary().get("serialPort")) != null) {
            serialPortName = o.toString();
        }
    }



}
