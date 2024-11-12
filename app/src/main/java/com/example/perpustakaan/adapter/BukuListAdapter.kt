package com.example.perpustakaan.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R
import com.example.perpustakaan.adapter.BukuAdapter.BukuViewHolder
import com.example.perpustakaan.databinding.StyleDaftarBukuBinding

class BukuListAdapter (
    private val onItemClick: (Buku) -> Unit
) : ListAdapter<Buku, BukuListAdapter.BukuViewHolder>(BukuDiffCallback()){

    class BukuViewHolder private constructor(val binding: StyleDaftarBukuBinding) :
            RecyclerView.ViewHolder(binding.root){
        fun bind(buku: Buku, onItemClick: (Buku) -> Unit){
            binding.tvJudulBuku.text = buku.judul
            binding.tvPenulis.text = buku.penulis
            binding.tvStok.text = buku.stok.toString()

            itemView.setOnClickListener{
                onItemClick(buku)
            }
    }
        companion object{
            fun from(parent: ViewGroup): BukuViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StyleDaftarBukuBinding.inflate(layoutInflater, parent, false)
                return BukuViewHolder(binding)
            }
        }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BukuViewHolder{
        return BukuViewHolder.from(parent)

    }

    override fun onBindViewHolder(holder: BukuViewHolder, position: Int){
        val buku = getItem(position)
        holder.bind(buku, onItemClick)
    }

    override fun getItem(position: Int): Buku {
        return getItem(position)
    }

//    override fun getItemCount(): Int = bukuList.size

//    fun setData(buku: List<Buku>) {
//        this.bukuList = buku
//        notifyDataSetChanged()


    class BukuDiffCallback : DiffUtil.ItemCallback<Buku>() {
        override fun areItemsTheSame(oldItem: Buku, newItem: Buku): Boolean {
            return oldItem.id_buku == newItem.id_buku
    }
    override fun areContentsTheSame(oldItem: Buku, newItem: Buku): Boolean {
        return oldItem == newItem
    }
    }
}

