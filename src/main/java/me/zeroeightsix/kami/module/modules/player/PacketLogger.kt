package me.zeroeightsix.kami.module.modules.player

import me.zeroeightsix.kami.KamiMod
import me.zeroeightsix.kami.event.events.PacketEvent
import me.zeroeightsix.kami.module.Module
import me.zeroeightsix.kami.util.event.listener
import net.minecraft.network.Packet
import java.io.*
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

@Module.Info(
        name = "PacketLogger",
        description = "Logs sent packets to a file",
        category = Module.Category.PLAYER
)
object PacketLogger : Module() {
    private val filename = "KAMIBluePackets.txt"
    private val lines = ArrayList<String>()
    private val FORMAT = SimpleDateFormat("HH:mm:ss.SSS")

    override fun onEnable() {
        if (mc.player == null) disable() else readToList()
    }

    override fun onDisable() {
        if (mc.player != null) write()
    }

    init {
        listener<PacketEvent.Send> {
            if (mc.player == null) {
                disable(); return@listener
            }
            addLine(it.packet)
        }
    }

    /* see https://kotlinlang.org/docs/reference/basic-types.html#string-templates for usage of $*/
    private fun addLine(packet: Packet<*>) {
        lines.add("""
            ${FORMAT.format(Date())}
            ${packet.javaClass.simpleName}
            ${packet.javaClass}
            $packet
            """)
    }

    private fun write() {
        try {
            val writer = FileWriter(filename)

            for (line in lines) {
                writer.write(line)
            }

            writer.close()
        } catch (e: IOException) {
            KamiMod.log.error("$chatName Error saving!")
            e.printStackTrace()
        }
    }

    private fun readToList() {
        val bufferedReader: BufferedReader

        try {
            bufferedReader = BufferedReader(InputStreamReader(FileInputStream(filename), StandardCharsets.UTF_8))
            var line: String
            lines.clear()
            while (bufferedReader.readLine().also { line = it } != null) {
                lines.add(line)
            }
            bufferedReader.close()
        } catch (ignored: IOException) {
        }
    }
}