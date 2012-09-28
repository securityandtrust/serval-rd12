package lu.snt.iot.serval.rn12.modem.cmd;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 28/09/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.modem.core.Kernel;
import org.slf4j.LoggerFactory;

public class CMGR extends Command {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CMGR.class);
    private String datetime = "";
    private String phoneNumber = "";
    private String content = "";
    private String index = "";

    public CMGR() {

    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getDatetime() {
        return datetime;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void execute() {
        Kernel.getSerialConnectionManager().write("AT+CMGR=" + index + "\r\n");
    }

    public boolean isExpected(String s) {
        logger.debug("isExpected(" + s + ") ["+status.toString()+"]");
        switch(status) {
            case WAITING: {
                if(s.startsWith("AT+CMGR")) { //ECHO Pin check
                    status = Status.INITIALIZED;
                    logger.debug("Echo:" + s);
                } else {
                    logger.warn("[WAITING] Unexpected answer:" + s);
                    return false;
                }
                return true;
            }
            case INITIALIZED:{
                logger.debug("Answer:" + s);
                if(s.startsWith("+CMGR")) {
                    status = Status.ANSWERED;
                    String[] messageParts = s.split(",");
                    for(String mp : messageParts) {
                        logger.debug("MessagePart:" + mp);
                    }
                    phoneNumber = messageParts[1];
                    datetime = messageParts[3];
                } else {
                    logger.warn("[Answered] Unexpected answer:" + s);
                    return false;
                }
                return true;
            }
            case ANSWERED: {
                if(s.equals("OK")) {
                    status = Status.TERMINATED;
                    result = Result.OK;
                    Kernel.getComponent().messageReceived(this);
                } else if(s.contains("ERROR")) {
                    status = Status.TERMINATED;
                    result = Result.ERROR;
                } else {
                    content += s; //filling content.
                }
                return true;
            }
        }
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}


/*
*
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
                        logger.debug("Message Content:" + answer);
                        Thread.sleep(latency);
                        answer = readLine();
                        logger.debug("CMSG Answer:" + answer);
                        Thread.sleep(latency);

                        //Ack
                        answer = readLine();
                        logger.debug("Answer:" + answer);

                    } else {
                        logger.warn("AT+CMGF command answered: " + answer + ". Message sending process aborted.");
                    }
                } else {
                    logger.error("IconGI505::Modem not ready. CheckPin returned false.");
                }
                serial.close();*/