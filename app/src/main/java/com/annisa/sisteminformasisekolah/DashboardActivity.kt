package com.annisa.sisteminformasisekolah

import com.annisa.sekolah.SekolahAdapter
import com.annisa.sisteminformasisekolah.api.ApiClient
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import com.annisa.sekolah.models.SekolahResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var svNamaSekolah: SearchView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvSekolah: RecyclerView
    private lateinit var floatBtnTambahSekolah: FloatingActionButton
    private lateinit var sekolahAdapter: SekolahAdapter
    private lateinit var imgNotFound : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        svNamaSekolah = findViewById(R.id.svNamaSekolah)
        progressBar = findViewById(R.id.progressBar)
        rvSekolah = findViewById(R.id.rvSekolah)
        floatBtnTambahSekolah = findViewById(R.id.floatBtnTambahSekolah)
        imgNotFound = findViewById(R.id.imgNotFound)

        // Call method to fetch schools
        getSekolah("")

        // Search functionality
        svNamaSekolah.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(pencarian: String?): Boolean {
                getSekolah(pencarian.toString())
                return true
            }
        })

        // Handle the floating action button for adding new school
        floatBtnTambahSekolah.setOnClickListener {
            // Handle adding a new school
            // You can navigate to a different activity or show a dialog to add school details
        }
    }

    private fun getSekolah(namaSekolah: String) {
        progressBar.visibility = View.VISIBLE
        ApiClient.apiService.getListSekolah(namaSekolah).enqueue(object: Callback<SekolahResponse> {
            override fun onResponse(
                call: Call<SekolahResponse>,
                response: Response<SekolahResponse>
            ) {
                if(response.isSuccessful) {
                    if(response.body()!!.success) {
                        // Set the data to RecyclerView adapter
                        sekolahAdapter = SekolahAdapter(arrayListOf())
                        rvSekolah.adapter = sekolahAdapter
                        sekolahAdapter.setData(response.body()!!.data)
                        imgNotFound.visibility = View.GONE
                    } else {
                        // If no data found
                        sekolahAdapter = SekolahAdapter(arrayListOf())
                        rvSekolah.adapter = sekolahAdapter
                        imgNotFound.visibility = View.VISIBLE
                    }
                }
                progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<SekolahResponse>, t: Throwable) {
                Toast.makeText(this@DashboardActivity, "Error : ${t.message}", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }
        })
    }
}
