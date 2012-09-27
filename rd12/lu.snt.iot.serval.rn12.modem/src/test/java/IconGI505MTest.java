
import lu.snt.iot.serval.rn12.modem.cmp.IconGI505M3;

import java.util.HashMap;
import java.util.Hashtable;

public class IconGI505MTest {

    public static void main(String[] args) {
        IconGI505M3 modem = new IconGI505M3();

        HashMap<String, Object> dico = new HashMap<String, Object>();

        dico.put("pincode","1828");
        dico.put("serialPort","/dev/tty.GI505M Modem");

        modem.setDictionary(dico);

        modem.start();

        try {
            Thread.sleep(5*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        final Hashtable<String,Object> p = new Hashtable<String,Object>();
        p.put("text.id", Integer.valueOf(0));
        p.put("text.0.content", "TesMessage");
        p.put("ecl.0.number","621159813");

        modem.sendSMS(p);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        modem.stop();

    }


}
