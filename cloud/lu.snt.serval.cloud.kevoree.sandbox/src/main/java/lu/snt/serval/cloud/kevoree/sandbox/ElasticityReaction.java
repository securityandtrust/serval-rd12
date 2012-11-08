package lu.snt.serval.cloud.kevoree.sandbox;

import org.kevoree.*;
import org.kevoree.KevoreeFactory;
import org.kevoree.framework.KevoreeXmiHelper;
import org.kevoree.tools.aether.framework.NodeTypeBootstrapHelper;
import org.kevoree.tools.marShell.KevScriptOfflineEngine;
import org.kevoree.tools.ui.editor.KevoreeEditor;

import javax.swing.*;

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
        initModel = bean.populateCustomerNode(initModel);

        ContainerNode overloadNode = bean.detectOverLoad(initModel);
        if(overloadNode != null){
            initModel = bean.reallocate(initModel, overloadNode);
        }

        KevoreeXmiHelper.save("/Users/duke/optimized.kev",initModel);

        /* Display result */
        KevoreeEditor artpanel = new KevoreeEditor();
        artpanel.loadModel("/Users/duke/optimized.kev");
        JFrame frame = new JFrame();
        frame.add(artpanel.getPanel());
        frame.setVisible(true);


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

    public ContainerRoot populateCustomerNode(ContainerRoot model) throws Exception {
        KevScriptOfflineEngine kevScriptEngine = new KevScriptOfflineEngine(model,new NodeTypeBootstrapHelper());
        //CREATE CUSTOMER NODE ON INFRA 0
        kevScriptEngine.append("addNode cust0:PJavaSENode");
        kevScriptEngine.append("updateDictionary cust0 { CPU_FREQUENCY=\"500\" }");
        kevScriptEngine.append("addChild cust0@INode0");
        //CREATE CUSTOMER NODE ON INFRA 0
        kevScriptEngine.append("addNode cust1:PJavaSENode");
        kevScriptEngine.append("updateDictionary cust1 { CPU_FREQUENCY=\"800\" }");
        kevScriptEngine.append("addChild cust1@INode0");
        return kevScriptEngine.interpret();
    }

    /* */

    public ContainerNode detectOverLoad(ContainerRoot model){
        for(ContainerNode infraNode : model.getNodesForJ()){
             if(infraNode.getHostsForJ().size()>0){

                 Integer hostPower = computePowerUsage(infraNode);
                 if(!hostPower.equals(0)){
                     Integer sumVal = computeChildPowerUsage(infraNode);
                     if(hostPower<sumVal){
                         return infraNode;
                     }
                 }

             }
        }
        return null;
    }

    public Integer computePowerUsage(Instance instance){
        if(!instance.getDictionary().isEmpty()){
            for(DictionaryValue value :  instance.getDictionary().get().getValuesForJ()){
                if(value.getAttribute().getName().equals("CPU_FREQUENCY")){
                    return Integer.parseInt(value.getValue());
                }
            }
        }
        return 0;
    }

    public Integer computeChildPowerUsage(ContainerNode parentNode){
        Integer sumVal = 0;
        for(ContainerNode child : parentNode.getHostsForJ()){
            sumVal = sumVal +computePowerUsage(child);
        }
        return sumVal;
    }

    public ContainerRoot reallocate(ContainerRoot model,ContainerNode overloadedNode) throws Exception{
        if(overloadedNode.getHostsForJ().size()>0){
            //FOUND NEW TARGET
            for(ContainerNode IAASNODE: model.getNodesForJ()){
                NodeType nodeType = (NodeType) IAASNODE.getTypeDefinition();
                boolean selected = false;
                for(AdaptationPrimitiveType pt : nodeType.getManagedPrimitiveTypesForJ()){
                    if(!selected){
                      if(pt.getName().toLowerCase().equals("addnode")){
                          Integer sumVal = computeChildPowerUsage(IAASNODE);
                          Integer capacityPower = computePowerUsage(IAASNODE);
                          Integer powerNeeded = computePowerUsage(overloadedNode.getHostsForJ().get(0));
                          System.out.println("ChildSum="+sumVal+",PowerCapacity="+capacityPower+"PowerNeeded="+powerNeeded);
                          if(powerNeeded < (capacityPower - sumVal)){
                              selected= true;
                          }
                      }
                    }
                }
                if(selected){
                    KevScriptOfflineEngine kevScriptEngine = new KevScriptOfflineEngine(model,new NodeTypeBootstrapHelper());
                    kevScriptEngine.append("moveChild "+overloadedNode.getHostsForJ().get(0).getName()+"@"+overloadedNode.getName()+" => "+IAASNODE.getName());
                    System.out.println("moveChild "+overloadedNode.getHostsForJ().get(0).getName()+"@"+overloadedNode.getName()+" => "+IAASNODE.getName());
                    return kevScriptEngine.interpret();
                }
            }
        }
        return model;
    }
}
