package lu.snt.iot.zephyr.bh3.api.periodic;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.DataFrame;

public abstract class PeriodicDataPacket extends DataFrame {

    public static PeriodicDataPacket parse(byte[] raw) {
        System.out.println("PeriodicDataPacket Parse");

        switch(raw[1]) {
            case 0x20 : return GeneralDataPacket.parse(raw);
            case 0x21 : return BreathingWaveformDataPacket.parse(raw);
            case 0x22 : return ECGWaveformDataPacket.parse(raw);
            case 0x23 : return LifeSign.parse(raw);
            default: {
                System.out.println("PeriodicDataPacket: Unkown messageId: " + Byte.toString(raw[1]));
                return null;
            }
        }
    }

}
