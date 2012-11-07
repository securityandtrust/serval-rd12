/**
 * Project: EnTiMid
 * Copyright: INRIA/IRISA 2011
 */
package lu.snt.iot.serval.rn12.modem.cmp;


import org.kevoree.extra.kserial.SerialPort.*;
import org.kevoree.extra.kserial.Utils.KHelpers;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class IconGI505M {

    public  String pin = "1828";
    public  String serialPortName = "/dev/ttyHS3";

    public IconGI505M() {

        /*
        String system = System.getProperty("os.name");
        if (system.equals("Linux")) {
            System.setProperty("gnu.io.rxtx.SerialPorts",
                    "/dev/wmodem0:/dev/ttyACM0:/dev/ttyHS0:/dev/ttyHS1:/dev/ttyHS2:/dev/ttyHS3:" + serialPortName);
        } else {
            System.err.println("IconGI505::RXTX:: Unknown system: " + system);
        }


            System.out.println("IconGI505::RXTX loading in IconGI505");
        try {
            RXTXLoader.load();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("IconGI505::RXTX loaded in IconGI505");
*/

    }

    private String readLine(InputStreamReader isr) {

        StringBuffer buf = new StringBuffer("");

        try {
            do {
                int lu = isr.read();
                buf.append((char) lu);
            } while (isr.ready());

        } catch (IOException exp) {
           // logger.log(Level.SEVERE, "IOException", exp);
        }
        return buf.toString();
    }

/*
    public void start() {

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
            System.out.println("Sent:" + "ATE0");

            Thread.sleep(50);
            StringBuffer answer = new StringBuffer();
            answer.append(readLine(isr));
            if(!answer.toString().contains("OK")){
                answer.append(readLine(isr));
            }
            System.out.println("Answered:" + answer.toString());

            osw.write("AT+CPIN?\r\n");
            osw.flush();
            System.out.println("Sent:" + "AT+CPIN?\r\n");



            answer.append(readLine(isr));
            System.out.println("Answered:" + answer.toString());

            if (answer.toString().contains("+CPIN: SIM PIN")) {
                osw.write("AT+CPIN=\"" + pin + "\"\r\n");
                osw.flush();
                Thread.sleep(50);
                answer = new StringBuffer();
                do {
                    answer.append(readLine(isr));
                } while (!answer.toString().contains("OK"));
                System.out.println("Answered:" + answer.toString());
            }

            osw.write("AT+CMGF=1\r\n");
            osw.flush();
            Thread.sleep(50);
            answer = new StringBuffer();
            do {
                answer.append(readLine(isr));
            } while (!answer.toString().contains("OK"));
            System.out.println("Answered:" + answer.toString());


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
     }*/


    public static void main(String[] args) {
        //IconGI505M modem = new IconGI505M();
        //modem.start();

        System.out.println(KHelpers.getPortIdentifiers());
        final SerialPort serial;
        try {
            serial = new SerialPort("/dev/wmodem0", 19200);
            serial.addEventListener(new SerialPortEventListener()
            {
                public void incomingDataEvent(SerialPortEvent evt)
                {
                    System.out.println("event=" + evt.getSize() + "/" + new String(evt.read()));
                }

                public void disconnectionEvent(SerialPortDisconnectionEvent evt)
                {
                    System.out.println("device " + serial.getPort_name() + " is not connected anymore ");
                    try {
                        serial.autoReconnect(20, this);
                    } catch (SerialPortException e) {
                    }
                }

                @Override
                public void concurrentOpenEvent(SerialConcurrentOpenEvent evt) {

                }
            });

            System.out.println("Opening port /dev/ttyHS3");
            serial.open();
            System.out.println("Opened");
            Thread.sleep(50);
            serial.write("ATE0\r\n".getBytes());
            System.out.println("Wrote: ATE0");
            Thread.sleep(100);
            serial.close();


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
