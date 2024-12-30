package com.annisa.sisteminformasisekolah

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.annisa.sekolah.models.SekolahResponse
import com.annisa.sisteminformasisekolah.R
import com.annisa.sisteminformasisekolah.api.ApiClient
import com.squareup.picasso.Picasso
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditSekolahActivity : AppCompatActivity() {

    private lateinit var edtNamaSekolah: EditText
    private lateinit var edtNoTelepon: EditText
    private lateinit var edtAkreditasi: EditText
    private lateinit var edtInformasi: EditText
    private lateinit var imgSekolah: ImageView
    private lateinit var btnUpdate: Button

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_sekolah)

        edtNamaSekolah = findViewById(R.id.edtNamaSekolah)
        edtNoTelepon = findViewById(R.id.edtNoTelepon)
        edtAkreditasi = findViewById(R.id.edtAkreditasi)
        edtInformasi = findViewById(R.id.edtInformasi)
        imgSekolah = findViewById(R.id.imgSekolah)
        btnUpdate = findViewById(R.id.btnUpdate)

        // Get the data passed from DashboardActivity
        val id = intent.getStringExtra("id")
        val namaSekolah = intent.getStringExtra("nama_sekolah")
        val noTelepon = intent.getStringExtra("no_telepon")
        val akreditasi = intent.getStringExtra("akreditasi")
        val informasi = intent.getStringExtra("informasi")
        val gambar = intent.getStringExtra("gambar")

        // Populate the fields with the current data
        edtNamaSekolah.setText(namaSekolah)
        edtNoTelepon.setText(noTelepon)
        edtAkreditasi.setText(akreditasi)
        edtInformasi.setText(informasi)
        Picasso.get().load(gambar).into(imgSekolah)

        // Handle save button click
        btnUpdate.setOnClickListener {
            updateSekolah(id)
        }

        imgSekolah.setOnClickListener {
            pickImage()
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageUri = data?.data
            imgSekolah.setImageURI(imageUri)
        }
    }

    private fun updateSekolah(id: String?) {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Updating School Data...")
        progressDialog.show()

        val namaSekolah = RequestBody.create("text/plain".toMediaTypeOrNull(), edtNamaSekolah.text.toString())
        val noTelepon = RequestBody.create("text/plain".toMediaTypeOrNull(), edtNoTelepon.text.toString())
        val akreditasi = RequestBody.create("text/plain".toMediaTypeOrNull(), edtAkreditasi.text.toString())
        val informasi = RequestBody.create("text/plain".toMediaTypeOrNull(), edtInformasi.text.toString())

        val file = imageUri?.let { Uri -> File(getRealPathFromURI(Uri)) }
        val requestFile = file?.let { MultipartBody.Part.createFormData("fileGambar", file.name, it.asRequestBody("image/*".toMediaTypeOrNull())) }

        val call = ApiClient.apiService.updateSekolah(
            RequestBody.create("text/plain".toMediaTypeOrNull(), id!!),
            namaSekolah,
            noTelepon,
            akreditasi,
            informasi,
            requestFile
        )

        call?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialog.dismiss()
                if (response.isSuccessful) {
                    Toast.makeText(this@EditSekolahActivity, "School updated successfully", Toast.LENGTH_SHORT).show()
                    // Return to DashboardActivity with updated data
                    val updatedSekolah = Intent().apply {
                        putExtra("id", id)
                        putExtra("nama_sekolah", edtNamaSekolah.text.toString())
                        putExtra("no_telepon", edtNoTelepon.text.toString())
                        putExtra("akreditasi", edtAkreditasi.text.toString())
                        putExtra("informasi", edtInformasi.text.toString())
                        putExtra("gambar", file?.path) // Send the updated image path
                    }
                    setResult(RESULT_OK, updatedSekolah)
                    finish()
                } else {
                    Toast.makeText(this@EditSekolahActivity, "Failed to update school", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                progressDialog.dismiss()
                Toast.makeText(this@EditSekolahActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        val cursor = contentResolver.query(contentUri, null, null, null, null)
        cursor?.moveToFirst()
        val index = cursor?.getColumnIndex(MediaStore.Images.Media.DATA)
        return cursor?.getString(index!!) ?: ""
    }
}
