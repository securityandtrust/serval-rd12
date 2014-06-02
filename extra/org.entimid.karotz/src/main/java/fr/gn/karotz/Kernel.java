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
public class Kernel {

    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }


    private String secretKey;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }


    private String interactiveId;

    public String getInteractiveId() {
        return interactiveId;
    }

    public void setInteractiveId(String interactiveId) {
        this.interactiveId = interactiveId;
    }


    private KarotzSigner signer;

    public KarotzSigner getSigner() {
        return signer;
    }

    public void setSigner(KarotzSigner signer) {
        this.signer = signer;
    }


    private String installId;

    public String getInstallId() {
        return installId;
    }

    public void setInstallId(String installId) {
        this.installId = installId;
    }


    private String serverAddress;

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }
}
