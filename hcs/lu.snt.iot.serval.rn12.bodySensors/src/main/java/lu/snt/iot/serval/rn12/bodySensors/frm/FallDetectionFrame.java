package lu.snt.iot.serval.rn12.bodySensors.frm;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: gregory.nain
 * Date: 19/03/12
 * Time: 08:53
 * To change this template use File | Settings | File Templates.
 */
public class FallDetectionFrame {
    
    private JFrame mainFrame;
    private JButton alertButton;
    
    public FallDetectionFrame() {
        mainFrame = new JFrame("BodySensor :: Fall Detector");
        alertButton = new JButton("Trigger Fall Detection");
        
        mainFrame.getContentPane().add(alertButton);
        
        mainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }
    
    public void addActionListener(ActionListener lst) {
        alertButton.addActionListener(lst); 
    }

    public void setVisible(boolean visible) {
        mainFrame.setVisible(visible);
    }

}
