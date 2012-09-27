package lu.snt.iot.serval.rn12.karotz;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 11/05/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/



/*
String sk = "87683e1e-c478-4c2a-b46e-f22bd6aca916";
   String ak = "ae5e7b12-bea8-4c5a-80d5-cd88576a95e5";
   String iid = "2b96ef18-b795-4b62-b528-29019c0e84d7";

*/

import fr.gn.karotz.Karotz;
import fr.gn.karotz.actions.TextToSpeachAction;
import fr.gn.karotz.utils.Languages;
import org.kevoree.annotation.*;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.LoggerFactory;

import java.util.Properties;


@Provides({
        @ProvidedPort(type = PortType.MESSAGE, name = "tts")
})
@DictionaryType({
        @DictionaryAttribute(name = "secretKey", optional = false),
        @DictionaryAttribute(name = "applicationKey", optional = false),
        @DictionaryAttribute(name = "installationId", optional = false)
})
@ComponentType
@Library(name = "Serval_RN12")
public class KarotzCmp  extends AbstractComponentType {

    private Karotz karotz;
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(KarotzCmp.class);

    @Port(name = "tts")
    public void onTtsRequest(Object o) {

        Properties p = (Properties)o;

        logger.info("Initializing session for TTS, saying "+ (String)p.get("message"));
        if(karotz.initSession()) {
            karotz.send(new TextToSpeachAction((String)p.get("message"), Languages.EN));
            try {
                Thread.sleep((((String) p.get("message")).length() * 100) + 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            karotz.closeSession();

        } else {
            logger.warn("Couldn't init the session.");
        }

    }

    @Start
    public void start(){
        karotz = new Karotz(
                (String)getDictionary().get("applicationKey"),
                (String)getDictionary().get("secretKey"),
                (String)getDictionary().get("installationId"));

    }

    @Stop
    public void stop(){
        karotz = null;
    }

    @Update
    public void update(){
        stop();
        start();
    }

}
