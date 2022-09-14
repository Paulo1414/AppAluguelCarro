package com.example.car4ever

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.car4ever.MapData
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.car4ever.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Places.initialize(this, BuildConfig.MAPS_API_KEY)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val hyundai = LatLng(-23.684214090444453, -46.61648161543652)
        mMap.addMarker(MarkerOptions().position(hyundai).title("Hyundai HMB Sinal Diadema"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(hyundai))




        val sydney = LatLng(-23.566246583252205, -46.59241262112984)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    -23.566246583252205,
                    -46.59241262112984
                ), 13F
            )
        )

        renderizarRota()
    }

    private fun renderizarRota() {
        CoroutineScope(Dispatchers.IO).launch {
            val resultado = getPolylines()
            withContext(Dispatchers.Main) {
                val lineOption = PolylineOptions()
                for (i in resultado.indices) {

                    lineOption.add(resultado[i])
                }

                mMap.addPolyline(lineOption)
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        LatLng(
                            -23.684214090444453,
                            -46.61648161543652
                        ), 13F
                    )
                )
            }
        }
    }
    private fun getPolylines(): ArrayList<LatLng> {

        val data =
            requestLocationData("maps.googleapis.com/maps/api/directions/json?origin=-23.684214090444453,-46.61648161543652&destination=-23.566246583252205,-46.59241262112984&sensor=false&mode=driving&key=\${MAPS_API_KEY}")
        val resultado = ArrayList<LatLng>()

        val responseObject = Gson().fromJson(data, MapData::class.java)
        for (i in 0 until responseObject.routes[0].legs[0].steps.size) {
            resultado.addAll(decodificarPolylines(responseObject.routes[0].legs[0].steps[i].polyline.points))
        }
        return resultado
    }

    private fun decodificarPolylines(points: String): ArrayList<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = points.length
        var lat = 0
        var lng = 0
        while (index<len){
            var b: Int
            var shift = 0
            var result = 0
            do{
                b = points[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            }while (b>= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = points[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            }while (b >= 0x20)
            val dlng = if(result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble()/ 1E5))
            poly.add(latLng)
        }
        return poly

    }

    private fun requestLocationData(url: String): String {

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        val response = client.newCall(request).execute()
        val data = response.body!!.string()
       return data
    }
}