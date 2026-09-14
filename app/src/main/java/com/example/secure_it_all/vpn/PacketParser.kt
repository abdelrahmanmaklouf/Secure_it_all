package com.example.secure_it_all.vpn


import java.nio.ByteBuffer

data class ParsedPacket(
    val protocol: String,
    val sourceIp: String,
    val destIp: String,
    val sourcePort: Int,
    val destPort: Int
)

object PacketParser {
    fun parse(buffer: ByteBuffer): ParsedPacket? {
        if (buffer.remaining() < 20) return null

        val versionAndIhl = buffer.get(0).toInt()
        val version = versionAndIhl shr 4
        if (version != 4) return null // هنتعامل مع IPv4 الأول، IPv6 بعدين

        val ihl = (versionAndIhl and 0x0F) * 4
        val protocolByte = buffer.get(9).toInt() and 0xFF
        val protocol = when (protocolByte) {
            6 -> "TCP"
            17 -> "UDP"
            else -> "OTHER"
        }

        val sourceIp = ipToString(buffer, 12)
        val destIp = ipToString(buffer, 16)

        var sourcePort = 0
        var destPort = 0
        if (protocol == "TCP" || protocol == "UDP") {
            sourcePort = ((buffer.get(ihl).toInt() and 0xFF) shl 8) or
                    (buffer.get(ihl + 1).toInt() and 0xFF)
            destPort = ((buffer.get(ihl + 2).toInt() and 0xFF) shl 8) or
                    (buffer.get(ihl + 3).toInt() and 0xFF)
        }

        return ParsedPacket(protocol, sourceIp, destIp, sourcePort, destPort)
    }

    private fun ipToString(buffer: ByteBuffer, offset: Int): String {
        return "${buffer.get(offset).toInt() and 0xFF}." +
                "${buffer.get(offset + 1).toInt() and 0xFF}." +
                "${buffer.get(offset + 2).toInt() and 0xFF}." +
                "${buffer.get(offset + 3).toInt() and 0xFF}"
    }
}