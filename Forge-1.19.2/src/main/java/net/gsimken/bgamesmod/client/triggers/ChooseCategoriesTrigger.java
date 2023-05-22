package net.gsimken.bgamesmod.client.triggers;

import io.netty.buffer.Unpooled;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class ChooseCategoriesTrigger implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return Component.literal("Choose Category");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ChooseCategoriesMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()));
    }
}

