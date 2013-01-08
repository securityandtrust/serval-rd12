package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class GetUnitBTFriendlyNamePacket extends StandardDataPacket {

    private String friendlyName = "";

    public String getFriendlyName() {
        return friendlyName;
    }

    protected GetUnitBTFriendlyNamePacket() {
        messageId = 0x17;
    }


    public static DataFrame parse(byte[] raw) {
        GetUnitBTFriendlyNamePacket packet = new GetUnitBTFriendlyNamePacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        if(packet.payloadLength > 0) {
            for(int i = 0; i < packet.payloadLength; i++) {
                packet.friendlyName += (char)raw[3+i];
            }
        }
        packet.ack_nack = raw[4+packet.payloadLength ];
        return packet;
    }

    public String toString() {
        return "Unit BT Friendly Name: " + friendlyName + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
