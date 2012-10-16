package lu.snt.iot.serval.rn12.framework.data.id;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 15/10/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

public class IdentityRecord {

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String toString() {
        return "IdentityRecord[firstName:"+firstName+", lastName:"+lastName+"]";
    }
}

