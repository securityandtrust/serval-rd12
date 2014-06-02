package lu.snt.iotlab.serval.rn12.proxy;

import org.kevoree.log.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 10/04/13
 * Time: 10:15
 */
public class OutputReader extends Thread {

    private InputStream is = null;
    private Boolean error = false;

    public OutputReader(InputStream _is,Boolean _error) {
        is = _is;
        error = _error;
    }

    public void run() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            for (; ; ) {
                String s = br.readLine();
                if (s == null){
                    break;
                } else {
                   if(error){
                       Log.error(s);
                   } else {
                       Log.info(s);
                   }
                    System.out.println("[ProxyLog]" + s);
                }
            }
        } catch (IOException ioe) {
        }
    }

}
