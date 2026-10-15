package me.lilly.elytraap

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.util.Mth
import net.minecraft.world.item.component.SwingAnimation
import net.minecraft.world.level.GameType
import net.minecraft.world.phys.Vec3
import kotlin.math.atan2

object Autopilot {
    var enabled: Boolean = false
    var targetloc: Vec3 = Vec3(0.0, 0.0, 0.0)
    var targetalt: Int = 0
    var turnspeed: Int = 10
    var rocketdelay: Int = 100

    var rocketCooldown: Int = 0

    var isClimbing: Boolean = true

    fun init() {
        Commands.register()

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            val player = client.player ?: return@register
            if (player.gameMode() != GameType.SURVIVAL) return@register
            if (!player.isFallFlying) return@register
            if (!enabled) return@register

            //wowie flying
            val dx = targetloc.x - player.x
            val dz = targetloc.z - player.z

            val targetYaw = (atan2(dz, dx) * (180.0 / Math.PI)).toFloat() - 90f

            val currentYaw = player.yRot

            val yawDifference = Mth.wrapDegrees(targetYaw - currentYaw)
            val clampedYawDiff = yawDifference.coerceIn((-turnspeed).toFloat(), turnspeed.toFloat())

            player.yRot = currentYaw + clampedYawDiff

            //PITCH SHIT
            val currentAlt = player.y

            val bufferHeight = 30.0

            if (currentAlt >= targetalt + bufferHeight) {
                isClimbing = false
            } else if (currentAlt <= targetalt) {
                isClimbing = true
            }

            val targetPitch = if (isClimbing) -10f else 0f

            val currentPitch = player.xRot
            val pitchDifference = targetPitch - currentPitch
            val clampedPitchDiff = pitchDifference.coerceIn((-10f), 10f)

            val finalPitch = currentPitch + clampedPitchDiff
            player.xRot = finalPitch
            player.xRotO = finalPitch

            //ROCKETS SHIT
            if (rocketCooldown > 0) { rocketCooldown-- }

            if (rocketCooldown < 1) {
                val mainHand = player.mainHandItem
                val offHand = player.offhandItem

                val hasRocket = mainHand.item is net.minecraft.world.item.FireworkRocketItem || offHand.item is net.minecraft.world.item.FireworkRocketItem

                if (hasRocket) {
                    val hand = if (mainHand.item is net.minecraft.world.item.FireworkRocketItem) { net.minecraft.world.InteractionHand.MAIN_HAND }
                    else { net.minecraft.world.InteractionHand.OFF_HAND }
                    client.gameMode?.useItem(player, hand)
                    player.swing(hand, SwingAnimation.DEFAULT, true)
                    rocketCooldown = rocketdelay
                }
            }

        }
    }
}
