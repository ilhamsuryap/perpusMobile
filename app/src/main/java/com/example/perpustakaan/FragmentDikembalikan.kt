package com.example.perpustakaan

import com.example.perpustakaan.ViewModel.AdminPengembalian
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment

private const val ARG_JUDUL_BUKU = "judul_buku"
private const val ARG_TANGGAL_PINJAM = "tanggal_pinjam"
private const val ARG_TANGGAL_KEMBALI = "tanggal_kembali"

class FragmentDikembalikan : Fragment() {

    private var judulBuku: String? = null
    private var tanggalPinjam: String? = null
    private var tanggalKembali: String? = null
    private lateinit var btnKembalikan: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            judulBuku = it.getString(ARG_JUDUL_BUKU)
            tanggalPinjam = it.getString(ARG_TANGGAL_PINJAM)
            tanggalKembali = it.getString(ARG_TANGGAL_KEMBALI)
        }
    }

    private fun checkIfUserIsAdmin(): Boolean {
        val sharedPreferences = requireActivity().getSharedPreferences("user_prefs", MODE_PRIVATE)
        val role = sharedPreferences.getString("USER_ROLE", "USER")
        Log.d("FragmentDikembalikan", "User Role: $role")  // Menambahkan log untuk memeriksa role
        return role == "ADMIN"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dikembalikan, container, false)

        val judul = judulBuku ?: "Judul tidak tersedia"
        val pinjam = tanggalPinjam ?: "Tanggal pinjam tidak tersedia"
        val kembali = tanggalKembali ?: "Tanggal kembali tidak tersedia"

        // Inisialisasi tampilan di sini
        val tvJudulBuku = view.findViewById<TextView>(R.id.tv_judul_buku)
        val tvTanggalPinjam = view.findViewById<TextView>(R.id.tv_tanggalpinjam)
        val tvTanggalKembali = view.findViewById<TextView>(R.id.tv_tanggalkembali)
        btnKembalikan = view.findViewById(R.id.btnKembalikan)

        tvJudulBuku?.text = judul
        tvTanggalPinjam?.text = pinjam
        tvTanggalKembali?.text = kembali

        // Memeriksa jika user adalah ADMIN, sembunyikan tombol
        if (checkIfUserIsAdmin()) {
            btnKembalikan.visibility = View.GONE
        } else {
            btnKembalikan.visibility = View.VISIBLE
        }

        // Set listener untuk tombol kembalikan
        btnKembalikan.setOnClickListener {
            // Kirim data pinjam ke halaman AdminPengembalian
            val intent = Intent(context, AdminPengembalian::class.java)
            intent.putExtra("id_pinjam", "1")
            intent.putExtra("namaanggota", "Nama Anggota")
            intent.putExtra("judulbuku_pinjam", judulBuku)
            intent.putExtra("tanggalpinjam", tanggalPinjam)
            intent.putExtra("tanggalkembali", tanggalKembali)
            startActivity(intent)
        }

        return view
    }

    companion object {
        fun newInstance(judulBuku: String, tanggalPinjam: String, tanggalKembali: String) =
            FragmentDikembalikan().apply {
                arguments = Bundle().apply {
                    putString(ARG_JUDUL_BUKU, judulBuku)
                    putString(ARG_TANGGAL_PINJAM, tanggalPinjam)
                    putString(ARG_TANGGAL_KEMBALI, tanggalKembali)
                }
            }
    }
}


