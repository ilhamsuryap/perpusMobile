package com.example.perpustakaan

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.R

class AdapterHome(private val context: Context, private val bookList: List<ItemHome>) :
    RecyclerView.Adapter<AdapterHome.HomeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cardhome, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val book = bookList[position]
        holder.bookTitle.text = book.title
        holder.borrowerName.text = book.name
        holder.bookImage.setImageResource(book.image)
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
