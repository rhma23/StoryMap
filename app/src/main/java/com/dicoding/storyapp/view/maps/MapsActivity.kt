package com.dicoding.storyapp.view.maps

import android.content.Context
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.dicoding.storyapp.R
import com.dicoding.storyapp.config.RetrofitClient.apiService
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.storyapp.databinding.ActivityMapsBinding
import com.dicoding.storyapp.util.SessionManager
import java.io.IOException
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sessionManager: SessionManager

    val mapsViewModel: MapsModel by viewModels {
        MapsViewModelFactory(MapsRepository(apiService))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this) // Ensure sessionManager is initialized here
        mapsViewModel.setSessionManager(sessionManager)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mapsViewModel.stories.observe(this, { stories ->
        })
        mapsViewModel.fetchAllStoriesWithLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mapsViewModel.stories.observe(this, { stories ->
            stories.forEach { story ->
                story.lon?.let {
                    story.name?.let { it1 ->
                        story.description?.let { it2 ->
                            story.lat?.let { it3 ->
                                addMarkerAtLocation(
                                    mMap,
                                    it3,
                                    it,
                                    it1,
                                    it2
                                )
                            }
                        }
                    }
                }
            }
        })
    }



    fun addMarkerAtLocation(
        mMap: GoogleMap,
        latitude: Double,
        longitude: Double,
        title: String,
        snipet: String
    ) {
        val location = LatLng(latitude, longitude)
        var nameLocation = getAddressFromLatLng(this, latitude, longitude)

        Log.d("MapsActivity 1", "addMarkerAtLocation: $latitude, $longitude, $location, $nameLocation")

        mMap.addMarker(MarkerOptions().position(location).title(title).snippet(nameLocation))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location))
    }

    private fun getAddressFromLatLng(context: Context, latitude: Double?, longitude: Double?): String {
        Log.d("MapsActivity 2", "addMarkerAtLocation: $latitude, $longitude")
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses = geocoder.getFromLocation(latitude as Double, longitude as Double, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                address.locality ?: address.subAdminArea ?: address.adminArea ?: "Lokasi tidak ditemukan"
            } else {
                "Lokasi tidak ditemukan"
            }
        } catch (e: IOException) {
            Log.e("DetailActivity", "Geocoder error: ${e.message}")
            "Lokasi tidak ditemukan"
        }
    }
}