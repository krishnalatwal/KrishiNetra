package com.example.krishinetra.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScanResultDao {

    @Insert
    suspend fun insert(result: ScanResult)

    @Query("SELECT * FROM scan_results ORDER BY id DESC")
    suspend fun getAllResults(): List<ScanResult>
}