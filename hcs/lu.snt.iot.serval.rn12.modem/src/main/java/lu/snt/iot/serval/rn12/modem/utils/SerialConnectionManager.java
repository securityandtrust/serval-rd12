package lu.snt.iot.serval.rn12.modem.utils;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 28/09/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.modem.cmd.CPIN;
import lu.snt.iot.serval.rn12.modem.core.Kernel;
import org.kevoree.extra.kserial.SerialPort.SerialPort;
import org.kevoree.extra.kserial.SerialPort.SerialPortException;
import org.slf4j.LoggerFactory;

import java.util.Hashtable;
import java.util.concurrent.LinkedBlockingQueue;

public class SerialConnectionManager {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SerialConnectionManager.class);

    private SerialPort serial;
    private String serialPortName;
    private String state = "init";



    public void setSerialPortName(String serialPortName) {
        this.serialPortName = serialPortName;
    }


    public void write(String s) {
        if(!s.endsWith("\r\n")) {
            s+="\r\n";
        }
        try {
            serial.write(s.getBytes());
        } catch (SerialPortException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public void initializeSerialConnection() {

        if(serialPortName==null || serialPortName.equals("")) {
            throw new UnsupportedOperationException("Serial port name not set.");
        }

        new Thread(new Runnable() {

            public void run() {
                logger.info("Starting serial connection");
                while(state.equals("init")) {
                    try {

                        logger.debug("Getting ComPortIdentifier for " + serialPortName);
                        serial = new SerialPort(serialPortName, 19200);
                        logger.debug("Got ComPortIdentifier for " + serialPortName);
                        serial.addEventListener(Kernel.getMessageReceiver());
                        logger.debug("Listener Registered");
                        serial.open();
                        Thread.sleep(200);
                        logger.info("Connection Opened with " + serialPortName + ". Modem Ready.");
                        state = "ready";
                    } catch (Exception e) {
                        if(e instanceof SerialPortException) {
                            logger.warn("Serial port " + serialPortName + " not available.");
                        }else {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                    if(state.equals("init")) {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                }
            }
        }).start();
    }

    public boolean isReady() {
        return state.equals("ready");
    }

    public void setDiconnected() {
        state = "init";
    }

    public boolean closeConnection() {
        if(serial == null) {
            return true;
        }

        serial.removeEventListener(Kernel.getMessageReceiver());
        try {
            serial.close();
            return true;
        } catch (SerialPortException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return false;
    }

}
