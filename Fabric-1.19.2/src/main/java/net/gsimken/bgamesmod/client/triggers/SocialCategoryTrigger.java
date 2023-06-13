package net.gsimken.bgamesmod.client.triggers;

import io.netty.buffer.Unpooled;
import net.gsimken.bgamesmod.client.menus.CognitiveCategoryMenu;
import net.gsimken.bgamesmod.client.menus.SocialCategoryMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

public class SocialCategoryTrigger implements NamedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.literal("Social Category");
    }

    @Override
    public ScreenHandler createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new SocialCategoryMenu(id, inventory, new PacketByteBuf(Unpooled.buffer()));
    }
}

