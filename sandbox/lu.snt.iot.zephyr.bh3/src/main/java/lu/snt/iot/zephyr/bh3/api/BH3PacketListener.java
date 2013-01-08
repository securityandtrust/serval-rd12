package lu.snt.iot.zephyr.bh3.api;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 20/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

public interface BH3PacketListener {

    void packetReceived(byte[] packet);
}
