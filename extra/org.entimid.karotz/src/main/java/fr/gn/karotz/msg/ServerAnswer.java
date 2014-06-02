/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz.msg;

import fr.gn.karotz.utils.DocumentHelper;
import org.kevoree.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
  * User: Gregory NAIN
 * Date: 20/05/11
 * Time: 17:46
 */
public abstract class ServerAnswer {

    protected String messageId;

    public String getMessageId() {
        return messageId;
    }

    protected ServerAnswer(String id) {
        this.messageId = id;
    }


    public static ServerAnswer parse(InputStream is) {

        try {
            //Document creation
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(is);

            //Check message type
            NodeList messageNodeList = doc.getElementsByTagName("VoosMsg");
            int messageItemCount = messageNodeList.getLength();
            if (messageItemCount != 0) {

                //InteractiveMode Answer
                if(doc.getElementsByTagName("interactiveMode").getLength() != 0) {
                    return InteractiveModeMessage.parse(doc);
                } else if(doc.getElementsByTagName("response").getLength() != 0) {
                    return ResponseMessage.parse(doc);
                } else if(doc.getElementsByTagName("callback").getLength() != 0) {
                    return CallbackMessage.parse(doc);
                } else {
                    Log.error("ServerAnswer:parse -> Unknown message kind: " + DocumentHelper.convertToString(doc));
                }


            } else {
                NodeList messageNodeList2 = doc.getElementsByTagName("ConfigResponse");
                int messageItemCount2 = messageNodeList.getLength();
                if (messageItemCount != 0) {
                    return ConfigurationMessage.parse(doc);
                } else {
                    Log.error("ServerAnswer:parse -> VoosMsg not found in answer: " + DocumentHelper.convertToString(doc));
                }
            }
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public String toString() {
        return "MessageID: " + messageId;
    }
}
