package com.example.perpustakaan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.R

class BukuAdapter(
    private val onItemClick: (Buku) -> Unit
) : ListAdapter<Buku, RecyclerView.ViewHolder>(BukuDiffCallback()) {

    enum class ITEM_VIEW_TYPE { PRODUCT, ADS }

    // Determine view type based on book stock
    override fun getItemViewType(position: Int): Int {
        val buku = getItem(position)
        return if (buku.stok == 0)
            ITEM_VIEW_TYPE.ADS.ordinal
        else
            ITEM_VIEW_TYPE.PRODUCT.ordinal
    }

    // ViewHolder for books with stock
    class BukuViewHolder(itemView: View, private val onItemClick: (Buku) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val tvJudulBuku: TextView = itemView.findViewById(R.id.tvJudulBuku)
        private val tvPenulis: TextView = itemView.findViewById(R.id.tvPenulis)
        private val tvStok: TextView = itemView.findViewById(R.id.tvStok)

        fun bind(buku: Buku) {
            tvJudulBuku.text = buku.judul
            tvPenulis.text = buku.penulis
            tvStok.text = "Stok: ${buku.stok}"

            // Set onClickListener
            itemView.setOnClickListener {
                onItemClick(buku)
            }
        }
    }

    // ViewHolder for books with stock 0 (ads)
    class BukuADSViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvJudulBuku: TextView = itemView.findViewById(R.id.tvJudulBuku)
        private val tvPenulis: TextView = itemView.findViewById(R.id.tvPenulis)
        private val ivSoldOut: ImageView = itemView.findViewById(R.id.tvStok)

        fun bind(buku: Buku) {
            tvJudulBuku.text = buku.judul
            tvPenulis.text = buku.penulis

            // Show "sold out" image
            ivSoldOut.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE.PRODUCT.ordinal) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.style_daftar_buku, parent, false)
            BukuViewHolder(view, onItemClick)
        } else {
            val viewAds = LayoutInflater.from(parent.context)
                .inflate(R.layout.style_daftar_bukuads, parent, false)
            BukuADSViewHolder(viewAds)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val buku = getItem(position)
        if (holder is BukuViewHolder) {
            holder.bind(buku)
        } else if (holder is BukuADSViewHolder) {
            holder.bind(buku)
        }
    }

    // Set new data by submitting the list to ListAdapter
    fun setData(bukuList: List<Buku>) {
        submitList(bukuList)
    }
}

// DiffUtil Callback for Buku
class BukuDiffCallback : DiffUtil.ItemCallback<Buku>() {
    override fun areItemsTheSame(oldItem: Buku, newItem: Buku): Boolean {
        return oldItem.id_buku == newItem.id_buku
    }

    override fun areContentsTheSame(oldItem: Buku, newItem: Buku): Boolean {
        return oldItem == newItem
    }
}
