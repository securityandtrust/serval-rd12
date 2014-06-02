package lu.snt.iot.serval.rn12.modem.cmp;

import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyContact;
import lu.snt.iot.serval.rn12.framework.data.msg.EmergencyMessage;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;
import org.kevoree.annotation.*;
import org.kevoree.api.Port;
import org.kevoree.library.xmpp.mngr.ConnectionManager;
import org.kevoree.log.Log;
import java.util.HashSet;
import java.util.Hashtable;


@ComponentType
@Library(name = "Serval_RN12")
public class XmppModem {

    @Param(optional = false)
    protected String login;

    @Param(optional = false)
    protected String password;

    @Output(optional = true)
    protected Port ackOut;

    @Output(optional = true)
    protected Port msgReceived;

    //private FakeConsole console;
    private ConnectionManager connection;

    private Hashtable<String,EmergencyMessage> messagesWaitingAck = new Hashtable<String,EmergencyMessage>();
    private HashSet<EmergencyContact> contacts = new HashSet<EmergencyContact>();

    private MyMessageListener listener = new MyMessageListener();

    public XmppModem() {

    }


    class MyMessageListener implements MessageListener {
        public void processMessage(Chat chat, Message message) {
            Log.debug("XMPP message received !");
            if (msgReceived.getConnectedBindingsSize() > 0) {
                Log.debug("New XMPP message from:" + message.getFrom());
                EmergencyMessage pendingMessage = null;


                for(String key : messagesWaitingAck.keySet()) {
                    if(message.getFrom().contains(key)) {
                        pendingMessage = messagesWaitingAck.remove(key);
                        break;
                    }
                }

                if(pendingMessage != null) {

                    pendingMessage.setAnswer(message.getBody());
                    if(ackOut.getConnectedBindingsSize() > 0) {
                        ackOut.send(pendingMessage);
                    }
                    msgReceived.send(pendingMessage);

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
                    if(msgReceived.getConnectedBindingsSize() > 0) {
                        msgReceived.send(m);
                    } else {
                        Log.warn("InstantMessage received, but port not binded.");
                        Log.warn(m.toString());
                    }

                    Log.warn("No message awaiting answer found for " + message.getFrom());
                }
            }else {
                Log.warn("IM Message received, but port not bound: " + message.getFrom());
            }
        }
    }

    @Input
    public void ackIn(Object o) {
        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getImAddr() != null) {
                for(String key : messagesWaitingAck.keySet()) {
                    if(msg.getContact().getImAddr().contains(key)) {
                        messagesWaitingAck.remove(key);
                        Log.debug("Answer for " + msg.getContact().getImAddr() + " received by another mean.");
                        break;
                    }
                }
            }
        }
    }


    @Input
    public void sendWithAck(Object o) {

        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getImAddr() != null) {
                Log.debug("Registering XMPP listener; sending message.");
                messagesWaitingAck.put(msg.getContact().getImAddr(), msg);
                connection.sendMessage(msg.getMessage(), msg.getContact().getImAddr(), listener);
            } else {
                Log.debug("No IM address for this contact: " + msg.getContact().toString());
            }
        } else {
            Log.error("Received an object to send, not instance of EmergencyMessage. Class" + o.getClass().getName());
        }
    }

    @Input
    public void send(Object o) {
        if(o instanceof EmergencyMessage) {
            EmergencyMessage msg = (EmergencyMessage)o;
            if(msg.getContact().getImAddr() != null) {
                Log.debug("Registering XMPP listener; sending message.");
                connection.sendMessage(msg.getMessage(),msg.getContact().getImAddr(),listener);
            } else {
                Log.debug("No IM address for this contact: " + msg.getContact().toString());
            }
        } else {
            Log.error("Received an object to send, not instance of EmergencyMessage. Class" + o.getClass().getName());
        }
    }

    @Start
    public void start() {
        connection=new ConnectionManager();
        connection.login(login, password);
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
