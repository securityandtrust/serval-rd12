package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

import java.util.Arrays;

public class GetRtcPacket extends StandardDataPacket {

    private int day, month, year, hour, minutes, seconds;

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    protected GetRtcPacket() {
        messageId = 0x08;
    }


    public static DataFrame parse(byte[] raw) {
        GetRtcPacket packet = new GetRtcPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        if(packet.payloadLength == 7) {
            packet.payload = Arrays.copyOfRange(raw, 3, 9);
            packet.day = (int)raw[3]&0xFF;
            packet.month = (int)raw[4]&0xFF;
            packet.year = (int)raw[5]&0xFF + (256*((int)raw[6]&0xFF));
            packet.hour= (int)raw[7]&0xFF;
            packet.minutes = (int)raw[8]&0xFF;
            packet.seconds = (int)raw[9]&0xFF;
        } else {
            System.out.println("GetRtcPacket::Wrong payload length. Payload:" + packet.payloadLength + " expected:7\n" + Arrays.toString(raw));
        }
        packet.ack_nack = raw[packet.payloadLength + 4];
        return packet;
    }

    public String toString() {
        return "GetRTC: " + day +  "/" + month +  "/" + year +  "  " + hour +  ":" + minutes +  ":"  + seconds +  (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}