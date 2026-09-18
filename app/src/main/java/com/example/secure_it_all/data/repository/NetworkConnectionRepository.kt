package com.example.secure_it_all.data.repository


import com.example.secure_it_all.data.database.NetworkConnectionDao
import com.example.secure_it_all.data.database.NetworkConnectionEntity
import com.example.secure_it_all.vpn.MockPacketSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class NetworkConnectionRepository(
    private val dao: NetworkConnectionDao
) {

    suspend fun startMockCollection() {
        MockPacketSource.connectionStream().collect { connection ->

            val entity = NetworkConnectionEntity(
                uid = connection.uid,
                packageName = connection.packageName,
                timestamp = connection.timestamp,
                protocol = connection.protocol,
                destinationIp = connection.destinationIp,
                destinationPort = connection.destinationPort,
                domain = connection.domain,
                bytesSent = connection.bytesSent,
                bytesReceived = connection.bytesReceived
            )

            dao.insert(entity)
        }
    }

    fun getRecentConnections(): Flow<List<NetworkConnectionEntity>> {
        return dao.getRecentConnections()
    }
}