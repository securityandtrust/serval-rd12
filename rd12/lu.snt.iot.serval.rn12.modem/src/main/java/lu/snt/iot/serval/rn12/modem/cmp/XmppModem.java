package lu.snt.iot.serval.rn12.modem.cmp;

import lu.snt.iot.serval.rn12.framework.data.msg.EmergencyMessage;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.kevoree.library.xmpp.mngr.ConnectionManager;
import org.slf4j.LoggerFactory;

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
public class XmppModem extends AbstractComponentType {

    //private FakeConsole console;
    private ConnectionManager connection;

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(XmppModem.class);
    private Hashtable<String,EmergencyMessage> messagesWaitingAck = new Hashtable<String,EmergencyMessage>();

    public XmppModem() {

    }

    @Port(name="sendWithAck")
    public void sendMessageAck(Object o) {

        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getImAddr() != null) {
                logger.debug("Registering XMPP listener; sending message.");
                messagesWaitingAck.put(msg.getContact().getImAddr(), msg);

                connection.sendMessage(msg.getMessage(),msg.getContact().getImAddr(),new MessageListener() {
                    public void processMessage(Chat chat, Message message) {
                        logger.debug("XMPP message received !");
                        if (isPortBinded("msgReceived")) {
                            logger.debug("New XMPP message from:" + message.getFrom());
                            EmergencyMessage pendingMessage = null;

                            for(String key : messagesWaitingAck.keySet()) {
                                if(message.getFrom().contains(key)) {
                                    pendingMessage = messagesWaitingAck.remove(key);
                                    break;
                                }
                            }

                            if(pendingMessage != null) {

                                MessagePort answer = (MessagePort) getPortByName("msgReceived");
                                pendingMessage.setAnswer(message.getBody());
                                answer.process(pendingMessage);

                                logger.debug("XMPP ALERT processed; Listener removed.");

                            } else {
                                logger.debug("No pending alert. Message ignored");
                            }
                        }
                    }
                });
            } else {
                logger.debug("No IM address for this contact: " + msg.getContact().toString());
            }
        } else {
            logger.error("Received an object to send, not instance of EmergencyMessage. Class" + o.getClass().getName());
        }
    }

    @Port(name="send")
    public void sendMessage(Object msg) {
        Hashtable<String, Object> alert = (Hashtable<String,Object>)msg;
        int textId = (Integer)alert.get("text.id");
        // JOptionPane.showMessageDialog(null, alert.get("text."+textId+".content"), "Message Received (to:" + alert.get("text."+textId+".number") + ")", JOptionPane.INFORMATION_MESSAGE);
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
