package lu.snt.iot.serval.rn12.framework.data.ecl;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 15/10/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.serval.rn12.framework.data.Alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmergencyCallList {

    private ArrayList<EmergencyContact> contactList = new ArrayList<EmergencyContact>();
    private Map<Alert,Integer> contactIndexes = new HashMap<Alert,Integer>();

    public void addEmergencyContact(EmergencyContact contact) {
        contactList.add(contact);
    }

    public void insertEmergencyContact(EmergencyContact contact, int place) {
        contactList.add(place, contact);
    }

    /**
     * Returns the next contact in the list; null if not more contact is
     * @param alert
     * @return
     */
    public EmergencyContact getNextContact(Alert alert) {
        if(!contactIndexes.containsKey(alert)) {
            contactIndexes.put(alert,-1);
        }
        int nextIndex = contactIndexes.get(alert);
        if(!(nextIndex == (contactList.size()-1))) { //
            nextIndex++;
            contactIndexes.put(alert, nextIndex);
            return contactList.get(nextIndex);
        } else {
            //No more contact
            return null;
        }
    }

    public String toString() {

        String result = "EmergencyCallList {\n";
        for(EmergencyContact c : contactList) {
            result += "\t" + c.toString() + "\n";
        }
        result += "}";

        return result;
    }

}
