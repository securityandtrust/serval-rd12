package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class SetGeneralDataPacket extends StandardDataPacket {

    SetGeneralDataPacket() {
        messageId = 0x14;
        payloadLength = 1;
        payload = new byte[1];
    }

    SetGeneralDataPacket(boolean active) {
        this();
        payload[0] = (byte)(active?1:0);
    }

    public static DataFrame parse(byte[] raw) {
        SetGeneralDataPacket packet = new SetGeneralDataPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        packet.ack_nack = raw[packet.payloadLength + 4];
        return packet;
    }

    public String toString() {
        return "SetGeneralDataPacket: " + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
