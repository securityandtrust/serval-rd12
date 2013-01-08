package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class GetUnitMacAddressPacket extends StandardDataPacket {

    private String mac = "";

    public String getMacAddress() {
        return mac;
    }

    protected GetUnitMacAddressPacket() {
        messageId = 0x12;
    }


    public static DataFrame parse(byte[] raw) {
        GetUnitMacAddressPacket packet = new GetUnitMacAddressPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        if(packet.payloadLength > 0) {
            for(int i = 0; i < packet.payloadLength; i++) {
                packet.mac += (char)raw[3+i];
            }
        }
        packet.ack_nack = raw[4+packet.payloadLength ];
        return packet;
    }

    public String toString() {
        return "Unit MAC Address: " + mac + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
