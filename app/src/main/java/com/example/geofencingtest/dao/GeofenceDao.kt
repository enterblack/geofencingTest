package com.example.geofencingtest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.geofencingtest.data.GeofenceItem

@Dao
interface GeofenceDao {

    @Query("SELECT * FROM GeofenceItem")
    fun getAllGeofence() : List<GeofenceItem>

    @Query("SELECT COUNT(id) FROM GeofenceItem")
    fun getGeofenceLastCheck() : Int

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun insertGeofence(geofence : GeofenceItem)


}
