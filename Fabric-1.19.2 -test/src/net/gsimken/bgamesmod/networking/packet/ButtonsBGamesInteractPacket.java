package net.gsimken.bgamesmod.networking.packet;

import net.gsimken.bgameslibrary.BgamesLibrary;
import net.gsimken.bgameslibrary.bgames.BGamesLibraryTools;
import net.gsimken.bgamesmod.client.triggers.ChooseCategoriesTrigger;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class ButtonsBGamesInteractPacket {

    /*

    -------SOCIAL : 0
    ------PHYSICAL: 1
    * EFFECT LVL DURATION : ID
    * Haste 3 10MIN : 0
    * Jump Boost 2 10 MIN : 1
    * Speed 1 20 MIN : 2
    * Strength 4 5 MIN : 3
    * Regeneration 10 30SEC : 4
    * Absortion 10 10MIN : 5
    * Fire Resistance 1 30MIN : 6
    * Health Boost 10 10MIN : 7
    * Night Vision 1 30MIN : 8
    * Resistance 4 5MIN :9
    ------AFECTIVE:2
    ------COGNITIVE:3
    * DESCRIPTION : ID
    * set 2 enchantsments : 10
    * Reach Boost 10 20MIN: 11
    * Pickup Boost 20 20MIN: 12
    * TRANSMUTATION : 13
    ------LINGUISTIC: 4
    * */
    private final int categoryId;
    private final int buttonId;


    public ButtonsBGamesInteractPacket(int categoryId,int buttonId) {
        this.buttonId = buttonId;
        this.categoryId = categoryId;

    }

    public ButtonsBGamesInteractPacket(PacketByteBuf buf) {
        this.buttonId = buf.readInt();
        this.categoryId = buf.readInt();
    }

    public void toBytes(PacketByteBuf buf) {
        buf.writeInt(this.buttonId);
        buf.writeInt(this.categoryId);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
       NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();

            if(BGamesLibraryTools.isPlayerLogged(player)){
                switch (this.categoryId){
                    case 0: //social
                        if(this.buttonId<4 && this.buttonId>=0 && BGamesLibraryTools.spendPoints(player, BgamesLibrary.bgames_social_name,1)) {
                            socialBoosts(this.buttonId,player);
                        }
                        break;
                    case 1://physical
                        if(this.buttonId<10 && this.buttonId>=0 && BGamesLibraryTools.spendPoints(player, BgamesLibrary.bgames_physical_name,1)) {
                            physicalBoosts(this.buttonId,player);
                        }
                        break;
                    case 2://affective

                        break;
                    case 3://cognitive
                        if(this.buttonId<4 && this.buttonId>=0 && BGamesLibraryTools.spendPoints(player, BgamesLibrary.bgames_cognitive_name,1)) {
                            cognitiveBoosts(this.buttonId,player);
                        }
                        break;
                    case 4://linguistic

                        break;

                }
            }
            else{
                player.sendSystemMessage(Component.translatable(  "login.bgameslibrary.not_logged").withStyle(ChatFormatting.RED));

            }


        });

        return true;



    }
    private void socialBoosts(int id,ServerPlayerEntity player) {
        int effectLevel = 1 ;
        switch (id){
            case 0:
                effectLevel = 4;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HERO_OF_THE_VILLAGE, minutes2Ticks(30), effectLevel, false, false));
                break;
            case 1:
                effectLevel = 9;
                player.addStatusEffect(new StatusEffectInstance(ModEffects.AREA_REGENERATION.get(), minutes2Ticks(20), effectLevel, false, false));
                break;
            case 2:
                effectLevel = 9;
                player.addStatusEffect(new StatusEffectInstance(ModEffects.AREA_STRENGTH.get(), minutes2Ticks(20), effectLevel, false, false));
                break;
        }
    }
    private void cognitiveBoosts(int id,ServerPlayerEntity player) {
        int effectLevel = 1 ;
        switch (id){
            case 0:
                if(player.experienceLevel<33){
                    player.setExperienceLevel(33);
                }
                else{
                    player.addExperienceLevels(6);
                }
                break;
            case 1:
                effectLevel = 9;
                player.addStatusEffect(new StatusEffectInstance(ModEffects.REACH_BOOST.get(), minutes2Ticks(20), effectLevel, false, false));
                break;
            case 2:
                effectLevel = 19;
                player.addStatusEffect(new StatusEffectInstance(ModEffects.PICKUP_BOOST.get(), minutes2Ticks(20), effectLevel, false, false));
                break;
        }
    }
    private void physicalBoosts(int id,ServerPlayerEntity player) {
        int effectLevel = 1 ;
        switch (id) {
            case 0:
                effectLevel = 7;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, minutes2Ticks(10), effectLevel, false, false));
                break;
            case 1:
                effectLevel = 1;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.JUMP_BOOST, minutes2Ticks(10), effectLevel, false, false));
                break;
            case 2:
                effectLevel = 0;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, minutes2Ticks(20), effectLevel, false, false));
                break;
            case 3:
                effectLevel = 3;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, minutes2Ticks(5), effectLevel, false, false));
                break;
            case 4:
                effectLevel = 9;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, second2Ticks(30), effectLevel, false, false));
                break;
            case 5:
                effectLevel = 9;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, minutes2Ticks(10), effectLevel, false, false));
                break;
            case 6:
                effectLevel = 0;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, minutes2Ticks(30), effectLevel, false, false));
                break;
            case 7:
                effectLevel = 9;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, minutes2Ticks(10), effectLevel, false, false));
                break;
            case 8:
                effectLevel = 0;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, minutes2Ticks(30), effectLevel, false, false));
                break;
            case 9:
                effectLevel = 3;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, minutes2Ticks(5), effectLevel, false, false));
                break;
        }
    }
/**
 * Function that convert minutes
 * */
    private int minutes2Ticks(int minutes){
        int TICKS_IN_ONE_SECOND=20;
        return TICKS_IN_ONE_SECOND*60*minutes;
    }
    private int second2Ticks(int seconds){
        int TICKS_IN_ONE_SECOND=20;
        return TICKS_IN_ONE_SECOND*seconds;
    }
}