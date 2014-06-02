import lu.snt.iot.serval.rn12.modem.cmp.SendMms;

import java.io.File;
import java.io.IOException;

public class MmsTest {

    public static void main(String[] args) {
        try {

            SendMms mms = new SendMms("http://194.154.192.88:8080/", "mms", "mms");

            mms.addparameter ("PhoneNumber", "+352 621159813");

            mms.addparameter ("MMSSubject", "This a a test message");
            mms.addparameter ("MMSText", "test message");  // Optional

            mms.addparameter ("MMSFile", new File("/Users/gregory.nain/386890_459317780757960_998193334_n.jpeg"));

            mms.send ();
        }
        catch(IOException e) {
            System.out.println("unable to create new url: "+e.getMessage());
        }
    }
}
