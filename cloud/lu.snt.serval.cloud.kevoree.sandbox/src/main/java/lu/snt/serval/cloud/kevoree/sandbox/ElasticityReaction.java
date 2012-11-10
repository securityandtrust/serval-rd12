package lu.snt.serval.cloud.kevoree.sandbox;

import org.kevoree.*;
import org.kevoree.KevoreeFactory;
import org.kevoree.api.Bootstraper;
import org.kevoree.framework.KevoreeXmiHelper;
import org.kevoree.tools.aether.framework.NodeTypeBootstrapHelper;
import org.kevoree.tools.marShell.KevScriptOfflineEngine;
import org.kevoree.tools.ui.editor.KevoreeEditor;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 08/11/12
 * Time: 17:30
 */
public class ElasticityReaction {

    private final Integer numberOfInfraNode = 3;
    private final String propertyName = "CPU_FREQUENCY";

    public static void main(String[] args) throws Exception{
        Bootstraper bs = new NodeTypeBootstrapHelper();
        ElasticityReaction bean = new ElasticityReaction();
        ContainerRoot initModel = bean.initInfrastructureModel(bs);
        initModel = bean.populateCustomerNode(initModel,bs);

        bean.displayModel(initModel); //Display Before optimisation

        //run1
        ContainerNode detected = bean.patternDetection(initModel);
        if(detected != null){
            initModel = bean.reallocate(initModel, detected,bs);
        }
        bean.displayModel(initModel); //Display After Optimisation

        //run2
        detected = bean.patternDetection(initModel);
        if(detected != null){
            initModel = bean.reallocate(initModel, detected,bs);
        }
        bean.displayModel(initModel); //Display After Optimisation

    }

    public void displayModel(ContainerRoot model) throws IOException {
        KevoreeEditor artpanel = new KevoreeEditor();
        File tf = File.createTempFile("0"+model.hashCode(),"0"+model.hashCode());
        KevoreeXmiHelper.save(tf.getAbsolutePath(), model);
        artpanel.loadModel(tf.getAbsolutePath());
        JFrame frame = new JFrame();
        frame.add(artpanel.getPanel());
        frame.setVisible(true);
        frame.setSize(1024,768);
        frame.setPreferredSize(frame.getSize());
    }

    public ContainerRoot initInfrastructureModel(Bootstraper bs) throws Exception{
        ContainerRoot kevModel = KevoreeFactory.createContainerRoot();
        KevScriptOfflineEngine kevScriptEngine = new KevScriptOfflineEngine(kevModel,bs);
        kevScriptEngine.addVariable("kevoree.version", KevoreeFactory.getVersion());
        kevScriptEngine.append("merge 'mvn:org.kevoree.corelibrary.sky/org.kevoree.library.sky.minicloud/{kevoree.version}'");
        for(int i=0;i<numberOfInfraNode;i++){
            kevScriptEngine.append("addNode INode"+i+":PMiniCloudNode");
            kevScriptEngine.append("updateDictionary INode"+i+" { CPU_FREQUENCY=\"1000\" }");
        }
        return kevScriptEngine.interpret();
    }

    public ContainerRoot populateCustomerNode(ContainerRoot model,Bootstraper bs) throws Exception {
        KevScriptOfflineEngine kevScriptEngine = new KevScriptOfflineEngine(model,bs);
        //CREATE CUSTOMER NODE ON INFRA 0
        kevScriptEngine.append("addNode cust0:PJavaSENode");
        kevScriptEngine.append("updateDictionary cust0 { CPU_FREQUENCY=\"500\" }");
        kevScriptEngine.append("addChild cust0@INode0");
        //CREATE CUSTOMER NODE ON INFRA 0
        kevScriptEngine.append("addNode cust1:PJavaSENode");
        kevScriptEngine.append("updateDictionary cust1 { CPU_FREQUENCY=\"800\" }");
        kevScriptEngine.append("addChild cust1@INode0");
        //CREATE CUSTOMER NODE ON INFRA 0
        kevScriptEngine.append("merge 'mvn:lu.snt.serval.cloud.kevoree.sandbox/lu.snt.serval.cloud.kevoree.sandbox/1.0-SNAPSHOT'");
        kevScriptEngine.append("addNode custDonia:CloudCustomerNode");
        kevScriptEngine.append("updateDictionary custDonia { OWNER=\"Donia\" }");
        kevScriptEngine.append("addChild custDonia@INode0");
        kevScriptEngine.append("addNode custDonia2:CloudCustomerNode");
        kevScriptEngine.append("updateDictionary custDonia2 { OWNER=\"Jorje\" }");
        kevScriptEngine.append("addChild custDonia2@INode0");
        return kevScriptEngine.interpret();
    }




    /* Pattern detection */
    public ContainerNode patternDetection(ContainerRoot model){
        ContainerNode violated = detectOverLoad(model);
        if(violated != null){
            System.out.println("overload >"+violated.getName());
            return violated;
        }
        violated = isSecurityViolated(model);
        if(violated != null){
            for(ContainerNode parentN :model.getNodesForJ()){
               if(parentN.getHostsForJ().contains(violated)){
                   System.out.println("security >"+violated.getName());
                   return parentN;
               }
            }
            return violated;
        }
        return null;
    }


    public ContainerNode detectOverLoad(ContainerRoot model){
        for(ContainerNode infraNode : model.getNodesForJ()){
             if(infraNode.getHostsForJ().size()>0){
                 Integer hostPower = ModelHelper.sumIntegerProperty(infraNode,propertyName);
                 if(!hostPower.equals(0)){
                     Integer sumVal = ModelHelper.sumChildIntegerProperty(infraNode,propertyName);
                     if(hostPower<sumVal){
                         return infraNode;
                     }
                 }
             }
        }
        return null;
    }

    /* Pattern Exclusion :-) */


    /* Action */
    public ContainerRoot reallocate(ContainerRoot model,ContainerNode overloadedNode,Bootstraper bs) throws Exception{
        if(overloadedNode.getHostsForJ().size()>0){
            //FOUND NEW TARGET
            for(ContainerNode IAASNODE: model.getNodesForJ()){
                if(ModelHelper.isHostNode(IAASNODE)){
                    Integer sumVal = ModelHelper.sumChildIntegerProperty(IAASNODE,propertyName);
                    Integer capacityPower = ModelHelper.sumIntegerProperty(IAASNODE,propertyName);
                    Integer powerNeeded = ModelHelper.sumIntegerProperty(overloadedNode.getHostsForJ().get(0),propertyName);
                    if(powerNeeded < (capacityPower - sumVal)){
                        KevScriptOfflineEngine kevScriptEngine = new KevScriptOfflineEngine(model,bs);
                        kevScriptEngine.append("moveChild "+overloadedNode.getHostsForJ().get(0).getName()+"@"+overloadedNode.getName()+" => "+IAASNODE.getName());
                        System.out.println("moveChild "+overloadedNode.getHostsForJ().get(0).getName()+"@"+overloadedNode.getName()+" => "+IAASNODE.getName());
                        ContainerRoot candidateModel = kevScriptEngine.interpret();
                        ContainerNode nodeViolated=isSecurityViolated(candidateModel);
                        if( nodeViolated == null ){
                            return candidateModel;
                        } else {
                            KevScriptOfflineEngine kevScriptEngine2 =new KevScriptOfflineEngine(model,bs);
                            kevScriptEngine2.append("moveChild "+nodeViolated.getName()+"@"+overloadedNode.getName()+" => "+IAASNODE.getName());
                            System.out.println("moveChild "+nodeViolated.getName()+"@"+overloadedNode.getName()+" => "+IAASNODE.getName());
                            ContainerRoot candidateModel2 = kevScriptEngine2.interpret();
                            ContainerNode nodeViolated2=isSecurityViolated(candidateModel2);
                            if( nodeViolated2 == null ){
                                return candidateModel2;
                            }
                        }
                    }
                }
            }
        }
        return model;
    }


    public ContainerNode isSecurityViolated(ContainerRoot model){
        for(ContainerNode infraNode : model.getNodesForJ()){
            if(infraNode.getHostsForJ().size()>1){
                HashMap<String,Integer> owners = new HashMap<String,Integer>();
                for (ContainerNode child : infraNode.getHostsForJ()){
                    String newOwner = ModelHelper.stringProperty(child,"OWNER");
                    if(newOwner != null){
                        if(!owners.containsKey(newOwner)){
                            owners.put(newOwner,1);
                        } else {
                            owners.put(newOwner,owners.get(newOwner)+1);
                        }
                        if(owners.keySet().size() > 1){
                            System.out.println("isSecurityViolated for owner name = "+newOwner);
                            return child;
                        }
                    }
                }


            }
        }
        return null;
    }




}
