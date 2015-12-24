package com.example.android_sqlite;

/**
 * Created by jamesadams on 12/23/15.
 *
 * NOTE: this object defines a database table entry.
 *      This table has the following elements:
 *          - ID
 *          - Name
 *          - Email
 *          - Phone
 *
 *     Think of this object as "if you do a select statement on a single row, what is the return from
 *      that statement"
 */
public class Contact {

    private int _id;
    private String _name;
    private String _email;
    private String _phone;

    public Contact(int id, String name, String email, String phone){
        _id = id;
        _name = name;
        _email = email;
        _phone = phone;
    }

    // == GETTERS == //
    public int get_id() {
        return _id;
    }

    public String get_name() {
        return _name;
    }

    public String get_email() {
        return _email;
    }

    public String get_phone() {
        return _phone;
    }

    // == SETTERS == //
    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public void set_phone(String _phone) {
        this._phone = _phone;
    }
}
