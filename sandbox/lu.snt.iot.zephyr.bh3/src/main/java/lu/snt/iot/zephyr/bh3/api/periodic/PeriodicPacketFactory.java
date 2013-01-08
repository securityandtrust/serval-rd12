package lu.snt.iot.zephyr.bh3.api.periodic;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 18/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

public abstract class PeriodicPacketFactory {

    public static PeriodicDataPacket createLifesignPacket() {
        return new LifeSign();
    }


}
