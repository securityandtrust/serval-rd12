/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz.msg;


import fr.gn.karotz.utils.DocumentHelper;
import org.kevoree.log.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created by IntelliJ IDEA.
  * User: Gregory NAIN
 * Date: 21/05/11
 * Time: 09:10
 */
public class ResponseMessage extends ServerAnswer {

    public static enum ResponseCode{OK,ERROR,NOT_CONNECTED, TERMINATED};

    private String correlationId;
    private String interactiveId;
    private ResponseCode code;

    protected ResponseMessage(String id) {
        super(id);
    }


    public static ServerAnswer parse(Document message) {

        ResponseMessage msg = new ResponseMessage(message.getElementsByTagName("id").item(0).getFirstChild().getNodeValue());

        //Get InteractiveId
        if(message.getElementsByTagName("interactiveId").getLength() == 1) {
            msg.interactiveId = message.getElementsByTagName("interactiveId").item(0).getFirstChild().getNodeValue();
        } else {
            Log.error("ResponseMessage:parse -> InteractiveId number of nodes incorrect in " + DocumentHelper.convertToString(message));
        }

        //Get InteractiveId
        if(message.getElementsByTagName("correlationId").getLength() == 1) {
            msg.correlationId = message.getElementsByTagName("correlationId").item(0).getFirstChild().getNodeValue();
        } else {
            Log.error("ResponseMessage:parse -> correlationId number of nodes incorrect in " + DocumentHelper.convertToString(message));
        }

        //check response Node
        NodeList responseNodeList = message.getElementsByTagName("response");
        if(responseNodeList.getLength() ==1 ) {

            //Get ResponseCode
            Element responseNode = (Element)responseNodeList.item(0);
            msg.code = ResponseCode.valueOf(
                    responseNode.getElementsByTagName("code").item(0).getFirstChild().getNodeValue());

        } else {
            Log.error("ResponseMessage:parse -> 'response' number of nodes incorrect in " + DocumentHelper.convertToString(message));
        }

        return msg;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getInteractiveId() {
        return interactiveId;
    }

    public ResponseCode getCode() {
        return code;
    }

    public String toString() {
        return "ResponseMessage\n" + super.toString() + "\nCorrelationId: " + correlationId + "\nInteractiveId: " + interactiveId + "\nResponseCode: " + code.toString();
    }
}
