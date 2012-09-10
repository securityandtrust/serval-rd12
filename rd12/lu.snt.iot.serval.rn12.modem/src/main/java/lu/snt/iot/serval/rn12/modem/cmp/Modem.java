package lu.snt.iot.serval.rn12.modem.cmp;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.kevoree.library.FakeConsole;
import org.kevoree.library.xmpp.mngr.ConnectionManager;

import javax.swing.*;
import java.util.Hashtable;

@Provides({
        @ProvidedPort(name="sendWithAck", type = PortType.MESSAGE),
        @ProvidedPort(name="send", type = PortType.MESSAGE)
})

@Requires({
        @RequiredPort(name = "msgReceived", type = PortType.MESSAGE, optional = true)
})
@DictionaryType({
        @DictionaryAttribute(name="login",optional = false),
        @DictionaryAttribute(name="password",optional = false)
})
@ComponentType
@Library(name = "Serval_RN12")
public class Modem extends AbstractComponentType {

    private FakeConsole console;
    private ConnectionManager connection;

    public Modem() {

    }

    @Port(name="sendWithAck")
    public void sendMessageAck(Object msg) {
        final Hashtable<String, Object> alert = (Hashtable<String,Object>)msg;
        final int textId = (Integer)alert.get("text.id");

        if(alert.get("text."+textId+".xmpp") != null) {
            connection.sendMessage((String)alert.get("text."+textId+".content"),(String)alert.get("text."+textId+".xmpp"),new MessageListener() {
                public void processMessage(Chat chat, Message message) {
                    if (isPortBinded("msgReceived")) {
                        MessagePort answer = (MessagePort) getPortByName("msgReceived");
                        if(isPositive(message.getBody())) {
                        alert.put("text."+textId+".response","Yes");
                        } else {
                            alert.put("text."+textId+".response","No");
                        }
                        answer.process(alert);
                    }
                }
            });
        }
    }

    private boolean isPositive(String sentense) {
        return sentense.toLowerCase().contains("yes") || sentense.toLowerCase().contains("oui") || sentense.toLowerCase().contains("ok");
    }


    @Port(name="send")
    public void sendMessage(Object msg) {
        Hashtable<String, Object> alert = (Hashtable<String,Object>)msg;
        int textId = (Integer)alert.get("text.id");
        JOptionPane.showMessageDialog(null, alert.get("text."+textId+".content"), "Message Received (to:" + alert.get("text."+textId+".number") + ")", JOptionPane.INFORMATION_MESSAGE);
    }

    @Start
    public void start() {
        connection=new ConnectionManager();
        connection.login((String)getDictionary().get("login"), (String)getDictionary().get("password"));
    }

    @Stop
    public void stop() {
        connection.disconnect();
    }

    @Update
    public void update() {
        stop();
        start();
    }
}
