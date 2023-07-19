package net.gsimken.bgamesmod;

import net.fabricmc.api.ModInitializer;

import net.gsimken.bgamesmod.client.ModMenus;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.gsimken.bgamesmod.potion.ModPotions;
import net.gsimken.bgamesmod.test_utils.CSVwriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BgamesMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "bgamesmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		long startTime = System.currentTimeMillis();
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		ModMessages.registerC2SPackets();
		ModMenus.registerMenus();
		ModEffects.register();
		ModPotions.register();

		LOGGER.info("Hello Fabric world!");
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;
		CSVwriter.updateCSV("ISMFa", Long.toString(elapsedTime));
	}
}