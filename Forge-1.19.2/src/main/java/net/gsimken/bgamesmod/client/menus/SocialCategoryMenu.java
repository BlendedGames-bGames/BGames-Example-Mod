 package net.gsimken.bgamesmod.client.menus;


import net.gsimken.bgamesmod.client.ModMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;

 public class SocialCategoryMenu extends AbstractContainerMenu {
     public final static HashMap<String, Object> guistate = new HashMap<>();
     public final Player player;
     public SocialCategoryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
         super(ModMenus.SOCIAL_CATEGORY.get(), id);
         this.player = inv.player;

     }


     @Override
     public boolean stillValid(Player player) {
         return true;
     }

     @Override
     public ItemStack quickMoveStack(Player playerIn, int index) {
         return ItemStack.EMPTY;
     }


 }
