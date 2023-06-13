
package net.gsimken.bgamesmod.client.gui;

import net.gsimken.bgamesmod.networking.ModMessages;
import net.gsimken.bgamesmod.networking.packet.ButtonOpenScreenC2SPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class OpenPowersBGamesButtonOverlay {
	private static final Identifier BGAMES_BUTTON_LOCATION=new Identifier("bgamesmod:textures/screens/placeholder.png");
	//Button on Inventory
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void inventoryEventHandler(ScreenEvent.Init.Post event) {
		if (event.getScreen() instanceof InventoryScreen) {
			InventoryScreen inv = (InventoryScreen) event.getScreen();
			int w = inv.width;
			int h = inv.height;
			int posX = w / 2;
			int posY = h / 2;



			PlayerEntity entity = MinecraftClient.getInstance().player;
			int buttonPosX=  posX + 44; //recipeBook.updateScreenPosition(posX + 64,20);
			int buttonPosY =posY + -101;
			if (entity != null) {
				TexturedButtonWidget bGamesLogo= new TexturedButtonWidget(buttonPosX,buttonPosY , 20, 18, 0, 0, 19,  BGAMES_BUTTON_LOCATION,20,37,
						e -> {
							ModMessages.sendToServer(new ButtonOpenScreenC2SPacket(0));
						});
				event.addListener(bGamesLogo);

			}

		}
	}
}
