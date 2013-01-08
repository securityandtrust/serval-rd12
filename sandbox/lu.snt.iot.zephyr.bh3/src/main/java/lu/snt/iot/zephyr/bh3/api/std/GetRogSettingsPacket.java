package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.periodic.PeriodicDataPacket;

import java.util.Arrays;

public class GetRogSettingsPacket extends PeriodicDataPacket {


    private int heartRate_high;
    private int heartRate_low;
    private float respirationRate_high;
    private float respirationRate_low;
    private float activity_high;
    private float activity_low;
    private int greenToOrangeTime;
    private int orangeToRedTime;


    GetRogSettingsPacket() {
        messageId = (byte)(0x9C & 0xFF);
    }

    public static GetRogSettingsPacket parse(byte[] raw) {
        System.out.println("Parsing Rog");
        GetRogSettingsPacket packet = new GetRogSettingsPacket();
        packet.rawFrame = raw;
        packet.payloadLength = raw[2] & 0xFF;
        if(packet.payloadLength == 16) {
        packet.heartRate_high = (raw[3] & 0xFF) + (256*(raw[4] & 0xFF));
        packet.heartRate_low = (raw[5] & 0xFF) + (256*(raw[6] & 0xFF));
        packet.respirationRate_high = ((raw[7] & 0xFF) + (256*(raw[8] & 0xFF))) / 10f;
        packet.respirationRate_low = ((raw[9] & 0xFF) + (256*(raw[10] & 0xFF))) / 10f;
        packet.activity_high = (raw[11] + (256*raw[12])) / 100f;
        packet.activity_low = (raw[13] + (256*raw[14])) / 100f;
        packet.greenToOrangeTime = (raw[15] & 0xFF) + (256*(raw[16] & 0xFF));
        packet.orangeToRedTime = (raw[17] & 0xFF) + (256*(raw[18] & 0xFF));
        } else {
            System.out.println("GetRogSettingsPacket::Unexpected payload length: " + packet.payloadLength + " expected:16\n" + Arrays.toString(raw));
        }

        return packet;
    }

    public String toString() {
        String result = "ROG Settings :\n"
                + "\t" + heartRate_low + " < Heart Rate < " + heartRate_high + "\n"
                + "\t" + respirationRate_low + " < Respiration Rate < " + respirationRate_high + "\n"
                + "\t" + activity_low + " < Activity < " + activity_high + "\n"
                + "\t Green to Orange time: " + greenToOrangeTime + "\n"
                + "\t Orange to Red time: " + orangeToRedTime + "\n";
        return result;

    }
}
