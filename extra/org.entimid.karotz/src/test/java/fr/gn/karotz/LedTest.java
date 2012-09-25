/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz;

import fr.gn.karotz.actions.LedAction;
import fr.gn.karotz.msg.InteractiveModeMessage;
import fr.gn.karotz.msg.ResponseMessage;
import fr.gn.karotz.session.InteractiveAction;
import fr.gn.karotz.session.InteractiveCommand;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: gnain
 * Date: 21/05/11
 * Time: 11:04
 * To change this template use File | Settings | File Templates.
 */
public class LedTest {

    private static Karotz jp;

    @BeforeClass
    public static void setUp() {
        String sk = "87683e1e-c478-4c2a-b46e-f22bd6aca916";
        String ak = "ae5e7b12-bea8-4c5a-80d5-cd88576a95e5";
        String iid = "2b96ef18-b795-4b62-b528-29019c0e84d7";

        jp = new Karotz(ak, sk, iid);

        jp.initSession();
        if(!jp.isSessionActive()) {
            fail("Session not active.");
        }
    }

    @Test
    public void ledTest() {

        try {

            LedAction led = LedAction.createLightCommand(Color.BLUE);
            jp.send(led);

            Thread.sleep(2000);

            led = LedAction.createLightCommand(Color.YELLOW);
            jp.send(led);

            Thread.sleep(2000);

            led = LedAction.createLightCommand(Color.BLACK);
            jp.send(led);

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void fadeTest() {
        try {

            //Blue Fade UP
            LedAction led = LedAction.createFadeCommand(Color.BLUE, 1500);
            jp.send(led);
            Thread.sleep(1500);

            //Blue Fade DOWN
            led = LedAction.createFadeCommand(Color.BLACK, 1500);
            jp.send(led);
            Thread.sleep(1500);

            //yellow Fade UP
            led = LedAction.createFadeCommand(Color.YELLOW, 1500);
            jp.send(led);
            Thread.sleep(1500);

            //Yellow Fade DOWN
            led = LedAction.createFadeCommand(Color.BLACK, 1500);
            jp.send(led);
            Thread.sleep(1500);

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void pulseTest() {
        try {
            //Set Base Color to black
            LedAction led = LedAction.createLightCommand(Color.BLACK);
            jp.send(led);

            //Pulse Black-Blue 200ms ON during 1,5s
            led = LedAction.createPulseCommand(Color.BLUE, 5, 1500);
            jp.send(led);
            Thread.sleep(1500);


            //Set Base Color to black
            led = LedAction.createLightCommand(Color.BLACK);
            jp.send(led);

            //Pulse Black-Blue 500ms ON during 1,5s
            led = LedAction.createPulseCommand(Color.YELLOW, 2, 1500);
            jp.send(led);

            Thread.sleep(1500);



        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @AfterClass
    public static void tearDown() {
       jp.closeSession();
    }


}
