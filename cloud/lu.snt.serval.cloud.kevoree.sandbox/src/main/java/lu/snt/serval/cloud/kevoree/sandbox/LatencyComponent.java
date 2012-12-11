package lu.snt.serval.cloud.kevoree.sandbox;

import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Library;
import org.kevoree.annotation.Start;
import org.kevoree.annotation.Stop;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.framework.AbstractComponentType;


import java.io.IOException;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: jmeira
 * Date: 11/12/12
 * Time: 09:04
 * To change this template use File | Settings | File Templates.
 */
@Library(name = "JavaSE")
@ComponentType
@DictionaryType({
        @DictionaryAttribute(name = "latencyWindow", defaultValue = "1", optional = true),
        @DictionaryAttribute(name = "latencyPeakWindow", defaultValue = "1", optional = true),
        @DictionaryAttribute(name = "latencyPeak", defaultValue = "10", optional = true),
        @DictionaryAttribute(name = "latencyPlus", defaultValue = "10", optional = true)
})
public class LatencyComponent extends AbstractComponentType implements Runnable{

        static final Integer peakLoadWindow = 1;
        static final Integer peakLoad = 20;
        static final Integer loadPlus = 10;


        @Start
        public void start() throws Exception {
        }

        @Stop
        public void stop() throws Exception {
        }


        @Override
        public void run() {

            LinkedList<Integer> fifo = new LinkedList<Integer>();
            int j=0;
            int i=0;

            for (i =0 ; i < 100; i+=loadPlus) {

                fifo.offer(i);

                if( i >= peakLoad ){
                    for(j=0; j < peakLoadWindow; j++){
                        fifo.offer(i);
                    }
                    i=100;
                }

            }

            fifo.offer(j);

            System.out.println(fifo.size());

            while (! fifo.isEmpty()) {
                System.out.println(fifo.poll());
            }
        }

        public static void main(String[] args) throws IOException, InterruptedException {
            BasicProbes p = new BasicProbes();
            p.start();
        }

}

