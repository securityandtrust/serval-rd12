/*
 * Copyright (c) 2011. Gregory Nain.
 */

package fr.gn.karotz.actions;

import fr.gn.karotz.Kernel;
import fr.gn.karotz.utils.KarotzCommand;
import fr.gn.karotz.utils.Languages;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 22/05/11
 * Time: 15:32
 */
public class TextToSpeachAction implements KarotzCommand {

    private Languages language;
    private String text;

    public TextToSpeachAction(String text, Languages language) {
        this.language = language;
        this.text = text;
    }


    @Override
    public String getCommand() {
        try {
            return Kernel.getServerAddress() + "tts?action=speak&lang=" + language + "&text=" + URLEncoder.encode(text, "UTF-8") + "&interactiveid=" + Kernel.getInteractiveId();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
