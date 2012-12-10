package lu.snt.serval.cloud.kevoree.microbench;

import com.google.caliper.Param;
import com.google.caliper.SimpleBenchmark;
import lu.snt.serval.cloud.kevoree.sandbox.ElasticityReaction;
import lu.snt.serval.cloud.kevoree.sandbox.ElasticityReactionV2;
import org.kevoree.ContainerNode;
import org.kevoree.ContainerRoot;
import org.kevoree.api.Bootstraper;
import org.kevoree.tools.aether.framework.NodeTypeBootstrapHelper;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 20/11/12
 * Time: 23:34
 */
public class MatcherBench extends SimpleBenchmark {
    @Param
    int size;

    ContainerRoot initModel;

    @Override
    protected void setUp() throws Exception {
        ElasticityReaction.numberOfInfraNode = size;
      /*  Bootstraper bs = new NodeTypeBootstrapHelper();
        ElasticityReaction bean = new ElasticityReaction();
        initModel = bean.initInfrastructureModel(bs);
        initModel = bean.populateCustomerNode(initModel,bs);*/
    }

    public void timeRuleMatcher(int reps) {
        ElasticityReactionV2.main(null);
    }


    public void timeHandWriteMatcher(int reps) throws Exception {
        ElasticityReaction.main(null);
    }

}



