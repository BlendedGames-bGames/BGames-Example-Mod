package net.gsimken.bgamesmod.networking.packet;

import net.gsimken.bgameslibrary.BgamesLibrary;
import net.gsimken.bgameslibrary.bgames.BGamesLibraryTools;
import net.gsimken.bgamesmod.client.triggers.ChooseCategoriesTrigger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class ButtonsBGamesInteractPacket {

    /*
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
    * */
    private final int buttonId;


    public ButtonsBGamesInteractPacket(int buttonId) {
        this.buttonId = buttonId;
    }

    public ButtonsBGamesInteractPacket(FriendlyByteBuf buf) {
        this.buttonId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.buttonId);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
       NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();

            if(BGamesLibraryTools.isPlayerLogged(player)){

                int ticks= 20;
                int effectLevel = 1 ;
                if(BGamesLibraryTools.spendPoints(player, BgamesLibrary.bgames_physical_name,1)) {
                    switch (this.buttonId) {
                        case 0:
                            effectLevel = 7;
                            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, minutes2Ticks(10), effectLevel, false, false));
                            break;
                        case 1:
                            effectLevel = 1;
                            player.addEffect(new MobEffectInstance(MobEffects.JUMP, minutes2Ticks(10), effectLevel, false, false));
                            break;
                        case 2:
                            effectLevel = 0;
                            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, minutes2Ticks(20), effectLevel, false, false));
                            break;
                        case 3:
                            effectLevel = 3;
                            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, minutes2Ticks(5), effectLevel, false, false));
                            break;
                        case 4:
                            effectLevel = 9;
                            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, second2Ticks(30), effectLevel, false, false));
                            break;
                        case 5:
                            effectLevel = 9;
                            player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, minutes2Ticks(10), effectLevel, false, false));
                            break;
                        case 6:
                            effectLevel = 0;
                            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, minutes2Ticks(30), effectLevel, false, false));
                            break;
                        case 7:
                            effectLevel = 9;
                            player.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, minutes2Ticks(10), effectLevel, false, false));
                            break;
                        case 8:
                            effectLevel = 0;
                            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, minutes2Ticks(30), effectLevel, false, false));
                            break;
                        case 9:
                            effectLevel = 3;
                            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, minutes2Ticks(5), effectLevel, false, false));
                            break;
                    }
                }
            }
            else{
                player.sendSystemMessage(Component.translatable(  "login.bgameslibrary.not_logged").withStyle(ChatFormatting.RED));

            }


        });

        return true;



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