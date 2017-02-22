package info.hackernews.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Utility functions
 */

public class AppUtils {

    public AppUtils() {
    }


    /**
     * Method to detect the internet availability
     * @param theActivity context
     * @return boolean
     */
    public static Boolean isNetworkAvailable(final Activity theActivity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) theActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
