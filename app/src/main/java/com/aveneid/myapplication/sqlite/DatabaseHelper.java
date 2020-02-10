package com.aveneid.myapplication.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    private Context context;
    public DatabaseHelper(Context c) {
       super(c, "dataset", null, 1);
        this.context = c;
    }


    @Override
    public void onCreate(SQLiteDatabase db){}

    @Override
    public void onUpgrade(SQLiteDatabase db ,int i, int j){}


    public long getRows(String table){
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db,table);
        db.close();
        return count;
    }

    public Cursor makeQuery(String table, String[] columns){
        SQLiteDatabase db = getReadableDatabase();
        Cursor data = db.query(table,columns,null,null,null,null,null,null);
        return data;
    }
}