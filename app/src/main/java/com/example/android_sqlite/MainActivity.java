package com.example.android_sqlite;

import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "SQLiteDBTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //this.setupDatabaseWithInitialTestValues();

        ContactsDatabaseHandler contactsDB = new ContactsDatabaseHandler(this);

        if (!contactsDB.doesTableAlreadyHaveEntries()){

        }


    }


    public void setupDatabaseWithInitialTestValues(ContactsDatabaseHandler db){

        //for loop to create records
        for (int i=0; i <20; i++){

            String name = generateRandomName();
            String email = name + i + "@test.com";
            String phoneNumber = Integer.toString((int) Math.random() * 10000);

            try{
                db.addContact(new Contact(i, name, email, phoneNumber));
            }
            catch (SQLiteException e){
                Log.d(TAG, "SQLite Error: " + e);
                Log.d(TAG,"SQLite Error: at insert=" + i );
                break;
            }
        }
    }

    private String generateRandomName(){
        String name = "";

        //generate 10 random characters and put them in a string, this is the name
        for (int i=0; i < 10; i++){
            Random r = new Random();
            char c = (char)(r.nextInt(26) + 'a');
            name += Character.toString(c);
        }
        Log.d(TAG, "Random name generated = " + name);
        return name;
    }



}
