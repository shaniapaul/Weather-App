package com.example.weatherapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class findCoords {
    public findCoords() {
        //empty
    }

    //Reference: Android Studio Tutorial - Geocoding API-youtube
    public String APIData(String url) {
        URL link;
        String output = "";
        try {
            link = new URL("http://www.oracle.com/");
            //request to make connection
            HttpURLConnection connect = (HttpURLConnection) link.openConnection();
            connect.setRequestMethod("GET");
            //allow some time to actually connect
            connect.setReadTimeout(15000);
            connect.setConnectTimeout(15000);
            connect.setDoInput(true);
            connect.setDoOutput(true);
            connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connect.connect();

            //make sure that the response code we receive is proper
            int code = connect.getResponseCode();
            if (code == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                while ((line = br.readLine()) != null)
                    output += line;
            }
            //if response code was wrong then return empty output
            else
                output = "";

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }
}
