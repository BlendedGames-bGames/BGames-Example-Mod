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
	ImageButton affectiveButton;
	ImageButton cognitiveButton;
	ImageButton linguisticButton;
	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/backgrounds/generic_background.png");
	private static final ResourceLocation SOCIAL_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/social_button_20x18.png");
	private static final ResourceLocation COGNITIVE_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/cognitive_button_20x18.png");
	private static final ResourceLocation LINGUISTIC_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/linguistic_button_20x18.png");
	private static final ResourceLocation PHYSICAL_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_button_20x18.png");
	private static final ResourceLocation AFFECTIVE_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/affective_button_20x18.png");

	public ChooseCategoriesScreen(ChooseCategoriesMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.player = container.player;
		this.imageWidth = 260;
		this.imageHeight = 135;
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
		this.font.draw(poseStack, Component.translatable("gui.bgamesmod.choose_category.label_social"), 34, 67, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.bgamesmod.choose_category.label_physical"), 110, 68, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.bgamesmod.choose_category.label_consume_points"), 90, 11, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.bgamesmod.choose_category.label_affective"), 189, 67, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.bgamesmod.choose_category.label_cognitive"), 66, 115, -12829636);
		this.font.draw(poseStack, Component.translatable("gui.bgamesmod.choose_category.label_linguistic"), 145, 115, -12829636);
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
		int x = this.leftPos+34;
		int y = this.topPos+ 35;
		socialButton = new ImageButton(this.leftPos + 34, this.topPos + 35, 20, 18, 0, 0, 19, SOCIAL_BUTTON_TEXTURE,20,37,
				e -> {

				}
		);
		physicalButton = new ImageButton(this.leftPos + 114, this.topPos + 35, 20, 18, 0, 0, 19,PHYSICAL_BUTTON_TEXTURE,20,37,
				e -> {
					player.closeContainer();
					ModMessages.sendToServer(new ButtonOpenScreenC2SPacket(1));

				}
		);
		affectiveButton = new ImageButton(this.leftPos + 194, this.topPos + 35, 20, 18, 0, 0, 19,AFFECTIVE_BUTTON_TEXTURE,20,37,
				e -> {
				}
		);
		cognitiveButton = new ImageButton(this.leftPos + 74, this.topPos + 83, 20, 18, 0, 0, 19, COGNITIVE_BUTTON_TEXTURE,20,37,
				e -> {
				}
		);
		linguisticButton = new ImageButton(this.leftPos + 154, this.topPos + 83, 20, 18, 0, 0, 19, LINGUISTIC_BUTTON_TEXTURE,20,37,
				e -> {
				}
		);
		guistate.put("button:social_button", socialButton);
		guistate.put("button:physical_button", physicalButton);
		guistate.put("button:affective_button", affectiveButton);
		guistate.put("button:cognitive_button", cognitiveButton);
		guistate.put("button:linguistic_button", linguisticButton);
		this.addRenderableWidget(socialButton);
		this.addRenderableWidget(physicalButton);
		this.addRenderableWidget(affectiveButton);
		this.addRenderableWidget(cognitiveButton);
		this.addRenderableWidget(linguisticButton);
	}
}
