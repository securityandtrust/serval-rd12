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
public class MultimediaAction implements KarotzCommand {

    private Languages language;
    private String text;
    private Kernel kernel;

    public MultimediaAction(Kernel k, String text, Languages language) {
        this.language = language;
        this.text = text;
        this.kernel = k;
    }


    @Override
    public String getCommand() {
        try {

            String url = "http://translate.google.com/translate_tts?ie=UTF-8&tl="+language+"&q="+text + ". h.";

            return kernel.getServerAddress() + "multimedia?action=play&url="+ URLEncoder.encode(url, "UTF-8") + "&interactiveid=" + kernel.getInteractiveId();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
