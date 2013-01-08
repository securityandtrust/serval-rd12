package lu.snt.iot.zephyr.bh3.api.periodic;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.data.BreathWaveformRecord;
import lu.snt.iot.zephyr.bh3.api.data.EcgWaveformRecord;

import java.util.Arrays;
import java.util.List;

public class ECGWaveformDataPacket extends PeriodicDataPacket {

    private int sequenceNumber;
    private int year;
    private int month;
    private int day;
    private long baseRecordTimeInDay;
    private List<EcgWaveformRecord> records;

    ECGWaveformDataPacket() {
        messageId = 0x22;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public long getBaseRecordTimeInDay() {
        return baseRecordTimeInDay;
    }

    public List<EcgWaveformRecord> getRecords() {
        return records;
    }

    public static ECGWaveformDataPacket parse(byte[] raw) {
        ECGWaveformDataPacket packet = new ECGWaveformDataPacket();
        packet.rawFrame = raw;
        packet.payloadLength = raw[2] & 0xFF;
        packet.sequenceNumber = raw[3] & 0xFF;
        packet.year = (raw[4] & 0xFF) + (256*(raw[5] & 0xFF));
        packet.month = raw[6] & 0xFF;
        packet.day = raw[7] & 0xFF;
        packet.baseRecordTimeInDay = (raw[8] & 0xFF) + (256*(raw[9] & 0xFF)) + (long)(Math.pow(256,2)*(raw[10] & 0xFF)) + (long)(Math.pow(256,3)*(raw[11] & 0xFF));
        packet.records = EcgWaveformRecord.parse(Arrays.copyOfRange(raw,12,91));
        return packet;
    }

    public String toString() {
        String result = "ECG Waveform Data:\n"
                + "\tSeqNum: " + sequenceNumber + "\n"
                + "\tTimestamp: " + day + "/" + month + "/" + year + "::" + baseRecordTimeInDay + "ms\n"
                + "\tRecords: \n";

        for(EcgWaveformRecord rec : records) {
            result += "\t\t" + (baseRecordTimeInDay + rec.getTimeFromBaseTimestamp()) + " : " + rec.getValue() + "\n";
        }

        return result;

    }
}
