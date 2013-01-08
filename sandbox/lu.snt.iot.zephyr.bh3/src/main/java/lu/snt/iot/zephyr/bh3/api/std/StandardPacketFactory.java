package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 18/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import java.util.Calendar;
import java.util.Date;

public class StandardPacketFactory {


    public static SetGeneralDataPacket createEnablingGeneralDataRequest() {
        return new SetGeneralDataPacket(true);
    }

    public static SetGeneralDataPacket createDisablingGeneralDataRequest() {
        return new SetGeneralDataPacket(false);
    }

    public static SetAccelerometerTransmitStatePacket createEnablingAccelerometerDataTransmitionRequest() {
        return new SetAccelerometerTransmitStatePacket(true);
    }

    public static SetAccelerometerTransmitStatePacket createDisablingAccelerometerDataTransmitionRequest() {
        return new SetAccelerometerTransmitStatePacket(false);
    }

    public static SetBreathingWaveformTransmitStatePacket createEnablingBreathingWaveformDataTransmitionRequest() {
        return new SetBreathingWaveformTransmitStatePacket(true);
    }

    public static SetBreathingWaveformTransmitStatePacket createDisablingBreathingWaveformTransmitionRequest() {
        return new SetBreathingWaveformTransmitStatePacket(false);
    }

    public static SetECGWaveformTransmitStatePacket createEnablingECGWaveformTransmitionRequest() {
        return new SetECGWaveformTransmitStatePacket(true);
    }

    public static SetECGWaveformTransmitStatePacket createDisablingECGWaveformTransmitionRequest() {
        return new SetECGWaveformTransmitStatePacket(false);
    }

    public static SetR2RDataTransmitStatePacket createEnablingR2RDataTransmitionRequest() {
        return new SetR2RDataTransmitStatePacket(true);
    }

    public static SetR2RDataTransmitStatePacket createDisablingR2RDataTransmitionRequest() {
        return new SetR2RDataTransmitStatePacket(false);
    }

    public static GetRtcPacket createGetRtcRequest() {
        return new GetRtcPacket();
    }

    public static SetRtcPacket createSetRtcPacket(int day, int month, int year, int hour, int min, int sec) {
        return new SetRtcPacket(day,month,year,hour,min,sec);
    }

    public static SetNetworkIdPacket createSetNetworkIdPacket(String networkId) {
        return new SetNetworkIdPacket(networkId);
    }

    public static SetRtcPacket createSetRtcPacket() {
        return new SetRtcPacket(
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
                Calendar.getInstance().get(Calendar.MONTH)+1,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                Calendar.getInstance().get(Calendar.MINUTE),
                Calendar.getInstance().get(Calendar.SECOND));
    }


    public static GetUnitSerialNumberPacket createGetUnitSerialNumberRequest() {
        return new GetUnitSerialNumberPacket();
    }

    public static GetBootloaderVersionNumberPacket createGetBootloaderVersionNumberRequest() {
        return new GetBootloaderVersionNumberPacket();
    }

    public static GetFirmwareVersionNumberPacket createGetFirmwareVersionNumberRequest() {
        return new GetFirmwareVersionNumberPacket();
    }

    public static GetHardwarePartNumberPacket createGetHardwarePartNumberRequest() {
        return new GetHardwarePartNumberPacket();
    }

    public static GetBootloaderPartNumberPacket createGetBootloaderPartNumberRequest() {
        return new GetBootloaderPartNumberPacket();
    }

    public static GetApplicationPartNumberPacket createGetApplicationPartNumberRequest() {
        return new GetApplicationPartNumberPacket();
    }

    public static GetNetworkIdPacket createGetNetworkIdRequest() {
        return new GetNetworkIdPacket();
    }

    public static GetUnitMacAddressPacket createGetUnitMacAddressRequest() {
        return new GetUnitMacAddressPacket();
    }

    public static GetUnitBTFriendlyNamePacket createGetUnitBTFriendlyNameRequest() {
        return new GetUnitBTFriendlyNamePacket();
    }

    public static GetRogSettingsPacket createGetRogSettingRequest() {
        return new GetRogSettingsPacket();
    }

    public static GetBTUserConfigPacket createGetBTUserConfigRequest() {
        return new GetBTUserConfigPacket();
    }

    public static GetBTLinkConfigPacket createGetBTLinkConfigRequest() {
        return new GetBTLinkConfigPacket();
    }

    public static GetBHUserConfigPacket createGetBHUserConfigRequest() {
        return new GetBHUserConfigPacket();
    }

    public static GetBatteryStatusPacket createGetBatteryStatusRequest() {
        return new GetBatteryStatusPacket();
    }


}
