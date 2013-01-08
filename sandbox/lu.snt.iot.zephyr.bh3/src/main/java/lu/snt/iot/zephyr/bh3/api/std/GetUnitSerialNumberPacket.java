package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class GetUnitSerialNumberPacket extends StandardDataPacket {

    private String serialNumber = "";

    public String getSerialNumber() {
        return serialNumber;
    }

    protected GetUnitSerialNumberPacket() {
        messageId = 0x0B;
    }


    public static DataFrame parse(byte[] raw) {
        GetUnitSerialNumberPacket packet = new GetUnitSerialNumberPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        if(packet.payloadLength > 0) {
            for(int i = 0; i < packet.payloadLength; i++) {
                packet.serialNumber += (char)raw[3+i];
            }
        }
        packet.ack_nack = raw[4+packet.payloadLength ];
        return packet;
    }

    public String toString() {
        return "SerialNumber: " + serialNumber + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
