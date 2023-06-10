package net.gsimken.bgamesmod.potion;

import net.gsimken.bgamesmod.BgamesMod;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, BgamesMod.MOD_ID);
    public static final RegistryObject<Potion> REACH_BOOST_POTION = POTIONS.register("reach_boost_potion",
            ()-> new Potion(new MobEffectInstance(ModEffects.REACH_BOOST.get(),20*60*5,4)));
    public static final RegistryObject<Potion> PICKUP_BOOST_POTION = POTIONS.register("pickup_boost_potion",
            ()-> new Potion(new MobEffectInstance(ModEffects.PICKUP_BOOST.get(),20*60*5,9)));
    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

}
