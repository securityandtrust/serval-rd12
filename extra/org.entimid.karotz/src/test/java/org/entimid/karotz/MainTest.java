/*
 * Copyright (c) 2011. Gregory Nain.
 */
package org.entimid.karotz;

import sun.misc.BASE64Encoder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by IntelliJ IDEA.
 * User: gnain
 * Date: 18/05/11
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class MainTest {

    //API KEY :: apikey   c15870d2-a15e-4c8f-95bf-4a16385f6c81
    //SECRET::  Secret key: 5477b6c0-7d50-4ba1-b40a-45fbb324e853


    //@Test
    public void getInteractiveIDUsingStartMethod() {


        try {

            String parameterChain = "";
            String secretKey = "87683e1e-c478-4c2a-b46e-f22bd6aca916";

            String serverAddress = "http://api.karotz.com/api/karotz/start?";

            parameterChain += "apikey=" + URLEncoder.encode("ae5e7b12-bea8-4c5a-80d5-cd88576a95e5", "UTF-8");

            parameterChain += "&installid=" + URLEncoder.encode("2b96ef18-b795-4b62-b528-29019c0e84d7", "UTF8");

            parameterChain += "&once=" + (long) (Math.random() * 1000000000);
            parameterChain += "&timestamp=" + (System.currentTimeMillis()/1000);

            /*Mac mac = Mac.getInstance("HmacSHA1");
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
            mac.init(keySpec);
            byte[] digest = mac.doFinal(parameterChain.getBytes());


            BASE64Encoder bencoder = new BASE64Encoder();
            */


            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA1");

// get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

// compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(parameterChain.getBytes("UTF-8"));


            parameterChain += "&signature=" + URLEncoder.encode(new BASE64Encoder().encode(rawHmac), "UTF8");//URLEncoder.encode(computeHmac(parameterChain,secretKey));

            System.out.println("Address: " + serverAddress + parameterChain);

            URL serverInteractiveIdQuery = new URL(serverAddress + parameterChain);

            HttpURLConnection connection = (HttpURLConnection) serverInteractiveIdQuery.openConnection();

            BufferedReader br = null;
            if (connection.getResponseCode() >= 500) {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }


            String line = "";

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public String computeHmac(String baseString, String key)
            throws NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, UnsupportedEncodingException {
        Mac mac = Mac.getInstance("HmacSHA1");
        SecretKeySpec secret = new SecretKeySpec(key.getBytes(), mac.getAlgorithm());
        mac.init(secret);
        byte[] digest = mac.doFinal(baseString.getBytes());
        return new BASE64Encoder().encode(digest);
    }

}
