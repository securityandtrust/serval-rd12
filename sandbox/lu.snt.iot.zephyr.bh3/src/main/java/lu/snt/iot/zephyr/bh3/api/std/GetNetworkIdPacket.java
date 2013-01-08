package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class GetNetworkIdPacket extends StandardDataPacket {

    private String id = "";

    public String getId() {
        return id;
    }

    protected GetNetworkIdPacket() {
        messageId = 0x11;
    }


    public static DataFrame parse(byte[] raw) {
        GetNetworkIdPacket packet = new GetNetworkIdPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        if(packet.payloadLength > 0) {
            for(int i = 0; i < packet.payloadLength; i++) {
                packet.id += (char)raw[3+i];
            }
        }
        packet.ack_nack = raw[4+packet.payloadLength ];
        return packet;
    }

    public String toString() {
        return "GetNetwork Id: " + id + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
