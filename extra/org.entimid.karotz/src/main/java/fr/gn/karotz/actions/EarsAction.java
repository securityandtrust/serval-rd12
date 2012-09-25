/*
 * Copyright (c) 2011. Gregory Nain.
 */

package fr.gn.karotz.actions;

import fr.gn.karotz.Kernel;
import fr.gn.karotz.utils.KarotzCommand;

/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 21/05/11
 * Time: 12:20
 */
public class EarsAction implements KarotzCommand {

    private static final int LEFT_ONLY = 1;
    private static final int RIGHT_ONLY = 2;
    private static final int BOTH = 3;
    private static final int ABSOLUTE_MOVE = 4;


    private int commantKind;


    private int left;
    private int right;
    private boolean reset;

    private EarsAction() {
    }

    /**
     *  Creates a command setting the ears positions to the given values.
     * @param left absolute left ear position value. Range[0,15]
     * @param right absolute right ear position value. Range[0,15]
     * @return The KarotzCommand to be passed to {@link fr.gn.karotz.Karotz#send(KarotzCommand)}
     */
    public static EarsAction createAbsoluteMove(int left, int right) {
        EarsAction command = new EarsAction();
        command.left = left;
        command.right = right;
        command.commantKind = BOTH + ABSOLUTE_MOVE;
        return command;
    }


    public static EarsAction createLeftAbsoluteMove(int left) {
        EarsAction command = new EarsAction();
        command.left = left;
        command.commantKind = LEFT_ONLY + ABSOLUTE_MOVE;
        return command;
    }

    public static EarsAction createRightAbsoluteMove(int right) {
        EarsAction command = new EarsAction();
        command.right = right;
        command.commantKind = RIGHT_ONLY + ABSOLUTE_MOVE;
        return command;
    }

    /**
     * Creates a relative movement command for both ears.
     * @param left relative position of left ear. If the value < 0, the ear moves to the REAR. Range[-15,15]
     * @param right relative position of right ear. If the value < 0, the ear moves to the REAR. Range[-15,15]
     * @return
     */
    public static EarsAction createRelativeMove(int left, int right) {
        EarsAction command = new EarsAction();
        command.left = left;
        command.right = right;
        command.commantKind = BOTH;
        return command;
    }

    public static EarsAction createLeftRelativeMove(int left) {
        EarsAction command = new EarsAction();
        command.left = left;
        command.commantKind = LEFT_ONLY;
        return command;
    }

    public static EarsAction createRightRelativeMove(int right) {
        EarsAction command = new EarsAction();
        command.right = right;
        command.commantKind = RIGHT_ONLY;
        return command;
    }

    public static EarsAction createReset() {
        EarsAction command = new EarsAction();
        command.reset = true;
        return command;
    }

    @Override
    public String getCommand() {

        String rootAddr = Kernel.getServerAddress() + "ears?";

        if (reset) {
            rootAddr += "reset=true";
        } else {

            if ((commantKind & ABSOLUTE_MOVE) != 0) {
                rootAddr += "relative=false";
            } else {
                rootAddr += "relative=true";
            }

            if ((commantKind & LEFT_ONLY) != 0) {

                rootAddr += "&left=" + left;

            }
            if ((commantKind & RIGHT_ONLY) != 0) {

                rootAddr += "&right=" + right;

            }
        }

        return rootAddr += "&interactiveid=" + Kernel.getInteractiveId();  //To change body of implemented methods use File | Settings | File Templates.
    }
}
