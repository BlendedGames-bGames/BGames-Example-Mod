package net.gsimken.bgamesmod.effects;

import java.util.List;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldAccess;

public class PickUpRangeBoostEffect extends StatusEffect {

    public PickUpRangeBoostEffect(StatusEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity,int amplifier){

        if(!entity.world.isClient() && entity instanceof PlayerEntity){
            int diameter = amplifier*4;
            if(diameter>80){
                diameter = 80;
            }
            WorldAccess world=entity.getWorld();
            List<ItemEntity> items=world.getNonSpectatingEntities(ItemEntity.class, Box.of(new Vec3d(entity.getX(),entity.getY(),entity.getZ()),diameter,diameter,diameter));
            for(int i=0;i<items.size();i++){
                items.get(i).onPlayerCollision((PlayerEntity)entity);
            }
        }
        super.applyUpdateEffect(entity,amplifier);
    }
    @Override
    public boolean canApplyUpdateEffect(int Duration,int amplifier){
        return true;
    }
}