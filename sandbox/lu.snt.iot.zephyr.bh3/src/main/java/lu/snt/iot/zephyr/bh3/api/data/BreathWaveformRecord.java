package lu.snt.iot.zephyr.bh3.api.data;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 21/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/


import java.util.ArrayList;
import java.util.List;

public class BreathWaveformRecord {

    private long timeFromBaseTimestamp;
    private int value;


    public long getTimeFromBaseTimestamp() {
        return timeFromBaseTimestamp;
    }

    public int getValue() {
        return value;
    }

    public static List<BreathWaveformRecord> parse(byte[] payload) {
        ArrayList<BreathWaveformRecord> result = new ArrayList<BreathWaveformRecord>();

        int tempValue = 0;
        for(int i = 0; i < payload.length ; i ++) {
            switch(i%5) {
                case 0: {
                    tempValue = (payload[i]&0xFF);
                }break;
                case 1: {
                    tempValue += ((payload[i]&0x03) << 8);
                    BreathWaveformRecord rec = new BreathWaveformRecord();
                    rec.value = (tempValue & 0x3FF);
                    if(result.size() == 0) {
                        rec.timeFromBaseTimestamp = 0;
                    } else {
                        rec.timeFromBaseTimestamp = result.get(result.size()-1).getTimeFromBaseTimestamp() + 56;
                    }
                    result.add(rec);
                    tempValue = (payload[i]&0xFC) >>> 2;
                }break;
                case 2: {
                    tempValue += ((payload[i]&0x0F) << 6);
                    BreathWaveformRecord rec = new BreathWaveformRecord();
                    rec.value = (tempValue & 0x3FF);
                    if(result.size() == 0) {
                        rec.timeFromBaseTimestamp = 0;
                    } else {
                        rec.timeFromBaseTimestamp = result.get(result.size()-1).getTimeFromBaseTimestamp() + 56;
                    }
                    result.add(rec);
                    tempValue =(payload[i]&0xF0) >>> 4;
                }break;
                case 3: {
                    tempValue += ((payload[i]&0x3F) << 4);
                    BreathWaveformRecord rec = new BreathWaveformRecord();
                    rec.value = (tempValue & 0x3FF);
                    if(result.size() == 0) {
                        rec.timeFromBaseTimestamp = 0;
                    } else {
                        rec.timeFromBaseTimestamp = result.get(result.size()-1).getTimeFromBaseTimestamp() + 56;
                    }
                    result.add(rec);
                    tempValue =(payload[i]&0xC0) >>> 6;
                }break;
                case 4: {
                    tempValue += (payload[i] << 2);
                    BreathWaveformRecord rec = new BreathWaveformRecord();
                    rec.value = (tempValue & 0x3FF);
                    if(result.size() == 0) {
                        rec.timeFromBaseTimestamp = 0;
                    } else {
                        rec.timeFromBaseTimestamp = result.get(result.size()-1).getTimeFromBaseTimestamp() + 56;
                    }
                    result.add(rec);
                    tempValue = 0;
                }break;
            }
        }

        return result;
    }
}
