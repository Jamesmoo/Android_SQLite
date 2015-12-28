package com.example.android_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamesadams on 12/23/15.
 * based of the tutorial found here: http://www.androidhive.info/2011/11/android-sqlite-database-tutorial/
 */
public class DatabaseHandler extends SQLiteOpenHelper {

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



    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }


    /*
     * This method is used when we need to create the table
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL statement that creates the table
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
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

        Cursor cursor = db.query(TABLE_NAME, new String[]{
                        KEY_ID,
                        KEY_NAME,
                        KEY_EMAIL,
                        KEY_PHONE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        //cursor.getString is the result coming from the database, and using the result values parse
        //them into a Contact object
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3));

        // return contact
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


    public int getContactsCount() {
        //TODO: does SQLite support SELECT COUNT(*) ?
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.get_name());
        values.put(KEY_NAME, contact.get_email());
        values.put(KEY_NAME, contact.get_phone());

        // updating row
        return db.update(TABLE_NAME, values, KEY_ID + " = ?",
                new String[]{
                        String.valueOf(contact.get_id())
                });
    }

    public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] {
                        String.valueOf(contact.get_id())
                });

        db.close();
    }
}
