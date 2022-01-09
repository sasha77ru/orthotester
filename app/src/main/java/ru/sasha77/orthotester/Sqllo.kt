package ru.sasha77.orthotester

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Date

class Sqllo (val mContext : Context, val mDBname : String) {
    private var mDbHelper: DBHelper
    private var db: SQLiteDatabase

    init {
        mDbHelper = this.DBHelper()
        db = mDbHelper.writableDatabase
    }

    fun close () {db.close()}

    fun tstClearDB () {
        db.delete("meases",null,null)
    }

    fun tstInsert (date:String, time:String, pulse1:Float, pulse2:Float, probe:Float) {
        db.execSQL("""insert into meases (date,time,pulse1,pulse2,probe) values ("$date","$time",$pulse1,$pulse2,$probe);""")
    }

    fun inflateTheTableFromDatabase () {
        theTable.clear()
        val cursor : Cursor = db.rawQuery("SELECT id as _id,date,pulse1,pulse2,probe FROM meases ORDER BY time ASC", null)

        val myDate = object {
            private var prevDate = 0L
            @SuppressLint("SimpleDateFormat")
            fun addBlankStrings (curDate : String? = null) {
                val date = if (curDate == null) onlyDate().time else SimpleDateFormat("yyyy-MM-dd").parse(curDate).time
                if (prevDate==0L) {prevDate = date;return}
                var tim = prevDate
                tim += 86400000
                while (tim < date) {
                    theTable.add(0,
                        MA.MyRow(
                            -100,
                            SimpleDateFormat("yyyy-MM-dd").format(Date().apply { time = tim }),
                            0F, 0F, -100F
                        )
                    )
                    tim += 86400000
                }
                prevDate = date
            }
        }
        while (cursor.moveToNext()) {
            val date = cursor.getString(cursor.getColumnIndex("date"))
            myDate.addBlankStrings(date)
            theTable.add(0,
                MA.MyRow(
                    cursor.getInt(cursor.getColumnIndex("_id")),
                    date,
                    cursor.getFloat(cursor.getColumnIndex("pulse1")),
                    cursor.getFloat(cursor.getColumnIndex("pulse2")),
                    cursor.getFloat(cursor.getColumnIndex("probe"))
                )
            )
        }
        cursor.close()
        myDate.addBlankStrings()
    }

    @SuppressLint("SimpleDateFormat")
    fun addMeas (pulse1 : Float, pulse2 : Float, probe : Float) {
        val cv = ContentValues()
        cv.put("pulse1", pulse1)
        cv.put("pulse2", pulse2)
        cv.put("probe", probe)
        cv.put("date", SimpleDateFormat("yyyy-MM-dd").format(Date()))
        cv.put("time", SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()))
        db.insert("meases",null,cv)
        vlog("db.insert(meases,$cv)")
    }
    fun removeMeas (id : Int) {
        db.delete("meases","id = ?",arrayOf(id.toString()))
        vlog("db.delete(meases,$id)")
    }

    fun rawQuery(sql: String, selectionArgs: Array<String>?): Cursor {
        return db.rawQuery(sql,selectionArgs)
    }

    inner class DBHelper : SQLiteOpenHelper(mContext, mDBname, null, DATABASE_VERSION) {
        /**
         * Called when the database is created for the first time. This is where the
         * creation of tables and the initial population of the tables should happen.
         *
         * @param db The database.
         */
        override fun onCreate(db: SQLiteDatabase?) {
            if (db == null) throw SQLiteException()
            db.execSQL(
                "CREATE TABLE meases (" +
                        "id integer primary key autoincrement," +
                        "date text," +
                        "time text," +
                        "pulse1 real," +
                        "pulse2 real," +
                        "probe real" +
                    ");"
            )
        }

        /**
         * Called when the database needs to be upgraded. The implementation
         * should use this method to drop tables, add tables, or do anything else it
         * needs to upgrade to the new schema version.
         *
         *
         *
         * The SQLite ALTER TABLE documentation can be found
         * [here](http://sqlite.org/lang_altertable.html). If you add new columns
         * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
         * you can use ALTER TABLE to rename the old table, then create the new table and then
         * populate the new table with the contents of the old table.
         *
         *
         * This method executes within a transaction.  If an exception is thrown, all changes
         * will automatically be rolled back.
         *
         *
         * @param db The database.
         * @param oldVersion The old database version.
         * @param newVersion The new database version.
         */
        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

        }

        override fun onOpen(db: SQLiteDatabase) {
            db.execSQL("PRAGMA foreign_keys = ON;")
        }
    }
}