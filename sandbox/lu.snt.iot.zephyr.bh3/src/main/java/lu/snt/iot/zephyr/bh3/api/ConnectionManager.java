package lu.snt.iot.zephyr.bh3.api;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 20/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.periodic.PeriodicPacketFactory;
import org.kevoree.extra.kserial.SerialPort.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.*;

public class ConnectionManager implements SerialPortEventListener {


    private final short WAITING_PACKET = 0;
    private final short WAITING_ID = 1;
    private final short WAITING_PAYLOADLENGTH = 2;
    private final short READING_PAYLOAD = 3;
    private final short WAITING_CRC= 4;
    private final short WAITING_END_OF_PACKET= 5;
    private final short INIT= 6;
    private short readingPhase = INIT;
    private int currentPayloadLength = -1;
    private int currentPayloadRead = 0;


    private String port;
    private ScheduledExecutorService ping;
    private ExecutorService fireThread = Executors.newSingleThreadExecutor();
    private ExecutorService queueExecutor = Executors.newSingleThreadExecutor();
    private SerialPort serial;
    private ArrayList<BH3PacketListener> listeners = new ArrayList<BH3PacketListener>();

    private byte[] byteBuffer;
    private BlockingQueue<Byte> readBytesQueue = new ArrayBlockingQueue<Byte>(500);


    public ConnectionManager(String port) {
        this.port = port;
    }

    public boolean connect() {
        try {
            serial = new SerialPort(port,115200);
            serial.addEventListener(this);
            serial.open();


            ping = Executors.newSingleThreadScheduledExecutor();
            ping.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    send(PeriodicPacketFactory.createLifesignPacket());
                }
            }, 0, 900, TimeUnit.MILLISECONDS);

            queueExecutor = Executors.newSingleThreadExecutor();
            queueExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        while(true) {
                            byte b = readBytesQueue.take();

                            switch (readingPhase) {
                                case INIT: {
                                    if(b == Consts.ETX) {
                                        readingPhase = WAITING_PACKET;
                                    }
                                }break;
                                case WAITING_PACKET: {
                                    if(b == Consts.STX) {
                                        byteBuffer = new byte[270];
                                        byteBuffer[0] = b;
                                        readingPhase = WAITING_ID;
                                    }
                                }break;
                                case WAITING_ID: {
                                    byteBuffer[1] = b;
                                  //  System.out.println("Receiving message id:" + Integer.toHexString(b&0xFF));
                                    readingPhase = WAITING_PAYLOADLENGTH;
                                }break;
                                case WAITING_PAYLOADLENGTH: {
                                    byteBuffer[2] = (byte)(b&0xFF);
                                    currentPayloadLength = (b & 0xFF);
                                    // System.out.println("Receiving message payload:" +(b&0xFF));
                                    currentPayloadRead = 0;
                                    if(currentPayloadLength == 0){
                                        readingPhase = WAITING_CRC;
                                    } else {
                                        readingPhase = READING_PAYLOAD;
                                    }

                                }break;
                                case READING_PAYLOAD : {
                                    byteBuffer[3+currentPayloadRead] = b;
                                    currentPayloadRead++;

                                   // System.out.println("Receiving message red:" + currentPayloadRead + "/" + currentPayloadLength + "("+Integer.toHexString(b&0xFF)+")");
                                    if(currentPayloadRead == currentPayloadLength) {
                                        readingPhase = WAITING_CRC;
                                    }
                                }break;
                                case WAITING_CRC: {
                                    if((b&0xFF)==Consts.ETX || (b&0xFF)==Consts.ACK || (b&0xFF)==Consts.NACK){
                                        //System.out.println("Receiving message NO_CRC:" + Integer.toHexString(b&0xFF));
                                        byteBuffer[3+currentPayloadLength] = 0;
                                        byteBuffer[4+currentPayloadLength] = b;
                                       // System.out.println("Receiving message END:" + Integer.toHexString(b&0xFF));
                                        assert((b&0xFF)==Consts.ETX || (b&0xFF)==Consts.ACK || (b&0xFF)==Consts.NACK) : "B=0x" + Integer.toHexString((b&0xFF));
                                        fireNewPacket(Arrays.copyOfRange(byteBuffer, 0, 5 + currentPayloadLength));
                                        readingPhase = WAITING_PACKET;
                                    } else {
                                        byteBuffer[3+currentPayloadLength] = b;
                                        //System.out.println("Receiving message CRC:" + Integer.toHexString(b&0xFF));
                                        readingPhase = WAITING_END_OF_PACKET;
                                    }
                                }break;
                                case WAITING_END_OF_PACKET: {
                                    byteBuffer[4+currentPayloadLength] = b;
                                    //System.out.println("Receiving message END:" + Integer.toHexString(b&0xFF));
                                    assert((b&0xFF)==Consts.ETX || (b&0xFF)==Consts.ACK || (b&0xFF)==Consts.NACK) : "B=0x" + Integer.toHexString((b&0xFF));
                                    fireNewPacket(Arrays.copyOfRange(byteBuffer, 0, 5 + currentPayloadLength));
                                    readingPhase = WAITING_PACKET;
                                }break;
                            }
                        }
                    } catch (InterruptedException e) {
                    }
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
        ping.shutdown();
        queueExecutor.shutdownNow();
        serial.exit();
        serial = null;
    }

    @Override
    public void incomingDataEvent(SerialPortEvent evt) {
        byte[] tmp = evt.read();
        for(int i = 0 ; i < tmp.length ; i++) {
            try {
                readBytesQueue.put(tmp[i]);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }

    @Override
    public void disconnectionEvent(SerialPortDisconnectionEvent evt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void concurrentOpenEvent(SerialConcurrentOpenEvent evt) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    private void fireNewPacket(final byte[] packet) {
        fireThread.submit(new Runnable() {
            public void run() {
                for(BH3PacketListener listener : listeners) {
                    listener.packetReceived(packet);
                }
            }
        });
    }

    public void addPacketListener(BH3PacketListener lst) {
        listeners.add(lst);
    }

    public void removePacketListener(BH3PacketListener lst) {
        listeners.remove(lst);
    }

    public void send(DataFrame frame) {
        try {
            // System.out.println("Sending: " + Arrays.toString(frame.toByteArray()));
            serial.writeBytes(frame.toByteArray());
            serial.flush();
        } catch (SerialPortException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


}
