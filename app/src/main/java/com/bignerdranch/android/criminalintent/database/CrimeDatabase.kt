package com.bignerdranch.android.criminalintent.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bignerdranch.android.criminalintent.Note

@Database(entities = [Note::class], version = 3)
//通过注解实现了数据的类型转换，类的实现在CrimeTypeConverters上
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase(){
    abstract fun crimeDao(): CrimeDao
}

// 迁移数据库
val migration_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE CRIME ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
        )
    }
}

// 升级数据库
val migration_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE CRIME ADD COLUMN duration INT NOT NULL DEFAULT ''"
        )
    }
}


