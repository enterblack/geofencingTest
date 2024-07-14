package com.example.geofencingtest.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingEvent
import com.google.android.gms.location.LocationServices

class GeofenceReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)

        geofencingEvent?.let {
            if(it.hasError()){
                return
            }
            val geofenceTransition = it.geofenceTransition
            if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER || geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT){
                val triggerEvent = geofencingEvent.triggeringGeofences
                
            }
        }

    }

    companion object{
        const val GEOFENCE_ID = "LOCATION_BATTERY_TEST"
    }
}