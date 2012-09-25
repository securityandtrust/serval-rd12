/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz.actions;

import fr.gn.karotz.Kernel;
import fr.gn.karotz.utils.KarotzCommand;

import java.awt.*;


/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 21/05/11
 * Time: 10:44
 */
public class LedAction implements KarotzCommand {

    public static final String PULSE = "pulse";
    public static final String FADE = "fade";
    public static final String LIGHT = "light";

    private String action;
    private Color color;
    private long duration;
    private long frequency;

    private LedAction() {
    }

    public static LedAction createPulseCommand(Color c, long frequency, long duration) {
        LedAction command = new LedAction();
        command.action = PULSE;
        command.duration = duration;
        command.frequency = frequency;
        command.color = c;
        return command;
    }

    public static LedAction createFadeCommand(Color c, long duration) {
        LedAction command = new LedAction();
        command.action = FADE;
        command.duration = duration;
        command.color = c;
        return command;
    }

    /**
     * Sets the Karotz LED to the given color. Set the color to BLACK to switch OFF the LED.
     * @param c the color requested.
     * @return The {@link KarotzCommand} to be passed to {@link fr.gn.karotz.Karotz#send(KarotzCommand)}
     */
    public static LedAction createLightCommand(Color c) {
        LedAction command = new LedAction();
        command.action = LIGHT;
        command.color = c;
        return command;
    }


    @Override
    public String getCommand() {
        String rootAddr = Kernel.getServerAddress() + "led?action=" + action;

        if (action.equals(PULSE)) {
            rootAddr += "&color=" + Integer.toHexString(color.getRGB()).substring(2);
            rootAddr += "&period=" + duration;
            rootAddr += "&pulse=" + (1000/frequency);
        } else if (action.equals(FADE)) {
            rootAddr += "&color=" + Integer.toHexString(color.getRGB()).substring(2);
            rootAddr += "&period=" + duration;
        } else if(action.equals(LIGHT)) {
            if( ! color.equals(Color.black)) {
                rootAddr += "&color=" + Integer.toHexString(color.getRGB()).substring(2);
            }
        }

        rootAddr += "&interactiveid=" + Kernel.getInteractiveId();

        return rootAddr;
    }
}
