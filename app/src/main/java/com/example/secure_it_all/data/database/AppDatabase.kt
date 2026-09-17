package com.example.secure_it_all.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SecurityEventEntity::class,
        NetworkConnectionEntity::class,
        FirewallRuleEntity::class,
        AppInfoEntity::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun securityEventDao(): SecurityEventDao
    abstract fun networkConnectionDao(): NetworkConnectionDao
    abstract fun firewallRuleDao(): FirewallRuleDao
    abstract fun appInfoDao(): AppInfoDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "secure_it_all.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}