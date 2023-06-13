package net.gsimken.bgamesmod.networking.packet;


import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.gsimken.bgameslibrary.bgames.BGamesLibraryTools;
import net.gsimken.bgamesmod.client.triggers.ChooseCategoriesTrigger;
import net.gsimken.bgamesmod.client.triggers.CognitiveCategoryTrigger;
import net.gsimken.bgamesmod.client.triggers.PhysicalCategoryTrigger;
import net.gsimken.bgamesmod.client.triggers.SocialCategoryTrigger;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ButtonOpenScreenC2SPacket {
    public static void receive(MinecraftServer server,
                               ServerPlayerEntity player,
                               ServerPlayNetworkHandler handler,
                               PacketByteBuf buf,
                               PacketSender responseSender) {
        int screenId = buf.readInt();
        if(BGamesLibraryTools.isPlayerLogged(player)){
            player.closeHandledScreen();
            switch (screenId){
                case 0:
                    player.openHandledScreen(new ChooseCategoriesTrigger());
                    break;
                case 1:
                    player.openHandledScreen(new PhysicalCategoryTrigger());
                    break;
                case 2:
                    player.openHandledScreen(new CognitiveCategoryTrigger());
                    break;
                case 3:
                    player.openHandledScreen(new SocialCategoryTrigger());
                    break;
            }

        }
        else{
            player.sendMessage(Text.translatable(   "login.bgameslibrary.not_logged").fillStyle(Style.EMPTY.withColor(Formatting.RED)));

        }


    }
}