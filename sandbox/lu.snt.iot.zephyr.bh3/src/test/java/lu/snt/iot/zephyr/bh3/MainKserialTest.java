package lu.snt.iot.zephyr.bh3;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 17/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.BH3PacketListener;
import lu.snt.iot.zephyr.bh3.api.ConnectionManager;
import lu.snt.iot.zephyr.bh3.api.Consts;
import lu.snt.iot.zephyr.bh3.api.DataFrame;
import lu.snt.iot.zephyr.bh3.api.periodic.BreathingWaveformDataPacket;
import lu.snt.iot.zephyr.bh3.api.periodic.ECGWaveformDataPacket;
import lu.snt.iot.zephyr.bh3.api.periodic.GeneralDataPacket;
import lu.snt.iot.zephyr.bh3.api.std.*;
import lu.snt.iot.zephyr.bh3.lst.SetNetworkIdListener;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class MainKserialTest {

    private static ConnectionManager connection;
    private static Semaphore sema = new Semaphore(0);


    @BeforeClass
    public static void initConnection() {
        //connection = new ConnectionManager("/dev/tty.BHBHT004768-iSerialPort1");
        connection = new ConnectionManager("/dev/tty.BHBHT004887-iSerialPort1");
        connection.connect();
    }

    @AfterClass
    public static void shutdownConnection() {
        connection.close();
    }


    @Test
    public void getUnitSerialNumberTest() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {

                    DataFrame frame = DataFrame.parse(packet);

                    if(frame instanceof GetUnitSerialNumberPacket && frame.getAckNackValue() == Consts.ACK) {
                        assertNotNull(((GetUnitSerialNumberPacket)frame).getSerialNumber());
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetUnitSerialNumberRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Test
    public void getBatteryStatusTest() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {

                    DataFrame frame = DataFrame.parse(packet);

                    if(frame instanceof GetBatteryStatusPacket && frame.getAckNackValue() == Consts.ACK) {
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetBatteryStatusRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Test
    public void getHardwarePartNumber() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {

                    DataFrame frame = DataFrame.parse(packet);

                    if(frame instanceof GetHardwarePartNumberPacket && frame.getAckNackValue() == Consts.ACK) {
                        assertNotNull(((GetHardwarePartNumberPacket)frame).getPartNumber());
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetHardwarePartNumberRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Test
    public void getBootloaderPartNumber() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {

                    DataFrame frame = DataFrame.parse(packet);

                    if(frame instanceof GetBootloaderPartNumberPacket && frame.getAckNackValue() == Consts.ACK) {
                        assertNotNull(((GetBootloaderPartNumberPacket)frame).getPartNumber());
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetBootloaderPartNumberRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Test
    public void getApplicationPartNumber() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {

                    DataFrame frame = DataFrame.parse(packet);

                    if(frame instanceof GetApplicationPartNumberPacket && frame.getAckNackValue() == Consts.ACK) {
                        assertNotNull(((GetApplicationPartNumberPacket)frame).getPartNumber());
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetApplicationPartNumberRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Test
    public void getNetworkIdNumber() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {

                    DataFrame frame = DataFrame.parse(packet);

                    if(frame instanceof GetNetworkIdPacket && frame.getAckNackValue() == Consts.ACK) {
                        assertNotNull(((GetNetworkIdPacket)frame).getId());
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetNetworkIdRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Test
    public void setNetworkIdNumber() {
        try {
            SetNetworkIdListener listener = new SetNetworkIdListener(sema);

            //Get default ID
            connection.addPacketListener(listener);
            DataFrame getDefaultNetworkId = StandardPacketFactory.createGetNetworkIdRequest();
            connection.send(getDefaultNetworkId);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

            //Sets with new ID
            DataFrame setNetworkId = StandardPacketFactory.createSetNetworkIdPacket("BH SnT-CoPAInS");
            connection.send(setNetworkId);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

            //Check the new ID
            DataFrame getNetworkId = StandardPacketFactory.createGetNetworkIdRequest();
            connection.send(getNetworkId);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

            //Restore default
            setNetworkId = StandardPacketFactory.createSetNetworkIdPacket(listener.defaultId);
            connection.send(setNetworkId);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

            connection.removePacketListener(listener);


        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }



    @Test
    public void getUnitMacAddress() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame instanceof GetUnitMacAddressPacket && frame.getAckNackValue() == Consts.ACK) {
                        assertNotNull(((GetUnitMacAddressPacket)frame).getMacAddress());
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetUnitMacAddressRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Test
    public void getUnitBTFriendlyName() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame instanceof GetUnitBTFriendlyNamePacket && frame.getAckNackValue() == Consts.ACK) {
                        assertNotNull(((GetUnitBTFriendlyNamePacket)frame).getFriendlyName());
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetUnitBTFriendlyNameRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Test
    public void getRogSettings() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame instanceof GetRogSettingsPacket && frame.getAckNackValue() == Consts.ACK) {
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetRogSettingRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(2, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    @Test
    public void getBTUserConfig() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame instanceof GetBTUserConfigPacket && frame.getAckNackValue() == Consts.ACK) {
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetBTUserConfigRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(2, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Test
    public void getBTLinkConfig() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame instanceof GetBTLinkConfigPacket && frame.getAckNackValue() == Consts.ACK) {
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetBTLinkConfigRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(2, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Test
    public void getBHUserConfig() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame instanceof GetBHUserConfigPacket && frame.getAckNackValue() == Consts.ACK) {
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetBHUserConfigRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(2, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }



    @Test
    public void setRtcTest() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame instanceof SetRtcPacket && frame.getAckNackValue() == Consts.ACK) {
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();

                    } else {
                        System.out.println("received:" + frame.getClass().getSimpleName());
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createSetRtcPacket();
            connection.send(getRTC);

            if(!sema.tryAcquire(10, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        }  catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }





    @Test
    public void getRtcTest() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame.getAckNackValue() == Consts.NACK) {
                        System.out.println("Got a NackValue");
                    }
                    if(frame instanceof GetRtcPacket) {
                        GetRtcPacket packet1 = (GetRtcPacket)frame;
                        assertTrue(packet1.getDay() > 0 && packet1.getDay() < 32);
                        assertTrue(packet1.getMonth() > 0 && packet1.getMonth() < 13);
                        assertTrue("Year:" + packet1.getYear(),packet1.getYear() > 1999 && packet1.getYear() < 2100);
                        assertTrue(packet1.getHour() >= 0 && packet1.getHour() < 24);
                        assertTrue(packet1.getMinutes() >= 0 && packet1.getMinutes() < 60);
                        assertTrue(packet1.getSeconds() >= 0 && packet1.getSeconds() < 60);

                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetRtcRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(10, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }



    @Test
    public void setGeneralDataPacketTest() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame instanceof SetGeneralDataPacket && frame.getAckNackValue() == Consts.ACK) {
                        System.out.println(frame.toString());
                        sema.release();
                    } else if(frame instanceof GeneralDataPacket) {
                        System.out.println(frame.toString());
                        sema.release();
                    } else {
                        System.out.println("received:" + frame.getClass().getSimpleName());
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createEnablingGeneralDataRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(4, 30, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

            getRTC = StandardPacketFactory.createDisablingGeneralDataRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(10, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

            connection.removePacketListener(listener);

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Test
    public void setGBreathingWaveformDataTransmitionPacketTest() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame instanceof SetBreathingWaveformTransmitStatePacket && frame.getAckNackValue() == Consts.ACK) {
                        System.out.println(frame.toString());
                        sema.release();
                    } else if(frame instanceof BreathingWaveformDataPacket) {
                        System.out.println(frame.toString());
                        sema.release();
                    } else {
                        System.out.println("received:" + frame.getClass().getSimpleName());
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createEnablingBreathingWaveformDataTransmitionRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(4, 30, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

            getRTC = StandardPacketFactory.createDisablingBreathingWaveformTransmitionRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(10, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

            connection.removePacketListener(listener);

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }


    @Test
    public void setECGWaveformDataTransmitionPacketTest() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame instanceof SetECGWaveformTransmitStatePacket && frame.getAckNackValue() == Consts.ACK) {
                        System.out.println(frame.toString());
                        sema.release();
                    } else if(frame instanceof ECGWaveformDataPacket) {
                        System.out.println(frame.toString());
                        sema.release();
                    } else {
                        System.out.println("received:" + frame.getClass().getSimpleName());
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createEnablingECGWaveformTransmitionRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(4, 30, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

            getRTC = StandardPacketFactory.createDisablingECGWaveformTransmitionRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(10, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

            connection.removePacketListener(listener);

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }



    @Test
    public void getBootloaderVersionNumberTest() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame.getAckNackValue() == Consts.NACK) {
                        System.out.println("Got a NackValue");
                    }
                    if(frame instanceof GetBootloaderVersionNumberPacket) {
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetBootloaderVersionNumberRequest();
            connection.send(getRTC);


            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Test
    public void getFirmwareVersionNumberTest() {
        try {
            BH3PacketListener listener = new BH3PacketListener() {
                @Override
                public void packetReceived(byte[] packet) {
                    DataFrame frame = DataFrame.parse(packet);
                    if(frame.getAckNackValue() == Consts.NACK) {
                        System.out.println("Got a NackValue");
                    }
                    if(frame instanceof GetFirmwareVersionNumberPacket) {
                        System.out.println(frame.toString());
                        connection.removePacketListener(this);
                        sema.release();
                    }
                }
            };

            connection.addPacketListener(listener);
            DataFrame getRTC = StandardPacketFactory.createGetFirmwareVersionNumberRequest();
            connection.send(getRTC);

            if(!sema.tryAcquire(1, TimeUnit.SECONDS)){
                fail("Did not receive the good message in time.");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

}
