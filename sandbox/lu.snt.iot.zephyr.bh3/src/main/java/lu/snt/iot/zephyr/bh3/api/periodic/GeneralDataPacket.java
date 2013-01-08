package lu.snt.iot.zephyr.bh3.api.periodic;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

public class GeneralDataPacket extends PeriodicDataPacket {

    private int sequenceNumber;
    private int year;
    private int month;
    private int day;
    private long milliseconds;
    private float heartRate;
    private float respirationRate;
    private float skinTemperature;
    private float posture;
    private float VMU;
    private float peakAcceleration;
    private float batteryVoltage;
    private float breathingWaveAmplitude;
    private float ECGAmplitude;
    private float ECGNoise;
    private float verticalAxisAccelerationMin;
    private float verticalAxisAccelerationPeak;
    private float lateralAxisAccelerationMin;
    private float lateralAxisAccelerationPeak;
    private float sagitalAxisAccelerationMin;
    private float sagitalAxisAccelerationPeak;
    private int zephyrSystemChannel;
    private int gsr;
    private int rog;
    private int alarm;
    private int batteryStatus;
    private int button_worn;

    GeneralDataPacket() {
        messageId = 0x20;
    }

    public static GeneralDataPacket parse(byte[] raw) {
        GeneralDataPacket packet = new GeneralDataPacket();
        packet.rawFrame = raw;
        packet.payloadLength = raw[2] & 0xFF;
        packet.sequenceNumber = raw[3] & 0xFF;
        packet.year = (raw[4] & 0xFF) + (256*(raw[5] & 0xFF));
        packet.month = raw[6] & 0xFF;
        packet.day = raw[7] & 0xFF;
        packet.milliseconds = (raw[8] & 0xFF) + (256*(raw[9] & 0xFF)) + (long)(Math.pow(256,2)*(raw[10] & 0xFF)) + (long)(Math.pow(256,3)*(raw[11] & 0xFF));
        packet.heartRate = (raw[12] & 0xFF) + (256*(raw[13] & 0xFF));
        packet.respirationRate = ((raw[14] & 0xFF) + (256*(raw[15] & 0xFF))) / 10f;
        packet.skinTemperature = ((raw[16] & 0xFF) + (256*(raw[17] & 0xFF))) / 10f;
        packet.posture = raw[18] + (256*raw[19]);
        packet.VMU = (raw[20] + (256*raw[21])) / 100f;
        packet.peakAcceleration = (raw[22] + (256*raw[23])) / 100f;
        packet.batteryVoltage = (raw[24] + (256*raw[25])) / 1000f;
        packet.breathingWaveAmplitude = raw[26] + (256*raw[27]);
        packet.ECGAmplitude = (raw[28] + (256*raw[29])) / 1000000f;
        packet.ECGNoise = (raw[30] + (256*raw[31])) / 1000000f;
        packet.verticalAxisAccelerationMin = (raw[32] + (256*raw[33])) / 100f;
        packet.verticalAxisAccelerationPeak = (raw[34] + (256*raw[35])) / 100f;
        packet.lateralAxisAccelerationMin = (raw[36] + (256*raw[37])) / 100f;
        packet.lateralAxisAccelerationPeak = (raw[38] + (256*raw[39])) / 100f;
        packet.sagitalAxisAccelerationMin = (raw[40] + (256*raw[41])) / 100f;
        packet.sagitalAxisAccelerationPeak = (raw[42] + (256*raw[43])) / 100f;
        packet.zephyrSystemChannel = (raw[44] & 0xFF) + (256*(raw[45] & 0xFF));
        packet.gsr = (raw[46] & 0xFF) + (256*(raw[46] & 0xFF));
        packet.rog = (raw[52] & 0xFF) + (256*(raw[53] & 0xFF));
        packet.batteryStatus = raw[54] & 0xFF;
        packet.button_worn = raw[55] & 0xFF;


        return packet;
    }

    public String toString() {
        String result = "GeneralData:\n"
                + "\tSeqNum: " + sequenceNumber + "\n"
                + "\tTimestamp: " + day + "/" + month + "/" + year + "::" + milliseconds + "ms\n"
                + "\tHeart Rate: " + heartRate + " BPM\n"
                + "\tRespiration Rate: " + respirationRate + " RPM\n"
                + "\tSkin Temp.: " + skinTemperature + "°C\n"
                + "\tPosture: " + posture + "°\n"
                + "\tActivity: " + VMU + " VMU\n"
                + "\tPeak Acc.: " + peakAcceleration + "g\n"
                + "\tBattery Volt.: " + batteryVoltage + " V\n"
                + "\tBreathing Ampl.: " + breathingWaveAmplitude + " LSB\n"
                + "\tECG Ampl.: " + ECGAmplitude + " V\n"
                + "\tECG Noise: " + ECGNoise + " V\n"
                + "\tVert. Acc.: " + verticalAxisAccelerationMin + "g(min) / "+verticalAxisAccelerationPeak+"g(peak)\n"
                + "\tLat. Acc.: " + lateralAxisAccelerationMin + "g(min) / "+lateralAxisAccelerationPeak+"g(peak)\n"
                + "\tSag. Acc.: " + sagitalAxisAccelerationMin + "g(min) / "+sagitalAxisAccelerationPeak+"g(peak)\n"
                + "\tZephyr Channel System: " + zephyrSystemChannel + "\n"
                + "\tGSR: " + gsr + " nanoSiemens\n";
        result += "\tROG: ";
        switch (rog&0x07) {
            case 0: result += "Invalid ";break;
            case 1: result += "Green ";break;
            case 2: result += "Orange ";break;
            case 3: result += "Red ";break;
            default: result += "Reserved Value ";break;
        }
        int time = (rog & 0xFFF8) >>> 3;
        result += " since " + time + " seconds\n";
        result += "\tBattery Status: " + batteryStatus + "%";
        result += "\tBH isWorn: " + ((button_worn&0x80)==128?"Yes":"No") + "\n";
        result += "\tButton isPressed: " + ((button_worn&0x40)==64?"Yes":"No") + "\n";
        result += "\tHeartRate Signal: " + ((button_worn&0x20)==32?"Low":"Good ") + "\n";
        result += "\tExt. Sensor Connected: " + ((button_worn&0x10)==16?"Some":"None") + "\n";
        return result;

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

    public long getMilliseconds() {
        return milliseconds;
    }

    public float getHeartRate() {
        return heartRate;
    }

    public float getRespirationRate() {
        return respirationRate;
    }

    public float getSkinTemperature() {
        return skinTemperature;
    }

    public float getPosture() {
        return posture;
    }

    public float getVMU() {
        return VMU;
    }

    public float getPeakAcceleration() {
        return peakAcceleration;
    }

    public float getBatteryVoltage() {
        return batteryVoltage;
    }

    public float getBreathingWaveAmplitude() {
        return breathingWaveAmplitude;
    }

    public float getECGAmplitude() {
        return ECGAmplitude;
    }

    public float getECGNoise() {
        return ECGNoise;
    }

    public float getVerticalAxisAccelerationMin() {
        return verticalAxisAccelerationMin;
    }

    public float getVerticalAxisAccelerationPeak() {
        return verticalAxisAccelerationPeak;
    }

    public float getLateralAxisAccelerationMin() {
        return lateralAxisAccelerationMin;
    }

    public float getLateralAxisAccelerationPeak() {
        return lateralAxisAccelerationPeak;
    }

    public float getSagitalAxisAccelerationMin() {
        return sagitalAxisAccelerationMin;
    }

    public float getSagitalAxisAccelerationPeak() {
        return sagitalAxisAccelerationPeak;
    }

    public int getZephyrSystemChannel() {
        return zephyrSystemChannel;
    }

    public int getGsr() {
        return gsr;
    }

    public int getRog() {
        return rog;
    }

    public int getAlarm() {
        return alarm;
    }

    public int getBatteryStatus() {
        return batteryStatus;
    }

    public int getButton_worn() {
        return button_worn;
    }
}
