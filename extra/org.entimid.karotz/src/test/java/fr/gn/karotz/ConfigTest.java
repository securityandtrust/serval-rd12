/*
 * Copyright (c) 2011. Gregory Nain.
 */

package fr.gn.karotz;

import fr.gn.karotz.msg.InteractiveModeMessage;
import fr.gn.karotz.msg.ResponseMessage;
import fr.gn.karotz.session.InteractiveAction;
import fr.gn.karotz.session.InteractiveCommand;
import fr.gn.karotz.utils.KarotzCommand;
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
public class ConfigTest {

    private static Karotz jp;

    @BeforeClass
    public static void setUp() {
        String sk = "87683e1e-c478-4c2a-b46e-f22bd6aca916";
        String ak = "ae5e7b12-bea8-4c5a-80d5-cd88576a95e5";
        String iid = "2b96ef18-b795-4b62-b528-29019c0e84d7";

        jp = new Karotz(ak, sk, iid);

        jp.initSession();
    }

    @Test
    public void configTest() {
        if( jp.isSessionActive()) {
            jp.send(new KarotzCommand() {
                @Override
                public String getCommand() {
                    return "http://api.karotz.com/api/karotz/config?interactiveid=" + Kernel.getInteractiveId();
                }
            });
        } else {
            fail("Session not active");
        }
    }



    @AfterClass
    public static void tearDown() {
        jp.closeSession();
    }


}
