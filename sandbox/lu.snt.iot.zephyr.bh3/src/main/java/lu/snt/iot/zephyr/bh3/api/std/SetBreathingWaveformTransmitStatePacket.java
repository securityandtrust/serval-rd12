package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class SetBreathingWaveformTransmitStatePacket extends StandardDataPacket {

    private SetBreathingWaveformTransmitStatePacket() {
        messageId = 0x15;
    }

    SetBreathingWaveformTransmitStatePacket(boolean active) {
        this();
        payloadLength = 1;
        payload = new byte[1];
        payload[0] = (byte)(active?1:0);
    }

    public static DataFrame parse(byte[] raw) {
        SetBreathingWaveformTransmitStatePacket packet = new SetBreathingWaveformTransmitStatePacket();
        packet.rawFrame = raw;
        packet.payloadLength = 0;
        packet.ack_nack = raw[packet.payloadLength + 4];
        return packet;
    }

    public String toString() {
        return "SetBreathingWaveformTransmitState: " + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
