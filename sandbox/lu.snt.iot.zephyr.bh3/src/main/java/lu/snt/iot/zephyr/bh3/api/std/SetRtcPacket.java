package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

import java.util.Arrays;

public class SetRtcPacket extends StandardDataPacket {

    private int day, month, year, hour, minutes, seconds;

    public void setDay(int day) {
        if(day > 0 && day < 32) {
            this.day = day;
            payload[0] = (byte)day;
        } else {
            System.out.println("Day value must be 1<= day <= 31");
        }
    }

    public void setMonth(int month) {
        if(month > 0 && month < 13) {
            this.month = month;
            payload[1] = (byte)month;
        } else {
            System.out.println("Month value must be 1<= month <= 12");
        }
    }

    public void setYear(int year) {
        if(year > 1999 && year < 2100) {
            this.year = year;
            //System.out.println("YEAR:" + year + " %: " + ((year % 256)&0xFF) + " /:" + ((year / 256)&0xFF));
            payload[2] = (byte)(year % 256);
            payload[3] = (byte)(year / 256);
        } else {
            System.out.println("Year value must be 2000<= year <= 2099");
        }
    }

    public void setHour(int hour) {
        if(hour >= 0 && hour < 24) {
            this.hour = hour;
            payload[4] = (byte)hour;
        } else {
            System.out.println("Hour value must be 0<= hour <= 23");
        }
    }

    public void setMinutes(int minutes) {
        if(minutes > 0 && minutes < 60) {
            this.minutes = minutes;
            payload[5] = (byte)minutes;
        } else {
            System.out.println("Minutes value must be 0<= min <= 59");
        }
    }

    public void setSeconds(int seconds) {
        if(seconds > 0 && seconds < 60) {
            this.seconds = seconds;
            payload[6] = (byte)seconds;
        } else {
            System.out.println("Seconds value must be 0<= sec <= 59");
        }
    }

    SetRtcPacket(int day, int month, int year, int hour, int min, int sec) {
        messageId = 0x07;
        payloadLength = 7;
        payload = new byte[7];
        setDay(day);
        setMonth(month);
        setYear(year);
        setHour(hour);
        setMinutes(min);
        setSeconds(sec);
    }

    private SetRtcPacket(){
        messageId = 0x07;
    }

    public static DataFrame parse(byte[] raw) {
        SetRtcPacket packet = new SetRtcPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        packet.ack_nack = raw[packet.payloadLength + 4];
        return packet;
    }

    public String toString() {
        return "SetRTC: " + (ack_nack== Consts.ACK ? "(ACK)":"(NACK)");
    }

}
