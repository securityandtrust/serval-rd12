package lu.snt.iot.serval.rn12.modem.cmd;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 28/09/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.modem.core.Kernel;
import org.slf4j.LoggerFactory;

public class CMGD extends Command {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CMGD.class);

    private String index = "";

    public CMGD() {
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void execute() {
        logger.debug("Exec");
        Kernel.getSerialConnectionManager().write("AT+CMGD="+index+",2\r\n");
    }

    public boolean isExpected(String s) {
        logger.debug("isExpected(" + s + ") ["+status.toString()+"]");
        switch(status) {
            case WAITING: {
                if(s.startsWith("AT+CMGD")) { //ECHO Pin check
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
