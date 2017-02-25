package info.hackernews.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import info.hackernews.models.Posts;

/**
 * Created by imran on 25/02/17.
 * <p>
 * HackerNews
 */

public class DatabaseAdapter {
    String todayDate;
    Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;


    public DatabaseAdapter open(Context ctx) throws Exception {
        databaseHelper = DatabaseHelper.getInstance(ctx);
        db = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public void savePosts(Posts post) {
        ContentValues newValues = new ContentValues();
        // Assign values for each row.
        newValues.put("id", post.get_id());
        newValues.put("title", post.get_title());
        newValues.put("author", post.get_author());
        newValues.put("points", post.get_points());
        newValues.put("comments", post.get_comments());
        newValues.put("timestamp", post.get_time());
        newValues.put("url", post.get_url());
        newValues.put("type", post.get_type());
        newValues.put("hasLoaded", post.isHasDataLoaded());

        try {
            db.insertOrThrow("posts", null, newValues);
        } catch (SQLiteConstraintException sqlExc) {
            db.update("posts", newValues, "id" + " = ?", new String[]{String.valueOf(post.get_id())});
        } catch (Exception e) {
            System.out.println("exception" + e);
        }
    }

    public Posts getPost(int id) {
        Posts posts = null;
        String selectQuery = "SELECT * FROM posts where id = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                posts = new Posts();
                posts.set_id(cursor.getInt(cursor.getColumnIndex("id")));
                posts.set_title(cursor.getString(cursor.getColumnIndex("title")));
                posts.set_author(cursor.getString(cursor.getColumnIndex("author")));
                posts.set_points(cursor.getInt(cursor.getColumnIndex("points")));
                posts.set_comments(cursor.getInt(cursor.getColumnIndex("comments")));
                posts.set_time(cursor.getInt(cursor.getColumnIndex("timestamp")));
                posts.set_url(cursor.getString(cursor.getColumnIndex("url")));
                posts.set_type(cursor.getString(cursor.getColumnIndex("type")));
                int intValue = (cursor.getInt(cursor.getColumnIndex("hasLoaded")));
                Boolean flag2 = (intValue == 1);
                posts.setHasDataLoaded(flag2);
            }while (cursor.moveToNext());
        }


        cursor.close();
        return posts;
    }
}

