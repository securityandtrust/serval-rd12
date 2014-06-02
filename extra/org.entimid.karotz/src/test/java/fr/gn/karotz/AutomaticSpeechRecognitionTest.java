/*
 * Copyright (c) 2011. Gregory Nain.
 */

package fr.gn.karotz;

import fr.gn.karotz.msg.InteractiveModeMessage;
import fr.gn.karotz.msg.ResponseMessage;
import fr.gn.karotz.session.InteractiveAction;
import fr.gn.karotz.session.InteractiveCommand;
import fr.gn.karotz.utils.KarotzCommand;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Created by IntelliJ IDEA.
 * User: gnain
 * Date: 21/05/11
 * Time: 11:04
 * To change this template use File | Settings | File Templates.
 */
public class AutomaticSpeechRecognitionTest {

    private static Karotz jp;

    @BeforeClass
    public static void setUp() {

        String sk = "87683e1e-c478-4c2a-b46e-f22bd6aca916";
        String ak = "ae5e7b12-bea8-4c5a-80d5-cd88576a95e5";
        String iid = "2b96ef18-b795-4b62-b528-29019c0e84d7";



        jp = new Karotz(ak, sk, iid);

      jp.initSession();
        if(!jp.isSessionActive()) {
            fail("Session not active.");
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                ServerSocket socket = new ServerSocket(3546);
                    System.out.println("Server started");
                    Socket client = socket.accept();
                    System.out.println("Client Conneced");
                    BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    String line;
                    while((line = br.readLine()) != null) {
                        System.out.println(line);
                    }


                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }


            }
        }).start();


    }

    @Test
    public void ttsTest() {

        try {
            if(!jp.isSessionActive()) {
                fail("Session not active.");
            }
            jp.send(new KarotzCommand() {
                @Override
                public String getCommand() throws UnknownHostException {
                    return "http://api.karotz.com/api/karotz/asr?grammar=yes,no&url=http://"+ InetAddress.getLocalHost().getHostAddress()+":3546/&interactiveid=" + jp.getKernel().getInteractiveId();
                }
            });



            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();  
        }

    }



    @AfterClass
    public static void tearDown() {
        jp.closeSession();
    }


}
