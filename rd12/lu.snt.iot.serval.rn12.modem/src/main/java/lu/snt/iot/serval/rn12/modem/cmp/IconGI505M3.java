/**
 * Project: EnTiMid
 * Copyright: INRIA/IRISA 2011
 */
package lu.snt.iot.serval.rn12.modem.cmp;

import lu.snt.iot.serval.rn12.modem.lst.SerialEvtListener;
import org.kevoree.annotation.*;
import org.kevoree.extra.kserial.SerialPort.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.LoggerFactory;

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
public class IconGI505M3 extends AbstractComponentType implements SerialPortEventListener {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(IconGI505M3.class);
    private SerialEvtListener listener = new SerialEvtListener();
    private String pin = "0000";
    private long latency = 200;
    //private String phoneNum;
    private String serialPortName = "/dev/ttyHS2";


    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    public IconGI505M3() {
    }

    private String readLine() {

        try {
            //String taken = queue.take();
            return queue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }



    @Ports({@Port(name="sendWithAck"),@Port(name="send")})
    public boolean sendSMS(Object msg) {
        /*
        * Nominal sequence
        *
        * AT+CPIN=<codepin>
        * OK
        * AT+CMGF=1
        * OK
        * AT+CMGS="<numDeTel>"
        * >ligne1
        * >ligne n<ctrl+Z>
        *
        * +CMGI <num>
        *
        * OK
        */


        final Hashtable<String,Object> p = (Hashtable<String,Object>)msg;
        final int textId = (Integer)p.get("text.id");
        String message = (String)p.get("text."+textId+".content");
        String phoneNum = (String)p.get("ecl."+textId+".number");

        logger.debug("Starting sending \"" + message + " to " + phoneNum);

        try {

            SerialPort serial = initializeSerialConnection();

            if(checkPin(serial)) {
                logger.debug("Checking registration");
                while(!checkRegistration(serial)) {
                    logger.debug("Not registered. Sleep.");
                    Thread.sleep(3000);
                }
                logger.debug("Registered");

                serial.write("AT+CMGF=1\r\n".getBytes());
                Thread.sleep(latency);
                logger.debug("Echo:" + readLine());
                Thread.sleep(latency);
                String answer = readLine();
                logger.debug("Answer:" + answer);
                if(answer.contains("OK")) {

                    //Set phone number
                    boolean errorOccured;
                    do {
                        errorOccured = false;
                        serial.write(("AT+CMGS=\"" + phoneNum + "\"\r\n").getBytes());
                        Thread.sleep(latency);
                        logger.debug("Echo:" + readLine());

                        while(!queue.isEmpty()) {
                            errorOccured = true;
                            Thread.sleep(latency);
                            answer = readLine();
                            logger.debug("Answer:" + answer);
                        }
                        Thread.sleep(1000);
                    }while(errorOccured);

                    serial.write((message + (char) 26).getBytes());
                    Thread.sleep(latency);


                    answer = readLine();
                    logger.debug("AnswerA:" + answer);
                    Thread.sleep(latency);
                    answer = readLine();
                    logger.debug("AnswerB:" + answer);
                    Thread.sleep(latency);
                    answer = readLine();
                    logger.debug("AnswerC:" + answer);

                } else {
                    logger.warn("AT+CMGF command answered: " + answer + ". Message sending process aborted.");
                }
            } else {
                logger.error("IconGI505::Modem not ready. CheckPin returned false.");
            }
            serial.close();

        } catch (SerialPortException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }

    private boolean checkPin(SerialPort serial) {
        try {
            serial.write("AT+CPIN?\r\n".getBytes());
            Thread.sleep(latency);
            logger.debug("Echo:" + readLine());

            // SIM PIN required or CPIN READY
            String answer = readLine();
            logger.debug("Answer:" + answer);
            if(answer.contains("+CPIN: SIM PIN")) {

                //SIM PIN followed by a OK.
                answer = readLine();
                logger.debug("Answer:" + answer);

                //Send PIN
                serial.write(("AT+CPIN=\"" + pin + "\"\r\n").getBytes());
                Thread.sleep(latency);
                logger.debug("Echo:" + readLine());

                //CPIN followed by a OK.
                answer = readLine();
                logger.debug("Answer:" + answer);
            } else if (answer.contains("READY")) {

                //CPIN: READY followed by a OK.
                answer = readLine();
                logger.debug("Answer:" + answer);
            } else {
                logger.warn("Unkown answer:" + answer);
                return false;
            }

            //waiting for Network registration
            //checkRegistration(serial);

            return true;


        } catch (SerialPortException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }


    private boolean checkRegistration(SerialPort serial) {
        try {
            serial.write("AT+CGREG?\r\n".getBytes());

            Thread.sleep(latency);
            logger.debug("Echo:" + readLine());

            String answer = readLine();
            logger.debug("Answer:" + answer);
            int comaPosition = answer.indexOf(",");
            String status = answer.substring(comaPosition+1, comaPosition+2);

            //+CGREG followed by a OK.
            answer = readLine();
            logger.debug("Answer:" + answer);

            return (status.equals("1")||status.equals("5"));

        } catch (SerialPortException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }


    private SerialPort initializeSerialConnection() {
        SerialPort serial = null;
        try {
            logger.debug("Getting ComPortIdentifier for " + serialPortName);
            serial = new SerialPort(serialPortName, 19200);
            logger.debug("Got ComPortIdentifier for " + serialPortName);
            serial.addEventListener(this);
            logger.debug("Listener Registered");
            serial.open();
            Thread.sleep(200);
            logger.info("Connection Opened with " + serialPortName);
        }catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return serial;
    }

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

    @Start
    public void start() {
        update();

        logger.info("Starting");
        try {

            final SerialPort serial = initializeSerialConnection();

            //setEcho(serial,false);

            checkPin(serial);

            /*
            StringBuffer answer = new StringBuffer();
            serial.write("AT+CMGF=1\r\n".getBytes());
            answer = new StringBuffer();
            do {
                answer.append(readLine());
                logger.debug(answer.toString());
            } while (!answer.toString().contains("OK"));
            */

            serial.close();
            logger.info("Modem ready");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Stop
    public void stop() {
    }

    public void update() {
        Object o;
        if ((o = getDictionary().get("pincode")) != null) {
            pin = o.toString();
            if (pin.length() != 4) {
                logger.error("Pin code does seem to be correct");
            }
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

    @Override
    public synchronized void incomingDataEvent(SerialPortEvent serialPortEvent) {
        final String received = new String(serialPortEvent.read());
        String[] split = received.split("\r");
        //for(int i = split.length-1; i>-1;i--) {
        //  String messagePart = split[i];
        for(String messagePart : split) {

            if(messagePart != null && messagePart.length()>0 && !messagePart.equals("\r")){
                queue.add(messagePart);
                logger.debug("MessageQueued:" + messagePart);
            } else {
                logger.debug("MessageIgnored:" + Arrays.toString(messagePart.getBytes()));
            }
        }


    }

    @Override
    public void disconnectionEvent(SerialPortDisconnectionEvent serialPortDisconnectionEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void concurrentOpenEvent(SerialConcurrentOpenEvent serialConcurrentOpenEvent) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}
