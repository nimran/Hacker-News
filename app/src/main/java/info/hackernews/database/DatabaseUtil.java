package info.hackernews.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseUtil
{
    public static void closeCursor(Cursor cursor)
    {
        if (cursor != null)
        {
            cursor.close();
        }
    }

    public static void closeDataBase(SQLiteDatabase database)
    {
        if (database != null)
        {
            database.close();
        }
    }
}