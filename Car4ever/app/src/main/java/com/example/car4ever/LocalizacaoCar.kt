package com.example.car4ever

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.example.car4ever.model.Localidade
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class LocalizacaoCar : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnPolylineClickListener, GoogleMap.OnPolygonClickListener {

    private val REQUEST_LOCATION_PERMISSION = 1
    private lateinit var googleMap: GoogleMap
    private lateinit var locationCallback: LocationCallback
    val localidades = mutableListOf<Localidade>()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())
    private val PATTERN_POLYLINE_DOTTED = listOf(GAP, DOT)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localizacao_car)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for(location in locationResult.locations){
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude,location.longitude), 13f))
                }
            }

        }

        val map = supportFragmentManager.findFragmentById(R.id.map1) as SupportMapFragment
        map.getMapAsync(this)
        val localidade1 = Localidade("Hyunda mooca", "Concessionária Hyundai\n" +
                "Av. Paes de Barros, 1581", -23.566133414769432, -46.59236441590371)

        val localidade2 = Localidade("Hyundai Paulista","Concessionária Hyundai\n" +
                "Av. Dr. Ricardo Jafet, 1209", -23.58722727618045, -46.618564072034964)

        localidades.add(localidade1)
        localidades.add(localidade2)



        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        val polyline1 = googleMap.addPolyline(PolylineOptions()
            .clickable(true)
            .add(
                LatLng(-35.016, 143.321),
                LatLng(-34.747, 145.592),
                LatLng(-34.364, 147.891),
                LatLng(-33.501, 150.217),
                LatLng(-32.306, 149.248),
                LatLng(-32.491, 147.309)))


        polyline1.tag = "A"


    googleMap.addMarker(
        MarkerOptions()
            .position(LatLng( localidades[0].latitude, localidades[0].longitude))
            .title(localidades[0].nome)
            .snippet(localidades[0].logradouro)
            .icon(BitmapDescriptorFactory.defaultMarker())
    )
        googleMap.addMarker(
            MarkerOptions()
                .position(LatLng( localidades[1].latitude, localidades[1].longitude))
                .title(localidades[1].nome)
                .snippet(localidades[1].logradouro)
                .icon(BitmapDescriptorFactory.defaultMarker())
        )
        googleMap.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng( localidades[1].latitude, localidades[1].longitude), 13F
            )

        )

        googleMap.setOnPolylineClickListener(this)
        googleMap.setOnPolygonClickListener(this)
        enableMyLocation()
       // renderizarRota()
    }
    private fun isPermissionGranted():Boolean{
        return ContextCompat.checkSelfPermission(
            this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED

    }
   @SuppressLint("MissingPermission")
    private fun enableMyLocation(){
        if(isPermissionGranted()){
            googleMap.isMyLocationEnabled = true
        }else{
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_LOCATION_PERMISSION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode ==REQUEST_LOCATION_PERMISSION){
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
               enableMyLocation()
            }
        }

        }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
       /* locationRequest = LocationRequest.create().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(3000)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())*/
    }

    override fun onPause() {
        super.onPause()
       /* fusedLocationProviderClient.removeLocationUpdates(locationCallback)*/
    }

    override fun onPolylineClick(polyline: Polyline) {
        // Flip from solid stroke to dotted stroke pattern.
        if (polyline.pattern == null || !polyline.pattern!!.contains(DOT)) {
            polyline.pattern = PATTERN_POLYLINE_DOTTED
        } else {
            // The default pattern is a solid stroke.
            polyline.pattern = null
        }
        Toast.makeText(this, "Route type" + polyline.tag.toString(), Toast.LENGTH_SHORT).show()
        //Toast.makeText(this, "Route type " + polyline.tag.toString(),
            //Toast.LENGTH_SHORT).show()
    }

    override fun onPolygonClick(p0: Polygon) {
        TODO("Not yet implemented")
    }

}

