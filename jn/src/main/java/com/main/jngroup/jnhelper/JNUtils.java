package com.main.jngroup.jnhelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by nove1398 on 3/26/2014.
 */
public class JNUtils {

    /**
     * Returns state of internet connectivity
     * @param context context used to call the system service
     * @return true on/false off
     */
    public static boolean hasInternetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    /**
     * Fetch image from url and return bitmap from inputstream
     * called on main thread still
     */
    public static Bitmap fetchBitmapFromUrl(String url){
        Bitmap bmp = null;
        URL newurl = null;
        try {
            newurl = new URL(url);
        } catch( MalformedURLException e ) {
            e.printStackTrace();
        }
        try {
            bmp = BitmapFactory.decodeStream( newurl.openConnection().getInputStream() );
        } catch( IOException e ) {
            e.printStackTrace();
        }
        return bmp;
    }

    /**
     * Requests and parses response from server to json objects
     */
    public static String getJsonFromUrl( String url, int method, List<NameValuePair> params ){

        String response = null;
        try {
            // http client
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpEntity httpEntity;
            HttpResponse httpResponse = null;

            // Checking http request method type
            if (method == AppConstant.POST_REQUEST) {
                HttpPost httpPost = new HttpPost(url);
                // adding post params
                if (params != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }

                httpResponse = httpClient.execute(httpPost);

            } else if (method == AppConstant.GET_REQUEST) {
                // appending params to url
                if (params != null) {
                    String paramString = URLEncodedUtils.format( params, "utf-8" );
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);

                httpResponse = httpClient.execute(httpGet);
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString( httpEntity );

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }
}
