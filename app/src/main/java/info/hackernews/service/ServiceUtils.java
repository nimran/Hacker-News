package info.hackernews.service;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import info.hackernews.models.Posts;

/**
 * Created by imran on 25/02/17.
 * <p>
 * HackerNews
 */

public class ServiceUtils {

    private static final String TAG = ServiceUtils.class.getCanonicalName();



    public static Posts bindNewsItem(String response) {
        Posts newsItem = new Posts();
        if(!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                newsItem.set_author(jsonObject.getString("by"));
                if(jsonObject.has("descendants")) {
                    newsItem.set_comments(jsonObject.getInt("descendants"));
                }
                newsItem.set_id(jsonObject.getInt("id"));
                ArrayList<Integer> kidId = new ArrayList<>();
                if(jsonObject.has("kids")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("kids");
                    for(int i = 0;i<jsonArray.length();i++) {
                        kidId.add(jsonArray.getInt(i));
                    }
                    newsItem.setKids(kidId);
                }
                newsItem.set_points(jsonObject.getInt("score"));
                newsItem.set_time(jsonObject.getInt("time"));
                newsItem.set_title(jsonObject.getString("title"));
                newsItem.set_type(jsonObject.getString("type"));
                newsItem.set_url(jsonObject.getString("url"));
                newsItem.setHasDataLoaded(true);
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
        }

        return newsItem;
    }
}
