/**
 * Author : Assaad MOAWAD (developer.name@uni.lu)
 * Date: 10/10/12
 * Time: 15:10
 * (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
 */
import lu.snt.iot.serval.rn12.karotz.KarotzCmp;

import java.util.HashMap;
import java.util.Properties;

public class IconKarotzTest {
    public static void main(String[] args) {
        KarotzCmp karot = new KarotzCmp();


        final HashMap<String,Object> hash = new HashMap<String,Object>();
        hash.put("secretKey", "87683e1e-c478-4c2a-b46e-f22bd6aca916");
        hash.put("applicationKey", "ae5e7b12-bea8-4c5a-80d5-cd88576a95e5");
        hash.put("installationId","2b96ef18-b795-4b62-b528-29019c0e84d7");

        karot.setDictionary(hash);



        karot.start();
        Properties p = new Properties();
        p.setProperty("message", "testing rabbit");
        karot.onTtsRequest(p);

        //Why is this?
        try {
            Thread.sleep(3 * 60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        karot.stop();
    }
}