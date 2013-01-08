package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

import java.util.Arrays;

public class GetBatteryStatusPacket extends StandardDataPacket {

    private int percentage;
    private float voltage;


    public int getPercentage() {
        return percentage;
    }

    public float getVoltage() {
        return voltage;
    }

    protected GetBatteryStatusPacket() {
        messageId = (byte)0xAC;
    }


    public static DataFrame parse(byte[] raw) {
        GetBatteryStatusPacket packet = new GetBatteryStatusPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        if(packet.payloadLength == 3) {
            packet.payload = Arrays.copyOfRange(raw, 3, 5);
            packet.voltage = (raw[3]&0xFF + (256*(raw[4]&0xFF))) / 100f;
            packet.percentage = raw[5]&0xFF;
        } else {
            System.out.println("GetFirmwareVersionNumberPacket::Wrong payload length. Payload:" + packet.payloadLength + " expected:8\n" + Arrays.toString(raw));
        }
        packet.ack_nack = raw[packet.payloadLength + 4];
        return packet;
    }

    public String toString() {
        return "Battery Status: " + getVoltage() + "V - " + getPercentage() + "% " + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
