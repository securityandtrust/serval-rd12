/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz.session;

import fr.gn.karotz.Kernel;
import fr.gn.karotz.utils.KarotzCommand;

/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 20/05/11
 * Time: 17:47
 */
public class InteractiveCommand implements KarotzCommand {

    private InteractiveAction action;

    public InteractiveCommand(InteractiveAction action) {
        this.action = action;
    }


    public String getCommand() {
        switch (action) {
            case START: {
                String parameterChain = "";

                parameterChain += "apikey=" + Kernel.getApiKey();
                parameterChain += "&installid=" + Kernel.getInstallId();
                parameterChain += "&once=" + (long) (Math.random() * 1000000000);
                parameterChain += "&timestamp=" + (System.currentTimeMillis() / 1000);

                String signedRequest = Kernel.getSigner().sign(parameterChain);

                return Kernel.getServerAddress() + "start?" + signedRequest;
            }

            case STOP: {
                return Kernel.getServerAddress() + "interactivemode?action=stop&interactiveid=" + Kernel.getInteractiveId();
            }

            default: return null;
        }
    }
}
