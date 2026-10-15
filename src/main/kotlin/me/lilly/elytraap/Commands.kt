package me.lilly.elytraap

import com.mojang.brigadier.arguments.IntegerArgumentType
import net.fabricmc.fabric.api.client.command.v2.ClientCommands
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.minecraft.commands.arguments.coordinates.Coordinates
import net.minecraft.commands.arguments.coordinates.Vec2Argument
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3

object Commands {
    fun register() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->

            //flyto
            dispatcher.register(
                ClientCommands.literal("flyto")
                    .then(ClientCommands.argument("x", IntegerArgumentType.integer())
                        .then(ClientCommands.argument("z", IntegerArgumentType.integer())
                            .executes { context ->
                                val targetX = IntegerArgumentType.getInteger(context, "x").toDouble()
                                val targetZ = IntegerArgumentType.getInteger(context, "z").toDouble()
                                Autopilot.targetloc = Vec3(targetX, 0.0, targetZ)
                                context.source.sendFeedback(Component.literal("§aAP flying to x: ${targetX.toInt()}, z: ${targetZ.toInt()}"))
                                1
                            }
                        )
                    )
            )


            //alt
            dispatcher.register(
                ClientCommands.literal("flyalt")
                    .then(ClientCommands.argument("altitude", IntegerArgumentType.integer(0, 500))
                        .executes { context ->
                            val alt = IntegerArgumentType.getInteger(context, "altitude")

                            Autopilot.targetalt = alt
                            context.source.sendFeedback(Component.literal("§aAP altitude set to Y: $alt"))
                            1
                        }
                    )
            )

            //toggle
            dispatcher.register(
                ClientCommands.literal("flytoggle")
                    .executes { context ->
                        Autopilot.enabled = !Autopilot.enabled

                        val status = if (Autopilot.enabled) "§aEnabled" else "§cDisabled"
                        context.source.sendFeedback(Component.literal("§7AP is $status"))
                        1
                    }
            )

            //turnspeeed
            dispatcher.register(
                ClientCommands.literal("turnspeed")
                    .then(ClientCommands.argument("turnspeed", IntegerArgumentType.integer(1, 100))
                        .executes { context ->
                            val ts = IntegerArgumentType.getInteger(context, "turnspeed")

                            Autopilot.turnspeed = ts
                            context.source.sendFeedback(Component.literal("turn speed set to $ts"))
                            1
                        }
                    )
            )
        }
    }
}
