/*
 * Copyright (c) 2011. Gregory Nain.
 */

package fr.gn.karotz.actions;

import fr.gn.karotz.Kernel;
import fr.gn.karotz.session.PictureReceiver;
import fr.gn.karotz.utils.KarotzCommand;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 22/05/11
 * Time: 14:42
 */
public class WebcamAction implements KarotzCommand {

    private String callBackIp;
    private int callBackPort;
    private PictureReceiver receiver;

    public WebcamAction(int callBackPort) {

        try {
            callBackIp = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        this.callBackPort = callBackPort;
        new PictureReceiver(callBackPort);
    }

    @Override
    public String getCommand() {
        return Kernel.getServerAddress() + "webcam?action=photo&url=http://"+callBackIp+":"+callBackPort+"/"+"&interactiveid=" + Kernel.getInteractiveId();
    }


    public boolean engageReceiver() {
        receiver = new PictureReceiver(callBackPort);
        receiver.start();
        return true;
    }

    public PictureReceiver getReceiver() {
        return receiver;
    }
}
