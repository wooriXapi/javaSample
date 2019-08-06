package com.woorix.api;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

public class cancelOrder {

    public static void main(String[] args) {

        if(args.length != 1) {
            System.out.println("cancelOrder orderNo");
            System.exit(0);
        }
        String ordNo = args[0];

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String method = "POST";
        String Url = Constants.TradeUrl+ "/cancelOrder";
        String timeStamp = String.valueOf(timestamp.getTime());

        JSONObject jCancel = new JSONObject();
        jCancel.put("orgOrdNo", ordNo);


        try {
            String Sign = Common.Hmac(Constants.SecretKey, jCancel.toString()+"+"+timeStamp);

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

            byte[] wBuf = jCancel.toString().getBytes("UTF-8");
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
