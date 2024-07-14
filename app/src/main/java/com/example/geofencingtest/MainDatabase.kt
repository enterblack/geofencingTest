package com.example.geofencingtest

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.geofencingtest.dao.GeofenceDao
import com.example.geofencingtest.data.GeofenceItem
import java.text.SimpleDateFormat
import java.util.Locale


@Database(entities = [GeofenceItem::class] , version = 1)
abstract class MainDatabase: RoomDatabase() {
    abstract  fun GeofenceDao() : GeofenceDao

    companion object {
        private val koreaDateFormat = SimpleDateFormat("yyyyMMdd", Locale.KOREA)
        private var getInstance : MainDatabase? = null

        fun getInstance(context: Context , current : Long) : MainDatabase?{
            if (getInstance == null){
                synchronized(MainDatabase::class.java){
                    getInstance = Room.databaseBuilder(
                        context.applicationContext,
                        MainDatabase::class.java,
                        koreaDateFormat.format(current)
                    ).fallbackToDestructiveMigration().build()
                }
            }
            return getInstance!!
        }
    }
}