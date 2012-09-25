/**
 * Project: EnTiMid
 * Copyright: INRIA/IRISA 2011
 *//*

package lu.snt.iot.serval.rn12.modem.cmp;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Hashtable;
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
public class IconGI505M extends AbstractComponentType {

    static final Logger logger = Logger.getLogger(IconGI505M.class.getName());
    private String pin = "0000";
    //private String phoneNum;
    private String serialPortName = "/dev/ttyHS2";

    public IconGI505M() {
    }

    private String readLine(InputStreamReader isr) {

        StringBuffer buf = new StringBuffer("");

        try {
            do {
                int lu = isr.read();
                buf.append((char) lu);
            } while (isr.ready());

        } catch (IOException exp) {
            logger.log(Level.SEVERE, "IOException", exp);
        }
        return buf.toString();
    }
    @Ports({@Port(name="sendWithAck"),@Port(name="send")})
    public boolean sendSMS(Object msg) {

        SerialPort connection;
        InputStreamReader isr;
        OutputStreamWriter osw;
        CommPortIdentifier id;

        final Hashtable<String,Object> p = (Hashtable<String,Object>)msg;
        final int textId = (Integer)p.get("text.id");
        String message = (String)p.get("text."+textId+".content");
        String phoneNum = (String)p.get("ecl."+textId+".number");

        try {
            id = CommPortIdentifier.getPortIdentifier(serialPortName);
            connection = (SerialPort) id.open("IconGI505M", 2000);
            isr = new InputStreamReader(connection.getInputStream());
            osw = new OutputStreamWriter(connection.getOutputStream());

            */
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
             *//*


            osw.write("ATE0\r\n");
            osw.flush();
            logger.log(Level.INFO, "Sent:" + "ATE0");

            Thread.sleep(50);
            StringBuffer answer = new StringBuffer();
            answer.append(readLine(isr));
            if(!answer.toString().contains("OK")){
                answer.append(readLine(isr));
            }
            logger.log(Level.INFO, "Answered:" + answer.toString());

            osw.write("AT+CPIN?\r\n");
            osw.flush();
            logger.log(Level.INFO, "Sent:" + "AT+CPIN?\r\n");



            answer.append(readLine(isr));
            logger.log(Level.INFO, "Answered:" + answer.toString());

            if (answer.toString().contains("+CPIN: SIM PIN")) {
                osw.write("AT+CPIN=\"" + pin + "\"\r\n");
                osw.flush();
                Thread.sleep(50);
                answer = new StringBuffer();
                do {
                    answer.append(readLine(isr));
                } while (!answer.toString().contains("OK"));
                logger.log(Level.INFO, "Answered:" + answer.toString());
            }

            osw.write("AT+CMGF=1\r\n");
            osw.flush();
            Thread.sleep(50);
            answer = new StringBuffer();
            do {
                answer.append(readLine(isr));
            } while (!answer.toString().contains("OK"));
            logger.log(Level.INFO, "Answered:" + answer.toString());

            osw.write("AT+CMGS=\"" + phoneNum + "\"" + (char) 13);
            osw.flush();
            Thread.sleep(50);
            answer = new StringBuffer();
            do {
                answer.append(readLine(isr));
            } while (!answer.toString().contains(">") && !answer.toString().contains("+CMS ERROR"));
            logger.log(Level.INFO, "Answered:" + answer.toString());

            if (!answer.toString().contains("+CMS ERROR")) {

                osw.write(message + (char) 26);
                osw.flush();
                answer = new StringBuffer();
                do {
                    answer.append(readLine(isr));
                } while (!answer.toString().contains("+CMGS:"));
                logger.log(Level.INFO, "Answered:" + answer.toString());

                if (!answer.toString().substring(answer.toString().indexOf("+CMGS:")).contains("OK")) {
                    answer = new StringBuffer();
                    do {
                        answer.append(readLine(isr));
                    } while (!answer.toString().contains("OK"));
                    logger.log(Level.INFO, "Answered:" + answer.toString());
                }
                logger.info("Message sent.");
            } else {
                logger.log(Level.SEVERE, "SMS:: Error while sending.");
            }

            isr.close();
            osw.close();
            connection.close();

        } catch (IOException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        } catch (PortInUseException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        } catch (NoSuchPortException ex) {
            logger.log(Level.SEVERE, null, ex);
            return false;
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "InterruptedException", e);
            return false;
        }
        return true;
    }

    @Start
    public void start() {
        update();

        System.out.println("IconGI505::Starting");

        SerialPort connection;
        InputStreamReader isr;
        OutputStreamWriter osw;
        CommPortIdentifier id;


        try {

            System.out.println("IconGI505::Getting ComPortIdentifier for " + serialPortName );

            id = CommPortIdentifier.getPortIdentifier(serialPortName);

            System.out.println("IconGI505::Got ComPortIdentifier for " + serialPortName );

            connection = (SerialPort) id.open("IconGI505M", 2000);
            System.out.println("IconGI505::Openning connection");
            isr = new InputStreamReader(connection.getInputStream());
            osw = new OutputStreamWriter(connection.getOutputStream());
            System.out.println("IconGI505::Streams opened");
            System.out.println("Got Serial Connection on " + serialPortName);

            osw.write("ATE0\r\n");
            osw.flush();
            logger.log(Level.INFO, "Sent:" + "ATE0");

            Thread.sleep(50);
            StringBuffer answer = new StringBuffer();
            answer.append(readLine(isr));
            if(!answer.toString().contains("OK")){
                answer.append(readLine(isr));
            }
            logger.log(Level.INFO, "Answered:" + answer.toString());

            osw.write("AT+CPIN?\r\n");
            osw.flush();
            logger.log(Level.INFO, "Sent:" + "AT+CPIN?\r\n");



            answer.append(readLine(isr));
            logger.log(Level.INFO, "Answered:" + answer.toString());

            if (answer.toString().contains("+CPIN: SIM PIN")) {
                osw.write("AT+CPIN=\"" + pin + "\"\r\n");
                osw.flush();
                Thread.sleep(50);
                answer = new StringBuffer();
                do {
                    answer.append(readLine(isr));
                } while (!answer.toString().contains("OK"));
                logger.log(Level.INFO, "Answered:" + answer.toString());
            }

            osw.write("AT+CMGF=1\r\n");
            osw.flush();
            Thread.sleep(50);
            answer = new StringBuffer();
            do {
                answer.append(readLine(isr));
            } while (!answer.toString().contains("OK"));
            logger.log(Level.INFO, "Answered:" + answer.toString());


            isr.close();
            osw.close();
            connection.close();
        } catch (NoSuchPortException e) {
            System.out.println("IconGI505::Port not found: " + serialPortName);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (PortInUseException e) {
            System.out.println("IconGI505::Port in use: " + serialPortName);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // just for test - Create a stub for that
        */
/*
        new Thread() {

            public void run() {
                try {
                    Thread.sleep(4000);

                    sendSMS("Test OK if you can read it !");

                    Thread.sleep(20000);
                    sendSMS("Et même un 2ème SMS");

                    Thread.sleep(20000);
                    sendSMS("Yet another one");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Icon225.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }.start();
         * 
         *//*

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
        */
/*
        if ((o = getDictionary().get("phonenumber")) != null) {
            phoneNum = o.toString();
        }
        *//*

        if ((o = getDictionary().get("serialPort")) != null) {
            serialPortName = o.toString();
        }
    }

    public void processSend(Object input) {
        sendSMS(input.toString());
    }
}
*/
