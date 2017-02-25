package info.hackernews.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.AbsoluteSizeSpan;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

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

    /**
     * @param point no.of.points
     * @return returns the Charsequence of Same string with different sizes
     */
    public static CharSequence getPoints(int point) {
        String text1 = String.valueOf(point);
        String text2 = point > 1 ? "Points" : "Point";
        SpannableString span1 = new SpannableString(text1);
        span1.setSpan(new AbsoluteSizeSpan(70), 0, text1.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString span2 = new SpannableString(text2);
        span2.setSpan(new AbsoluteSizeSpan(35), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return TextUtils.concat(span1, "\n", span2);
    }

    /**
     * @param count no.of.comments
     * @return returns the Charsequence of Same string with different sizes
     */
    public static CharSequence getCommentsCount(int count) {
        String text1 = String.valueOf(count);
        String text2 = count > 1 ? "Comments" : "Comment";
        SpannableString span1 = new SpannableString(text1);
        span1.setSpan(new AbsoluteSizeSpan(70), 0, text1.length(), SPAN_INCLUSIVE_INCLUSIVE);
        SpannableString span2 = new SpannableString(text2);
        span2.setSpan(new AbsoluteSizeSpan(35), 0, text2.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return TextUtils.concat(span1, "\n", span2);
    }

    /**
     * gives relative time
     * @param time timestamp in long
     * @return the time in relative.
     */
    public static CharSequence getRelativeTime(long time) {
        return DateUtils.getRelativeTimeSpanString(
                time,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
    }

    public static String getFeedUrl(int id) {
        return "https://hacker-news.firebaseio.com/v0/item/"+id+".json?print=pretty";
    }
}
