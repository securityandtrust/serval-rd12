package lu.snt.iot.zephyr.bh3.lst;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 21/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.BH3PacketListener;
import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;
import lu.snt.iot.zephyr.bh3.api.std.GetNetworkIdPacket;
import lu.snt.iot.zephyr.bh3.api.std.SetNetworkIdPacket;

import java.util.concurrent.Semaphore;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SetNetworkIdListener implements BH3PacketListener {

    private final short GET_DEFAULT = 0;
    private final short SET_NEW = 1;
    private final short GET_NEW = 2;
    private final short SET_DEFAULT = 3;
    private short phase = GET_DEFAULT;

    public String defaultId;
    private Semaphore sema;

    public SetNetworkIdListener(Semaphore sema) {
        this.sema = sema;
    }

    @Override
    public void packetReceived(byte[] packet) {

        DataFrame frame = DataFrame.parse(packet);

        switch (phase) {
            case GET_DEFAULT: {
                if(frame instanceof GetNetworkIdPacket && frame.getAckNackValue() == Consts.ACK) {
                    assertNotNull(((GetNetworkIdPacket)frame).getId());
                    defaultId = ((GetNetworkIdPacket)frame).getId();
                    System.out.println(frame.toString());
                    phase = SET_NEW;
                    sema.release();
                }
            }break;
            case SET_NEW: {
                if(frame instanceof SetNetworkIdPacket && frame.getAckNackValue() == Consts.ACK) {
                    assertTrue(frame.getAckNackValue() == Consts.ACK);
                    System.out.println(frame.toString());
                    phase = GET_NEW;
                    sema.release();
                }
            }break;
            case GET_NEW: {

                if(frame instanceof GetNetworkIdPacket && frame.getAckNackValue() == Consts.ACK) {
                    assertTrue(((GetNetworkIdPacket)frame).getId().equals("BH SnT-CoPAInS"));
                    System.out.println(frame.toString());
                    phase = SET_DEFAULT;
                    sema.release();
                }

            }break;
            case SET_DEFAULT: {
                if(frame instanceof SetNetworkIdPacket && frame.getAckNackValue() == Consts.ACK) {
                    assertTrue(frame.getAckNackValue() == Consts.ACK);
                    System.out.println(frame.toString());
                    sema.release();
                }
            }break;
        }
    }
}
