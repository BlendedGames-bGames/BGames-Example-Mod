package net.gsimken.bgamesmod.client.triggers;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.gsimken.bgamesmod.client.menus.CognitiveCategoryMenu;
import net.gsimken.bgamesmod.client.menus.PhysicalCategoryMenu;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CognitiveCategoryTrigger implements ExtendedScreenHandlerFactory {
    @Override
    public Text getDisplayName() {
        return Text.literal("Cognitive Category");
    }

    @Override
    public ScreenHandler createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new CognitiveCategoryMenu(id, inventory, new PacketByteBuf(Unpooled.buffer()));
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {

    }
}

