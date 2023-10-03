package net.gsimken.bgamesmod;

import com.mojang.logging.LogUtils;
import net.gsimken.bgamesmod.client.ModMenus;
import net.gsimken.bgamesmod.client.gui.ChooseCategoriesScreen;
import net.gsimken.bgamesmod.client.gui.CognitiveCategoryEffectsScreen;
import net.gsimken.bgamesmod.client.gui.PhysicalCategoryEffectsScreen;
import net.gsimken.bgamesmod.client.gui.SocialCategoryEffectsScreen;
import net.gsimken.bgamesmod.client.menus.CognitiveCategoryMenu;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.gsimken.bgamesmod.potion.ModPotions;
import net.gsimken.bgamesmod.potion.PickupBoostPotionRecipe;
import net.gsimken.bgamesmod.potion.ReachBoostPotionRecipe;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
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
        ModPotions.register(eventBus);
        ModMenus.register(eventBus);

        MinecraftForge.EVENT_BUS.register(this);


    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        ModMessages.registerServer();
        //Recipe for create a potion of pickup boost
        BrewingRecipeRegistry.addRecipe(new PickupBoostPotionRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION),Potions.AWKWARD)),
                Ingredient.of(Items.ENDER_PEARL),
                PotionUtils.setPotion(new ItemStack(Items.POTION),ModPotions.PICKUP_BOOST_POTION.get())
                )
        );
        //Recipe for create a potion of reach boost
        BrewingRecipeRegistry.addRecipe(new ReachBoostPotionRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION),Potions.AWKWARD)),
                Ingredient.of(Items.STICK),
                PotionUtils.setPotion(new ItemStack(Items.POTION),ModPotions.REACH_BOOST_POTION.get())
                )
        );

    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            ModMessages.registerClient();
            MenuScreens.register(ModMenus.CHOOSE_CATEGORY.get(), ChooseCategoriesScreen::new);
            MenuScreens.register(ModMenus.PHYSICAL_CATEGORY.get(), PhysicalCategoryEffectsScreen::new);
            MenuScreens.register(ModMenus.COGNITIVE_CATEGORY.get(), CognitiveCategoryEffectsScreen::new);
            MenuScreens.register(ModMenus.SOCIAL_CATEGORY.get(), SocialCategoryEffectsScreen::new);
        }
    }
}
