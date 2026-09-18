package com.example.secure_it_all.vpn


import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import java.net.InetSocketAddress

class AppAttributor(private val context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    fun getUidForConnection(
        protocol: Int, // java.net.Socket.IPPROTO_TCP or UDP
        localAddress: InetSocketAddress,
        remoteAddress: InetSocketAddress
    ): Int? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return null
        return connectivityManager.getConnectionOwnerUid(protocol, localAddress, remoteAddress)
            .takeIf { it != -1 }
    }

    fun getPackageNameForUid(uid: Int): String? {
        return context.packageManager.getPackagesForUid(uid)?.firstOrNull()
    }
}