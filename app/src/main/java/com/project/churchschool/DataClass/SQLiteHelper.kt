package com.project.churchschool.DataClass

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class SQLiteHelper (
    val context: Context?,
    val name: String?,
    val factory: SQLiteDatabase.CursorFactory?,
    val version: Int,
) : SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {

        var sql : String = "CREATE TABLE if not exists myMemo (" +
                "_id integer primary key autoincrement," +
                "title text, " +
                "date text," +
                "text text);"

        db.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val sql : String = "DROP TABLE if exists myMemo"

        db.execSQL(sql)
        onCreate(db)
    }

}