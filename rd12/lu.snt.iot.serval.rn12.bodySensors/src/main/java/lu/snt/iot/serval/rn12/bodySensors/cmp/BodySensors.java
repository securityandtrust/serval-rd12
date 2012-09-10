package lu.snt.iot.serval.rn12.bodySensors.cmp;

import lu.snt.iot.serval.rn12.bodySensors.frm.FallDetectionFrame;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Requires({
        @RequiredPort(name="fall", type = PortType.MESSAGE, optional = true)
})

@ComponentType
@Library(name = "Serval_RN12")
public class BodySensors extends org.kevoree.framework.AbstractComponentType {

    private FallDetectionFrame frame;
    
    public BodySensors() {
        frame = new FallDetectionFrame();
        frame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(isPortBinded("fall")) {
                    ((MessagePort)getPortByName("fall")).process(new Object());
                }
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
