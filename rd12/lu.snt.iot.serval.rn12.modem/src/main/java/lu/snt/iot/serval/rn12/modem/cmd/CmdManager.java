package lu.snt.iot.serval.rn12.modem.cmd;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 28/09/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.modem.core.Kernel;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class CmdManager implements Runnable {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(CmdManager.class);

    private ArrayList<Command> outgoingPending = new ArrayList<Command>();
    private ArrayList<Command> commandHistory = new ArrayList<Command>();
    private Command commandProcessed;
    private Thread starter;

    public void renewCommand(Command cmd) {
        logger.debug("renewCommand:"+cmd.getClass().getSimpleName());
        outgoingPending.add(0, cmd);
        if(starter== null || starter.getState()== Thread.State.TERMINATED) {
            starter = new Thread(this);
            starter.start();
        }
    }

    public void process(Command cmd) {
        logger.debug("process:"+cmd.getClass().getSimpleName());
        outgoingPending.add(cmd);
        if(starter== null || starter.getState() == Thread.State.TERMINATED) {
            starter = new Thread(this);
            starter.start();
        }
    }

    public void proceed() {
        logger.debug("proceed: " + (commandProcessed==null?"NONE":commandProcessed.getClass().getSimpleName()) + " -> " + (outgoingPending.isEmpty()?"NONE":outgoingPending.get(0).getClass().getSimpleName()));
        if(commandProcessed != null) {
            commandHistory.add(commandProcessed);
            commandProcessed = null;
        }
        if(!outgoingPending.isEmpty()) {
            commandProcessed = outgoingPending.remove(0);
            commandProcessed.execute();
        }
    }

    public void run() {
        logger.debug("Starter Launched. Pending commands:" + outgoingPending.size() + (commandProcessed!= null?" processed: " + commandProcessed.getClass().getSimpleName() + "["+commandProcessed.getStatus()+"]":" processed:NONE"));
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if(!outgoingPending.isEmpty()) {
            while(!Kernel.getSerialConnectionManager().isReady()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
            if(commandProcessed == null || commandProcessed.getStatus()==Status.TERMINATED) {
                proceed();
            } else {
                if(commandProcessed.getStatus() == Status.WAITING) {
                    commandProcessed.execute();
                }
            }
        } else {

            if(commandProcessed != null && commandProcessed.getStatus()==Status.TERMINATED) {
                commandHistory.add(commandProcessed);
                commandProcessed = null;
            }
        }
        logger.debug("Starter stop.");
    }

    public boolean isExepected(String s) {
        logger.debug("CmdManager::isExpected(" + s + ") ["+commandProcessed+(commandProcessed!=null?"," + commandProcessed.getStatus():"") +"]");
        if(commandProcessed != null && commandProcessed.isExpected(s)) {
            logger.debug("Token was expected by: " + commandProcessed.getClass().getSimpleName() + " now in state ["+commandProcessed.getStatus()+"]");
            if(starter == null || starter.getState()== Thread.State.TERMINATED) {
                starter = new Thread(this);
                starter.start();
            }
            return true;
        } else {
            logger.debug("Not expected by the current command:" + s);
            return false;
        }

    }

    public void reinitialize() {
        if(commandProcessed != null) {
            commandProcessed.resetCommand();
            if(starter == null) {
                starter = new Thread(this);
                starter.start();
            }
        }
    }

}



