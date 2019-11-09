package org.xxpay.service.channel.fuyou;

import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class FuyouPayUtil {

    static OkHttpClient fuyou_http_client = null;

    static {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();
        fuyou_http_client = okHttpBuilder.connectionPool(new ConnectionPool(5,5, TimeUnit.SECONDS)).connectTimeout(10,TimeUnit.SECONDS).build();
    }

    public static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");

    public static String httpGet(String url){
        String result = "";
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        try {
            response = fuyou_http_client.newCall(request).execute();
            result = new String(response.body().bytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String httpPost(String url,String json){
        String result = "";
        RequestBody requestBody = RequestBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = null;
        try {
            response = fuyou_http_client.newCall(request).execute();
            result = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }


}
