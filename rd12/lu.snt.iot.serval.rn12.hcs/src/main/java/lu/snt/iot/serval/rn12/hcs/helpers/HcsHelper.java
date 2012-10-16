package lu.snt.iot.serval.rn12.hcs.helpers;

import lu.snt.iot.serval.rn12.framework.data.msg.EmergencyMessage;

public class HcsHelper {

    public static boolean isAnswerPositive(EmergencyMessage message) {
        String text = message.getAnswer().toLowerCase();
        return text.contains("yes")
                || text.contains("oui")
                || text.contains("ok")
                || text.contains("sure");
    }

}
