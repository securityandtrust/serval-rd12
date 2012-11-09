package lu.snt.serval.cloud.kevoree.sandbox;

import org.kevoree.ContainerNode;
import org.kevoree.ContainerRoot;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Library;
import org.kevoree.annotation.Start;
import org.kevoree.annotation.Stop;
import org.kevoree.api.service.core.handler.ModelListener;
import org.kevoree.api.service.core.handler.UUIDModel;
import org.kevoree.framework.AbstractComponentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 09/11/12
 * Time: 10:23
 */
@Library(name = "Snt-Cloud")
@ComponentType
public class ElasticReasonerComponent extends AbstractComponentType implements ModelListener {

    private ElasticityReaction bean = null;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Start
    public void start() throws Exception {
        bean = new ElasticityReaction();
        getModelService().registerModelListener(this);
    }

    @Stop
    public void stop() throws Exception {
        getModelService().unregisterModelListener(this);
        bean = null;
    }


    @Override
    public boolean preUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
        return true;
    }

    @Override
    public boolean initUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
        return true;
    }

    @Override
    public boolean afterLocalUpdate(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
        return true;
    }

    @Override
    public void modelUpdated() {
        logger.info("Compute optimisation");
        //good us
        UUIDModel liveModel = getModelService().getLastUUIDModel();
        ContainerNode detected = bean.patternDetection(liveModel.getModel());
        if (detected != null) {
            logger.info("Problem detected on node " + detected.getName());
            try {
                ContainerRoot optimizedModel = bean.reallocate(liveModel.getModel(), detected, getBootStrapperService());
                getModelService().compareAndSwapModel(liveModel, optimizedModel);
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }

    @Override
    public void preRollback(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
    }

    @Override
    public void postRollback(ContainerRoot containerRoot, ContainerRoot containerRoot2) {
    }
}
