package lu.snt.iot.serval.rn12.doorlock.frm;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 30/11/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import javax.swing.*;
import java.awt.*;

public class DoorView extends JPanel {


    public DoorView() {
      doorClosed();
    }


    public void doorOpened() {
        setBackground(Color.GREEN);
        getGraphics().setColor(Color.WHITE);
        getGraphics().drawString("OPEN",0,0);
        setPreferredSize(new Dimension(50,50));
    }

    public void doorClosed() {
        setBackground(Color.RED);
        getGraphics().setColor(Color.WHITE);
        getGraphics().drawString("CLOSED",0,0);
        setPreferredSize(new Dimension(50,50));
    }


}
