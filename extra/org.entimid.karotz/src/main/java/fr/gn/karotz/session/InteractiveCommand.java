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
    private Kernel kernel;

    public InteractiveCommand(Kernel k, InteractiveAction action) {
        this.action = action;
        this.kernel = k;
    }


    public String getCommand() {
        switch (action) {
            case START: {
                String parameterChain = "";

                parameterChain += "apikey=" + kernel.getApiKey();
                parameterChain += "&installid=" + kernel.getInstallId();
                parameterChain += "&once=" + (long) (Math.random() * 1000000000);
                parameterChain += "&timestamp=" + (System.currentTimeMillis() / 1000);

                String signedRequest = kernel.getSigner().sign(parameterChain);

                return kernel.getServerAddress() + "start?" + signedRequest;
            }

            case STOP: {
                return kernel.getServerAddress() + "interactivemode?action=stop&interactiveid=" + kernel.getInteractiveId();
            }

            default: return null;
        }
    }
}
