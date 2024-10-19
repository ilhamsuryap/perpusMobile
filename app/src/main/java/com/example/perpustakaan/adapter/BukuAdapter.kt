package com.example.perpustakaan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R


class BukuAdapter : RecyclerView.Adapter<BukuAdapter.BukuViewHolder>() {

    private var bukuList = emptyList<Buku>()

    inner class BukuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvJudulBuku: TextView = itemView.findViewById(R.id.tvJudulBuku)
        val tvPenulis: TextView = itemView.findViewById(R.id.tvPenulis)
        val tvStok: TextView = itemView.findViewById(R.id.tvStok)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.style_daftar_buku, parent, false)
        return BukuViewHolder(view)
    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int) {
        val currentItem = bukuList[position]
        holder.tvJudulBuku.text = currentItem.judul
        holder.tvPenulis.text = currentItem.penulis
        holder.tvStok.text = currentItem.stok.toString()
    }

    override fun getItemCount(): Int = bukuList.size

    fun setData(buku: List<Buku>) {
        this.bukuList = buku
        notifyDataSetChanged()
    }
}
