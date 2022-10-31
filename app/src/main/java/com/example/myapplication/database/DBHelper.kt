package com.example.myapplication.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.pojo.UserData


class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                FIRST_NAME + " TEXT," +
                LAST_NAME + " TEXT," + EMAIL + " TEXT," + PASSWORD + " TEXT," + MO_NUMBER + " TEXT," + LATITUDE + " TEXT," + LONGITUDE + " TEXT," + TIME + " TEXT" + ")")

        db.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addData(userData: UserData) {
        val values = ContentValues()
        values.put(FIRST_NAME, userData.firstName)
        values.put(LAST_NAME, userData.lastName)
        values.put(EMAIL, userData.email)
        values.put(PASSWORD, userData.password)
        values.put(MO_NUMBER, userData.moNumber)
        values.put(LATITUDE, userData.latitude ?: "")
        values.put(LONGITUDE, userData.longitude ?: "")
        values.put(TIME, userData.time ?: "0000-00-00 00:00:00")
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun updateTime(givenEmail: String, time: String) {
        val db: SQLiteDatabase = writableDatabase
        val values = ContentValues()
        values.put(DBHelper.TIME, time)
        val selection = "$EMAIL = ?"
        val selectionArgs = arrayOf(givenEmail)
        db.update(TABLE_NAME, values, selection, selectionArgs)
    }

    fun checkAlreadyExist(email: String): Boolean {
        val query: String =
            "SELECT $EMAIL FROM $TABLE_NAME WHERE $EMAIL =?"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(query, arrayOf(email))

        return cursor.count > 0
    }

    fun getData(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun removeAll() {
        val db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME, null, null)
    }

    @SuppressLint("Range")
    fun getUserData(givenEmail: String): UserData? {
        val db: SQLiteDatabase = readableDatabase
        val cursor =
            db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $EMAIL = ?", arrayOf(givenEmail), null)
        if (cursor.moveToFirst()) {
            val user = UserData().apply {
                firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
                email = cursor.getString(cursor.getColumnIndex(EMAIL))
                password = cursor.getString(cursor.getColumnIndex(PASSWORD))
                moNumber = cursor.getString(cursor.getColumnIndex(MO_NUMBER))
                time = cursor.getString(cursor.getColumnIndex(TIME))
                latitude = cursor.getString(cursor.getColumnIndex(LATITUDE))
                longitude = cursor.getString(cursor.getColumnIndex(LONGITUDE))
            }

            cursor.close()
            db.close()
            return user
        }
        cursor.close()
        db.close()
        return null
    }

    companion object {
        val DATABASE_NAME = "MUSIC_APP_DATA"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "music_table"
        val ID_COL = "id"
        val FIRST_NAME = "first_name"
        val LAST_NAME = "last_name"
        val EMAIL = "email"
        val PASSWORD = "password"
        val MO_NUMBER = "mo_number"
        val TIME = "time"
        val LATITUDE = "latitude"
        val LONGITUDE = "longitude"
    }
}