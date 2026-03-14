package com.example.krishinetra

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.krishinetra.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var emptyState: LinearLayout
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        recycler = findViewById(R.id.historyRecycler)
        emptyState = findViewById(R.id.emptyState)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "krishinetra_db"
        ).build()

        loadHistory()
    }

    private fun loadHistory() {

        CoroutineScope(Dispatchers.IO).launch {

            val list = db.scanResultDao().getAllResults()

            withContext(Dispatchers.Main) {

                if (list.isEmpty()) {
                    emptyState.visibility = View.VISIBLE
                    recycler.visibility = View.GONE
                } else {
                    emptyState.visibility = View.GONE
                    recycler.visibility = View.VISIBLE

                    recycler.layoutManager = LinearLayoutManager(this@HistoryActivity)
                    recycler.adapter = HistoryAdapter(list)
                }
            }
        }
    }
}