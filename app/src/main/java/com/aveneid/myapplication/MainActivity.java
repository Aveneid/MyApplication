package com.aveneid.myapplication;

import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;

import android.os.Debug;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.aveneid.myapplication.sqlite.DatabaseHelper;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.app.PendingIntent.getActivities;
import static android.app.PendingIntent.getActivity;
import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    public int randomCuriosityId;
    private AppBarConfiguration mAppBarConfiguration;


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt("randomCuriosityId", randomCuriosityId);
        super.onSaveInstanceState(savedInstanceState);
    }
    void listfiles( String p){
        String path = getApplicationInfo().dataDir + p;
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        File[] files = directory.listFiles();
        Log.d("Files", "Size: "+ files.length);
        for (int i = 0; i < files.length; i++)
        {
            Log.d("Files", "FileName:" + files[i].getName());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context contextNew = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DatabaseHelper db = new DatabaseHelper(contextNew,"dataset.db",1);

        try {
            db.openDatabase(SQLiteDatabase.OPEN_READONLY );
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),e.toString() + "ioexception",Toast.LENGTH_SHORT).show();
        } catch (SQLiteException e) {
            Toast.makeText(getApplicationContext(),e.toString()+"sqliteexception",Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getApplicationContext(),db.getDatabaseName(),Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(),db.getReadableDatabase().toString(),Toast.LENGTH_SHORT).show();
        db.getDatabaseStructure();
/*
        if (savedInstanceState != null)
            randomCuriosityId = savedInstanceState.getInt("randomCuriosityId");
        else {
            randomCuriosityId = (int) (Math.random() * db.getRows("curiosities") + 1);
            Toast.makeText(getApplicationContext(),""+randomCuriosityId,Toast.LENGTH_SHORT).show();
        }

       /* FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,R.id.nav_search,R.id.nav_categories,R.id.nav_settings,R.id.nav_about,R.id.nav_exit)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
