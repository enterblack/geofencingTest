package com.example.geofencingtest

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.geofencingtest.receiver.GeofenceReceiver
import com.example.geofencingtest.ui.theme.GeofencingTestTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

class MainActivity : ComponentActivity() {
    lateinit var geofencingClient: GeofencingClient
    lateinit var fusedLocationClient: FusedLocationProviderClient
    val geofenceList : MutableList<Geofence> by lazy {
        mutableListOf()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val locationPermissionCheck = mutableStateOf(false)
        val backLocationPermissionCheck = mutableStateOf(false)
        geofencingClient = LocationServices.getGeofencingClient(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        var lastLocation : Location? =null


        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeofencingTestTheme {

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val context = LocalContext.current
                    val locationPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions()
                    ) { isGranted ->
                        locationPermissionCheck.value = isGranted.all { it.value }

                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            lastLocation = location
                        }
                    }
                    val backLocationPermissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestMultiplePermissions()
                    ) { isGranted ->
                        backLocationPermissionCheck.value = isGranted.all { it.value }
                    }


                    if (backLocationPermissionCheck.value){
                        ShowMap(context, lastLocation)
                    }else{
                        LaunchedEffect(key1 = locationPermissionCheck.value) {
                            if (!locationPermissionCheck.value) {
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                                    )
                                )
                            }else{
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                                    backLocationPermissionLauncher.launch(
                                        arrayOf(
                                            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                                        )
                                    )
                                }else{
                                    backLocationPermissionCheck.value = true
                                }

                            }
                        }

                    }
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }

    @Composable
    private fun ShowMap(context: Context, lastLocation: Location?) {
        val lastLatLng = if (lastLocation != null){LatLng(lastLocation.latitude,lastLocation.longitude) } else {LatLng(1.35, 103.87)}
        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(lastLatLng, 10f)
        }
        val geofence = Geofence.Builder()
            .setRequestId(GeofenceReceiver.GEOFENCE_ID)
            .setCircularRegion(lastLatLng.latitude, lastLatLng.longitude, 100F)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
            .build()
        geofenceList.add(geofence)
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {

            Marker(
                state = MarkerState(position = lastLatLng),
                title = "Singapore",
                snippet = "Marker in Singapore"
            )
        }
    }
}

@Composable
fun Greeting(name: String, context : Context, modifier: Modifier = Modifier) {
    TextButton(onClick = {
        //백그라운드 권한 확인
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            if(context.checkSelfPermission(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED){

            }else{
                //지오펜스 시작
            }
        }else{
            //지오펜스 시작
        }

    }) {
        Text(
            text = "Hello Geofence!",
            modifier = modifier
        )
    }
}