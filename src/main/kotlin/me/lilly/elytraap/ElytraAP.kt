package me.lilly.elytraap

import net.fabricmc.api.ClientModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory

object ElytraAP : ClientModInitializer {
	const val MOD_ID: String = "elytra-ap"
	private val LOGGER = LoggerFactory.getLogger(MOD_ID)

	override fun onInitializeClient() {
		LOGGER.info("Initializing Elytra Autopilot Client...")

		Autopilot.init()
	}

	fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
