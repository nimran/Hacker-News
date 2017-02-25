package info.hackernews;

import android.app.Application;

import info.hackernews.database.DatabaseAdapter;

/**
 * Created by imran on 25/02/17.
 * <p>
 * HackerNews
 */

public class App extends Application {

    static DatabaseAdapter dbAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        dbAdapter = new DatabaseAdapter();
        try {
            dbAdapter = dbAdapter.open(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DatabaseAdapter getDatabase(){
        return dbAdapter;
    }

}
