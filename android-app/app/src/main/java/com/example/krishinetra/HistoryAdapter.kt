package com.example.krishinetra

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.krishinetra.database.ScanResult
import java.io.File

class HistoryAdapter(private val list: List<ScanResult>) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val image: ImageView = view.findViewById(R.id.historyImage)
        val disease: TextView = view.findViewById(R.id.historyDisease)
        val confidence: TextView = view.findViewById(R.id.historyConfidence)
        val date: TextView = view.findViewById(R.id.historyDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]

        val file = File(item.imageUri)

        if (file.exists()) {
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            holder.image.setImageBitmap(bitmap)
        }

        holder.disease.text = item.disease
        holder.confidence.text = "Confidence: ${"%.2f".format(item.confidence)}%"
        holder.date.text = item.date
    }

    override fun getItemCount(): Int {
        return list.size
    }
}