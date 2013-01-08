package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.DataFrame;

public abstract class StandardDataPacket extends DataFrame {

    protected StandardDataPacket() {
        super();
    }

    public static DataFrame parse(byte[] raw) {
        switch (raw[1]) {
            case 0x07 : return SetRtcPacket.parse(raw);
            case 0x08 : return GetRtcPacket.parse(raw);
            case 0x09 : return GetBootloaderVersionNumberPacket.parse(raw);
            case 0x0A : return GetFirmwareVersionNumberPacket.parse(raw);
            case 0x0B : return GetUnitSerialNumberPacket.parse(raw);
            case 0x0C : return GetHardwarePartNumberPacket.parse(raw);
            case 0x0D : return GetBootloaderPartNumberPacket.parse(raw);
            case 0x0E : return GetApplicationPartNumberPacket.parse(raw);
            case 0x10 : return SetNetworkIdPacket.parse(raw);
            case 0x11 : return GetNetworkIdPacket.parse(raw);
            case 0x12 : return GetUnitMacAddressPacket.parse(raw);
            case 0x14 : return SetGeneralDataPacket.parse(raw);
            case 0x15 : return SetBreathingWaveformTransmitStatePacket.parse(raw);
            case 0x16 : return SetECGWaveformTransmitStatePacket.parse(raw);
            case 0x17 : return GetUnitBTFriendlyNamePacket.parse(raw);
            case 0x19 : return SetR2RDataTransmitStatePacket.parse(raw);
            case 0x1E : return SetAccelerometerTransmitStatePacket.parse(raw);
            case (byte)0x9C : return GetRogSettingsPacket.parse(raw);
            case (byte)0xA3 : return GetBTUserConfigPacket.parse(raw);
            case (byte)0xA5 : return GetBTLinkConfigPacket.parse(raw);
            case (byte)0xA7 : return GetBHUserConfigPacket.parse(raw);
            case (byte)0xAC : return GetBatteryStatusPacket.parse(raw);
            default: {
                System.out.println("StandardDataPacket: Unkown messageId: " + raw[1]);
                return null;
            }
        }
    }
}
