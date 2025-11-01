package com.example.baitapcuoiky.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.baitapcuoiky.Model.Contact;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "contacts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CONTACTS = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_NOTE = "note";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CONTACTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_PHONE + " TEXT NOT NULL, " +
                COLUMN_NOTE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // Thêm liên hệ mới
    public long addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhone());
        values.put(COLUMN_NOTE, contact.getNote());

        long id = db.insert(TABLE_CONTACTS, null, values);
        db.close();
        return id;
    }

    // Lấy tất cả liên hệ
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS + " ORDER BY " + COLUMN_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))
                );
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return contactList;
    }

    // Cập nhật liên hệ
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.getName());
        values.put(COLUMN_PHONE, contact.getPhone());
        values.put(COLUMN_NOTE, contact.getNote());

        int result = db.update(TABLE_CONTACTS, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
        return result;
    }

    // Xóa liên hệ
    public void deleteContact(int contactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(contactId)});
        db.close();
    }

    // Lấy một liên hệ theo ID
    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        Contact contact = null;
        if (cursor != null && cursor.moveToFirst()) {
            contact = new Contact(
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))
            );
            cursor.close();
        }
        db.close();
        return contact;
    }
}

