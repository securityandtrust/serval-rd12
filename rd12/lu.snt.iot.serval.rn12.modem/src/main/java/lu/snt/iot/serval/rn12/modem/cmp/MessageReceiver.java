package lu.snt.iot.serval.rn12.modem.cmp;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 28/09/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.modem.core.Kernel;
import lu.snt.iot.serval.rn12.modem.utils.SmsReader;
import org.kevoree.extra.kserial.SerialPort.SerialConcurrentOpenEvent;
import org.kevoree.extra.kserial.SerialPort.SerialPortDisconnectionEvent;
import org.kevoree.extra.kserial.SerialPort.SerialPortEvent;
import org.kevoree.extra.kserial.SerialPort.SerialPortEventListener;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class MessageReceiver implements SerialPortEventListener {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MessageReceiver.class);
    private LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
    private Thread dispatcher = null;

    public void dispatch() {
        logger.debug("dispatch Start: queue:" + queue.size() + "");

        try {

            while(!queue.isEmpty()) {
                String taken = queue.take();
                if(!Kernel.getCommandManager().isExepected(taken)) {
                    SmsReader reader = new SmsReader();
                    if(!reader.isExpected(taken)) {
                        logger.warn("Unexpected token received: " + taken);
                    }
                }
            }
            logger.debug("Dispatcher stop");
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    @Override
    public synchronized void incomingDataEvent(SerialPortEvent serialPortEvent) {
        final String received = new String(serialPortEvent.read());
        String[] split = received.split("\r");
        for(String messagePart : split) {

            if(messagePart != null && messagePart.length()>0 && !messagePart.equals("\r")){
                queue.add(messagePart);
                logger.debug("MessageQueued:" + messagePart);

                logger.debug("Incomming Message: Dispatcher state:" + dispatcher);
                if(dispatcher == null || dispatcher.getState()== Thread.State.TERMINATED) {
                    dispatcher = new Thread(new Runnable(){
                        public void run() {dispatch();}
                    });
                    dispatcher.start();
                }

            } else {
                logger.debug("MessageIgnored:" + Arrays.toString(messagePart.getBytes()));
            }
        }
    }

    @Override
    public void disconnectionEvent(SerialPortDisconnectionEvent serialPortDisconnectionEvent) {
        logger.warn("Serial port disconnected !");
        Kernel.getSerialConnectionManager().setDiconnected();
        Kernel.getSerialConnectionManager().initializeSerialConnection();
        Kernel.getComponent().pinCheck();
        Kernel.getCommandManager().reinitialize();
    }

    @Override
    public void concurrentOpenEvent(SerialConcurrentOpenEvent serialConcurrentOpenEvent) {
        logger.warn("Concurrent open event detected!" + serialConcurrentOpenEvent.toString());
    }
}
