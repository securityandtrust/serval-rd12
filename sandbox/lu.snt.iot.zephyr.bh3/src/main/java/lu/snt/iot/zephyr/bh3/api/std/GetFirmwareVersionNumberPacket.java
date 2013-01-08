package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

import java.util.Arrays;

public class GetFirmwareVersionNumberPacket extends StandardDataPacket {

    private int major, minor, build;

    public String getVersion() {
        return major + "." + minor + "(" + build + ")";
    }

    protected GetFirmwareVersionNumberPacket() {
        messageId = 0x0A;
    }


    public static DataFrame parse(byte[] raw) {
        GetFirmwareVersionNumberPacket packet = new GetFirmwareVersionNumberPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        if(packet.payloadLength == 8) {
            packet.payload = Arrays.copyOfRange(raw, 3, 10);
            packet.major = raw[3]&0xFF + (256*(raw[4]&0xFF));
            packet.minor = raw[5]&0xFF + (256*(raw[6]&0xFF));
            packet.build = raw[9]&0xFF + (256*(raw[10]&0xFF));
        } else {
            System.out.println("GetFirmwareVersionNumberPacket::Wrong payload length. Payload:" + packet.payloadLength + " expected:8\n" + Arrays.toString(raw));
        }
        packet.ack_nack = raw[packet.payloadLength + 4];
        return packet;
    }

    public String toString() {
        return "Firmware Version: " + getVersion() +  (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
