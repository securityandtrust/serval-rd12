package lu.snt.iot.serval.rn12.ecl.cmp;

import org.kevoree.annotation.*;
import org.kevoree.framework.MessagePort;

import java.util.Hashtable;


@Provides({
        @ProvidedPort(name="getList", type=PortType.MESSAGE)
})

@Requires(
        @RequiredPort(name="ECL", type=PortType.MESSAGE, optional = true)
)

@ComponentType
@Library(name = "Serval_RN12")
public class EmergencyCallList extends org.kevoree.framework.AbstractComponentType {

    @Start
    public void start() {
    }

    @Stop
    public void stop() {
    }

    @Update
    public void update() {
    }

    @Port(name = "getList")
    public void onGetListCall(Object o) {
        Hashtable<String, Object> alert =  (Hashtable<String, Object>)o;

        //System.out.println("ECL::HashtableStart:" + alert.toString() + " size:" + alert.size());

        if(isPortBinded("ECL")) {


            alert.put("ecl.0.name", "Grégory");
            alert.put("ecl.0.number", "621159813");
            alert.put("ecl.0.xmpp", "sntlabphone01@entimid.org");
            alert.put("ecl.1.name", "Patrice");
            alert.put("ecl.1.number", "4666445250");
            alert.put("ecl.1.xmpp", "pbcaire@gmail.com");
           // System.out.println("ECL::HashtableEnd:" + alert.toString() + " size:" + alert.size());
           ((MessagePort)getPortByName("ECL")).process(alert);
        }
    }

}
