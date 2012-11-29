/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.snt.iot.serval.rn12.doorlock;


import java.awt.BorderLayout;

import javax.swing.JFrame;

/**
 *
 * @author Assaad
 */
public class MainLock {
    
    public static final int D_WIDTH = 200, D_HEIGHT = 180;
    private static DoorClass dClass;

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {
        JFrame window = new JFrame("Door Lock");
        ViewMainLock viewLock = new ViewMainLock(dClass);
        window.add(viewLock, BorderLayout.CENTER);
        window.setSize(D_WIDTH, D_HEIGHT);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(window.getParent());
        window.setVisible(true);
    }

    public MainLock( DoorClass dc) {
        this.dClass=dc;
    }
}
