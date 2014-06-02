/**
 * © 2012 - University of Luxebourg - Interdisciplinary Center for Security, Reliability and Trust
 * Author: Gregory NAIN
 */

package lu.snt.iot.serval.rn12.runtime;
/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 29/10/12
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import org.json.JSONException;
import org.json.JSONStringer;
import org.json.JSONWriter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CustomLogbackAppender extends AppenderBase<ILoggingEvent> {

    @Override
    protected void append(ILoggingEvent iLoggingEvent) {
        App.getInstance().getWebSocket().handleMessage(mkString(iLoggingEvent));
       // System.out.println("[MYLOGGER-"+iLoggingEvent.getLevel()+"]->" + iLoggingEvent.getFormattedMessage());
    }

    private String mkString(ILoggingEvent event) {
        String result = "AN ERROR OCCURED WHILE JSONing THE LOG";
        try {
            DateFormat getDayFormated = new SimpleDateFormat("d MMM yyyy");
            DateFormat getTimeFormated = new SimpleDateFormat("HH:mm:ss:S");
            Date date = new Date(event.getTimeStamp());
            JSONStringer stringer = new JSONStringer();
            JSONWriter object = stringer.object();
            object.key("logLevel").value(event.getLevel());
            object.key("logFrom").value(event.getLoggerName() + "::"+event.getCallerData()[0].getMethodName()+":" + event.getCallerData()[0].getLineNumber());
            object.key("logMessage").value(event.getFormattedMessage());
            object.key("logDate").value(getDayFormated.format(date));
            object.key("logTime").value(getTimeFormated.format(date));

            result = object.endObject().toString();
        } catch (JSONException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return result;
    }


}
