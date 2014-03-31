package com.main.jngroup.jnhelper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by nove1398 on 3/26/2014.
 */
public class JNUtils {

    /**
     * Returns state of internet connectivity
     * @param context context used to call the system service
     * @return true on/false off
     */
    public static boolean hasIntenernetConnection(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService( Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }
}
