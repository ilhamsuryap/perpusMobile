package com.example.perpustakaan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R
import com.example.perpustakaan.database.PerpustakaanDatabase

class AdapterHome(private val context: Context, private val bookList: List<Buku>, private val database: PerpustakaanDatabase) :
    RecyclerView.Adapter<AdapterHome.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardhome, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookTitle.text = book.judul
        holder.borrowerName.text = book.penulis
        holder.bookImage.setImageResource(R.drawable.menara5negara)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookTitle: TextView = itemView.findViewById(R.id.text1)
        val borrowerName: TextView = itemView.findViewById(R.id.text2)
        val bookImage: ImageView = itemView.findViewById(R.id.imageView)
    }
}
