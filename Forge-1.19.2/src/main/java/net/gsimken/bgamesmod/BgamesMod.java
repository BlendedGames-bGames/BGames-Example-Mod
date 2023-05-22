package net.gsimken.bgamesmod;

import com.mojang.logging.LogUtils;
import net.gsimken.bgamesmod.client.ModMenus;
import net.gsimken.bgamesmod.client.gui.ChooseCategoriesScreen;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BgamesMod.MOD_ID)
public class BgamesMod
{

    public static final String MOD_ID = "bgamesmod";

    private static final Logger LOGGER = LogUtils.getLogger();
    public BgamesMod()
    {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        eventBus.addListener(this::commonSetup);
        ModEffects.register(eventBus);
        ModMenus.register(eventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModMessages.register();

    }

     // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            MenuScreens.register(ModMenus.CHOOSE_CATEGORY.get(), ChooseCategoriesScreen::new);
        }
    }
}
