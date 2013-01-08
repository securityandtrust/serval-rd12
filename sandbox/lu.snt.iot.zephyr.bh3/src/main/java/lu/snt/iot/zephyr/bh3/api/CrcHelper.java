package lu.snt.iot.zephyr.bh3.api;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 18/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

public abstract class CrcHelper {

    public static int getCrc8(byte[] buffer, int start, int end) {
        int[] crc = { 0x0 };

        for (int i = start; i < (end+1); i++) {
            crc8PushByte(crc, buffer[i]);
        }
        return crc[0];
    }


    private static void crc8PushByte(int[] crc, byte add) {
        int addInt = (add & 0x000000FF);
        crc[0] = crc[0] ^ addInt;
        for (int i = 0; i < 8; i++) {
            if ((crc[0] & 0x00000001) != 0x00000000) {
                crc[0] = (crc[0] >> 1) ^ 0x0000008C;
            } else {
                crc[0] = (crc[0] >> 1);
            }
        }
    }



}
