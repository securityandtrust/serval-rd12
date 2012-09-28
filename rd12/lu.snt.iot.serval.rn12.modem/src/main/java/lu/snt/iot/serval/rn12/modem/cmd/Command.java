package lu.snt.iot.serval.rn12.modem.cmd;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 28/09/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

public abstract class Command {

    protected Result result = Result.NONE;
    protected Status status = Status.WAITING;


    public abstract void execute();

    public abstract boolean isExpected(String s);

    public Result getResult() {
        return result;
    }

    public Status getStatus() {
        return status;
    }

    public void resetCommand() {
        status = Status.WAITING;
    }

}
