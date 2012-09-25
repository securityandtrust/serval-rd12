/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz.msg;

import fr.gn.karotz.session.InteractiveAction;
import fr.gn.karotz.utils.DocumentHelper;
import org.w3c.dom.Document;

/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 20/05/11
 * Time: 18:06
 */
public class ConfigurationMessage extends ServerAnswer {

    private boolean interruptible = false;
    private boolean awake = false;
    private String name = "";

    //TODO: collect "permanentTriggerActivator", "scheduledDateTriggerActivator", "scheduledTriggerActivator" in parameters.

    private ConfigurationMessage(String id) {
        super(id);
    }

    public static ConfigurationMessage parse(Document message) {

        ConfigurationMessage msg = new ConfigurationMessage(message.getElementsByTagName("uuid").item(0).getFirstChild().getNodeValue());

        msg.interruptible = Boolean.valueOf(
                message.getElementsByTagName("interruptible").item(0).getFirstChild().getNodeValue()
        );

        msg.awake = Boolean.valueOf(
                message.getElementsByTagName("awake").item(0).getFirstChild().getNodeValue()
        );

        msg.name = message.getElementsByTagName("name").item(0).getFirstChild().getNodeValue();

        return msg;
    }

    public String toString() {
        return "ConfigurationMessage\n" + super.toString() + "\nName: " + name + "\nAwake:" + awake + "\nInterruptible:" + interruptible;
    }

}
