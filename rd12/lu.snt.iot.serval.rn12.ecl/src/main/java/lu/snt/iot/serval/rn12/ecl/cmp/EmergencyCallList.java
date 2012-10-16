package lu.snt.iot.serval.rn12.ecl.cmp;

import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyContact;
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


       if(isPortBinded("ECL")) {

           lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyCallList ecl = new lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyCallList();
           EmergencyContact first = new EmergencyContact();
           first.setName("Mike");
           first.setPhoneNumber("621159813");
           first.setImAddr("sntlabphone01@entimid.org");
           ecl.addEmergencyContact(first);


           EmergencyContact second = new EmergencyContact();
           second.setName("Anna");
           second.setPhoneNumber("621159814");
           second.setImAddr("sntlabphone02@entimid.org");
           ecl.addEmergencyContact(second);


           EmergencyContact third = new EmergencyContact();
           third.setName("Grégory");
           third.setImAddr("sntlabphone01@entimid.org");
           ecl.addEmergencyContact(third);

           /*
            alert.put("ecl.0.name", "Mike");
            alert.put("ecl.0.number", "621159813");
            alert.put("ecl.0.xmpp", "sntlabphone01@entimid.org");
            alert.put("ecl.1.name", "Anna");
            alert.put("ecl.1.number", "621159814");
            alert.put("ecl.1.xmpp", "sntlabphone02@entimid.org");
            alert.put("ecl.2.name", "Grégory");
            alert.put("ecl.2.xmpp", "gregory.nain@gmail.com");
            */
           // System.out.println("ECL::HashtableEnd:" + alert.toString() + " size:" + alert.size());
           ((MessagePort)getPortByName("ECL")).process(ecl);
        }
    }

}
