package lu.snt.iot.serval.rn12.modem.cmp;

import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyContact;
import lu.snt.iot.serval.rn12.framework.data.msg.EmergencyMessage;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;
import org.kevoree.library.xmpp.mngr.ConnectionManager;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Hashtable;

@Provides({
        @ProvidedPort(name="sendWithAck", type = PortType.MESSAGE),
        @ProvidedPort(name="send", type = PortType.MESSAGE),
        @ProvidedPort(name="ackIn", type = PortType.MESSAGE)
})

@Requires({
        @RequiredPort(name = "msgReceived", type = PortType.MESSAGE, optional = true),
        @RequiredPort(name = "ackOut", type = PortType.MESSAGE, optional = true)
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
    private HashSet<EmergencyContact> contacts = new HashSet<EmergencyContact>();

    private MyMessageListener listener = new MyMessageListener();

    public XmppModem() {

    }


    class MyMessageListener implements MessageListener {
        public void processMessage(Chat chat, Message message) {
            logger.debug("XMPP message received !");
            if (isPortBinded("msgReceived")) {
                logger.debug("New XMPP message from:" + message.getFrom());
                EmergencyMessage pendingMessage = null;
                MessagePort answer = (MessagePort) getPortByName("msgReceived");

                for(String key : messagesWaitingAck.keySet()) {
                    if(message.getFrom().contains(key)) {
                        pendingMessage = messagesWaitingAck.remove(key);
                        break;
                    }
                }

                if(pendingMessage != null) {

                    pendingMessage.setAnswer(message.getBody());
                    if(isPortBinded("ackOut")) {
                        ((MessagePort)getPortByName("ackOut")).process(pendingMessage);
                    }
                    answer.process(pendingMessage);

                } else {


                    EmergencyContact contact = new EmergencyContact();
                    contact.setImAddr(message.getFrom());

                    for(EmergencyContact c : contacts) {
                        //try to find an existing contact.
                        if(message.getFrom().contains(c.getImAddr())){
                            contact = c;
                            break;
                        }
                    }

                    EmergencyMessage m = new EmergencyMessage(contact,"");
                    m.setAnswer(message.getBody());
                    if(isPortBinded("msgReceived")) {
                        answer.process(m);
                    } else {
                        logger.warn("InstantMessage received, but port not binded.");
                        logger.warn(m.toString());
                    }

                    logger.warn("No message awaiting answer found for " + message.getFrom());
                }
            }else {
                logger.warn("IM Message received, but port not bound: " + message.getFrom());
            }
        }
    }

    @Ports({@Port(name="ackIn")})
    public void ackReceivedFromAnotherMean(Object o) {
        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getImAddr() != null) {
                for(String key : messagesWaitingAck.keySet()) {
                    if(msg.getContact().getImAddr().contains(key)) {
                        messagesWaitingAck.remove(key);
                        logger.debug("Answer for " + msg.getContact().getImAddr() + " received by another mean.");
                        break;
                    }
                }
            }
        }
    }


    @Port(name="sendWithAck")
    public void sendMessageAck(Object o) {

        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getImAddr() != null) {
                logger.debug("Registering XMPP listener; sending message.");
                messagesWaitingAck.put(msg.getContact().getImAddr(), msg);
                connection.sendMessage(msg.getMessage(),msg.getContact().getImAddr(),listener);
            } else {
                logger.debug("No IM address for this contact: " + msg.getContact().toString());
            }
        } else {
            logger.error("Received an object to send, not instance of EmergencyMessage. Class" + o.getClass().getName());
        }
    }

    @Port(name="send")
    public void sendMessage(Object o) {
        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getImAddr() != null) {
                logger.debug("Registering XMPP listener; sending message.");
                connection.sendMessage(msg.getMessage(),msg.getContact().getImAddr(),listener);
            } else {
                logger.debug("No IM address for this contact: " + msg.getContact().toString());
            }
        } else {
            logger.error("Received an object to send, not instance of EmergencyMessage. Class" + o.getClass().getName());
        }
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
