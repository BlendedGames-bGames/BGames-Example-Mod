package net.gsimken.bgamesmod.potion;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.gsimken.bgamesmod.BgamesMod;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.gsimken.bgamesmod.mixin.BrewingRecipeRegistryMixin;
import net.gsimken.bgamesmod.effects.ReachBoostEffect;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ModPotions {

    public static Potion REACH_BOOST_POTION;
    public static Potion PICKUP_BOOST_POTION;


    public static void register(){
        registerPotions();
        registerPotionRecipes();
    }
    public static void registerPotions() {
        REACH_BOOST_POTION = Registry.register(Registry.POTION, new Identifier(BgamesMod.MOD_ID, "reach_boost_potion"),
                new Potion(new StatusEffectInstance(ModEffects.REACH_BOOST, 20 * 60 * 5, 4)));

        PICKUP_BOOST_POTION = Registry.register(Registry.POTION, new Identifier(BgamesMod.MOD_ID, "pickup_boost_potion"),
                new Potion(new StatusEffectInstance(ModEffects.PICKUP_BOOST, 20 * 60 * 5, 4)));


    }
    private static  void registerPotionRecipes(){
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.ENDER_PEARL,
                ModPotions.PICKUP_BOOST_POTION);
        BrewingRecipeRegistryMixin.invokeRegisterPotionRecipe(Potions.AWKWARD, Items.STICK,
                ModPotions.REACH_BOOST_POTION);
    }

}
