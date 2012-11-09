package lu.snt.serval.cloud.kevoree.sandbox;

import org.kevoree.*;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 09/11/12
 * Time: 08:04
 */
public class ModelHelper {

    public static Integer sumIntegerProperty(Instance instance,String propName){
        if(!instance.getDictionary().isEmpty()){
            for(DictionaryValue value :  instance.getDictionary().get().getValuesForJ()){
                if(value.getAttribute().getName().equals(propName)){
                    return Integer.parseInt(value.getValue());
                }
            }
        }
        return 0;
    }

    public static Integer sumChildIntegerProperty(ContainerNode parentNode,String propName){
        Integer sumVal = 0;
        for(ContainerNode child : parentNode.getHostsForJ()){
            sumVal = sumVal +sumIntegerProperty(child, propName);
        }
        return sumVal;
    }

    public static Boolean isHostNode(ContainerNode node){
        NodeType nodeType = (NodeType) node.getTypeDefinition();
        for(AdaptationPrimitiveType pt : nodeType.getManagedPrimitiveTypesForJ()){
            if(pt.getName().toLowerCase().equals("addnode")){
                return true;
            }
        }
        return false;
    }

}
