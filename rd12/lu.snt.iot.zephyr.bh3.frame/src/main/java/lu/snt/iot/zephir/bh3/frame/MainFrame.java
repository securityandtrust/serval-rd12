package lu.snt.iot.zephir.bh3.frame;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 21/12/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import lu.snt.iot.zephyr.bh3.api.ConnectionManager;
import lu.snt.iot.zephyr.bh3.api.std.StandardPacketFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    private ConnectionManager connection;
    private BHListener listener;

    public MainFrame() {

        Ploter ploter = new Ploter();

        listener = new BHListener(ploter);

        setLayout(new BorderLayout());
        JPanel buttonPanel = new JPanel();
        final JButton connect = new JButton("Connect");
        final JToggleButton heart = new JToggleButton("Activate Heart");
        heart.setEnabled(false);
        final JToggleButton breath = new JToggleButton("Activate Breath");
        breath.setEnabled(false);


        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(connect.getText().equals("Connect")) {
                    if(connection==null) {

                        //connection = new ConnectionManager("/dev/tty.BHBHT004887-iSerialPort1");
                        connection = new ConnectionManager("/dev/tty.BHBHT004768-iSerialPort1");
                        connection.addPacketListener(listener);
                    }
                    connection.connect();
                    heart.setEnabled(true);
                    breath.setEnabled(true);
                    connect.setText("Disconnect");
                } else {
                    heart.setEnabled(false);
                    breath.setEnabled(false);
                    connection.close();
                    connect.setText("Connect");
                }
            }
        });
        buttonPanel.add(connect);


        heart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(heart.getText().equals("Activate Heart")) {
                    connection.send(StandardPacketFactory.createEnablingECGWaveformTransmitionRequest());
                    heart.setText("Deactivate Heart");
                } else {
                    connection.send(StandardPacketFactory.createDisablingECGWaveformTransmitionRequest());
                    heart.setText("Activate Heart");
                }
            }
        });
        buttonPanel.add(heart);


        breath.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(breath.getText().equals("Activate Breath")) {
                    connection.send(StandardPacketFactory.createEnablingBreathingWaveformDataTransmitionRequest());
                    breath.setText("Deactivate Breath");
                } else {
                    connection.send(StandardPacketFactory.createDisablingBreathingWaveformTransmitionRequest());
                    breath.setText("Activate Breath");
                }
            }
        });
        buttonPanel.add(breath);

        add(buttonPanel, BorderLayout.NORTH);
        add(ploter, BorderLayout.CENTER);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }




    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
    }


}
