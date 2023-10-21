package net.gsimken.bgamesmod.networking.packet;

import net.gsimken.bgameslibrary.bgames.BGamesLibraryTools;
import net.gsimken.bgamesmod.client.triggers.ChooseCategoriesTrigger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class ButtonOpenCategoriesC2SPacket {

    public ButtonOpenCategoriesC2SPacket() {

    }

    public ButtonOpenCategoriesC2SPacket(FriendlyByteBuf buf) {

    }

    public void toBytes(FriendlyByteBuf buf) {

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
       NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            if(BGamesLibraryTools.isPlayerLogged(player)){
                NetworkHooks.openScreen(player,new ChooseCategoriesTrigger());
            }
            else{
                player.sendSystemMessage(Component.translatable(  "login.bgameslibrary.not_logged").withStyle(ChatFormatting.RED));

            }


        });

        return true;



    }

}