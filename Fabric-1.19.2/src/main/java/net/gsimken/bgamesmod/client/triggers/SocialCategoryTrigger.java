package net.gsimken.bgamesmod.client.triggers;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.gsimken.bgamesmod.client.menus.SocialCategoryMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class SocialCategoryTrigger implements ExtendedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.literal("Social Category");
    }

    @Override
    public ScreenHandler createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new SocialCategoryMenu(id, inventory, new PacketByteBuf(Unpooled.buffer()));
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {

    }

    @Override
    public boolean shouldCloseCurrentScreen() {
        return ExtendedScreenHandlerFactory.super.shouldCloseCurrentScreen();
    }
}

