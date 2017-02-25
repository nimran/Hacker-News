package info.hackernews.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class DatabaseHelper extends SQLiteOpenHelper

{
    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_FILE_PATH = "data/data/info.hackernews/databases";
    private static final String DATABASE_NAME = "hacker.db";
    private static final int OLD_VERSION = 1;
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase database;
    private static DatabaseHelper mInstance = null;


    //table constants
    private final String POSTS = "create table if not exists posts (\n" +
            " id INTEGER,\n" +
            " title  TEXT,\n" +
            " author  TEXT,\n" +
            " points  INTEGER,\n" +
            " comments  INTEGER,\n" +
            " timestamp  INTEGER,\n" +
            " url  TEXT,\n" +
            " type  TEXT,\n" +
            " hasLoaded  INTEGER,\n" +
            " PRIMARY KEY(id)\n" +
            ")";

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(context);
        }
        return mInstance;
    }


    private DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_FILE_PATH + File.separator + DATABASE_NAME, null, DATABASE_VERSION);

        File folder = new File(DATABASE_FILE_PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }
        database = SQLiteDatabase.openDatabase(DATABASE_FILE_PATH + File.separator + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE + SQLiteDatabase.CREATE_IF_NECESSARY);
        database = SQLiteDatabase.openOrCreateDatabase(DATABASE_FILE_PATH + File.separator + DATABASE_NAME, null);
        onCreate(database);
//        onUpgrade(database, OLD_VERSION, DATABASE_VERSION);
    }


    //Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void close() {
        DatabaseUtil.closeDataBase(database);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            database.execSQL(POSTS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SQLiteDatabase getReadableDatabase() {
        database = SQLiteDatabase.openDatabase(DATABASE_FILE_PATH

                        + File.separator + DATABASE_NAME, null,

                SQLiteDatabase.OPEN_READWRITE);
        return database;
    }

    public SQLiteDatabase getWritableDatabase() {
        database = SQLiteDatabase.openDatabase(DATABASE_FILE_PATH

                        + File.separator + DATABASE_NAME, null,

                SQLiteDatabase.OPEN_READWRITE);

        return database;
    }

}
