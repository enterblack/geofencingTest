package com.example.geofencingtest.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class GeofenceItem(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,

    var time : Long,
    var latitude : Double,
    var longitude : Double,

){
}
