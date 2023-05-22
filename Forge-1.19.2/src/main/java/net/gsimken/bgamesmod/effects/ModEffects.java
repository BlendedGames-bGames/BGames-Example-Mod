package net.gsimken.bgamesmod.effects;

import net.gsimken.bgamesmod.BgamesMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS
            = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BgamesMod.MOD_ID);


    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}