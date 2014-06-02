package lu.snt.iot.serval.rn12.bodySensors.cmp;

import lu.snt.iot.serval.rn12.bodySensors.frm.FallDetectionFrame;
import org.kevoree.annotation.*;
import org.kevoree.api.Port;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@ComponentType
@Library(name = "Serval_RN12")
public class BodySensors {

    private FallDetectionFrame frame;

    @Output(optional = true)
    public Port fall;

    public BodySensors() {
        frame = new FallDetectionFrame();
        frame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                fall.send(null);
            }
        });
    }
    
    @Start
    public void start() {
        frame.setVisible(true);
    }

    @Stop
    public void stop() {
        frame.setVisible(false);
    }

    @Update
    public void update() {
    }
}
