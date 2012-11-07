package lu.snt.iot.serval.rn12.display.server;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 29/10/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import org.kevoree.annotation.*;
import org.kevoree.library.javase.nodejs.AbstractNodeJSComponentType;


@DictionaryType({
        @DictionaryAttribute(name = "port", defaultValue = "8022", optional = true)
})
@ComponentType
public class DisplayServer extends AbstractNodeJSComponentType {
    @Override
    public String getMainFile() {
        return "server.js";
    }
}