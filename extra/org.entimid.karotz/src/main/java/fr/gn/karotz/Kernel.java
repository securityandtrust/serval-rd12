/*
 * Copyright (c) 2011. Gregory Nain.
 */
package fr.gn.karotz;

import fr.gn.karotz.utils.KarotzSigner;

/**
 * Created by IntelliJ IDEA.
  * User: Gregory NAIN
 * Date: 21/05/11
 * Time: 10:18
 */
public abstract class Kernel {

    private static String apiKey;

    public static String getApiKey() {
        return apiKey;
    }

    public static void setApiKey(String apiKey) {
        Kernel.apiKey = apiKey;
    }


    private static String secretKey;

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        Kernel.secretKey = secretKey;
    }


    private static String interactiveId;

    public static String getInteractiveId() {
        return interactiveId;
    }

    public static void setInteractiveId(String interactiveId) {
        Kernel.interactiveId = interactiveId;
    }


    private static KarotzSigner signer;

    public static KarotzSigner getSigner() {
        return signer;
    }

    public static void setSigner(KarotzSigner signer) {
        Kernel.signer = signer;
    }


    private static String installId;

    public static String getInstallId() {
        return installId;
    }

    public static void setInstallId(String installId) {
        Kernel.installId = installId;
    }


    private static String serverAddress;

    public static String getServerAddress() {
        return serverAddress;
    }

    public static void setServerAddress(String serverAddress) {
        Kernel.serverAddress = serverAddress;
    }
}
