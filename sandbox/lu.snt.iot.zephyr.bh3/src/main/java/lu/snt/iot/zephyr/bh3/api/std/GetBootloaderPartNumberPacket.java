package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class GetBootloaderPartNumberPacket extends StandardDataPacket {

    private String partNumber = "";

    public String getPartNumber() {
        return partNumber;
    }

    protected GetBootloaderPartNumberPacket() {
        messageId = 0x0D;
    }


    public static DataFrame parse(byte[] raw) {
        GetBootloaderPartNumberPacket packet = new GetBootloaderPartNumberPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        if(packet.payloadLength > 0) {
            for(int i = 0; i < packet.payloadLength; i++) {
                packet.partNumber += (char)raw[3+i];
            }
        }
        packet.ack_nack = raw[4+packet.payloadLength ];
        return packet;
    }

    public String toString() {
        return "Bootloader Part Number: " + partNumber + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
