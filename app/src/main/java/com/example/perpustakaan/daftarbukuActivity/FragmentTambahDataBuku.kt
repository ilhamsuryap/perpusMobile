package com.example.perpustakaan.daftarbukuActivity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.databinding.FragmentTambahdatabukuBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentTambahDataBuku : Fragment() {

    private lateinit var binding: FragmentTambahdatabukuBinding
    private val bukuViewModel: BukuViewModel by viewModels()
    private var imageUri: Uri? = null

    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTambahdatabukuBinding.inflate(inflater, container, false)

        selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data?.data
                imageUri?.let {
                    if (isValidImage(it)) {
                        binding.ivBuku.setImageURI(it)
                    } else {
                        Toast.makeText(requireContext(), "Pilih gambar yang valid", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.btnSelectImage.setOnClickListener {
            openGallery()
        }

        binding.btnSimpanBuku.setOnClickListener {
            simpanBuku()
        }

        bukuViewModel.uploadStatus.observe(viewLifecycleOwner) { isSuccess ->
            Toast.makeText(requireContext(), if (isSuccess) "Buku berhasil disimpan" else "Gagal menyimpan buku", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageLauncher.launch(intent)
    }

    private fun simpanBuku() {
        val judul = binding.etJudulBuku.text.toString().trim()
        val penulis = binding.etPenulis.text.toString().trim()
        val tahunTerbit = binding.etTahunTerbit.text.toString().trim()
        val stok = binding.etStok.text.toString().trim()
        val deskripsi = binding.etDeskripsi.text.toString().trim()
        val imageUrl = imageUri?.toString()

        if (judul.isEmpty()) {
            binding.etJudulBuku.error = "Judul tidak boleh kosong"
            return
        }

        if (penulis.isEmpty()) {
            binding.etPenulis.error = "Penulis tidak boleh kosong"
            return
        }

        if (tahunTerbit.isEmpty() || !tahunTerbit.matches(Regex("\\d{4}"))) {
            binding.etTahunTerbit.error = "Tahun terbit harus berupa angka valid"
            return
        }

        if (stok.isEmpty() || !stok.matches(Regex("\\d+"))) {
            binding.etStok.error = "Stok harus berupa angka"
            return
        }

        if (deskripsi.isEmpty()) {
            binding.etDeskripsi.error = "Deskripsi tidak boleh kosong"
            return
        }

        if (imageUrl == null) {
            Toast.makeText(requireContext(), "Pilih gambar buku", Toast.LENGTH_SHORT).show()
            return
        }

        val buku = Buku(
            id = 0,
            judul = judul,
            penulis = penulis,
            tahunTerbit = tahunTerbit.toInt(),
            stok = stok.toInt(),
            deskripsi = deskripsi,
            gambarUrl = null // Gambar bersifat opsional
        )

        bukuViewModel.insert(buku, imageUri) // Jika imageUri null, proses tetap berlanjut
    }


    private fun isValidImage(uri: Uri): Boolean {
        // Implementasikan logika untuk memverifikasi bahwa gambar valid
        return true
    }
}
