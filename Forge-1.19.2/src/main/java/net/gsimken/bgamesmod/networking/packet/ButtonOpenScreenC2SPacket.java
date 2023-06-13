package net.gsimken.bgamesmod.networking.packet;

import net.gsimken.bgameslibrary.bgames.BGamesLibraryTools;
import net.gsimken.bgamesmod.client.triggers.ChooseCategoriesTrigger;
import net.gsimken.bgamesmod.client.triggers.CognitiveCategoryTrigger;
import net.gsimken.bgamesmod.client.triggers.PhysicalCategoryTrigger;
import net.gsimken.bgamesmod.client.triggers.SocialCategoryTrigger;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.function.Supplier;

public class ButtonOpenScreenC2SPacket {
    /*
    * Screens id:
    * Choose Category: 0
    * Physical Category: 1
    * Social Category: 2
    * */
    private final int screenId;


    public ButtonOpenScreenC2SPacket(int screenId) {
        this.screenId = screenId;
    }

    public ButtonOpenScreenC2SPacket(FriendlyByteBuf buf) {
        this.screenId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(this.screenId);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
       NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE SERVER!
            ServerPlayer player = context.getSender();
            if(BGamesLibraryTools.isPlayerLogged(player)){
                if(player.hasContainerOpen()){
                    player.closeContainer();
                }
                switch (this.screenId){
                    case 0:
                        NetworkHooks.openScreen(player,new ChooseCategoriesTrigger());
                        break;
                    case 1:
                        NetworkHooks.openScreen(player,new PhysicalCategoryTrigger());
                        break;
                    case 2:
                        NetworkHooks.openScreen(player,new CognitiveCategoryTrigger());
                        break;
                    case 3:
                        NetworkHooks.openScreen(player,new SocialCategoryTrigger());
                        break;
                }

            }
            else{
                player.sendSystemMessage(Component.translatable(  "login.bgameslibrary.not_logged").withStyle(ChatFormatting.RED));
            }


        });

        return true;



    }

}