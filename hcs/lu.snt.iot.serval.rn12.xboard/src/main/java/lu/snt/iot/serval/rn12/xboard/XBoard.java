package lu.snt.iot.serval.rn12.xboard;

import org.kevoree.annotation.*;
import org.kevoree.log.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

@ComponentType
@Library(name = "Java")
public class XBoard {

    @Param(optional = false)
    String address;

    @Input
    public void up(Object i) {
        callUrl(address+(address.endsWith("/")?"":"/")+"up");
    }

    @Input
    public void down(Object i) {
        callUrl(address+(address.endsWith("/")?"":"/")+"down");
    }

    @Input
    public void stop(Object i) {
        callUrl(address+(address.endsWith("/")?"":"/")+"stop");
    }

    private void callUrl(String urlString) {
        //System.out.println("Requeted URL:" + myURL);
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
            URL url = new URL(urlString);
            urlConn = url.openConnection();
            if (urlConn != null) {
                urlConn.setReadTimeout(60 * 1000);
            }
            if (urlConn != null && urlConn.getInputStream() != null) {
                in = new InputStreamReader(urlConn.getInputStream(),
                        Charset.defaultCharset());
                BufferedReader bufferedReader = new BufferedReader(in);
                if (bufferedReader != null) {
                    int cp;
                    while ((cp = bufferedReader.read()) != -1) {}
                    bufferedReader.close();
                }
            }
            in.close();
        } catch (Exception e) {
            Log.warn("Could not execute the command to XBoard at address:" + urlString, e);
        }
    }


    @Start
    public void start() {}

    @Stop
    public void stop() {}

    @Update
    public void update() {}

}



