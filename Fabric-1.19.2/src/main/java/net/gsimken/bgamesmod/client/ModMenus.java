package net.gsimken.bgamesmod.client;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.gsimken.bgamesmod.BgamesMod;
import net.gsimken.bgamesmod.client.gui.ChooseCategoriesScreen;
import net.gsimken.bgamesmod.client.gui.CognitiveCategoryEffectsScreen;
import net.gsimken.bgamesmod.client.gui.PhysicalCategoryEffectsScreen;
import net.gsimken.bgamesmod.client.gui.SocialCategoryEffectsScreen;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.client.menus.CognitiveCategoryMenu;
import net.gsimken.bgamesmod.client.menus.PhysicalCategoryMenu;
import net.gsimken.bgamesmod.client.menus.SocialCategoryMenu;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModMenus {

    public static ScreenHandlerType<ChooseCategoriesMenu> CHOOSE_CATEGORY  ;
    public static ScreenHandlerType<PhysicalCategoryMenu> PHYSICAL_CATEGORY  ;
    public static ScreenHandlerType<CognitiveCategoryMenu> COGNITIVE_CATEGORY  ;
    public static ScreenHandlerType<SocialCategoryMenu> SOCIAL_CATEGORY  ;

    public static void registerMenus() {
        CHOOSE_CATEGORY = Registry.register(Registry.SCREEN_HANDLER,
                new Identifier(
                        BgamesMod.MOD_ID,
                        "bgamesmod_choose_category"
                ),
                new ExtendedScreenHandlerType<>(ChooseCategoriesMenu::new));
        PHYSICAL_CATEGORY = Registry.register(Registry.SCREEN_HANDLER,new Identifier(BgamesMod.MOD_ID,"bgamesmod_physical_category"),new ExtendedScreenHandlerType<>(PhysicalCategoryMenu::new));
        COGNITIVE_CATEGORY = Registry.register(Registry.SCREEN_HANDLER,new Identifier(BgamesMod.MOD_ID,"bgamesmod_cognitive_category"),new ExtendedScreenHandlerType<>(CognitiveCategoryMenu::new));
        SOCIAL_CATEGORY = Registry.register(Registry.SCREEN_HANDLER,new Identifier(BgamesMod.MOD_ID,"bgamesmod_social_category"),new ExtendedScreenHandlerType<>(SocialCategoryMenu::new));

    }

    public static void registerScreens() {
        HandledScreens.register(CHOOSE_CATEGORY, ChooseCategoriesScreen::new);
        HandledScreens.register(PHYSICAL_CATEGORY, PhysicalCategoryEffectsScreen::new);
        HandledScreens.register(COGNITIVE_CATEGORY, CognitiveCategoryEffectsScreen::new);
        HandledScreens.register(SOCIAL_CATEGORY, SocialCategoryEffectsScreen::new);
    }
}
