package net.gsimken.bgamesmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.gsimken.bgameslibrary.BgamesLibrary;
import net.gsimken.bgameslibrary.bgames.BGamesLibraryTools;
import net.gsimken.bgameslibrary.bgames.BGamesPlayerData;
import net.gsimken.bgameslibrary.utils.IBGamesDataSaver;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.client.menus.PhysicalCategoryMenu;
import net.gsimken.bgamesmod.client.utils.BGamesButton;
import net.gsimken.bgamesmod.client.utils.ScreenHelper;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.HashMap;
import java.util.function.Consumer;

public class PhysicalCategoryEffectsScreen extends HandledScreen<PhysicalCategoryMenu> {
	private final static HashMap<String, Object> guistate = ChooseCategoriesMenu.guistate;
	private final PlayerEntity player;
	BGamesButton hasteButton;
	BGamesButton jumpBoostButton;
	BGamesButton speedButton;
	BGamesButton strengthButton;
	BGamesButton regenerationButton;
	BGamesButton absortionButton;
	BGamesButton fireResistanceButton;
	BGamesButton healthBoostButton;
	BGamesButton nightVisionButton;
	BGamesButton resistanceButton;
	ScreenHelper screenHelper;

	private static final Identifier BACKGROUND_TEXTURE = new Identifier("bgamesmod:textures/screens/backgrounds/generic_background.png");
	private static final Identifier BACK_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/backgrounds/back_20x18.png");

	private static final Identifier HASTE_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_category/haste.png");
	private static final Identifier JUMP_BOOST_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_category/jump_boost.png");
	private static final Identifier SPEED_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_category/speed.png");
	private static final Identifier STRENGTH_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_category/strength.png");
	private static final Identifier REGENERATION_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_category/regeneration.png");
	private static final Identifier ABSORTION_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_category/absortion.png");
	private static final Identifier FIRE_RESISTANCE_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_category/fire_resistance.png");
	private static final Identifier HEALTH_BOOST_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_category/health_boost.png");
	private static final Identifier NIGHT_VISION_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_category/night_vision.png");
	private static final Identifier RESISTANCE_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/physical_category/resistance.png");

	private static final int BUTTONS_WIDTH = 29; //width of button texture
	private static final int BUTTONS_HEIGHT = 26; //size of button texture unhovered
	private static final int BUTTONS_OFFSET = 2; //separation with hovered texture

	private static final int BUTTONS_TOTAL_HEIGHT = 2*BUTTONS_HEIGHT+BUTTONS_OFFSET; //image total height



	public PhysicalCategoryEffectsScreen(PhysicalCategoryMenu container, PlayerInventory inventory, Text text) {
		super(container, inventory, text);
		this.player = container.player;
		this.backgroundWidth = 500;
		this.backgroundHeight = 180;
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
		effectCheck();
	}

	@Override
	protected void drawForeground(MatrixStack poseStack, int mouseX, int mouseY) {
		Text consumePoints= Text.translatable("gui.bgamesmod.choose_category.label_consume_points");
		Text physicalLabel=Text.translatable("gui.bgamesmod.choose_category.label_physical");
		Text points  = ScreenHelper.getPoints(BGamesLibraryTools.getPoints(BgamesLibrary.bgames_physical_name,player));




		this.textRenderer.draw(poseStack,physicalLabel , this.screenHelper.tittleOffset(physicalLabel), 5, -12829636);
		this.textRenderer.draw(poseStack, consumePoints, this.screenHelper.tittleOffset(consumePoints), 15, -12829636);
		this.textRenderer.draw(poseStack, points, this.screenHelper.pointsTextOffset(points), 5, -12829636);
		int x;
		int y = (BUTTONS_HEIGHT*5/2) + 1;
		Text effectValue = ScreenHelper.getPoints(-1);
		Text[] effectNamesFirstRow= {StatusEffects.HASTE.getName(),StatusEffects.JUMP_BOOST.getName(),StatusEffects.SPEED.getName(),StatusEffects.STRENGTH.getName(),StatusEffects.REGENERATION.getName()};
		for(int i=0; i<5;i++){
			x = this.screenHelper.labelOffSet(effectNamesFirstRow[i],0,i);
			this.textRenderer.draw(poseStack,effectNamesFirstRow[i],x, y, -12829636);
			x = this.screenHelper.labelOffSet(effectValue, 0,i);
			this.textRenderer.draw(poseStack,effectValue,x, y+10, -12829636);
		}
		y+= BUTTONS_HEIGHT*5/2;
		Text[] effectNamesSecondRow= {StatusEffects.ABSORPTION.getName(),StatusEffects.FIRE_RESISTANCE.getName(),StatusEffects.HEALTH_BOOST.getName(),StatusEffects.NIGHT_VISION.getName(),StatusEffects.RESISTANCE.getName()};
		for(int i=0; i<5;i++){
			x = this.screenHelper.labelOffSet(effectNamesSecondRow[i],0,i);
			this.textRenderer.draw(poseStack,effectNamesSecondRow[i],x, y, -12829636);
			x = this.screenHelper.labelOffSet(effectValue, 0,i);
			this.textRenderer.draw(poseStack,effectValue,x, y+10, -12829636);
		}


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
		this.addDrawableChild(new TexturedButtonWidget(this.x + 5, this.y + 5, 20, 18, 0, 0, 19,BACK_BUTTON_TEXTURE,20,37,
				e ->{
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(0);
					ClientPlayNetworking.send(ModMessages.BUTTON_OPEN_SCREEN,  buf);

					}
		));

		this.screenHelper = new ScreenHelper(this.backgroundWidth,5,this.x);
		int y = this.y + BUTTONS_HEIGHT*3/2;
		this.screenHelper.addRow(5);
		setFirstRowButtons(y);
		y+= BUTTONS_HEIGHT*5/2;
		setSecondRowButtons(y);


	}
	private void effectCheck(){
		if(player.hasStatusEffect(StatusEffects.HASTE) && hasteButton!=null){
			hasteButton.setOff();
		}else{
			if(hasteButton!=null && !hasteButton.isEnabled()) {
				hasteButton.setOn();
			}
		}
		if(player.hasStatusEffect(StatusEffects.ABSORPTION) && absortionButton!=null){
			absortionButton.setOff();
		}else{
			if(absortionButton!=null && !absortionButton.isEnabled()) {
				absortionButton.setOn();
			}
		}
		if(player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE) && fireResistanceButton!=null){
			fireResistanceButton.setOff();
		}else{
			if(fireResistanceButton!=null && !fireResistanceButton.isEnabled()) {
				fireResistanceButton.setOn();
			}
		}
		if(player.hasStatusEffect(StatusEffects.HEALTH_BOOST) && healthBoostButton!=null){
			healthBoostButton.setOff();
		}else{
			if(healthBoostButton!=null && !healthBoostButton.isEnabled()) {
				healthBoostButton.setOn();
			}
		}
		if(player.hasStatusEffect(StatusEffects.JUMP_BOOST) && jumpBoostButton!=null){
			jumpBoostButton.setOff();
		}else{
			if(jumpBoostButton!=null && !jumpBoostButton.isEnabled()) {
				jumpBoostButton.setOn();
			}
		}
		if(player.hasStatusEffect(StatusEffects.NIGHT_VISION) && nightVisionButton!=null){
			nightVisionButton.setOff();
		}else{
			if(nightVisionButton!=null && !nightVisionButton.isEnabled()) {
				nightVisionButton.setOn();
			}
		}
		if(player.hasStatusEffect(StatusEffects.REGENERATION) && regenerationButton!=null){
			regenerationButton.setOff();
		}else{
			if(regenerationButton!=null && !regenerationButton.isEnabled()) {
				regenerationButton.setOn();
			}
		}
		if(player.hasStatusEffect(StatusEffects.RESISTANCE) && resistanceButton!=null){
			resistanceButton.setOff();
		}else{
			if(resistanceButton!=null && !resistanceButton.isEnabled()) {
				resistanceButton.setOn();
			}
		}
		if(player.hasStatusEffect(StatusEffects.SPEED) && speedButton!=null){
			speedButton.setOff();
		}else{
			if(speedButton!=null && !speedButton.isEnabled()) {
				speedButton.setOn();
			}
		}
		if(player.hasStatusEffect(StatusEffects.STRENGTH) && strengthButton!=null){
			strengthButton.setOff();
		}else{
			if(strengthButton!=null && !strengthButton.isEnabled()) {
				strengthButton.setOn();
			}
		}

	}
	private void setFirstRowButtons(int y){
		int x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,0);

		hasteButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, HASTE_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					buf.writeInt(0);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						StatusEffect effect = StatusEffects.HASTE;
						String minutes= "10:00";
						String amplifier = "VIII";
						String firstToolTip = effect.getName().getString()+ " "+amplifier;
						String secondToolTip = Text.translatable("gui.minutes",minutes).getString();
						TextRenderer font = MinecraftClient.getInstance().textRenderer;

						renderTooltip(matrices,Text.literal(firstToolTip),i+(font.getWidth(secondToolTip)-font.getWidth(firstToolTip))/2,j);
						renderTooltip(matrices,Text.literal(secondToolTip),i,j+10);
						}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
			);

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,1);

		jumpBoostButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, JUMP_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					buf.writeInt(1);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);



				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						StatusEffect effect = StatusEffects.JUMP_BOOST;
						String minutes= "10:00";
						String amplifier = "II";
						String firstToolTip = effect.getName().getString()+ " "+amplifier;
						String secondToolTip = Text.translatable("gui.minutes",minutes).getString();
						TextRenderer font = MinecraftClient.getInstance().textRenderer;

						renderTooltip(matrices,Text.literal(firstToolTip),i+(font.getWidth(secondToolTip)-font.getWidth(firstToolTip))/2,j);
						renderTooltip(matrices,Text.literal(secondToolTip),i,j+10);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,2);

		speedButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, SPEED_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					buf.writeInt(2);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						StatusEffect effect = StatusEffects.SPEED;
						String minutes= "20:00";
						String amplifier = "I";
						String firstToolTip = effect.getName().getString()+ " "+amplifier;
						String secondToolTip = Text.translatable("gui.minutes",minutes).getString();
						TextRenderer font = MinecraftClient.getInstance().textRenderer;

						renderTooltip(matrices,Text.literal(firstToolTip),i+(font.getWidth(secondToolTip)-font.getWidth(firstToolTip))/2,j);
						renderTooltip(matrices,Text.literal(secondToolTip),i,j+10);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);
		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,3);

		strengthButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, STRENGTH_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					buf.writeInt(3);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						StatusEffect effect = StatusEffects.STRENGTH;
						String minutes= "5:00";
						String amplifier = "IV";
						String firstToolTip = effect.getName().getString()+ " "+amplifier;
						String secondToolTip = Text.translatable("gui.minutes",minutes).getString();
						TextRenderer font = MinecraftClient.getInstance().textRenderer;

						renderTooltip(matrices,Text.literal(firstToolTip),i+(font.getWidth(secondToolTip)-font.getWidth(firstToolTip))/2,j);
						renderTooltip(matrices,Text.literal(secondToolTip),i,j+10);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);
		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,4);

		regenerationButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, REGENERATION_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					buf.writeInt(4);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						StatusEffect effect = StatusEffects.REGENERATION;
						String minutes= "0:30";
						String amplifier = "X";
						String firstToolTip = effect.getName().getString()+ " "+amplifier;
						String secondToolTip = Text.translatable("gui.minutes",minutes).getString();
						TextRenderer font = MinecraftClient.getInstance().textRenderer;

						renderTooltip(matrices,Text.literal(firstToolTip),i+(font.getWidth(secondToolTip)-font.getWidth(firstToolTip))/2,j);
						renderTooltip(matrices,Text.literal(secondToolTip),i,j+10);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);

		guistate.put("button:haste_button", hasteButton);
		guistate.put("button:jump_boost_button", jumpBoostButton);
		guistate.put("button:speed_button", speedButton);
		guistate.put("button:strength_button", strengthButton);
		guistate.put("button:regeneration_button", regenerationButton);
		this.addDrawableChild(hasteButton);
		this.addDrawableChild(jumpBoostButton);
		this.addDrawableChild(speedButton);
		this.addDrawableChild(strengthButton);
		this.addDrawableChild(regenerationButton);

	}
	private void setSecondRowButtons(int y){
		int x = this.screenHelper.elementOffset(BUTTONS_WIDTH,1,0);

		absortionButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, ABSORTION_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					buf.writeInt(5);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						StatusEffect effect = StatusEffects.ABSORPTION;
						String minutes= "10:00";
						String amplifier = "X";
						String firstToolTip = effect.getName().getString()+ " "+amplifier;
						String secondToolTip = Text.translatable("gui.minutes",minutes).getString();
						TextRenderer font = MinecraftClient.getInstance().textRenderer;

						renderTooltip(matrices,Text.literal(firstToolTip),i+(font.getWidth(secondToolTip)-font.getWidth(firstToolTip))/2,j);
						renderTooltip(matrices,Text.literal(secondToolTip),i,j+10);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,1,1);
		fireResistanceButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, FIRE_RESISTANCE_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					buf.writeInt(6);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						StatusEffect effect = StatusEffects.FIRE_RESISTANCE;
						String minutes= "30:00";
						String amplifier = "I";
						String firstToolTip = effect.getName().getString()+ " "+amplifier;
						String secondToolTip = Text.translatable("gui.minutes",minutes).getString();
						TextRenderer font = MinecraftClient.getInstance().textRenderer;

						renderTooltip(matrices,Text.literal(firstToolTip),i+(font.getWidth(secondToolTip)-font.getWidth(firstToolTip))/2,j);
						renderTooltip(matrices,Text.literal(secondToolTip),i,j+10);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,1,2);
		healthBoostButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, HEALTH_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					buf.writeInt(7);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						StatusEffect effect = StatusEffects.HEALTH_BOOST;
						String minutes= "10:00";
						String amplifier = "X";
						String firstToolTip = effect.getName().getString()+ " "+amplifier;
						String secondToolTip = Text.translatable("gui.minutes",minutes).getString();
						TextRenderer font = MinecraftClient.getInstance().textRenderer;

						renderTooltip(matrices,Text.literal(firstToolTip),i+(font.getWidth(secondToolTip)-font.getWidth(firstToolTip))/2,j);
						renderTooltip(matrices,Text.literal(secondToolTip),i,j+10);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);
		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,1,3);
		nightVisionButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, NIGHT_VISION_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					buf.writeInt(8);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						StatusEffect effect = StatusEffects.NIGHT_VISION;
						String minutes= "30:00";
						String amplifier = "I";
						String firstToolTip = effect.getName().getString()+ " "+amplifier;
						String secondToolTip = Text.translatable("gui.minutes",minutes).getString();
						TextRenderer font = MinecraftClient.getInstance().textRenderer;

						renderTooltip(matrices,Text.literal(firstToolTip),i+(font.getWidth(secondToolTip)-font.getWidth(firstToolTip))/2,j);
						renderTooltip(matrices,Text.literal(secondToolTip),i,j+10);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);
		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,1,4);
		resistanceButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, RESISTANCE_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(1);
					buf.writeInt(9);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						StatusEffect effect = StatusEffects.RESISTANCE;
						String minutes= "5:00";
						String amplifier = "IV";
						String firstToolTip = effect.getName().getString()+ " "+amplifier;
						String secondToolTip = Text.translatable("gui.minutes",minutes).getString();
						TextRenderer font = MinecraftClient.getInstance().textRenderer;

						renderTooltip(matrices,Text.literal(firstToolTip),i+(font.getWidth(secondToolTip)-font.getWidth(firstToolTip))/2,j);
						renderTooltip(matrices,Text.literal(secondToolTip),i,j+10);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);

		guistate.put("button:absortion_button", absortionButton);
		guistate.put("button:fire_resistance_button", fireResistanceButton);
		guistate.put("button:health_boost_button", healthBoostButton);
		guistate.put("button:night_vision_button", nightVisionButton);
		guistate.put("button:resistance_button", resistanceButton);
		this.addDrawableChild(absortionButton);
		this.addDrawableChild(fireResistanceButton);
		this.addDrawableChild(healthBoostButton);
		this.addDrawableChild(nightVisionButton);
		this.addDrawableChild(resistanceButton);

	}



}
