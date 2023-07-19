package net.gsimken.bgamesmod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.gsimken.bgameslibrary.client.BGamesLibraryModScreens;
import net.gsimken.bgameslibrary.networking.BGamesLibraryModMessages;
import net.gsimken.bgamesmod.client.ModMenus;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.gsimken.bgamesmod.potion.ModPotions;
import net.gsimken.bgamesmod.test_utils.CSVwriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BgamesModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		long startTime = System.currentTimeMillis();
		ModMessages.registerS2CPackets();
		ModMenus.registerScreens();
		long endTime = System.currentTimeMillis();
		long elapsedTime = endTime - startTime;

		CSVwriter.updateCSV("ICMFa", Long.toString(elapsedTime));
	}
}