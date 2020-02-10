package com.aveneid.myapplication.sqlite;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private Context        mContext;
    private String         mDbPath;
    private String         mDbName;
    private int            mDbVersion;

    public  SQLiteDatabase db;

    public DatabaseHelper(Context context, String dbName, int version) {
        super(context, dbName, null, version);
        mContext   = context;
        mDbPath    = context.getApplicationInfo().dataDir + "/databases/";
        mDbName    = dbName;
        mDbVersion = version;
    }

    public boolean exists() {
        SQLiteDatabase db = null;

        try {
            db = SQLiteDatabase.openDatabase(mDbPath + mDbName, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch (SQLiteException e) {
            //database does not exist yet.
        }

        if (db != null) {
            db.close();
            return true;
        } else {
            return false;
        }
    }

    public void openDatabase(int flag) throws SQLiteException, IOException {
        if (!exists()) {
            if (flag == SQLiteDatabase.OPEN_READONLY) {
                this.getReadableDatabase();
            } else if (flag == SQLiteDatabase.OPEN_READWRITE) {
                this.getWritableDatabase();
            }
            InputStream  iStream = null;
            OutputStream oStream = null;
            try {
                iStream = mContext.getAssets().open(mDbName);
                oStream = new FileOutputStream(mDbPath + mDbName);
                Log.d("database copy path",mContext.getAssets() + mDbName);
                byte[]       buffer  = new byte[1024];
                int          length;

                while ((length = iStream.read(buffer)) > 0) {
                    oStream.write(buffer, 0, length);
                }
            } catch (IOException e) {
                throw e;
            } finally {
                if (iStream != null) {
                    iStream.close();
                }

                if (oStream != null) {
                    oStream.flush();
                    oStream.close();
                }
            }
        }

        try {
            if (flag == SQLiteDatabase.OPEN_READONLY) {
                db = SQLiteDatabase.openDatabase(mDbPath + mDbName, null,
                        SQLiteDatabase.OPEN_READONLY);
            } else if (flag == SQLiteDatabase.OPEN_READWRITE) {
                db = SQLiteDatabase.openDatabase(mDbPath + mDbName, null,
                        SQLiteDatabase.OPEN_READWRITE);
            }
        } catch (SQLiteException e) {
            throw e;
        }
    }

    @Override
    public synchronized void close() {
        if (db != null) {
            db.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long getRows(String table){

        long count = DatabaseUtils.queryNumEntries(db,table);
        db.close();
        return count;
    }
    public void getDatabaseStructure() {

        Cursor c = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table'", null);
        ArrayList<String[]> result = new ArrayList<String[]>();
        int i = 0;
        result.add(c.getColumnNames());
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String[] temp = new String[c.getColumnCount()];
            for (i = 0; i < temp.length; i++) {
                temp[i] = c.getString(i);
                Toast.makeText(mContext,"TABLE - "+temp[i],Toast.LENGTH_LONG).show();

            }
            result.add(temp);
        }
    }
    public Cursor makeQuery(String table, String[] columns){

        Cursor data = db.query(table,columns,null,null,null,null,null,null);
        return data;
    }
}