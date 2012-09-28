package lu.snt.iot.serval.rn12.modem.cmd;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 28/09/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.modem.core.Kernel;
import org.slf4j.LoggerFactory;

public class CMSG extends Command {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CMSG.class);
    private String phoneNumber;
    private String content;

    public CMSG() {

    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void execute() {
        logger.debug("Starting sending \"" + content + "\" to " + phoneNumber);
        Kernel.getSerialConnectionManager().write("AT+CMGS=\"" + phoneNumber + "\"\r\n" + content + (char) 26);
    }

    public boolean isExpected(String s) {
        logger.debug("CMGS is asked if (" + s + ") is expected in state ["+status.toString()+"]");
        switch(status) {
            case WAITING: {
                if(s.startsWith("AT+CMGS")) { //ECHO Pin check
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
                if(s.contains(content)) {
                    //ignore
                    logger.debug("Got content while initialized. Ignored.");
                } else if(s.startsWith(">")) {
                    //ignore - Prompt
                    logger.debug("Got a prompt while initialized. Ignored.");
                } else if(s.startsWith("+CMS ERROR")) {
                    logger.debug("Getting an error. Renew the command.");
                    status = Status.TERMINATED;
                    result = Result.ERROR;
                    CMSG messageCommand = new CMSG();
                    messageCommand.setContent(content);
                    messageCommand.setPhoneNumber(phoneNumber);
                    Kernel.getCommandManager().renewCommand(messageCommand);
                } else if(s.startsWith("+CMGS:")) {
                    logger.debug("Got acknowledgement: " + s);
                    status = Status.ACKNOWLEDGED;
                } else {
                    logger.warn("[Answered] Unexpected answer:" + s);
                    return false;
                }
                return true;
            }
            case ACKNOWLEDGED: {
                if(s.contains("OK")) {
                    status = Status.TERMINATED;
                    result = Result.OK;
                } else if(s.contains("ERROR")) {
                    status = Status.TERMINATED;
                    result = Result.ERROR;
                } else {
                    logger.warn("[Answered] Unexpected answer:" + s);
                    return false;
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