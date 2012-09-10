package lu.snt.iot.serval.rn12.doorSensors;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;

@Requires({
        @RequiredPort(name="opened", type = PortType.MESSAGE, optional = true),
        @RequiredPort(name="closed", type = PortType.MESSAGE, optional = true)
})

@ComponentType
@Library(name = "Serval_RN12")
public class DoorSensor extends AbstractComponentType {

    //private FallDetectionFrame frame;

    public DoorSensor() {
      /*
        frame = new FallDetectionFrame();
        frame.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if(isPortBinded("fall")) {
                    ((MessagePort)getPortByName("fall")).process(new Object());
                }
            }
        });
        */
    }
    
    @Start
    public void start() {
       // frame.setVisible(true);
    }

    @Stop
    public void stop() {
        //frame.setVisible(false);
    }

    @Update
    public void update() {
    }
}
