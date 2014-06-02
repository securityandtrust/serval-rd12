package lu.snt.iot.serval.rn12.modem.utils;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 28/09/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.modem.cmd.*;
import lu.snt.iot.serval.rn12.modem.core.Kernel;
import org.slf4j.LoggerFactory;

public class SmsReader extends Command {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SmsReader.class);

    @Override
    public void execute() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isExpected(String s) {
        logger.debug("isExpected(" + s + ") ["+status.toString()+"]");

        switch(status) {

            case WAITING:{
                if(s.startsWith("+CMTI:")) {
                    int comaPosition = s.indexOf(",");
                    String index = s.substring(comaPosition+1, s.length());
                    CMGR cmgr = new CMGR();
                    cmgr.setIndex(index);
                    Kernel.getCommandManager().process(cmgr);
                    status = Status.TERMINATED;
                    result = Result.OK;
                } else {
                    logger.warn("Unexpected message: " + s);
                    return false;
                }
            }
        }
        return true;
    }
}
