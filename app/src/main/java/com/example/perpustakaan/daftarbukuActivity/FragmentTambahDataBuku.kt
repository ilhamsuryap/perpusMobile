package com.example.perpustakaan.daftarbukuActivity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
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
import androidx.lifecycle.ViewModelProvider
import com.example.perpustakaan.Dao.Buku
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.databinding.FragmentTambahdatabukuBinding
import java.io.File
import java.io.FileOutputStream
import java.util.*

class FragmentTambahDataBuku : Fragment() {

    private lateinit var binding: FragmentTambahdatabukuBinding
    private lateinit var bukuViewModel: BukuViewModel
    private var imageUri: Uri? = null
    private var convertedImagePath: String? = null

    private lateinit var selectImageLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTambahdatabukuBinding.inflate(inflater, container, false)

        bukuViewModel = ViewModelProvider(this).get(BukuViewModel::class.java)

        selectImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                imageUri = data?.data
                if (imageUri != null && isValidImage(imageUri!!)) {
                    val judul = binding.etJudulBuku.text.toString()
                    if (judul.isNotEmpty()) {
                        convertedImagePath = convertUriToImageFile(imageUri!!)
                        if (convertedImagePath != null) {
                            binding.ivBuku.setImageURI(imageUri)
                        } else {
                            Toast.makeText(requireContext(), "Gagal mengonversi gambar", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Masukkan judul buku terlebih dahulu", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Pilih gambar dengan format .jpg, .jpeg, atau .png", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnSelectImage.setOnClickListener {
            openGallery()
        }

        binding.btnSimpanBuku.setOnClickListener {
            val judul = binding.etJudulBuku.text.toString()
            val penulis = binding.etPenulis.text.toString()
            val tahunTerbit = binding.etTahunTerbit.text.toString()
            val stok = binding.etStok.text.toString()
            val deskripsi = binding.etDeskripsi.text.toString()

            if (judul.isNotEmpty() && penulis.isNotEmpty() && tahunTerbit.isNotEmpty() && stok.isNotEmpty() && deskripsi.isNotEmpty() && convertedImagePath != null) {
                val tahunTerbitInt = tahunTerbit.toIntOrNull()
                val stokInt = stok.toIntOrNull()

                if (tahunTerbitInt != null && stokInt != null) {
                    val buku = Buku(
                        judul = judul,
                        penulis = penulis,
                        tahunTerbit = tahunTerbitInt,
                        stok = stokInt,
                        deskripsi = deskripsi,
                        gambarUrl = convertedImagePath!! // Menyimpan path gambar yang telah diubah
                    )
                    bukuViewModel.insert(buku)
                    Toast.makeText(requireContext(), "Buku berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Tahun terbit dan stok harus berupa angka", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        selectImageLauncher.launch(intent)
    }

    private fun isValidImage(uri: Uri): Boolean {
        val mimeType = requireContext().contentResolver.getType(uri)
        return mimeType != null && (mimeType.contains("image/jpeg") || mimeType.contains("image/png") || mimeType.contains("image/jpg"))
    }

    private fun convertUriToImageFile(uri: Uri): String? {
        return try {
            // Menghasilkan UUID sebagai nama file unik
            val uniqueFileName = UUID.randomUUID().toString() + ".jpg"
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, uri)
            val outputFile = File(requireContext().cacheDir, uniqueFileName)
            FileOutputStream(outputFile).use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            }
            outputFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
