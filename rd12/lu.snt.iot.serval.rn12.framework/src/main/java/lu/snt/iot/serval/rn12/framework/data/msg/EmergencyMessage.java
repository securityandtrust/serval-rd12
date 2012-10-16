package lu.snt.iot.serval.rn12.framework.data.msg;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 16/10/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyContact;

public class EmergencyMessage {

    private EmergencyContact contact;
    private String message;
    private String answer;

    public EmergencyMessage(EmergencyContact contact, String message) {
        this.contact = contact;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public EmergencyContact getContact() {
        return contact;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String toString() {
        return "Message[to:"+contact.toString()+", message:"+message+", answer:"+answer+"]";
    }
}
