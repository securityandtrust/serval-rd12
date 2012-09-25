/*
 * Copyright (c) 2011. Gregory Nain.
 */

package fr.gn.karotz.session;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 22/05/11
 * Time: 14:36
 */
public class PictureReceiver extends Thread implements Runnable {


    private static final String HTML_START = "<html><title>HTTP POST Server in java</title><body>";
    private static final String HTML_END = "</body></html>";


    private int port;
    private File lastDownloaded;


    public PictureReceiver(int port) {
        this.port = port;
    }

    public void run() {

        try {
            ServerSocket Server = new ServerSocket(port);
            System.out.println("PictureReceiver ready on port " + port + " at " + InetAddress.getLocalHost().getHostAddress());

            Socket connected = Server.accept();
            InputStream is = connected.getInputStream();
            DataInputStream dis = new DataInputStream(is);

            //Created the file
            lastDownloaded = new File("snapshot_" + System.currentTimeMillis() + ".jpg");
            DataOutputStream dout = new DataOutputStream(new FileOutputStream(lastDownloaded));


            //Searching for JPG start bytes
            int c = dis.read();
            int d = dis.read();
            while ((c != 0xFF && d != 0xD8)) {
                c = d;
                d = dis.read();
            }

            //Writing start bytes to the file
            dout.write(c);
            dout.write(d);

            //Writing all bytes to the file until JPG stop bytes are found.
            do {
                c = d;
                d = dis.read();
                dout.write(d);

            } while ((c != 0xFF || d != 0xD9));


            dout.flush();
            dout.close();
            sendAnswer(connected.getOutputStream(), 200, "File Uploaded..");

        } catch (Exception ex) {
            ex.printStackTrace();
            //sendAnswer(connected.getOutputStream(), 500, "Internal ERROR");
        }
    }


    private void sendAnswer(OutputStream out, int statusCode, String responseString) {


        String statusLine;
        String serverdetails = "Server: Java HTTPServer";
        String contentLengthLine;
        String contentTypeLine = "Content-Type: text/html" + "\r\n";
        PrintWriter pr = new PrintWriter(out);

        if (statusCode == 200)
            statusLine = "HTTP/1.1 200 OK" + "\r\n";
        else
            statusLine = "HTTP/1.1 404 Not Found" + "\r\n";

        responseString = HTML_START + responseString + HTML_END;
        contentLengthLine = "Content-Length: " + responseString.length() + "\r\n";

        pr.println(statusLine);
        pr.println(serverdetails);
        pr.println(contentTypeLine);
        pr.println(contentLengthLine);
        pr.println("Connection: close\r\n");
        pr.println("\r\n");
        pr.flush();
        pr.close();
    }

    public File getLastDownloaded() {
        return lastDownloaded;
    }
}

