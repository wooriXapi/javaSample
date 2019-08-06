package com.woorix.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/********************************
 *  우리거래소 OpenAPI 호가 정보
 */

public class depth {
    public static void main(String[] args) {
        String coin = "BTC";
        String market = "KRW";

        String url = Constants.MarketUrl+"/depth/"+coin + "%252f"+market;

        try {
            CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");

            HttpResponse response = httpClient.execute(getRequest);
            Integer responseCode = response.getStatusLine().getStatusCode();
            StringBuffer result = new StringBuffer();
            String line = "";
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while((line = rd.readLine()) != null) {
                result.append(line);
            }

            if(responseCode == 200) {
                System.out.println(result.toString());
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
