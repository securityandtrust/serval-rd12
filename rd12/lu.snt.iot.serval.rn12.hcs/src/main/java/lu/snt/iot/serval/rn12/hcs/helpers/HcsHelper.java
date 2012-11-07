package lu.snt.iot.serval.rn12.hcs.helpers;

import lu.snt.iot.serval.rn12.framework.data.msg.EmergencyMessage;

public class HcsHelper {

    public static boolean isAnswerPositive(EmergencyMessage message) {
        String text = message.getAnswer().toLowerCase();
        return text.contains("yes")
                || text.contains("oui")
                || text.contains("ok")
                || text.contains("ja")
                || text.contains("sure");
    }


    public static boolean isAnswerNegative(EmergencyMessage message) {
        String text = message.getAnswer().toLowerCase();
        return text.contains("no")
                || text.contains("can not")
                || text.contains("cannot")
                || text.contains("not")
                || text.contains("can't")
                || text.contains("sorry");

    }

}
