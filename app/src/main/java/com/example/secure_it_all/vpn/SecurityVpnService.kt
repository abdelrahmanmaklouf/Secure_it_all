package com.example.secure_it_all.vpn

import android.content.Intent
import android.net.VpnService
import android.os.ParcelFileDescriptor
import android.util.Log
import kotlinx.coroutines.*
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.DatagramSocket
import java.nio.ByteBuffer

class SecurityVpnService : VpnService() {

    private var vpnInterface: ParcelFileDescriptor? = null
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startVpn()
        return START_STICKY
    }

    private fun startVpn() {
        val builder = Builder()
            .setSession("SecureItAll")
            .addAddress("10.0.0.2", 32)   // VPN's virtual IP
            .addRoute("0.0.0.0", 0)       // كل الترافيك يعدي من هنا
            .addDnsServer("8.8.8.8")
            .setMtu(1500)

        vpnInterface = builder.establish()

        serviceScope.launch {
            readPackets()
        }
    }

    private suspend fun readPackets() {
        val fd = vpnInterface ?: return
        val input = FileInputStream(fd.fileDescriptor)
        val output = FileOutputStream(fd.fileDescriptor)
        val buffer = ByteBuffer.allocate(32767)

        // ملحوظة: forwarding حقيقي محتاج socket protection + إعادة تركيب TCP/UDP.
        // في M2 هنركّز الأول على "القراءة والتحليل"، مش forwarding كامل.
        while (currentCoroutineContext().isActive) {
            buffer.clear()

            val length = input.read(buffer.array())

            if (length > 0) {
                buffer.limit(length)
                parsePacket(buffer)
            }
        }
    }

    private fun parsePacket(buffer: ByteBuffer) {
        val parsed = PacketParser.parse(buffer) ?: return
        Log.d("SecureItAll", "Connection: ${parsed.protocol} " +
                "${parsed.sourceIp}:${parsed.sourcePort} -> " +
                "${parsed.destIp}:${parsed.destPort}")
    }
    override fun onDestroy() {
        vpnInterface?.close()
        serviceScope.cancel()
        super.onDestroy()
    }
}