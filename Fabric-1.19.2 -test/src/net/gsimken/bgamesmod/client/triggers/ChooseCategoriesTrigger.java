package net.gsimken.bgamesmod.client.triggers;

import io.netty.buffer.Unpooled;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;

public class ChooseCategoriesTrigger implements NamedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.literal("Choose Category");
    }

    @Override
    public ScreenHandler createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new ChooseCategoriesMenu(id, inventory, new PacketByteBuf(Unpooled.buffer()));
    }
}

