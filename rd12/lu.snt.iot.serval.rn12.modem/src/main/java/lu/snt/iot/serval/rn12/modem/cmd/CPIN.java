package lu.snt.iot.serval.rn12.modem.cmd;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 28/09/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.modem.core.Kernel;
import org.slf4j.LoggerFactory;

public class CPIN extends Command {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CPIN.class);
    private String pinCode;
    private String commandInternalStatus = "check";

    public CPIN() {

    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public void execute() {
        logger.debug("Exec");
        if(commandInternalStatus.equals("check")) {
            Kernel.getSerialConnectionManager().write("AT+CPIN?\r\n");
        } else {
            Kernel.getSerialConnectionManager().write("AT+CPIN=\"" + pinCode + "\"\r\n");
        }
    }

    public boolean isExpected(String s) {
        logger.debug("isExpected(" + s + ") ["+status.toString()+"]");
        switch(status) {
            case WAITING: {
                if(s.startsWith("AT+CPIN?")) { //ECHO Pin check
                    status = Status.INITIALIZED;
                    logger.debug("Echo:" + s);
                } else if(s.startsWith("AT+CPIN=")) { //ECHO Set Pin
                    status = Status.ANSWERED;
                    logger.debug("Echo:" + s);
                } else {
                    logger.warn("[WAITING] Unexpected answer:" + s);
                    return false;
                }
                return true;
            }
            case INITIALIZED:{
                logger.debug("Answer:" + s);
                if(s.contains("+CPIN: SIM PIN")) { //Modem waiting for a PIN
                    status = Status.ANSWERED;
                    CPIN cmd2 = new CPIN();
                    cmd2.setPinCode(pinCode);
                    cmd2.commandInternalStatus = "set";
                    Kernel.getCommandManager().renewCommand(cmd2);
                    logger.debug("Command renewed.");
                } else if (s.contains("READY")) {
                    status = Status.ANSWERED;
                } else {
                    logger.warn("[INITIALIZED] Unexpected answer:" + s);
                    return false;
                }
                return true;
            }
            case ANSWERED: {
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
