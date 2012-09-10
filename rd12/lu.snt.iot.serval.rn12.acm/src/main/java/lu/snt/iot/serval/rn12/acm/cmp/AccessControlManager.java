package lu.snt.iot.serval.rn12.acm.cmp;

import org.kevoree.framework.AbstractComponentType;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Start;
import org.kevoree.annotation.Stop;
import org.kevoree.annotation.Update;
import org.kevoree.annotation.Library;

@ComponentType
@Library(name = "Serval_RN12")
public class AccessControlManager extends org.kevoree.framework.AbstractComponentType {

    @Start
    public void start() {
    }

    @Stop
    public void stop() {
    }

    @Update
    public void update() {
    }
}
