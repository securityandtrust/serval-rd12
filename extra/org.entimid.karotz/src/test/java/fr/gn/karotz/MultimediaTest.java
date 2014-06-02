/*
 * Copyright (c) 2011. Gregory Nain.
 */

package fr.gn.karotz;

import fr.gn.karotz.actions.MultimediaAction;
import fr.gn.karotz.actions.TextToSpeachAction;
import fr.gn.karotz.utils.Languages;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: gnain
 * Date: 21/05/11
 * Time: 11:04
 * To change this template use File | Settings | File Templates.
 */
public class MultimediaTest {

    private static Karotz jp;

    @BeforeClass
    public static void setUp() {
        String sk = "87683e1e-c478-4c2a-b46e-f22bd6aca916";
        String ak = "ae5e7b12-bea8-4c5a-80d5-cd88576a95e5";
        String iid = "2b96ef18-b795-4b62-b528-29019c0e84d7";  //TOOTWI
        //String iid = "a4ddc6a6-f865-4e54-9fc7-0441a0b3e142"; // ALEKOUT
        jp = new Karotz(ak, sk, iid);

        jp.initSession();
        if(!jp.isSessionActive()) {
            fail("Session not active.");
        }
    }

    @Test
    public void ttsTest() {
        MultimediaAction action = new MultimediaAction(jp.getKernel(), "Dear Annette. I received your request for help. I am processing it.", Languages.EN);
        System.out.println("Sending: " + action.getCommand());
        jp.send(action);

        try {
            Thread.sleep(1 * 10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        MultimediaAction action2 = new MultimediaAction(jp.getKernel(), "I try to contact Mike.", Languages.EN);
        System.out.println("Sending: " + action2.getCommand());
        jp.send(action2);

        try {
            Thread.sleep(1 * 10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }



    @AfterClass
    public static void tearDown() {
        jp.closeSession();
    }

    public static void main(String[] args) {

        MultimediaTest test = new MultimediaTest();
        MultimediaTest.setUp();
        test.ttsTest();
        MultimediaTest.tearDown();

    }


}
