package com.postest.fhyrapost4
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WargaDao {
    // Mengambil semua data warga, diurutkan dari yang terbaru (ID terbesar)
    // Diubah ke ORDER BY id ASC agar data baru di bawah
    @Query("SELECT * FROM warga_table ORDER BY id ASC")
    fun getAllWarga(): Flow<List<Warga>> // Flow akan otomatis update UI jika ada data baru

    @Insert
    suspend fun insert(warga: Warga)

    @Query("DELETE FROM warga_table")
    suspend fun deleteAll()
}