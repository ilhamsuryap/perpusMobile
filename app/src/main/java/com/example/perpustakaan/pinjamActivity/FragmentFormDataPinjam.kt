package com.example.perpustakaan.pinjamActivity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.perpustakaan.ViewModel.BukuViewModel
import com.example.perpustakaan.ViewModel.PeminjamanViewModel
import com.example.perpustakaan.databinding.FragmentFormDataPinjamBinding
import com.example.perpustakaan.entity.Pinjam
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FragmentFormDataPinjam : Fragment() {
    private var _binding: FragmentFormDataPinjamBinding? = null
    private val binding get() = _binding!!

    private lateinit var pinjamViewModel: PeminjamanViewModel
    private lateinit var bukuViewModel: BukuViewModel

    private var selectedBukuId: String = ""  // Untuk menyimpan id buku yang dipilih

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFormDataPinjamBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pinjamViewModel = ViewModelProvider(this).get(PeminjamanViewModel::class.java)
        bukuViewModel = ViewModelProvider(this).get(BukuViewModel::class.java)

        setupDropdownBuku()
        setupDatePickers()
        setupSaveButton()
    }

    private fun setupDropdownBuku() {
        val databaseReference = FirebaseDatabase.getInstance().getReference("buku")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bukuMap = mutableMapOf<String, String>() // Map untuk menyimpan ID dan judul
                for (bukuSnapshot in snapshot.children) {
                    val id = bukuSnapshot.key ?: continue
                    val judul = bukuSnapshot.child("judul").getValue(String::class.java) ?: continue
                    bukuMap[id] = judul
                }

                val bukuAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    bukuMap.values.toList()
                )
                bukuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinnerJudulBuku.adapter = bukuAdapter

                // Listener untuk mendapatkan id buku yang dipilih
                binding.spinnerJudulBuku.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                        selectedBukuId = bukuMap.keys.toList()[position]
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        selectedBukuId = ""
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal mengambil data buku", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun setupDatePickers() {
        binding.etTglPinjam.setOnClickListener {
            showDatePicker { date ->
                binding.etTglPinjam.setText(date)
            }
        }

        binding.etTglKembali.setOnClickListener {
            showDatePicker { date ->
                binding.etTglKembali.setText(date)
            }
        }
    }

    private fun setupSaveButton() {
        binding.btnSimpan.setOnClickListener {
            val namaAnggota = binding.etNamaAnggota.text.toString().trim()
            val tanggalPinjam = binding.etTglPinjam.text.toString()
            val tanggalKembali = binding.etTglKembali.text.toString()

            if (validateInput(namaAnggota, tanggalPinjam, tanggalKembali)) {
                savePinjamData(namaAnggota, selectedBukuId, tanggalPinjam, tanggalKembali)
            }
        }
    }

    private fun validateInput(
        namaAnggota: String,
        tanggalPinjam: String,
        tanggalKembali: String
    ): Boolean {
        return when {
            namaAnggota.isEmpty() -> {
                Toast.makeText(requireContext(), "Nama Anggota harus diisi", Toast.LENGTH_SHORT).show()
                false
            }
            selectedBukuId.isEmpty() -> {
                Toast.makeText(requireContext(), "Judul Buku harus dipilih", Toast.LENGTH_SHORT).show()
                false
            }
            tanggalPinjam.isEmpty() -> {
                Toast.makeText(requireContext(), "Tanggal Pinjam harus dipilih", Toast.LENGTH_SHORT).show()
                false
            }
            tanggalKembali.isEmpty() -> {
                Toast.makeText(requireContext(), "Tanggal Kembali harus dipilih", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    private fun savePinjamData(
        namaAnggota: String,
        bukuId: String,
        tanggalPinjam: String,
        tanggalKembali: String
    ) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("buku/$bukuId")
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stok = snapshot.child("stok").getValue(Int::class.java) ?: return
                val judulBuku = snapshot.child("judul").getValue(String::class.java) ?: return

                if (stok > 0) {
                    // Kurangi stok buku sebanyak 1
                    databaseReference.child("stok").setValue(stok - 1).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Simpan data pinjam ke Firebase atau Room Database
                            val pinjam = Pinjam(
                                id_pinjam = 0, // ID akan dibuat secara otomatis oleh Firebase atau Room
                                namaanggota = namaAnggota,
                                judulbuku_pinjam = judulBuku, // Gunakan judul buku, bukan ID
                                tanggalpinjam = tanggalPinjam,
                                tanggalkembali = tanggalKembali
                            )
                            pinjamViewModel.insert(pinjam) // Simpan ke Room Database

                            Toast.makeText(requireContext(), "Data pinjam berhasil disimpan", Toast.LENGTH_SHORT).show()

                            // Reset form
                            binding.etNamaAnggota.text?.clear()
                            binding.etTglPinjam.text?.clear()
                            binding.etTglKembali.text?.clear()
                            binding.spinnerJudulBuku.setSelection(0)
                        } else {
                            Toast.makeText(requireContext(), "Gagal mengupdate stok buku", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), "Stok buku habis", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal mengambil data stok buku", Toast.LENGTH_SHORT).show()
            }
        })
    }




    private fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                }.time

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
