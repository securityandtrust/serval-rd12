package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class GetBTUserConfigPacket extends StandardDataPacket {

    private boolean uc1_device_discoverable;

    public boolean isDeviceDiscoverable() {
        return uc1_device_discoverable;
    }

    protected GetBTUserConfigPacket() {
        messageId = (byte)0xA3;
    }


    public static DataFrame parse(byte[] raw) {
        System.out.println("Parsing BT User Config");
        GetBTUserConfigPacket packet = new GetBTUserConfigPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        packet.uc1_device_discoverable = ((raw[3] & 0xFF) + (256*(raw[4] & 0xFF)) == 1?true:false);
        packet.ack_nack = raw[4+packet.payloadLength ];
        return packet;
    }

    public String toString() {
        return "BT User Config"+ (ack_nack== Consts.ACK ? "(ACK)":"(NACK)")+":\n\tDevice Discoverable: " + isDeviceDiscoverable() ;
    }

}
