package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class SetECGWaveformTransmitStatePacket extends StandardDataPacket {

    private SetECGWaveformTransmitStatePacket() {
        messageId = 0x16;
    }

    SetECGWaveformTransmitStatePacket(boolean active) {
        this();
        payloadLength = 1;
        payload = new byte[1];
        payload[0] = (byte)(active?1:0);
    }

    public static DataFrame parse(byte[] raw) {
        SetECGWaveformTransmitStatePacket packet = new SetECGWaveformTransmitStatePacket();
        packet.rawFrame = raw;
        packet.payloadLength = 0;
        packet.ack_nack = raw[packet.payloadLength + 4];
        return packet;
    }

    public String toString() {
        return "SetECGWaveformTransmitState: " + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
