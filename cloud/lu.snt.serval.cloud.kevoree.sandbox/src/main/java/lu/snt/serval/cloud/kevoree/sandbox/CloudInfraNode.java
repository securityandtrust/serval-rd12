package lu.snt.serval.cloud.kevoree.sandbox;

import org.kevoree.annotation.DictionaryAttribute;
import org.kevoree.annotation.DictionaryType;
import org.kevoree.annotation.Library;
import org.kevoree.annotation.NodeType;
import org.kevoree.library.sky.minicloud.nodeType.PMiniCloudNode;

/**
 * Created with IntelliJ IDEA.
 * User: duke
 * Date: 09/11/12
 * Time: 12:51
 */
@NodeType
@Library(name = "Snt-Cloud")
@DictionaryType({
        @DictionaryAttribute(name = "PRICE_PER_H")
})
public class CloudInfraNode extends PMiniCloudNode {
}
