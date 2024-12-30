package com.annisa.sekolah.models

import android.graphics.pdf.models.ListItem

data class SekolahResponse(
    val success: Boolean,
    val message: String,
    val data: ArrayList<ListItems>  // Assuming 'Sekolah' is the main data class
) {
    // Changed the name from L to Sekolah
    data class ListItems(
        val id: String,  // Use Int if ID is a number
        val nama_sekolah: String,
        val no_telepon: String,
        val akreditasi: String,
        val gambar: String,
        val informasi: String
    )
}