package com.example.perpustakaan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R
import com.example.perpustakaan.database.PerpustakaanDatabase

class AdapterHome(
    private val context: Context,
    private val bookList: List<Buku>,
    private val database: PerpustakaanDatabase,
    private val onItemClickListener: (Buku) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class ViewType { BOOK, AD }

    override fun getItemViewType(position: Int): Int {
        return if ((position + 1) % 6 == 0) ViewType.AD.ordinal else ViewType.BOOK.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.AD -> {
                val view = LayoutInflater.from(context).inflate(R.layout.cardhome_ads, parent, false)
                AdViewHolder(view)
            }
            ViewType.BOOK -> {
                val view = LayoutInflater.from(context).inflate(R.layout.cardhome, parent, false)
                HomeViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ViewType.AD.ordinal -> {
            }
            ViewType.BOOK.ordinal -> {
                val bookPosition = position - (position / 6)
                val book = bookList[bookPosition]
                (holder as HomeViewHolder).apply {
                    bookTitle.text = book.judul
                    bookImage.setImageResource(R.drawable.menara5negara)
                    butoon.setOnClickListener {
                        onItemClickListener(book)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return bookList.size + (bookList.size / 5)
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookTitle: TextView = itemView.findViewById(R.id.text1)
        val bookImage: ImageView = itemView.findViewById(R.id.imageView)
        val butoon: Button = itemView.findViewById(R.id.button)
    }

    inner class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    }
}


