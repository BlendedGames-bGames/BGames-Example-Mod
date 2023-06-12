package net.gsimken.bgamesmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.gsimken.bgamesmod.networking.packet.ButtonOpenScreenC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;

public class ChooseCategoriesScreen extends AbstractContainerScreen<ChooseCategoriesMenu> {
	private final static HashMap<String, Object> guistate = ChooseCategoriesMenu.guistate;
	private final Player player;
	ImageButton socialButton;
	ImageButton physicalButton;
	ImageButton cognitiveButton;
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/backgrounds/generic_background.png");
	private static final ResourceLocation SOCIAL_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/social_button_20x18.png");
	private static final ResourceLocation COGNITIVE_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/cognitive_button_20x18.png");
	private static final ResourceLocation PHYSICAL_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_button_20x18.png");
	private int BUTTON_WIDTH = 20;
	private int BUTTON_HEIGHT  = 18;
	public ChooseCategoriesScreen(ChooseCategoriesMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.player = container.player;
		this.imageWidth = 200;
		this.imageHeight = 120;
	}


	@Override
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		this.blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		int xCenter  = this.imageWidth/2;
		int y = BUTTON_HEIGHT*3+1;
		Component cognitiveLabel =  Component.translatable("gui.bgamesmod.choose_category.label_cognitive");
		this.font.draw(poseStack, cognitiveLabel, xCenter+3*BUTTON_WIDTH-this.font.width(cognitiveLabel)/2, y, -12829636);
		Component socialLabel =  Component.translatable("gui.bgamesmod.choose_category.label_social");
		this.font.draw(poseStack,socialLabel,xCenter-3*BUTTON_WIDTH-this.font.width(socialLabel)/2 , y, -12829636);
		Component physicalLabel =  Component.translatable("gui.bgamesmod.choose_category.label_physical");
		this.font.draw(poseStack,physicalLabel, xCenter-this.font.width(physicalLabel)/2, y, -12829636);
		Component consumePointsLabel =  Component.translatable("gui.bgamesmod.choose_category.label_consume_points");
		this.font.draw(poseStack,consumePointsLabel ,xCenter-this.font.width(consumePointsLabel)/2 , 10, -12829636);

	}

	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override
	public void init() {
		super.init();
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

		int xCenter  = this.leftPos+this.imageWidth/2;
		int y = this.topPos + BUTTON_HEIGHT*2;
		physicalButton = new ImageButton(xCenter-BUTTON_WIDTH/2, y, 20, 18, 0, 0, 19,PHYSICAL_BUTTON_TEXTURE,20,37,
				e -> {
					ModMessages.sendToServer(new ButtonOpenScreenC2SPacket(1));
				}
		);
		socialButton = new ImageButton(xCenter-3*BUTTON_WIDTH-BUTTON_WIDTH/2, y, 20, 18, 0, 0, 19, SOCIAL_BUTTON_TEXTURE,20,37,
				e -> {
					ModMessages.sendToServer(new ButtonOpenScreenC2SPacket(3));
				}
		);

		cognitiveButton = new ImageButton(xCenter+3*BUTTON_WIDTH-BUTTON_WIDTH/2, y, 20, 18, 0, 0, 19, COGNITIVE_BUTTON_TEXTURE,20,37,
				e -> {

					ModMessages.sendToServer(new ButtonOpenScreenC2SPacket(2));
				}
		);

		guistate.put("button:social_button", socialButton);
		guistate.put("button:physical_button", physicalButton);
		guistate.put("button:cognitive_button", cognitiveButton);
		this.addRenderableWidget(socialButton);
		this.addRenderableWidget(physicalButton);
		this.addRenderableWidget(cognitiveButton);
	}
}
