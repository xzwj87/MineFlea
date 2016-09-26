package com.github.xzwj87.mineflea.net;

import android.util.Log;
import android.widget.TabHost;

import com.github.xzwj87.mineflea.model.GoodsModel;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jason on 9/25/16.
 */

// TODO: if used asynchronously, should implement Callable<T> interface
public class HttpUrlApi {
    public static final String TAG = HttpUrlApi.class.getSimpleName();

    private static final String CONTENT_TYPE_LABEL = "Content-type";
    private static final String CONTENT_TYPE_VALUE_JSON = "application/json; charset-utf-8";

    private URL mUrl;
    private String mResponse;
    private OkHttpClient mHttpClient;

    private HttpUrlApi(String url) throws MalformedURLException {
        mUrl = new URL(url);
        mHttpClient = createHttpClient();
    }

    public HttpUrlApi create(String url) throws MalformedURLException{
        return new HttpUrlApi(url);
    }

    public String getData(){
        return connectToGet();
    }

    public String postData(String json){
        return  connectToPost(json);
    }

    private String connectToGet(){
        Log.v(TAG,"connectToGet()");

        Request request = new Request.Builder()
                .url(mUrl)
                .addHeader(CONTENT_TYPE_LABEL,CONTENT_TYPE_VALUE_JSON)
                .get()
                .build();

        try {
            Response resp = mHttpClient.newCall(request).execute();

            return resp.body().string();
        }catch (IOException e){
            Log.e(TAG,"connectToGet():fail to get data");
            e.printStackTrace();

            return null;
        }
    }

    private String connectToPost(String json){
        Log.v(TAG,"connectToPost()");

        final MediaType mediaType = MediaType.parse(CONTENT_TYPE_VALUE_JSON);
        RequestBody requestBody = RequestBody.create(mediaType,json);
        Request request = new Request.Builder()
                .url(mUrl)
                .post(requestBody)
                .build();

        try {
            Response resp = mHttpClient.newCall(request).execute();

            return resp.body().string();
        }catch (IOException e){
            Log.e(TAG,"connectToPost(): fail to post data");
            e.printStackTrace();

            return null;
        }


    }

    private OkHttpClient createHttpClient(){
        final OkHttpClient client = new OkHttpClient();

        OkHttpClient.Builder builder = client.newBuilder();
        builder.readTimeout(10000, TimeUnit.MILLISECONDS)
               .writeTimeout(10000, TimeUnit.MILLISECONDS)
               .connectTimeout(15000, TimeUnit.MILLISECONDS);


        return client;
    }




}
