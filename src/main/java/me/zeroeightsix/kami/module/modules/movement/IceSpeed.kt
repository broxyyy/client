package me.zeroeightsix.kami.module.modules.movement

import me.zeroeightsix.kami.event.events.SafeTickEvent
import me.zeroeightsix.kami.module.Module
import me.zeroeightsix.kami.setting.Settings
import net.minecraft.init.Blocks

@Module.Info(
        name = "IceSpeed",
        description = "Changes how slippery ice is",
        category = Module.Category.MOVEMENT
)
object IceSpeed : Module() {
    private val slipperiness = register(Settings.floatBuilder("Slipperiness").withMinimum(0.2f).withValue(0.4f).withMaximum(1.0f).build())

    override fun onUpdate(event: SafeTickEvent) {
        Blocks.ICE.slipperiness = slipperiness.value
        Blocks.PACKED_ICE.slipperiness = slipperiness.value
        Blocks.FROSTED_ICE.slipperiness = slipperiness.value
    }

    override fun onDisable() {
        Blocks.ICE.slipperiness = 0.98f
        Blocks.PACKED_ICE.slipperiness = 0.98f
        Blocks.FROSTED_ICE.slipperiness = 0.98f
    }
}