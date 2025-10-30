package com.postest.fhyrapost4

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.text.insert

// Gunakan AndroidViewModel untuk mendapatkan Application context
class WargaViewModel(application: Application) : AndroidViewModel(application) {

    private val wargaDao = WargaDatabase.getDatabase(application).wargaDao()

    // Ambil semua data sebagai LiveData agar UI bisa observe
    val allWarga: LiveData<List<Warga>> = wargaDao.getAllWarga().asLiveData()

    // Fungsi insert (suspend) dijalankan di coroutine
    fun insert(warga: Warga) = viewModelScope.launch {
        wargaDao.insert(warga)
    }

    // Fungsi delete (suspend) dijalankan di coroutine
    fun deleteAll() = viewModelScope.launch {
        wargaDao.deleteAll()
    }
}
