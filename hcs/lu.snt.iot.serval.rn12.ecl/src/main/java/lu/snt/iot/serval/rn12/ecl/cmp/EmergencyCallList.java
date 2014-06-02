package lu.snt.iot.serval.rn12.ecl.cmp;

import lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyContact;
import org.kevoree.annotation.*;
import org.kevoree.api.Port;

@ComponentType
@Library(name = "Serval_RN12")
public class EmergencyCallList {

    protected lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyCallList ecl = null;

    @Output
    public Port ECL;

    @Start
    public void start() {
    }

    @Stop
    public void stop() {
    }

    @Update
    public void update() {
    }

    private void buildEcl() {
        ecl = new lu.snt.iot.serval.rn12.framework.data.ecl.EmergencyCallList();
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
    }

    @Input
    public void getList(Object o) {

        if(ECL.getConnectedBindingsSize() > 0) {
            if(ecl == null) {
                buildEcl();
            }
            ECL.send(ecl);
        }
    }

}
