package lu.snt.iot.zephyr.bh3.api.periodic;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

public class LifeSign extends PeriodicDataPacket {

    LifeSign() {
        messageId = 0x23;
    }

    public static LifeSign parse(byte[] raw) {
        LifeSign packet = new LifeSign();
        packet.rawFrame = raw;
        return packet;
    }

    public String toString() {
        return "LifeSign";
    }
}
