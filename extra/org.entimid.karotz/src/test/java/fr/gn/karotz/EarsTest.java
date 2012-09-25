/*
 * Copyright (c) 2011. Gregory Nain.
 */

package fr.gn.karotz;

import fr.gn.karotz.actions.EarsAction;
import fr.gn.karotz.msg.InteractiveModeMessage;
import fr.gn.karotz.msg.ResponseMessage;
import fr.gn.karotz.session.InteractiveAction;
import fr.gn.karotz.session.InteractiveCommand;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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
public class EarsTest {

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
    public void relativeEarsTest() {

        try {

            jp.send(EarsAction.createReset());

            EarsAction command;

            for (int i = 0; i < 5; i++) {
                command = EarsAction.createLeftRelativeMove((i % 2 == 0 ? i : -i));
                jp.send(command);
                Thread.sleep(1000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void absoluteEarsTest() {
        try {
            EarsAction command;

            jp.send(EarsAction.createReset());

            for (int i = 0; i < 30; i++) {
                command = EarsAction.createRightAbsoluteMove(i);
                jp.send(command);
                Thread.sleep(3000);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Test
    public void absolutePositionEarsTest() {
        try {
            EarsAction command;
            int pos = 1;

            jp.send(EarsAction.createReset());

            command = EarsAction.createRightAbsoluteMove(pos);
            jp.send(command);
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test
    public void bothRealativePositionEarsTest() {
        try {
            jp.send(EarsAction.createReset());

            EarsAction command;

            for (int i = 0; i < 15; i++) {
                command = EarsAction.createRelativeMove(i,-i);
                jp.send(command);
                Thread.sleep(2000);
            }

            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    @AfterClass
    public static void tearDown() {
      jp.closeSession();
    }


}
