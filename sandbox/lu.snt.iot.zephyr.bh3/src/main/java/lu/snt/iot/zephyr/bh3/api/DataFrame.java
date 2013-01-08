package lu.snt.iot.zephyr.bh3.api;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.periodic.PeriodicDataPacket;
import lu.snt.iot.zephyr.bh3.api.std.StandardDataPacket;

public abstract class DataFrame {

    protected byte[] rawFrame;
    protected byte[] payload;
    protected int payloadLength;
    protected byte messageId;
    protected byte crc = 0x00;
    protected byte ack_nack;

    protected DataFrame() {
    }

    public byte getAckNackValue() {
        return ack_nack;
    }



    //public abstract byte[] toByteArray();

    public byte[] toByteArray() {
        rawFrame = new byte[payloadLength + 5];

        rawFrame[0] = Consts.STX;
        rawFrame[1] = messageId;
        rawFrame[2] = (byte)payloadLength;
        for(int i = 0 ; i < payloadLength ; i++) {
            rawFrame[3+i] = payload[i];
        }
        if(payloadLength >0) {
        rawFrame[payloadLength + 3] = (byte)CrcHelper.getCrc8(payload, 0, payload.length-1);
        } else {
            rawFrame[payloadLength + 3] = 0;
        }
        rawFrame[payloadLength + 4] = Consts.ETX;
        return rawFrame;
    }

    public static DataFrame parse(byte[] raw) {
        System.out.println("DataFrame Parse");
        if( (raw[1] & 0xF0) == 0x20) {
            return PeriodicDataPacket.parse(raw);
        } else {
            return StandardDataPacket.parse(raw);
        }
    }

}
