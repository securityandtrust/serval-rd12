package lu.snt.serval.cloud.kevoree.sandbox;

import org.kevoree.ContainerRoot;
import org.kevoree.KevoreeFactory;
import org.kevoree.tools.aether.framework.NodeTypeBootstrapHelper;
import org.kevoree.tools.marShell.KevScriptOfflineEngine;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 08/11/12
 * Time: 17:30
 */
public class ElasticityReaction {

    private final Integer numberOfInfraNode = 10;

    public static void main(String[] args) throws Exception{

        ElasticityReaction bean = new ElasticityReaction();
        ContainerRoot initModel = bean.initInfrastructureModel();
        System.out.println(initModel.getNodes().size());

        System.out.println(initModel);

    }




    public ContainerRoot initInfrastructureModel() throws Exception{

        ContainerRoot kevModel = KevoreeFactory.createContainerRoot();
        KevScriptOfflineEngine kevScriptEngine = new KevScriptOfflineEngine(kevModel,new NodeTypeBootstrapHelper());
        kevScriptEngine.addVariable("kevoree.version",KevoreeFactory.getVersion());
        kevScriptEngine.append("merge 'mvn:org.kevoree.corelibrary.sky/org.kevoree.library.sky.minicloud/{kevoree.version}'");
        for(int i=0;i<numberOfInfraNode;i++){
            kevScriptEngine.append("addNode INode"+i+":PMiniCloudNode");
            kevScriptEngine.append("updateDictionary INode"+i+" { CPU_FREQUENCY=\"1000\" }");
        }
        return kevScriptEngine.interpret();
    }








}
