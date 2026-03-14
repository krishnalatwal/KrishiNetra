package com.example.krishinetra.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScanResult::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun scanResultDao(): ScanResultDao
}