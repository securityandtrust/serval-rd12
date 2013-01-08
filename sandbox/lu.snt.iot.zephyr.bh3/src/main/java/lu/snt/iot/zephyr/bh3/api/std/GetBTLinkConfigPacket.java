package lu.snt.iot.zephyr.bh3.api.std;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;

public class GetBTLinkConfigPacket extends StandardDataPacket {

    private long linkTimeout;
    private long lifesign_period;

    public long getLinkTimeout() {
        return linkTimeout;
    }

    public long getLifesignPeriod() {
        return lifesign_period;
    }

    protected GetBTLinkConfigPacket() {
        messageId = (byte)0xA5;
    }


    public static DataFrame parse(byte[] raw) {
        System.out.println("Parsing BT User Config");
        GetBTLinkConfigPacket packet = new GetBTLinkConfigPacket();
        packet.rawFrame = raw;
        packet.payloadLength = (int)raw[2];
        packet.linkTimeout = (raw[3] & 0xFF) + (256*(raw[4] & 0xFF));
        packet.linkTimeout = (raw[5] & 0xFF) + (256*(raw[6] & 0xFF));
        packet.ack_nack = raw[4+packet.payloadLength ];
        return packet;
    }

    public String toString() {
        return "BT User Config"+ (ack_nack== Consts.ACK ? "(ACK)":"(NACK)")
                +":\n\tLink Timeout: " + (linkTimeout==0?"None":linkTimeout + "ms")
                + "\n\tLifesign Period: " + (lifesign_period==0?"Never":lifesign_period + "ms");
    }

}
