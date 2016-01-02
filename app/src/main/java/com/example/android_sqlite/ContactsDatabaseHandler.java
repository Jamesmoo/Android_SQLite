package com.example.android_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by jamesadams on 12/23/15.
 * based of the tutorial found here: http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 */
public class ContactsDatabaseHandler extends SQLiteOpenHelper {
    private final String TAG = "SQLiteDBTest";

    //Database version
    private static final int DATABASE_VERSION = 1;
    
    //Database name
    private static final String DATABASE_NAME = "UserContacts";

    //Table name
    private static final String TABLE_NAME = "contacts";

    //Table columns
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";



    public ContactsDatabaseHandler(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }


    /*
     * This method is used when we need to create the table
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        //SQL statement that creates the table only if the table does not exist
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
                                        + KEY_ID    + " INTEGER PRIMARY KEY, "
                                        + KEY_NAME  + " TEXT, "
                                        + KEY_EMAIL + " TEXT, "
                                        + KEY_PHONE + " TEXT)";

        //execute the statement
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    /*
     * This method is used when need to upgrade the table
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop the existing table if it exists
        //note: if you want to keep the data existing in the table, an ALTER TABLE statement
        //will be better
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        //call to the on create method which will re-create the table
        //so on an upgrade the onCreate method will have to be updated to the new functionality
        //so when this method executes the new changes are applied.
        onCreate(db);
    }

    // =============================================================================================
    // CRUD methods
    // =============================================================================================

    public void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        //content values is used to store a set of values that content resolver can process
        ContentValues values = new ContentValues();

        //prepare the values to be inserted
        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_EMAIL, contact.get_email());
        values.put(KEY_PHONE, contact.get_phone());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);

        // Closing database connection
        db.close();
    }


    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Contact contact = null;


        Cursor cursor = db.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_NAME,
                        KEY_EMAIL,
                        KEY_PHONE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);


        if (cursor != null) {
            cursor.moveToFirst();
            Log.d(TAG, "Cursor Record Finder: " + cursor.getCount());

            //cursor.getString is the result coming from the database, and using the result values parse
            //them into a Contact object
            //if the cursor has a record place in a return result

            if (cursor.getCount() == 1){
                contact = new Contact(Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3));
            }
            else if (cursor.getCount() == 0){
                Log.d(TAG, "No Records found for ID : " + id);
            }
        }

        return contact;
    }


    // Getting All Contacts
    public List<Contact> getAllContacts() {

        //list of contact objects, will hold all the rows from the database
        List<Contact> contactList = new ArrayList<Contact>();

        // Select All SQL Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        //connect and run the query on the database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.set_id(Integer.parseInt(cursor.getString(0)));
                contact.set_name(cursor.getString(1));
                contact.set_email(cursor.getString(2));
                contact.set_phone(cursor.getString(3));

                // Adding contact to list
                contactList.add(contact);

            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    //this is an alternate way to get a count from a table without using SELECT COUNT(*)
    public int getContactsCount() {

        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }


    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_EMAIL, contact.get_email());
        values.put(KEY_PHONE, contact.get_phone());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{
                        String.valueOf(contact.get_id())
                });
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[]{
                        String.valueOf(contact.get_id())
                });

        db.close();
    }

    /*
     * this method
     */
    public boolean doesTableExist(){
        boolean tableExists = false;

        String checkTable = "SELECT name FROM " + DATABASE_NAME + " WHERE tbl_name = '" + TABLE_NAME + "';";

        try{
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(checkTable, null);
            cursor.close();

            // return count
            String cursorResult = cursor.getString(0);
            Log.d(TAG, "Cursor String 0: " + cursorResult);
            Log.d(TAG, "Cursor count: " + cursor.getCount());
            if (cursor == null){
                Log.d(TAG, "Cursor is null");
            }

        }
        catch(SQLiteException e){
            Log.d(TAG, "Exception, table obviously does not exist");
        }


        return tableExists;
    }

    public void setupDatabaseWithInitialTestValues(){
        //for loop to create records
        for (int i=0; i <20; i++){

            String name = generateRandomName();
            String email = name + i + "@test.com";
            String phoneNumber = Integer.toString((int) Math.random() * 10000);

            try{
                this.addContact(new Contact(i, name, email, phoneNumber));
            }
            catch (SQLiteException e){
                Log.d(TAG, "SQLite Error: " + e);
                Log.d(TAG,"SQLite Error: at insert=" + i );
                break;
            }
        }
    }


    public String generateRandomName(){
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
