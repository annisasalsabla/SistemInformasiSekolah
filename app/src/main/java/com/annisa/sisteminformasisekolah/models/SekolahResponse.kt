package com.annisa.sekolah.models

data class SekolahResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<Sekolah>  // Assuming 'Sekolah' is the main data class
) {
    // Changed the name from L to Sekolah
    data class Sekolah(
        val id: Int,  // Use Int if ID is a number
        val nama_sekolah: String,
        val no_telepon: String,
        val akreditasi: String,
        val gambar: String,
        val informasi: String
    )
}
