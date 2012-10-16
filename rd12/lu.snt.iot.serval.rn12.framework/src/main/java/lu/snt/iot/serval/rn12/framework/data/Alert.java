package lu.snt.iot.serval.rn12.framework.data;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 09/10/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyCallList;
import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyContact;
import lu.snt.iot.serval.rn12.framework.data.id.IdentityRecord;
import lu.snt.iot.serval.rn12.framework.data.msg.EmergencyMessage;
import lu.snt.iot.serval.rn12.framework.data.phr.PersonalHealthRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Alert {

    private EmergencyCallList emergencyCallList;
    private PersonalHealthRecord personalHealthRecord;
    private IdentityRecord identityRecord;
    private Map<EmergencyContact, List<EmergencyMessage>> communications = new HashMap<EmergencyContact, List<EmergencyMessage>>();
    private EmergencyContact emergencyResponder = null;
    private long timeAlertBegin = 0;
    private long timeAlertEnd = 0;


    public Alert(IdentityRecord identity){
        this.identityRecord = identity;
        timeAlertBegin = System.currentTimeMillis();
    }

    public IdentityRecord getIdentityRecord() {
        return identityRecord;
    }

    public long getTimeAlertEnd() {
        return timeAlertEnd;
    }

    public long getTimeAlertBegin() {
        return timeAlertBegin;
    }

    public void addCommunication(EmergencyMessage message) {
        if(!communications.containsKey(message.getContact())) {
            communications.put(message.getContact(), new ArrayList<EmergencyMessage>());
        }
        communications.get(message.getContact()).add(message);
    }

    public List<EmergencyMessage> getCommunicationsWith(EmergencyContact contact) {
        return communications.get(contact);
    }

    public EmergencyCallList getEmergencyCallList() {
        return emergencyCallList;
    }

    public void setEmergencyCallList(EmergencyCallList emergencyCallList) {
        if(timeAlertEnd == 0) {
            this.emergencyCallList = emergencyCallList;
        }
    }

    public PersonalHealthRecord getPersonalHealthRecord() {
        return personalHealthRecord;
    }

    public void setPersonalHealthRecord(PersonalHealthRecord personalHealthRecord) {
        if(timeAlertEnd == 0) {
            this.personalHealthRecord = personalHealthRecord;
        }
    }

    public void closeAlert() {
        if(timeAlertEnd == 0) {
            timeAlertEnd = System.currentTimeMillis();
        }
    }

    public EmergencyContact getEmergencyResponder() {
        return emergencyResponder;
    }

    public void setEmergencyResponder(EmergencyContact emergencyResponder) {
        this.emergencyResponder = emergencyResponder;
    }

    public String toString() {
        String result = "Alert {";

        result += "\tBEGIN:" + timeAlertBegin + "\n";
        result += "\tEND:" + timeAlertEnd + "\n";

        result += "\t" + identityRecord.toString() + "\n";
        result += "\t" + emergencyCallList.toString() + "\n";
        result += "\t" + emergencyResponder.toString() + "\n";
        result += "\tCommunications{\n";

        for(EmergencyContact c : communications.keySet()) {
            result += "\t\t" + c.toString() + " {\n";
            for(EmergencyMessage m : communications.get(c)) {
                    result += "\t\t\t" + m.toString()+"\n";
            }
            result+= "\t\t}\n";
        }

        result += "\t}\n";

        result +="}";
        return result;
    }
}
