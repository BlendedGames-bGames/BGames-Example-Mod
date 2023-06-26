package net.gsimken.bgamesmod.effects;

import net.gsimken.bgamesmod.BgamesMod;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;

public class ModEffects {
    public static StatusEffect REACH_BOOST;
    public static StatusEffect PICKUP_BOOST;
    public static StatusEffect AREA_REGENERATION;
    public static StatusEffect AREA_STRENGTH;


    public static void register() {
        REACH_BOOST = Registry.register(Registry.STATUS_EFFECT, new Identifier(BgamesMod.MOD_ID, "reach_boost"),
                new ReachBoostEffect(StatusEffectCategory.BENEFICIAL, 3129339).addAttributeModifier(ReachEntityAttributes.REACH, "F934554A-097F-11EE-BE56-0242AC120002", 4.0, EntityAttributeModifier.Operation.ADDITION)
        );
        PICKUP_BOOST = Registry.register(Registry.STATUS_EFFECT, new Identifier(BgamesMod.MOD_ID, "pickup_boost"),
                new PickUpRangeBoostEffect(StatusEffectCategory.BENEFICIAL, 4653215)
        );
        AREA_REGENERATION = Registry.register(Registry.STATUS_EFFECT, new Identifier(BgamesMod.MOD_ID, "area_regeneration"),
                new AreaRegenerationEffect(StatusEffectCategory.BENEFICIAL, 16711680)
        );
        AREA_STRENGTH = Registry.register(Registry.STATUS_EFFECT, new Identifier(BgamesMod.MOD_ID, "area_strength"),
                new AreaStrengthEffect(StatusEffectCategory.BENEFICIAL, 16711680)
        );
    }



}