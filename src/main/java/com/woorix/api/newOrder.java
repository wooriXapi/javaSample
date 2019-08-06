package com.woorix.api;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

public class newOrder {

    public static void main(String[] args) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String method = "POST";
        String Url = Constants.TradeUrl+"/newOrder";
        String timeStamp = String.valueOf(timestamp.getTime());

        JSONObject jOrder = new JSONObject();
        jOrder.put("symbol", "BTC/KRW");
        jOrder.put("buySellType", "2");
        jOrder.put("ordPrcType", "2");
        jOrder.put("ordQty","1");
        jOrder.put("ordPrc","13261000");


        try {
            String Sign = Common.Hmac(Constants.SecretKey, jOrder.toString()+"+"+timeStamp);

            URL url = new URL(Url);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(60000); //60 secs
            connection.setReadTimeout(60000); //60 secs
            connection.setRequestMethod(method);
            connection.setRequestProperty("User-Agent","WooriX-API");
            connection.setRequestProperty("api-key", Constants.ApiKey);
            connection.setRequestProperty("api-sign", Sign);
            connection.setRequestProperty("api-nonce", timeStamp);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setDoInput(true);

            DataOutputStream os = new DataOutputStream(connection.getOutputStream());

             byte[] wBuf = jOrder.toString().getBytes("UTF-8");
             os.write(wBuf,0,wBuf.length);
             os.flush();
             os.close();

            Integer code = connection.getResponseCode();
            connection.getResponseMessage();
            String body = "";

            if (code == 200) {
                BufferedReader rd;
                try {
                    rd = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuffer strbuf = new StringBuffer();
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        strbuf.append(line);
                    }
                    body = strbuf.toString();
                    System.out.println(strbuf.toString());
                } catch (Exception e1) {
                    System.out.println(e1.toString());
                }
            } else {
                BufferedReader rd;
                try {
                    rd = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream()));
                    StringBuffer strbuf = new StringBuffer();
                    String line = "";
                    while ((line = rd.readLine()) != null) {
                        strbuf.append(line);
                    }
                    System.out.println(strbuf.toString());
                } catch (Exception e1) {
                    System.out.println(e1.toString());
                }
            }

            connection.disconnect();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
