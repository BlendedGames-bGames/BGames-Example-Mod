package net.gsimken.bgamesmod.client.triggers;

import io.netty.buffer.Unpooled;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.client.menus.PhysicalCategoryMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PhysicalCategoryTrigger implements MenuProvider {
    @Override
    public Component getDisplayName() {
        return Component.literal("Physical Category");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new PhysicalCategoryMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()));
    }
}

