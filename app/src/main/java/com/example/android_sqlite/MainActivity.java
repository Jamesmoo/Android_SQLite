package com.example.android_sqlite;

import android.database.sqlite.SQLiteDatabase;
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

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "SQLiteDBTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.setupDatabaseWithInitialTestValues();

        Log.d(TAG, "Application Launched");
        ContactsDatabaseHandler contactsDB = new ContactsDatabaseHandler(this);

        //Contact contact =  new Contact(1,"test","test@me.com","555-222-2222");
        //contactsDB.addContact(contact);
        //Log.d(TAG, "Contact inserted");

        //contactsDB.setupDatabaseWithInitialTestValues();


        // == TEST COUNT ALL IN TABLE == //
        int countContacts = contactsDB.getContactsCount();
        Log.d(TAG, "Contact Count is " + countContacts);


        // == TEST GET CONTACT BY ID == //
        Contact myContact = contactsDB.getContact(10);
        String contactResult = "ID: " + Integer.toString(myContact.get_id());
        contactResult += " - NAME: " + myContact.get_name();
        contactResult += " - EMAIL: " + myContact.get_email();
        contactResult += " - PHONE: " + myContact.get_phone();
        Log.d(TAG, "getting contact => " + contactResult );


        // == TEST GET ALL CONTACTS IN TABLE ==//
        int lastIdUsed = -1;
        List<Contact> allContactsList = contactsDB.getAllContacts();
        for(Contact singleContact : allContactsList){

            String result = "ID: " + Integer.toString(singleContact.get_id());
            result += " - NAME: " + singleContact.get_name();
            result += " - EMAIL: " + singleContact.get_email();
            result += " - PHONE: " + singleContact.get_phone();

            Log.d(TAG, "From Contact List => " + result );

            //the id of the last element on the list will not be overwritten, this is used for
            //the contact delete test
            lastIdUsed = singleContact.get_id();
        }


        // == TEST UPDATE == //
        //using the same contact object created in the TEST GET CONTACT BY ID portion
        Log.d(TAG, "Contact info BEFORE Update => " + contactResult);

        String newRandomName = contactsDB.generateRandomName();
        myContact.set_name(newRandomName);
        myContact.set_email(newRandomName  + "@test.com");
        
        contactsDB.updateContact(myContact);

        Contact myContactUpdated = contactsDB.getContact(10);
        String updateContactResult = "ID: " + Integer.toString(myContactUpdated.get_id());
        updateContactResult += " - NAME: " + myContactUpdated.get_name();
        updateContactResult += " - EMAIL: " + myContactUpdated.get_email();
        updateContactResult += " - PHONE: " + myContactUpdated.get_phone();
        Log.d(TAG, "Contact info AFTER Update => " + updateContactResult);


        // == TEST DELETE CONTACT == //
        Contact testDeleteContact = new Contact(lastIdUsed + 1,"testDelete", "testDelete@test.com", "123-123-1234");
        contactsDB.addContact(testDeleteContact);
        Contact confirmInsert = contactsDB.getContact(lastIdUsed + 1);
        Log.d(TAG, "Confirm Insert of Record for Testing DELETE  - name -" + confirmInsert.get_name() + " - email - " + confirmInsert.get_email() + " - ID - " + confirmInsert.get_id());

        contactsDB.deleteContact(testDeleteContact);
        Log.d(TAG, "Contact Deleted");

        int deletelastIdUsed = -1;
        List<Contact> allNewContactsList = contactsDB.getAllContacts();
        for(Contact singleContact : allNewContactsList){

            String result = "ID: " + Integer.toString(singleContact.get_id());
            result += " - NAME: " + singleContact.get_name();
            result += " - EMAIL: " + singleContact.get_email();
            result += " - PHONE: " + singleContact.get_phone();

            Log.d(TAG, "From Contact List => " + result );

            //the id of the last element on the list will not be overwritten, this is used for
            //the contact delete test
            deletelastIdUsed = singleContact.get_id();
        }

        Log.d(TAG, "Last ID of contact " + deletelastIdUsed);

        //final delete check, get a contact using the ID
        Contact confirmDeletion = contactsDB.getContact(lastIdUsed + 1);

        if (confirmDeletion == null){
            Log.d(TAG, "Database returned no record");

        }
        else{
            Log.d(TAG, "OH NO!!! database returned a record - should have been deleted!!");
            Log.d(TAG, "OH NO!!! ID of record: " + confirmDeletion.get_id());
        }

    }


}
