package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class GetBHUserConfigPacket extends StandardDataPacket {

    private boolean logEnabled;
    private boolean bluetoothEnabled;
    private boolean buttonEnabledWhenWorn;
    private boolean normalECGPolarity;
    private int loggingFormat;
    private boolean teamSystemEnabled;
    private boolean is802_15_4_Enabled;
    private boolean ledEnabled;
    private boolean audioEnabled;


    public boolean isLogEnabled() {
        return logEnabled;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothEnabled;
    }

    public boolean isButtonEnabledWhenWorn() {
        return buttonEnabledWhenWorn;
    }

    public boolean isNormalECGPolarity() {
        return normalECGPolarity;
    }

    public int getLoggingFormat() {
        return loggingFormat;
    }

    public boolean isTeamSystemEnabled() {
        return teamSystemEnabled;
    }

    public boolean isIs802_15_4_Enabled() {
        return is802_15_4_Enabled;
    }

    public boolean isLedEnabled() {
        return ledEnabled;
    }

    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    protected GetBHUserConfigPacket() {
        messageId = (byte)0xA7;
    }


    public static DataFrame parse(byte[] raw) {
        System.out.println("Parsing BT User Config");
        GetBHUserConfigPacket packet = new GetBHUserConfigPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        packet.logEnabled = ((raw[3] & 0xFF) == 1?true:false);
        packet.bluetoothEnabled = ((raw[4] & 0xFF) == 1?true:false);
        packet.buttonEnabledWhenWorn = ((raw[5] & 0xFF) == 1?true:false);
        packet.normalECGPolarity = ((raw[6] & 0xFF) == 1?false:true);
        packet.loggingFormat = (raw[6] & 0xFF);
        packet.teamSystemEnabled = ((raw[8] & 0xFF) == 1?true:false);
        packet.is802_15_4_Enabled = ((raw[9] & 0xFF) == 1?true:false);
        packet.ledEnabled = ((raw[10] & 0xFF) == 1?true:false);
        packet.audioEnabled = ((raw[11] & 0xFF) == 1?true:false);
        packet.ack_nack = raw[4+packet.payloadLength ];
        return packet;
    }

    public String toString() {
        return "BH User Config"+ (ack_nack== Consts.ACK ? "(ACK)":"(NACK)")
                +":\n\tLog: " + (logEnabled?"Enabled":"Disabled")
                + "\n\tLogging Format: " + loggingFormat
                + "\n\tBluetooth: " + (bluetoothEnabled?"Enabled":"Disabled")
                + "\n\tButton w. Worn: " + (buttonEnabledWhenWorn?"Enabled":"Disabled")
                + "\n\tECG Polarity: " + (normalECGPolarity?"Normal":"Reversed")
                + "\n\tTeam System: " + (teamSystemEnabled?"Enabled":"Disabled")
                + "\n\t802.15.4: " + (bluetoothEnabled?"Enabled":"Disabled")
                + "\n\tLED: " + (bluetoothEnabled?"Enabled":"Disabled")
                + "\n\tAudio: " + (bluetoothEnabled?"Enabled":"Disabled");
    }

}
