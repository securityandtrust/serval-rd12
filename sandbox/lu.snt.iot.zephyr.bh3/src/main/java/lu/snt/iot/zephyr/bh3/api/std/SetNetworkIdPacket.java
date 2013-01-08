package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class SetNetworkIdPacket extends StandardDataPacket {

    private String id = "";

    public void setId(String id) {
        if(id.length() > 1 && id.length() < 30) {
            this.id = id;
            payloadLength = id.length();
            payload = new byte[payloadLength];
            for(int i = 0; i < payloadLength ; i++) {
                payload[i] = (byte)id.charAt(i);
            }
        } else {
            System.out.println("The id string must be of length: 2 <= l <= 29");
        }
    }

    SetNetworkIdPacket(String id) {
        messageId = 0x10;
        setId(id);
    }

    private SetNetworkIdPacket() {
        messageId = 0x10;
    }


    public static DataFrame parse(byte[] raw) {
        SetNetworkIdPacket packet = new SetNetworkIdPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        packet.ack_nack = raw[4+packet.payloadLength ];
        return packet;
    }

    public String toString() {
        return "SetNetwork Id: " + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
