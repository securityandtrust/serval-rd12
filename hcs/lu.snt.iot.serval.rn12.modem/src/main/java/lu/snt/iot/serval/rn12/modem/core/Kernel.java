package lu.snt.iot.serval.rn12.modem.core;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 28/09/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.modem.cmd.CmdManager;
import lu.snt.iot.serval.rn12.modem.cmp.IconGI505M3;
import lu.snt.iot.serval.rn12.modem.cmp.MessageReceiver;
import lu.snt.iot.serval.rn12.modem.utils.SerialConnectionManager;

public abstract class Kernel {

    private static IconGI505M3 component;

    public static IconGI505M3 getComponent() {
        return component;
    }

    public static void setComponent(IconGI505M3 component) {
        Kernel.component = component;
    }


    private static CmdManager commandManager;

    public static CmdManager getCommandManager() {
        return commandManager;
    }

    public static void setCommandManager(CmdManager commandManager) {
        Kernel.commandManager = commandManager;
    }


    private static SerialConnectionManager serialConnectionManager;

    public static SerialConnectionManager getSerialConnectionManager() {
        return serialConnectionManager;
    }

    public static void setSerialConnectionManager(SerialConnectionManager serialConnectionManager) {
        Kernel.serialConnectionManager = serialConnectionManager;
    }



    private static MessageReceiver messageReceiver;

    public static MessageReceiver getMessageReceiver() {
        return messageReceiver;
    }

    public static void setMessageReceiver(MessageReceiver messageReceiver) {
        Kernel.messageReceiver = messageReceiver;
    }
}
