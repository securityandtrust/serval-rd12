/**
 * Project: EnTiMid
 * Copyright: INRIA/IRISA 2011
 */
package lu.snt.iot.serval.rn12.modem.cmp;

import org.kevoree.annotation.*;
import org.kevoree.extra.kserial.SerialPort.*;
import org.kevoree.framework.AbstractComponentType;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    static final Logger logger = Logger.getLogger(IconGI505M3.class.getName());
    private String pin = "0000";
    //private String phoneNum;
    private String serialPortName = "/dev/ttyHS2";


    private LinkedBlockingQueue<Byte> queue = new LinkedBlockingQueue<Byte>();

    public IconGI505M3() {
    }

    private String readLine() {

        StringBuffer buf = new StringBuffer("");

        try {
            do {
                int lu = queue.take().intValue();
                buf.append((char) lu);
            } while (!queue.isEmpty());

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return buf.toString();
    }



    @Ports({@Port(name="sendWithAck"),@Port(name="send")})
    public boolean sendSMS(Object msg) {

        SerialPort serial;

        final Hashtable<String,Object> p = (Hashtable<String,Object>)msg;
        final int textId = (Integer)p.get("text.id");
        String message = (String)p.get("text."+textId+".content");
        String phoneNum = (String)p.get("ecl."+textId+".number");

        try {

            System.out.println("IconGI505::Getting ComPortIdentifier for " + serialPortName );
            serial = new SerialPort("/dev/wmodem0", 19200);
            System.out.println("IconGI505::Got ComPortIdentifier for " + serialPortName );
            serial.addEventListener(this);
            System.out.println("IconGI505::Listener Registered");
            serial.open();
            System.out.println("IconGI505::Connection Opened");

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

            serial.write("ATE0\r\n".getBytes());
            logger.log(Level.INFO, "Sent:" + "ATE0");

            Thread.sleep(50);
            StringBuffer answer = new StringBuffer();
            answer.append(readLine());
            if(!answer.toString().contains("OK")){
                answer.append(readLine());
            }
            logger.log(Level.INFO, "Answered:" + answer.toString());

            serial.write("AT+CPIN?\r\n".getBytes());
            logger.log(Level.INFO, "Sent:" + "AT+CPIN?\r\n");

            answer.append(readLine());
            logger.log(Level.INFO, "Answered:" + answer.toString());

            if (answer.toString().contains("+CPIN: SIM PIN")) {
                serial.write(("AT+CPIN=\"" + pin + "\"\r\n").getBytes());
                Thread.sleep(50);
                answer = new StringBuffer();
                do {
                    answer.append(readLine());
                } while (!answer.toString().contains("OK"));
                logger.log(Level.INFO, "Answered:" + answer.toString());
            }

            serial.write("AT+CMGF=1\r\n".getBytes());
            Thread.sleep(50);
            answer = new StringBuffer();
            do {
                answer.append(readLine());
            } while (!answer.toString().contains("OK"));
            logger.log(Level.INFO, "Answered:" + answer.toString());

            serial.write(("AT+CMGS=\"" + phoneNum + "\"" + (char) 13).getBytes());
            Thread.sleep(50);
            answer = new StringBuffer();
            do {
                answer.append(readLine());
            } while (!answer.toString().contains(">") && !answer.toString().contains("+CMS ERROR"));
            logger.log(Level.INFO, "Answered:" + answer.toString());

            if (!answer.toString().contains("+CMS ERROR")) {

                serial.write((message + (char) 26).getBytes());
                answer = new StringBuffer();
                do {
                    answer.append(readLine());
                } while (!answer.toString().contains("+CMGS:"));
                logger.log(Level.INFO, "Answered:" + answer.toString());

                if (!answer.toString().substring(answer.toString().indexOf("+CMGS:")).contains("OK")) {
                    answer = new StringBuffer();
                    do {
                        answer.append(readLine());
                    } while (!answer.toString().contains("OK"));
                    logger.log(Level.INFO, "Answered:" + answer.toString());
                }
                logger.info("Message sent.");
            } else {
                logger.log(Level.SEVERE, "SMS:: Error while sending.");
            }

            serial.close();

        } catch (SerialPortException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }

    @Start
    public void start() {
        update();

        System.out.println("IconGI505::Starting");

        final SerialPort serial;
        try {

            System.out.println("IconGI505::Getting ComPortIdentifier for " + serialPortName);
            serial = new SerialPort("/dev/wmodem0", 19200);
            System.out.println("IconGI505::Got ComPortIdentifier for " + serialPortName );
            serial.addEventListener(this);
            System.out.println("IconGI505::Listener Registered");
            serial.open();
            System.out.println("IconGI505::Connection Opened");

            serial.write("ATE0\r\n".getBytes());
            logger.log(Level.INFO, "Sent:" + "ATE0");

            StringBuffer answer = new StringBuffer();
            answer.append(readLine());
            if(!answer.toString().contains("OK")){
                answer.append(readLine());
            }
            logger.log(Level.INFO, "Answered:" + answer.toString());

            serial.write("AT+CPIN?\r\n".getBytes());
            logger.log(Level.INFO, "Sent:" + "AT+CPIN?\r\n");



            answer.append(readLine());
            logger.log(Level.INFO, "Answered:" + answer.toString());

            if (answer.toString().contains("+CPIN: SIM PIN")) {
                serial.write(("AT+CPIN=\"" + pin + "\"\r\n").getBytes());
                Thread.sleep(50);
                answer = new StringBuffer();
                do {
                    answer.append(readLine());
                } while (!answer.toString().contains("OK"));
                logger.log(Level.INFO, "Answered:" + answer.toString());
            }

            serial.write("AT+CMGF=1\r\n".getBytes());
            Thread.sleep(50);
            answer = new StringBuffer();
            do {
                answer.append(readLine());
            } while (!answer.toString().contains("OK"));
            logger.log(Level.INFO, "Answered:" + answer.toString());

            serial.close();

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
                logger.log(Level.SEVERE, "Pin code does seem to be correct");
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

    public void processSend(Object input) {
        sendSMS(input.toString());
    }

    @Override
    public void incomingDataEvent(SerialPortEvent serialPortEvent) {
        byte[] buffer = serialPortEvent.read();
        for(byte b : buffer) {
            queue.add(b);
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
