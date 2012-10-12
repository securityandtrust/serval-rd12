
import lu.snt.iot.serval.rn12.modem.cmp.XmppModem;
import org.kevoree.framework.*;
import org.kevoree.framework.message.Message;
import java.util.HashMap;
import java.util.Hashtable;

public class XmppModemTest extends AbstractChannelFragment{

    public static void main(String[] args) {
        XmppModem modem = new XmppModem();

        HashMap<String, Object> dico = new HashMap<String, Object>();

        dico.put("login","entimid@gmail.com");
        dico.put("password","entimidpass");

        modem.setDictionary(dico);

        modem.start();


        final Hashtable<String,Object> p = new Hashtable<String,Object>();
        p.put("text.id", Integer.valueOf(0));
        p.put("text.0.content", "TestMessage0");
        p.put("text.0.xmpp","sntlabphone01@entimid.org");
        p.put("text.1.content", "TestMessage1");
        p.put("text.1.xmpp","sntlabphone01@entimid.org");

        modem.sendMessageAck(p);

        try {
            Thread.sleep(30 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        p.put("text.id", Integer.valueOf(1));
        modem.sendMessageAck(p);

        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        modem.stop();

    }

    @Override
    public Object dispatch(Message message) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ChannelFragmentSender createSender(String s, String s1) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
