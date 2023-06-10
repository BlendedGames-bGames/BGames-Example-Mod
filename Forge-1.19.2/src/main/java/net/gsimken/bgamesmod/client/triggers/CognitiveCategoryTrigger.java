package net.gsimken.bgamesmod.client.triggers;

import io.netty.buffer.Unpooled;
import net.gsimken.bgamesmod.client.menus.CognitiveCategoryMenu;
import net.gsimken.bgamesmod.client.menus.PhysicalCategoryMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class CognitiveCategoryTrigger implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return Component.literal("Cognitive Category");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new CognitiveCategoryMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()));
    }
}

