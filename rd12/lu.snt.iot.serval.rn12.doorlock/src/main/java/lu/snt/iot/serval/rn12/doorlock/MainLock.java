/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.snt.iot.serval.rn12.doorlock;


import lu.snt.iot.serval.rn12.doorlock.core.DoorClass;
import lu.snt.iot.serval.rn12.doorlock.frm.ViewMainLock;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

/**
 *
 * @author Assaad
 */
public class MainLock {
    
    public final int D_WIDTH = 200, D_HEIGHT = 180;
    private DoorClass dClass;

    private JFrame window;
    private ViewMainLock viewLock;


    public MainLock( DoorClass dc) {
        this.dClass=dc;
        window = new JFrame("Door Lock");
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        viewLock = new ViewMainLock(dClass);
        window.add(viewLock, BorderLayout.CENTER);
        window.setSize(D_WIDTH, D_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(window.getParent());
        window.pack();
    }

    public void setVisible(boolean visible) {
        window.setVisible(visible);
    }

    public void addLockListener(ActionListener listener) {
        viewLock.addLockListener(listener);
    }

}
