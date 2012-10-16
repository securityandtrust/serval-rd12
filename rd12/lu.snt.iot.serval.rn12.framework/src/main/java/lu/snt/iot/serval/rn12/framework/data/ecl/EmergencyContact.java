package lu.snt.iot.serval.rn12.framework.data.ecl;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 15/10/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

public class EmergencyContact {

    private String name;
    private String phoneNumber;
    private String imAddr;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImAddr() {
        return imAddr;
    }

    public void setImAddr(String imAddr) {
        this.imAddr = imAddr;
    }

    public String toString() {
        return "Contact[name:"+name+", phone:"+phoneNumber+", im:"+imAddr+"]";
    }

}
