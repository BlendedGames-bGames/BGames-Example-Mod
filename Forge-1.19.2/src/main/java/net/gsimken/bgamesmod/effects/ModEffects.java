package net.gsimken.bgamesmod.effects;

import net.gsimken.bgamesmod.BgamesMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, BgamesMod.MOD_ID);
    public static final RegistryObject<MobEffect> REACH_BOOST = MOB_EFFECTS.register("reach_boost",
            ()-> new ReachBoostEffect(MobEffectCategory.BENEFICIAL,3129339).
                    addAttributeModifier(ForgeMod.REACH_DISTANCE.get(),
                            "839A1A0E-FD2A-4880-96CB-94D18466DE60",
                            2.0, AttributeModifier.Operation.ADDITION)
    );
    public static final RegistryObject<MobEffect> PICKUP_BOOST = MOB_EFFECTS.register("pickup_boost",
            ()->new PickUpRangeBoostEffect(MobEffectCategory.BENEFICIAL,4653215)
    );
    public static final RegistryObject<MobEffect> AREA_REGENERATION = MOB_EFFECTS.register("area_regeneration",
            ()->new AreaRegenerationEffect(MobEffectCategory.BENEFICIAL,16711680)
    );
    public static final RegistryObject<MobEffect> AREA_STRENGTH = MOB_EFFECTS.register("area_strength",
            ()->new AreaStrengthEffect(MobEffectCategory.BENEFICIAL,16711680)
    );


    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}