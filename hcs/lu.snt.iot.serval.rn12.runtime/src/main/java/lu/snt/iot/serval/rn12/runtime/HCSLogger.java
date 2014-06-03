package lu.snt.iot.serval.rn12.runtime;

import org.kevoree.log.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created by gregory.nain on 02/06/2014.
 */
public class HCSLogger extends Log.Logger {

   // private StringBuilder logBuilder;
    DateFormat getDayFormated = new SimpleDateFormat("d MMM yyyy");
    DateFormat getTimeFormated = new SimpleDateFormat("HH:mm:ss:S");

    @Override
    protected void print(String message) {
        super.print(message);
    }

    @Override
    public synchronized void log(int level, String message, Throwable ex) {
        super.log(level, message, ex);

        StringBuilder logBuilder = new StringBuilder();
        Date date = new Date(System.currentTimeMillis());
        logBuilder.append('{');

        switch (level) {
            case Log.LEVEL_NONE:{}break;
            case Log.LEVEL_ERROR:{
                logBuilder.append("\"logLevel\":\"ERROR\"");
            }break;
            case Log.LEVEL_WARN:{
                logBuilder.append("\"logLevel\":\"WARN\"");
            }break;
            case Log.LEVEL_INFO:{
                logBuilder.append("\"logLevel\":\"INFO\"");
            }break;
            case Log.LEVEL_DEBUG:{
                logBuilder.append("\"logLevel\":\"DEBUG\"");
            }break;
            case Log.LEVEL_TRACE:{
                logBuilder.append("\"logLevel\":\"TRACE\"");
            }break;
        }

        if(ex != null) {
            StackTraceElement topElem = ex.getStackTrace()[0];
            logBuilder.append(",\"logFrom\":\"");
            logBuilder.append(topElem.getClassName());
            logBuilder.append("::");
            logBuilder.append(topElem.getMethodName());
            logBuilder.append("(");
            logBuilder.append(topElem.getLineNumber());
            logBuilder.append(")\"");
        }

        logBuilder.append(",\"logMessage\":\"");
        logBuilder.append(message);
        if(ex != null) {
            logBuilder.append(": ");
            logBuilder.append(ex.getMessage());
        }
        logBuilder.append("\"");
        logBuilder.append(",\"logDate\":\"");
        logBuilder.append(getDayFormated.format(date));
        logBuilder.append("\"");
        logBuilder.append(",\"logTime\":\"");
        logBuilder.append(getTimeFormated.format(date));
        logBuilder.append("\"");
        logBuilder.append('}');

        MyApp.getInstance().getWebSocket().handleMessage(logBuilder.toString());
    }
}
