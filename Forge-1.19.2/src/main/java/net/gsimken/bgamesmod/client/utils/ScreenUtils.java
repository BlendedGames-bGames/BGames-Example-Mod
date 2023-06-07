package net.gsimken.bgamesmod.client.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;

public class ScreenUtils {
    public static Component getPoints(int points){
        return Component.translatable("gui.bgameslibrary.display_attributes.points", Component.literal( String.valueOf(points)));
    }
    public static int calculateCenteredX(int xPosPivot,int totalWidth , Component text){
        return xPosPivot+(totalWidth/2) - (Minecraft.getInstance().font.width(text)/2);

    }
}
