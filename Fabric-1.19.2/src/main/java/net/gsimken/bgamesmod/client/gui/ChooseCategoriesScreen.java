package net.gsimken.bgamesmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.HashMap;

public class ChooseCategoriesScreen extends HandledScreen<ChooseCategoriesMenu> {
	private final static HashMap<String, Object> guistate = ChooseCategoriesMenu.guistate;
	private final PlayerEntity player;
	TexturedButtonWidget socialButton;
	TexturedButtonWidget physicalButton;
	TexturedButtonWidget cognitiveButton;
	private static final Identifier BACKGROUND_TEXTURE = new Identifier("bgamesmod:textures/screens/backgrounds/generic_background.png");
	private static final Identifier SOCIAL_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/social_button_20x18.png");
	private static final Identifier COGNITIVE_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/cognitive_button_20x18.png");
	private static final Identifier PHYSICAL_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_button_20x18.png");
	private int BUTTON_WIDTH = 20;
	private int BUTTON_HEIGHT  = 18;
	public ChooseCategoriesScreen(ChooseCategoriesMenu container, PlayerInventory inventory, Text text) {
		super(container, inventory, text);
		this.player = container.player;
		this.backgroundWidth = 200;
		this.backgroundHeight = 120;
	}


	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.drawMouseoverTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		this.drawTexture(ms, this.x, this.y, 0, 0, this.backgroundWidth, this.backgroundHeight, this.backgroundWidth, this.backgroundHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.client.player.closeHandledScreen();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void handledScreenTick() {
		super.handledScreenTick();
	}

	@Override
	protected void drawForeground(MatrixStack poseStack, int mouseX, int mouseY) {
		int xCenter  = this.backgroundWidth/2;
		int y = BUTTON_HEIGHT*3+1;
		Text cognitiveLabel =  Text.translatable("gui.bgamesmod.choose_category.label_cognitive");
		this.textRenderer.draw(poseStack, cognitiveLabel, xCenter+3*BUTTON_WIDTH-this.textRenderer.getWidth(cognitiveLabel)/2, y, -12829636);
		Text socialLabel =  Text.translatable("gui.bgamesmod.choose_category.label_social");
		this.textRenderer.draw(poseStack,socialLabel,xCenter-3*BUTTON_WIDTH-this.textRenderer.getWidth(socialLabel)/2 , y, -12829636);
		Text physicalLabel =  Text.translatable("gui.bgamesmod.choose_category.label_physical");
		this.textRenderer.draw(poseStack,physicalLabel, xCenter-this.textRenderer.getWidth(physicalLabel)/2, y, -12829636);
		Text consumePointsLabel =  Text.translatable("gui.bgamesmod.choose_category.label_consume_points");
		this.textRenderer.draw(poseStack,consumePointsLabel ,xCenter-this.textRenderer.getWidth(consumePointsLabel)/2 , 10, -12829636);

	}

	@Override
	public void close() {
		super.close();
		MinecraftClient.getInstance().keyboard.setRepeatEvents(false);
	}

	@Override
	public void init() {
		super.init();
		this.client.keyboard.setRepeatEvents(true);

		int xCenter  = this.x+this.backgroundWidth/2;
		int y = this.y + BUTTON_HEIGHT*2;
		physicalButton = new TexturedButtonWidget(xCenter-BUTTON_WIDTH/2, y, 20, 18, 0, 0, 19,PHYSICAL_BUTTON_TEXTURE,20,37,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					ClientPlayNetworking.send(ModMessages.BUTTON_OPEN_SCREEN,  buf);


				}
		);
		socialButton = new TexturedButtonWidget(xCenter-3*BUTTON_WIDTH-BUTTON_WIDTH/2, y, 20, 18, 0, 0, 19, SOCIAL_BUTTON_TEXTURE,20,37,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(2);
					ClientPlayNetworking.send(ModMessages.BUTTON_OPEN_SCREEN,  buf);

				}
		);

		cognitiveButton = new TexturedButtonWidget(xCenter+3*BUTTON_WIDTH-BUTTON_WIDTH/2, y, 20, 18, 0, 0, 19, COGNITIVE_BUTTON_TEXTURE,20,37,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(3);
					ClientPlayNetworking.send(ModMessages.BUTTON_OPEN_SCREEN,  buf);

				}
		);

		guistate.put("button:social_button", socialButton);
		guistate.put("button:physical_button", physicalButton);
		guistate.put("button:cognitive_button", cognitiveButton);
		this.addDrawableChild(socialButton);
		this.addDrawableChild(physicalButton);
		this.addDrawableChild(cognitiveButton);
	}
}
