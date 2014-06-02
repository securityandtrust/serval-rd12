/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz.msg;

import fr.gn.karotz.session.InteractiveAction;
import fr.gn.karotz.utils.DocumentHelper;
import org.kevoree.log.Log;
import org.w3c.dom.Document;

/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 20/05/11
 * Time: 18:06
 */
public class InteractiveModeMessage extends ServerAnswer {

    private InteractiveAction action;

    private InteractiveModeMessage(String id) {
        super(id);
    }


    public InteractiveAction getAction() {
        return action;
    }

    public String getInteractiveId() {
        return interactiveId;
    }

    private String interactiveId;

    public static InteractiveModeMessage parse(Document message) {

        InteractiveModeMessage msg = new InteractiveModeMessage(message.getElementsByTagName("id").item(0).getFirstChild().getNodeValue());

        msg.action = InteractiveAction.valueOf(
                message.getElementsByTagName("action").item(0).getFirstChild().getNodeValue()
        );

        //Get InteractiveId
        if (message.getElementsByTagName("interactiveId").getLength() == 1) {
            msg.interactiveId = message.getElementsByTagName("interactiveId").item(0).getFirstChild().getNodeValue();
        } else {
            Log.error("InteractiveModeMessage:parse -> InteractiveId number of nodes incorrect in " + DocumentHelper.convertToString(message));
        }

        return msg;
    }

    public String toString() {
        return "InteractiveModeMessage\n" + super.toString() + "\nInteractiveId: " + interactiveId + "\nAction" + action.toString();
    }

}
