package lu.snt.iot.serval.rn12.doorlock;

import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.kevoree.framework.MessagePort;


/**
 * Created by IntelliJ IDEA.
 * User: Assaad Moawad
 * Date: 28/11/12
 * Time: 11:40
 */

//Output
@Requires({
        @RequiredPort(name = "doorLog", type = PortType.MESSAGE, optional = true) ,
        @RequiredPort(name = "passwordOutput", type = PortType.MESSAGE, optional = true),
        @RequiredPort(name = "doorOpen", type = PortType.MESSAGE, optional = true)
})

//Input
@Provides({
        @ProvidedPort(name = "changePassword", type = PortType.MESSAGE)
})

@DictionaryType({
        @DictionaryAttribute(name = "publicKey", defaultValue = "", optional = true),
        @DictionaryAttribute(name = "passwordSize", defaultValue = "1", optional = true)
})

@ComponentType
public class DoorComponent extends AbstractComponentType {
private DoorClass doorControler;

    @Start
    public void startComponent() {



        String[] args = new String[2];
        args[0]=(String) getDictionary().get("publicKey");
        args[1]=(String) getDictionary().get("passwordSize");

        doorControler=new DoorClass(args);
        new MainLock(doorControler).main(null);

        System.out.println("Starting Door!  ");



    }

    @Stop
    public void stopComponent() {

    }

    @Update
    public void updateComponent() {
        stopComponent();
        startComponent();
    }

    @Port(name = "changePassword")
    public void changePassword(Object o) {
        doorControler.ChangePassword();

    }

}
