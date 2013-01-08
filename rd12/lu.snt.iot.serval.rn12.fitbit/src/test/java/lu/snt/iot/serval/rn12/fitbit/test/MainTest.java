package lu.snt.iot.serval.rn12.fitbit.test;/*
* Author : Gregory Nain (developer.name@uni.lu)
* Date : 07/01/13
* (c) 2012 University of Luxembourg – Interdisciplinary Centre for Security Reliability and Trust (SnT)
* All rights reserved
*/

import com.fitbit.api.FitbitAPIException;
import com.fitbit.api.client.*;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthConsumer;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainTest {

    public String apiBaseUrl, clientConsumerKey, clientSecret, fitbitSiteBaseUrl;

    public static void main(String[] args) {

        MainTest test = new MainTest();

        test.apiBaseUrl = "http://api.fitbit.com";
        test.clientConsumerKey = "6406dc74efb74869b06c7ca2e9304874";
        test.clientSecret = "403164b702c542bcb0faa8da71392a11";
        test.fitbitSiteBaseUrl = "http://www.fitbit.com";


        // create a consumer object and configure it with the access
        // token and token secret obtained from the service provider
        OAuthConsumer consumer = new DefaultOAuthConsumer(test.clientConsumerKey,
                test.clientSecret);

        consumer.setTokenWithSecret("f638d372a38c7a9e4f76740528199cbe", "9c384ac9c5496be345e41a78a3d2e836");


        OAuthProvider provider = new DefaultOAuthProvider(
                "http://api.fitbit.com/oauth/request_token", "http://api.fitbit.com/oauth/access_token",
                "http://www.fitbit.com/oauth/authorize");

        try {

           /*
            String url = provider.retrieveRequestToken(consumer, "oob");
            System.out.println("Please connect to " + url + " and authorize !. Enter PIN code:");

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            String pin = input.readLine();


            provider.retrieveAccessToken(consumer, pin);
            */

            System.out.println("Token: " + consumer.getToken() + " Secret:" + consumer.getTokenSecret());

            // create an HTTP request to a protected resource
            URL url2 = new URL(test.apiBaseUrl + "/1/user/-/profile.json");
            HttpURLConnection request = (HttpURLConnection) url2.openConnection();
            // sign the request
            consumer.sign(request);

            // send the request
            request.connect();

            BufferedReader response = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line;
            while((line = response.readLine()) != null) {
                System.out.println(line);
            }



        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

}
