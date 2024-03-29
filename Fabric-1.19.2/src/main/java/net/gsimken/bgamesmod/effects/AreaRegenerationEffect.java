package net.gsimken.bgamesmod.effects;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import java.util.List;
import java.util.stream.Collectors;

public class AreaRegenerationEffect extends StatusEffect {
    int ticks=0;
    public AreaRegenerationEffect(StatusEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier){

        int radius = Math.min(amplifier*2,20);
        if(entity instanceof PlayerEntity){

            if(!entity.world.isClient()) {
entity.isInvisible()
                ;//max radius of 40 blocks
                Box boundingBox = entity.getBoundingBox().expand(radius);
                List<LivingEntity> entities = entity.world.getNonSpectatingEntities(LivingEntity.class, boundingBox);

                entities = entities.stream()
                        .filter(entityCollected -> entityCollected instanceof TameableEntity && ((TameableEntity) entityCollected).isTamed() ||  entityCollected instanceof HorseEntity && ((HorseEntity) entityCollected).isTame() || entityCollected instanceof PlayerEntity)
                        .collect(Collectors.toList());
                for (int i = 0; i < entities.size(); i++) {
                    LivingEntity objective = entities.get(i);

                    if (!objective.hasStatusEffect(StatusEffects.REGENERATION) && objective!=entity) {
                        objective.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 1));
                    }
                }
                if(this.ticks>=15){
                    PacketByteBuf buf;
                    ServerWorld level = (ServerWorld)entity.getWorld();
                    List<ServerPlayerEntity> players = level.getPlayers(LivingEntity::isAlive);
                    BlockPos blockpos = entity.getBlockPos();
                    for (ServerPlayerEntity player: players) {
                        if (blockpos.isWithinDistance(new Vec3d(player.getX(), player.getY(), player.getZ()), 32.0D)){
                            buf = PacketByteBufs.create();
                            buf.writeInt(0);
                            buf.writeDouble(entity.getX());
                            buf.writeDouble(entity.getY());
                            buf.writeDouble(entity.getZ());
                            buf.writeDouble(radius*2);
                            buf.writeInt(30);
                            ServerPlayNetworking.send(player,ModMessages.CREATE_AREA_PARTICLES,  buf);


                        }
                    }
                    this.ticks=0;

                }
                this.ticks++;

            }

        }

        super.applyUpdateEffect(entity,amplifier);
    }
    @Override
    public boolean canApplyUpdateEffect(int Duration,int amplifier){
        return true;
    }
}
