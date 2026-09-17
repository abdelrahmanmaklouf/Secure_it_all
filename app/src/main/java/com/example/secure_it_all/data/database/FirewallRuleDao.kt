package com.example.secure_it_all.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FirewallRuleDao {
    @Insert
    suspend fun insert(rule: FirewallRuleEntity)

    @Update
    suspend fun update(rule: FirewallRuleEntity)

    @Delete
    suspend fun delete(rule: FirewallRuleEntity)

    @Query("SELECT * FROM firewall_rules WHERE enabled = 1")
    suspend fun getActiveRules(): List<FirewallRuleEntity>

    @Query("SELECT * FROM firewall_rules")
    fun getAllRules(): Flow<List<FirewallRuleEntity>>
}