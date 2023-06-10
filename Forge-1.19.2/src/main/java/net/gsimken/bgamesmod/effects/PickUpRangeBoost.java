package net.gsimken.bgamesmod.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PickUpRangeBoost extends MobEffect {

    public PickUpRangeBoost(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
    @Override
    public void applyEffectTick(LivingEntity entity,int amplifier){

        if(!entity.level.isClientSide() && entity instanceof Player){
            int diameter = amplifier*4;
            if(diameter>80){
                diameter = 80;
            }
            LevelAccessor world=entity.getLevel();
            List<ItemEntity> items=world.getEntitiesOfClass(ItemEntity.class, AABB.ofSize(new Vec3(entity.getX(),entity.getY(),entity.getZ()),diameter,diameter,diameter));
            for(int i=0;i<items.size();i++){
                items.get(i).playerTouch((Player)entity);
            }
        }
        super.applyEffectTick(entity,amplifier);
    }
    @Override
    public boolean isDurationEffectTick(int Duration,int amplifier){
        return true;
    }
}