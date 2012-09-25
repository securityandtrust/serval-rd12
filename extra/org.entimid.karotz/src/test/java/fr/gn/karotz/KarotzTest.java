/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz;

import fr.gn.karotz.msg.InteractiveModeMessage;
import fr.gn.karotz.msg.ResponseMessage;
import fr.gn.karotz.session.InteractiveAction;
import fr.gn.karotz.session.InteractiveCommand;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: gnain
 * Date: 20/05/11
 * Time: 17:33
 * To change this template use File | Settings | File Templates.
 */
public class KarotzTest {

    @Test
    public void karotzConnectionTest() {

        String sk = "87683e1e-c478-4c2a-b46e-f22bd6aca916";
        String ak = "ae5e7b12-bea8-4c5a-80d5-cd88576a95e5";
        String iid = "2b96ef18-b795-4b62-b528-29019c0e84d7";

        Karotz jp = new Karotz(ak, sk, iid);

       jp.initSession();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

       jp.closeSession();
    }


}
