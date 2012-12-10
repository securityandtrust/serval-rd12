package lu.snt.serval.cloud.kevoree.microbench;

import com.google.caliper.Runner;
import com.google.monitoring.runtime.instrumentation.AllocationRecorder;
import com.google.monitoring.runtime.instrumentation.Sampler;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 20/11/12
 * Time: 23:20
 */
public class Test {

    public static void main(String [] args) throws Exception {

        Runner runner = new Runner();
        runner.run(MatcherBench.class.getName(),"--timeUnit","ms","-Dsize","5,10,50,100,200,500","--printScore");

    }

}
