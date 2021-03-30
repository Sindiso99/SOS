package com.swansea.sindiso;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String USER_TABLE = "USER_TABLE";
    public static final String USER_TYPE_TABLE = "USER_TYPE_TABLE";
    public static final String COLUMN_USER_NAME = "USER_NAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_STUDENT = "STUDENT";
    public static final String COLUMN_ID = "ID";
    private String createTableStatement;

    public DataBaseHandler(Context context) {
        super(context, "Users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableStatement = "CREATE TABLE " + USER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME + " TEXT UNIQUE, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_STUDENT + " BOOL)";
        db.execSQL(createTableStatement);
    }

    public void newContainerDb(Integer id) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_NAME, user.getUserName());
        cv.put(COLUMN_PASSWORD, user.getPassword());
        cv.put(COLUMN_STUDENT, user.isStudent());
        long insert = db.insert(USER_TABLE, null, cv);
        if(insert ==-1) {
            return false;
        } else {
            return true;
        }
    }

//    public boolean addContainer(Container container){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//    }

    public User getDetails(String name){
        User user;
        String query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USER_NAME + " = '" + name + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
                int id = cursor.getInt(0);
                String userName = cursor.getString(1);
                String password = cursor.getString(2);
                Boolean status = cursor.getInt(3) == 1;
                user = new User(id, userName, password, status);
        } else {
            //failure case
            user = new User(-1, "error", "error", false);
        }
        cursor.close();
        db.close();
        return user;
    }
}
