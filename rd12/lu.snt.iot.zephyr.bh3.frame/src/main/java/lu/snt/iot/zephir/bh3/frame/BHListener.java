package lu.snt.iot.zephir.bh3.frame;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 21/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.BH3PacketListener;
import lu.snt.iot.zephyr.bh3.api.DataFrame;
import lu.snt.iot.zephyr.bh3.api.periodic.BreathingWaveformDataPacket;
import lu.snt.iot.zephyr.bh3.api.periodic.ECGWaveformDataPacket;

public class BHListener implements BH3PacketListener {

    private Ploter ploter;

    public BHListener(Ploter ploter) {
        this.ploter = ploter;
    }

    public void packetReceived(byte[] packet) {
        System.out.println("Packet");
        DataFrame frame = DataFrame.parse(packet);
        if(frame instanceof ECGWaveformDataPacket) {
            ECGWaveformDataPacket ecgPacket = (ECGWaveformDataPacket)frame;
            ploter.updateECG(ecgPacket);
        } else if(frame instanceof BreathingWaveformDataPacket) {
            BreathingWaveformDataPacket ecgPacket = (BreathingWaveformDataPacket)frame;
            ploter.updateBreath(ecgPacket);
        }
    }
}
