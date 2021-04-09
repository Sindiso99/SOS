package com.swansea.sindiso;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHandler extends SQLiteOpenHelper {

    public static final String USER_TABLE = "USER_TABLE";
    public static final String HOLDER_SPACE_TABLE = "HOLDER_SPACE_TABLE";
    public static final String ADDRESS_TABLE = "ADDRESS_TABLE";
    public static final String COLUMN_USER_NAME = "USER_NAME";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_STUDENT = "STUDENT";
    public static final String COLUMN_ID = "ID";
    private String createTableStatement;
    private String query;
    private SQLiteDatabase db;
    private Cursor cursor;
    private ContentValues cv;
    private long insert;
    public static final String CONTAINER_TABLE = "CONTAINER_TABLE";
    public static final String COLUMN_CONTAINER_ID = "BOX_ID";
    public static final String COLUMN_LABEL = "LABEL";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_LENGTH = "LENGTH";
    public static final String COLUMN_HEIGHT = "HEIGHT";
    public static final String COLUMN_WIDTH = "WIDTH";
    public static final String COLUMN_ADDRESS = "ADDRESS";
    public static final String COLUMN_LONGITUDE = "LONGITUDE";
    public static final String COLUMN_LATITUDE = "LATITUDE";
    public static final String COLUMN_EMAIL = "EMAIL";

    public DataBaseHandler(Context context) {
        super(context, "Users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTableStatement = "CREATE TABLE "
                + USER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_USER_NAME + " TEXT UNIQUE, "
                + COLUMN_PASSWORD + " TEXT, "
                + COLUMN_STUDENT + " BOOL)";
        db.execSQL(createTableStatement);

        createTableStatement = "CREATE TABLE "
                + CONTAINER_TABLE + " (" + COLUMN_CONTAINER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ID + " INTEGER, "
                + COLUMN_LABEL + " TEXT, "
                + COLUMN_LENGTH + " INTEGER, "
                + COLUMN_HEIGHT + " INTEGER, "
                + COLUMN_WIDTH + " INTEGER, "
                + COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createTableStatement);

        createTableStatement = "CREATE TABLE "
                + HOLDER_SPACE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_LENGTH + " INTEGER, "
                + COLUMN_HEIGHT + " INTEGER, "
                + COLUMN_WIDTH + " INTEGER, "
                + COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createTableStatement);

        createTableStatement = "CREATE TABLE "
                + ADDRESS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_ADDRESS + " TEXT, "
                + COLUMN_LATITUDE + " REAL, "
                + COLUMN_LONGITUDE + " REAL, "
                + COLUMN_EMAIL + " TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUser(User user){
        db = this.getWritableDatabase();
        cv = new ContentValues();
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

    public boolean addContainer(Container container, boolean existingContainer){
        db = this.getWritableDatabase();
        cv = new ContentValues();
        cv.put(COLUMN_ID, container.getOwnerId());
        cv.put(COLUMN_LABEL, container.getLabel());
        cv.put(COLUMN_LENGTH, container.getLength());
        cv.put(COLUMN_HEIGHT, container.getHeight());
        cv.put(COLUMN_WIDTH, container.getWidth());
        cv.put(COLUMN_DESCRIPTION, container.getDescription());
        if (existingContainer) {
            insert = db.update(CONTAINER_TABLE, cv, COLUMN_CONTAINER_ID + "=?", new String[]{container.getId().toString()});
        } else {
            insert = db.insert(CONTAINER_TABLE, null, cv);
        }
        if(insert ==-1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addHolderSpace(Space space, boolean existingSpace){
        db = this.getWritableDatabase();
        cv = new ContentValues();
        cv.put(COLUMN_ID, space.getOwnerId());
        cv.put(COLUMN_LENGTH, space.getLength());
        cv.put(COLUMN_HEIGHT, space.getHeight());
        cv.put(COLUMN_WIDTH, space.getWidth());
        cv.put(COLUMN_DESCRIPTION, space.getDescription());
        if (existingSpace) {
            insert = db.update(HOLDER_SPACE_TABLE, cv, COLUMN_ID + "=?", new String[]{space.getOwnerId().toString()});
        } else {
            insert = db.insert(HOLDER_SPACE_TABLE, null, cv);
        }
        if(insert ==-1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean addAddress(User user, boolean existingAddress){
        db = this.getWritableDatabase();
        cv = new ContentValues();
        cv.put(COLUMN_ID, user.getId());
        cv.put(COLUMN_ADDRESS, user.getFullAddress());
        cv.put(COLUMN_LATITUDE, user.getLocation().latitude);
        cv.put(COLUMN_LONGITUDE, user.getLocation().longitude);
        cv.put(COLUMN_EMAIL, user.getEmail());
        if (existingAddress) {
            insert = db.update(ADDRESS_TABLE, cv, COLUMN_ID + "=?", new String[]{user.getId().toString()});
        } else {
            insert = db.insert(ADDRESS_TABLE, null, cv);
        }
        if(insert ==-1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteContainer(Integer boxId){
        db = this.getWritableDatabase();
        query = "DELETE FROM " + CONTAINER_TABLE + " WHERE " + COLUMN_CONTAINER_ID + " = '" + boxId + "'";
        cursor = db.rawQuery(query, null);
        if(!cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return  true;
        } else {
            cursor.close();
            db.close();
            return false;
        }
    }

    public User getUserDetails(String name){
        User user;
        query = "SELECT * FROM " + USER_TABLE + " WHERE " + COLUMN_USER_NAME + " = '" + name + "'";
        db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
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

    public Space getHolderSpace(Integer owner) {
        Space space;
        query = "SELECT * FROM " + HOLDER_SPACE_TABLE + " WHERE " + COLUMN_ID + " = " + owner;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            Integer ownerId = cursor.getInt(0);
            Integer length = cursor.getInt(1);
            Integer height = cursor.getInt(2);
            Integer width = cursor.getInt(3);
            String description = cursor.getString(4);
            space = new Space(ownerId, length, height, width, description);
        } else {
            //no Space case
            space = new Space(-1, 0, 0, 0, "Provide a Description for your Space");
        }
        cursor.close();
        db.close();
        return space;
    }

    public User getAddressData(User user) {
        query = "SELECT * FROM " + ADDRESS_TABLE + " WHERE " + COLUMN_ID + " = " + user.getId();
        db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            user.setFullAddress(cursor.getString(1));
            double latitude = cursor.getDouble(2);
            double longitude = cursor.getDouble(3);
            user.setEmail(cursor.getString(4));
            user.setLocation(latitude, longitude);
        }
        cursor.close();
        db.close();
        return user;
    }
    public List<Container> getContainers(Integer owner) {
        Container container;
        List<Container> containers = new ArrayList<>();
        query = "SELECT * FROM " + CONTAINER_TABLE + " WHERE " + COLUMN_ID + " = " + owner;
        db = this.getReadableDatabase();
        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Integer containerId = cursor.getInt(0);
                Integer ownerId = cursor.getInt(1);
                String label = cursor.getString(2);
                Integer length = cursor.getInt(3);
                Integer height = cursor.getInt(4);
                Integer width = cursor.getInt(5);
                String description = cursor.getString(6);
                container = new Container(containerId, label, length, height, width, description, ownerId);
                containers.add(container);
            } while (cursor.moveToNext());
        } else {
            //in the event of failure to select, empty values used
            container = new Container(0 , "no Containers", 0,0, 0, "no description", 0);
            containers.add(container);
        }
        cursor.close();
        db.close();
        return containers;
    }

//    public Container getContainerDetails(Integer id) {
//
//    }

}
