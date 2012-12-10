package lu.snt.serval.cloud.kevoree.sandbox

import org.kevoree.tools.aether.framework.NodeTypeBootstrapHelper
import org.kevoree.api.Bootstraper
import hammurabi.{RuleEngine, WorkingMemory}
import hammurabi.Rule._
import org.kevoree.{ContainerRoot, Instance, ContainerNode}
import org.kevoree.impl.ContainerNodeImpl
import org.kevoree.tools.marShell.KevScriptOfflineEngine

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 12/11/12
 * Time: 18:57
 */
object ElasticityReactionV2 extends App {

  def getProperty(instance: Instance, propKey: String): String = {
    var res : String = null
    instance.getDictionary.map {
      dico =>
        dico.getValues.foreach {
          v =>
            if (v.getAttribute.getName == propKey) {
              res = v.getValue
            }
        }
    }
    res
  }

  val bs: Bootstraper = new NodeTypeBootstrapHelper
  val bean = new ElasticityReaction()

  var model = bean.initInfrastructureModel(bs)
  model = bean.populateCustomerNode(model, bs)
  tryAdapt(model)



  def tryAdapt(amodel : ContainerRoot) : ContainerRoot = {

    val r = rule("Security Rule 1") let {
      val parentNode = kindOf[ContainerNode].having(_.getHosts.size > 0)
      val childNode_1 = kindOf[ContainerNode].having(getProperty(_, "OWNER") != null)
      val childNode_2 = kindOf[ContainerNode].having(getProperty(_, "OWNER") != null)
      when {
        (
          (childNode_1 != parentNode)
            && (childNode_2 != parentNode)
            && (parentNode.getHosts.contains(childNode_1))
            && (parentNode.getHosts.contains(childNode_2))
            && (getProperty(childNode_1, "OWNER") != getProperty(childNode_2, "OWNER"))
          )
      } then {

        val kevScriptEngine2 =new KevScriptOfflineEngine(model,bs);
        kevScriptEngine2.addVariable("nodeViolated",childNode_1.getName);
        kevScriptEngine2.addVariable("overloadedNode",parentNode.getName);
        kevScriptEngine2.addVariable("iaasNode",parentNode.getName); //TODO CHANGE
        kevScriptEngine2.append("moveChild {nodeViolated}@{overloadedNode} => {iaasNode}");
        val candidateModel2 = kevScriptEngine2.interpret();

       // model = bean.reallocate(model,parentNode,bs)
       // println("HostFound " + parentNode.getName + "->" + childNode_1.getName + "->" + childNode_2.getName)
      }
    }

   // for (i <- 0 until 100) {
    //  val time = System.nanoTime()
      val objectPool = WorkingMemory(amodel.getNodes)
      RuleEngine(r) execOn objectPool
    //  println(System.nanoTime() - time)
  //  }
    model
  }



}
