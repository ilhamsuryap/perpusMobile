package com.example.perpustakaan

import com.example.perpustakaan.ViewModel.AdminPengembalian
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

private const val ARG_JUDUL_BUKU = "judul_buku"
private const val ARG_TANGGAL_PINJAM = "tanggal_pinjam"
private const val ARG_TANGGAL_KEMBALI = "tanggal_kembali"

class FragmentDikembalikanAdmin : Fragment() {

    private var judulBuku: String? = null
    private var tanggalPinjam: String? = null
    private var tanggalKembali: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            judulBuku = it.getString(ARG_JUDUL_BUKU)
            tanggalPinjam = it.getString(ARG_TANGGAL_PINJAM)
            tanggalKembali = it.getString(ARG_TANGGAL_KEMBALI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dikembalikan_admin, container, false)

        // Validasi data
        val judul = judulBuku ?: "Judul tidak tersedia"
        val pinjam = tanggalPinjam ?: "Tanggal pinjam tidak tersedia"
        val kembali = tanggalKembali ?: "Tanggal kembali tidak tersedia"

        // Menampilkan data di TextView
        view.findViewById<TextView>(R.id.tv_judul_buku)?.text = judul
        view.findViewById<TextView>(R.id.tv_tanggalpinjam)?.text = pinjam
        view.findViewById<TextView>(R.id.tv_tanggalkembali)?.text = kembali

        // Tombol Kembalikan Buku ditekan
        view.findViewById<TextView>(R.id.btnTerimaPengembalian)?.setOnClickListener {
            val intent = Intent(context, AdminPengembalian::class.java)
            startActivity(intent)
        }

        return view
    }

    companion object {
        fun newInstance(judulBuku: String, tanggalPinjam: String, tanggalKembali: String) =
            FragmentDikembalikanAdmin().apply {
                arguments = Bundle().apply {
                    putString(ARG_JUDUL_BUKU, judulBuku)
                    putString(ARG_TANGGAL_PINJAM, tanggalPinjam)
                    putString(ARG_TANGGAL_KEMBALI, tanggalKembali)
                }
            }
    }
}