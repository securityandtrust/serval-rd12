/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz;

import fr.gn.karotz.msg.InteractiveModeMessage;
import fr.gn.karotz.msg.ResponseMessage;
import fr.gn.karotz.msg.ServerAnswer;
import fr.gn.karotz.session.InteractiveAction;
import fr.gn.karotz.session.InteractiveCommand;
import fr.gn.karotz.utils.KarotzCommand;
import fr.gn.karotz.utils.KarotzSigner;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * Created by IntelliJ IDEA.
 * User: Gregory NAIN
 * Date: 20/05/11
 * Time: 17:26
 * User: Gregory NAIN
 */
public class Karotz implements KarotzSigner {


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(Karotz.class);

    public Karotz(String apiKey, String secretKey, String installId) {
        Kernel.setApiKey(apiKey);
        Kernel.setSecretKey(secretKey);
        Kernel.setInstallId(installId);
        Kernel.setSigner(this);
        Kernel.setServerAddress("http://api.karotz.com/api/karotz/");

    }

    public boolean isSessionActive() {
        return Kernel.getInteractiveId() != null;
    }

    public boolean initSession() {
        if(isSessionActive()) {
            return true;
        } else {
            InteractiveCommand startSession = new InteractiveCommand(InteractiveAction.START);
            ServerAnswer answ = send(startSession);
            if( answ instanceof InteractiveModeMessage) {
                InteractiveModeMessage msg = (InteractiveModeMessage)answ;
                if(msg.getInteractiveId() != null) {
                    Kernel.setInteractiveId(msg.getInteractiveId());
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }
    }

    public boolean closeSession() {
        if(isSessionActive()) {
            InteractiveCommand closeSession = new InteractiveCommand(InteractiveAction.STOP);
            ResponseMessage closedMessage = (ResponseMessage) send(closeSession);
            if(closedMessage.getCode() == ResponseMessage.ResponseCode.OK){
                Kernel.setInteractiveId(null);
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }


    public ServerAnswer send(KarotzCommand command) {
        try {

            logger.debug("Sending request: " + command.getCommand());
            URL serverInteractiveIdQuery = new URL(command.getCommand());

            HttpURLConnection connection = (HttpURLConnection) serverInteractiveIdQuery.openConnection();

            BufferedReader br;
            if (connection.getResponseCode() >= 500) {
                logger.warn("Error code returned: " + connection.getResponseCode());
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                String line;
                while ((line = br.readLine()) != null) {
                    logger.error(line);
                }

            } else {
                ServerAnswer answer = ServerAnswer.parse(connection.getInputStream());
                logger.debug("\nServerAnswer: " + answer.toString() + "\n==============================\n");

                return answer;
            }

        } catch (IOException e) {
            //e.printStackTrace();
        }

        return null;
    }

    public String sign(String parameters) {

        try {

            SecretKeySpec signingKey = new SecretKeySpec(Kernel.getSecretKey().getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            byte[] rawHmac = mac.doFinal(parameters.getBytes("UTF-8"));

            return parameters + "&signature=" + URLEncoder.encode(new BASE64Encoder().encode(rawHmac), "UTF8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

}
