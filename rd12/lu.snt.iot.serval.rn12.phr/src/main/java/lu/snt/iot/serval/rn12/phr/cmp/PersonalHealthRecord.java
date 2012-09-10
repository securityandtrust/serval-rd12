package lu.snt.iot.serval.rn12.phr.cmp;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;


@Provides({
        @ProvidedPort(name="getRecord", type=PortType.MESSAGE)
})

@Requires(
        @RequiredPort(name="PHR", type=PortType.MESSAGE, optional = true)
)


@ComponentType
@Library(name = "Serval_RN12")
public class PersonalHealthRecord extends org.kevoree.framework.AbstractComponentType {

    @Start
    public void start() {
    }

    @Stop
    public void stop() {
    }

    @Update
    public void update() {
    }
    
    @Port(name="getRecord")
    public void onGetRecord(Object o) {

    }
    
}
