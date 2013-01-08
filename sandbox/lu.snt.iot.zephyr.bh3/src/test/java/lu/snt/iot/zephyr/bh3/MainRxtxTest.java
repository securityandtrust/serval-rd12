//package lu.snt.iot.zephyr.bh3;
/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/
/*
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import lu.snt.iot.zephyr.bh3.api.DataFrame;
import lu.snt.iot.zephyr.bh3.api.periodic.LifeSign;
import lu.snt.iot.zephyr.bh3.api.std.GetRtcPacket;
import lu.snt.iot.zephyr.bh3.api.std.GetUnitSerialNumberPacket;
import lu.snt.iot.zephyr.bh3.api.std.SetGeneralDataPacket;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class MainRxtxTest {


    public static void main(String[] args) {
        try {
            /*SerialPort serial = new SerialPort("/dev/tty.BHBHT004768-iSerialPort1",115200);
            serial.addEventListener(new SerialPortEventListener() {
                @Override
                public void incomingDataEvent(SerialPortEvent evt) {
                    byte[] rawMsg = evt.read();
                    System.out.println(Arrays.toString(rawMsg));
                    System.out.println(DataFrame.parse(rawMsg).toString());

                }

                @Override
                public void disconnectionEvent(SerialPortDisconnectionEvent evt) {
                    System.out.println("Disconnected");
                }

                @Override
                public void concurrentOpenEvent(SerialConcurrentOpenEvent evt) {
                    //To change body of implemented methods use File | Settings | File Templates.
                }
            });

            CommPortIdentifier id = CommPortIdentifier.getPortIdentifier("/dev/tty.BHBHT004768-iSerialPort1");

            SerialPort connection = (SerialPort) id.open("X2dExampleProgram", 2000);
            connection.setSerialPortParams(115200, SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            connection.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);



            InputStream is = connection.getInputStream();
            OutputStream serial = connection.getOutputStream();




            if(is.available()>0) {
                byte[] rawMsg = new byte[255];
                is.read(rawMsg);
                System.out.println(Arrays.toString(rawMsg));
                System.out.println(DataFrame.parse(rawMsg).toString());
            }

            DataFrame getRTC = GetRtcPacket.createGetRtcRequest();
            System.out.println("Sending: " + Arrays.toString(getRTC.toByteArray()));
            serial.write(getRTC.toByteArray());
            serial.write((byte)'\n');
            serial.flush();
            Thread.sleep(1 * 1000);
            if(is.available()>0) {
                byte[] rawMsg = new byte[255];
                is.read(rawMsg);
                System.out.println(Arrays.toString(rawMsg));
                System.out.println(DataFrame.parse(rawMsg).toString());
            }
            DataFrame setGeneralDataPacket = SetGeneralDataPacket.createEnablingGeneralDataRequest();
            System.out.println("Sending: " + Arrays.toString(setGeneralDataPacket.toByteArray()));
            serial.write(setGeneralDataPacket.toByteArray());
            serial.flush();
            Thread.sleep(1 * 1000);
            if(is.available()>0) {
                byte[] rawMsg = new byte[255];
                is.read(rawMsg);
                System.out.println(Arrays.toString(rawMsg));
                System.out.println(DataFrame.parse(rawMsg).toString());
            }
            DataFrame frame = GetUnitSerialNumberPacket.createSerialNumberRequest();
            System.out.println("Sending: " + Arrays.toString(frame.toByteArray()));
            serial.write(frame.toByteArray());
            serial.flush();
            Thread.sleep(6 * 1000);
            if(is.available()>0) {
                byte[] rawMsg = new byte[255];
                is.read(rawMsg);
                System.out.println(Arrays.toString(rawMsg));
                System.out.println(DataFrame.parse(rawMsg).toString());
            }
            setGeneralDataPacket = SetGeneralDataPacket.createDisablingGeneralDataRequest();
            System.out.println("Sending: " + Arrays.toString(setGeneralDataPacket.toByteArray()));
            serial.write(setGeneralDataPacket.toByteArray());
            serial.flush();
            if(is.available()>0) {
                byte[] rawMsg = new byte[255];
                is.read(rawMsg);
                System.out.println(Arrays.toString(rawMsg));
                System.out.println(DataFrame.parse(rawMsg).toString());
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }




    }

}
*/