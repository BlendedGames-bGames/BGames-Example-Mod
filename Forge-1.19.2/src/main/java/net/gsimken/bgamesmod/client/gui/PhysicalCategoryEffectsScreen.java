package net.gsimken.bgamesmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.gsimken.bgameslibrary.bgames.BGamesPlayerData;
import net.gsimken.bgameslibrary.bgames.ClientBGamesPlayerData;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.client.menus.PhysicalCategoryMenu;
import net.gsimken.bgamesmod.client.utils.BGamesButton;
import net.gsimken.bgamesmod.client.utils.ScreenUtils;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.gsimken.bgamesmod.networking.packet.ButtonOpenScreenC2SPacket;
import net.gsimken.bgamesmod.networking.packet.ButtonsBGamesInteractPacket;
import net.minecraft.client.Minecraft;
import net.gsimken.bgamesmod.client.utils.BGamesButton;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.function.Consumer;

public class PhysicalCategoryEffectsScreen extends AbstractContainerScreen<PhysicalCategoryMenu> {
	private final static HashMap<String, Object> guistate = ChooseCategoriesMenu.guistate;
	private final Player player;
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

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/backgrounds/generic_background.png");
	private static final ResourceLocation HASTE_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physicalcategory/haste.png");
	private static final ResourceLocation JUMP_BOOST_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physicalcategory/jump_boost.png");
	private static final ResourceLocation SPEED_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physicalcategory/speed.png");
	private static final ResourceLocation STRENGTH_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physicalcategory/strength.png");
	private static final ResourceLocation REGENERATION_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physicalcategory/regeneration.png");
	private static final ResourceLocation ABSORTION_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physicalcategory/absortion.png");
	private static final ResourceLocation FIRE_RESISTANCE_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physicalcategory/fire_resistance.png");
	private static final ResourceLocation HEALTH_BOOST_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physicalcategory/health_boost.png");
	private static final ResourceLocation NIGHT_VISION_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physicalcategory/night_vision.png");
	private static final ResourceLocation RESISTANCE_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physicalcategory/resistance.png");

	private static final int BUTTONS_WIDTH = 29; //width of button texture
	private static final int BUTTONS_HEIGHT = 26; //size of button texture unhovered
	private static final int BUTTONS_OFFSET = 2; //separation with hovered texture

	private static final int BUTTONS_TOTAL_HEIGHT = 2*BUTTONS_HEIGHT+BUTTONS_OFFSET; //image total height



	public PhysicalCategoryEffectsScreen(PhysicalCategoryMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.player = container.player;
		this.imageWidth = 470;
		this.imageHeight = 180;
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
		RenderSystem.setShaderTexture(0, new ResourceLocation("bgamesmod:textures/screens/physical_button_20x18.png"));
		this.blit(ms, this.leftPos + 5, this.topPos + 5, 0,0,20, 18 , 20, 37);
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
		effectCheck();
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		Component consumePoints= Component.translatable("gui.bgamesmod.choose_category.label_consume_points");
		Component physicalLabel=Component.translatable("gui.bgamesmod.choose_category.label_physical");
		Component points  = ScreenUtils.getPoints(ClientBGamesPlayerData.getPlayerPhysicalPoints());


		this.font.draw(poseStack,physicalLabel , (this.imageWidth/2)-(this.font.width(physicalLabel)/2), 5, -12829636);
		this.font.draw(poseStack, consumePoints, (this.imageWidth/2)-(this.font.width(consumePoints)/2), 15, -12829636);
		this.font.draw(poseStack, points, this.imageWidth-this.font.width(points)-5, 5, -12829636);
		int x = (this.imageWidth/11);
		int y = (BUTTONS_HEIGHT*5/2) + 1;
		Component effectValue = ScreenUtils.getPoints(-1);
		Component[] effectNamesFirstRow= {MobEffects.DIG_SPEED.getDisplayName(),MobEffects.JUMP.getDisplayName(),MobEffects.MOVEMENT_SPEED.getDisplayName(),MobEffects.DAMAGE_BOOST.getDisplayName(),MobEffects.REGENERATION.getDisplayName()};
		for(int i=0; i<5;i++){
			this.font.draw(poseStack,effectNamesFirstRow[i], ScreenUtils.calculateCenteredX(x, BUTTONS_WIDTH, effectNamesFirstRow[i]) , y, -12829636);
			this.font.draw(poseStack,effectValue,ScreenUtils.calculateCenteredX(x, BUTTONS_WIDTH, effectValue) , y+10, -12829636);
			x= horizontalSpacing(x);
		}
		x = (this.imageWidth/11);
		y+= BUTTONS_HEIGHT*5/2;
		Component[] effectNamesSecondRow= {MobEffects.ABSORPTION.getDisplayName(),MobEffects.FIRE_RESISTANCE.getDisplayName(),MobEffects.HEALTH_BOOST.getDisplayName(),MobEffects.NIGHT_VISION.getDisplayName(),MobEffects.DAMAGE_RESISTANCE.getDisplayName()};
		for(int i=0; i<5;i++){
			this.font.draw(poseStack,effectNamesSecondRow[i],ScreenUtils.calculateCenteredX(x, BUTTONS_WIDTH, effectNamesSecondRow[i]) , y, -12829636);
			this.font.draw(poseStack,effectValue,ScreenUtils.calculateCenteredX(x, BUTTONS_WIDTH, effectValue) , y+10, -12829636);
			x= horizontalSpacing(x);
		}


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
		int x = this.leftPos + (this.imageWidth/11);
		int y = this.topPos + BUTTONS_HEIGHT*3/2;
		setFirstRowButtons(x,y);
		y+= BUTTONS_HEIGHT*5/2;
		setSecondRowButtons(x,y);


	}
	private void effectCheck(){
		if(player.hasEffect(MobEffects.DIG_SPEED) && hasteButton!=null){
			hasteButton.setOff();
		}else{
			if(hasteButton!=null && !hasteButton.isEnabled()) {
				hasteButton.setOn();
			}
		}
		if(player.hasEffect(MobEffects.ABSORPTION) && absortionButton!=null){
			absortionButton.setOff();
		}else{
			if(absortionButton!=null && !absortionButton.isEnabled()) {
				absortionButton.setOn();
			}
		}
		if(player.hasEffect(MobEffects.FIRE_RESISTANCE) && fireResistanceButton!=null){
			fireResistanceButton.setOff();
		}else{
			if(fireResistanceButton!=null && !fireResistanceButton.isEnabled()) {
				fireResistanceButton.setOn();
			}
		}
		if(player.hasEffect(MobEffects.HEALTH_BOOST) && healthBoostButton!=null){
			healthBoostButton.setOff();
		}else{
			if(healthBoostButton!=null && !healthBoostButton.isEnabled()) {
				healthBoostButton.setOn();
			}
		}
		if(player.hasEffect(MobEffects.JUMP) && jumpBoostButton!=null){
			jumpBoostButton.setOff();
		}else{
			if(jumpBoostButton!=null && !jumpBoostButton.isEnabled()) {
				jumpBoostButton.setOn();
			}
		}
		if(player.hasEffect(MobEffects.NIGHT_VISION) && nightVisionButton!=null){
			nightVisionButton.setOff();
		}else{
			if(nightVisionButton!=null && !nightVisionButton.isEnabled()) {
				nightVisionButton.setOn();
			}
		}
		if(player.hasEffect(MobEffects.REGENERATION) && regenerationButton!=null){
			regenerationButton.setOff();
		}else{
			if(regenerationButton!=null && !regenerationButton.isEnabled()) {
				regenerationButton.setOn();
			}
		}
		if(player.hasEffect(MobEffects.DAMAGE_RESISTANCE) && resistanceButton!=null){
			resistanceButton.setOff();
		}else{
			if(resistanceButton!=null && !resistanceButton.isEnabled()) {
				resistanceButton.setOn();
			}
		}
		if(player.hasEffect(MobEffects.MOVEMENT_SPEED) && speedButton!=null){
			speedButton.setOff();
		}else{
			if(speedButton!=null && !speedButton.isEnabled()) {
				speedButton.setOn();
			}
		}
		if(player.hasEffect(MobEffects.DAMAGE_BOOST) && strengthButton!=null){
			strengthButton.setOff();
		}else{
			if(strengthButton!=null && !strengthButton.isEnabled()) {
				strengthButton.setOn();
			}
		}

	}
	private void setFirstRowButtons(int x,int y){

		hasteButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, HASTE_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(0));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						MobEffect effect = MobEffects.DIG_SPEED;
						String minutes= "10:00";
						String amplifier = "VIII";
						String firstToolTip = effect.getDisplayName().getString()+ " "+amplifier;
						String secondToolTip = Component.translatable("gui.minutes",minutes).getString();
						Font font = Minecraft.getInstance().font;

						renderTooltip(matrices,Component.literal(firstToolTip),i+(font.width(secondToolTip)-font.width(firstToolTip))/2,j);
						renderTooltip(matrices,Component.literal(secondToolTip),i,j+10);
						}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
			);

		x = horizontalSpacing(x);
		jumpBoostButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, JUMP_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(1));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						MobEffect effect = MobEffects.JUMP;
						String minutes= "10:00";
						String amplifier = "II";
						String firstToolTip = effect.getDisplayName().getString()+ " "+amplifier;
						String secondToolTip = Component.translatable("gui.minutes",minutes).getString();
						Font font = Minecraft.getInstance().font;

						renderTooltip(matrices,Component.literal(firstToolTip),i+(font.width(secondToolTip)-font.width(firstToolTip))/2,j);
						renderTooltip(matrices,Component.literal(secondToolTip),i,j+10);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);

		x = horizontalSpacing(x);
		speedButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, SPEED_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(2));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						MobEffect effect = MobEffects.MOVEMENT_SPEED;
						String minutes= "20:00";
						String amplifier = "I";
						String firstToolTip = effect.getDisplayName().getString()+ " "+amplifier;
						String secondToolTip = Component.translatable("gui.minutes",minutes).getString();
						Font font = Minecraft.getInstance().font;

						renderTooltip(matrices,Component.literal(firstToolTip),i+(font.width(secondToolTip)-font.width(firstToolTip))/2,j);
						renderTooltip(matrices,Component.literal(secondToolTip),i,j+10);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);
		x = horizontalSpacing(x);
		strengthButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, STRENGTH_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(3));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						MobEffect effect = MobEffects.DAMAGE_BOOST;
						String minutes= "5:00";
						String amplifier = "IV";
						String firstToolTip = effect.getDisplayName().getString()+ " "+amplifier;
						String secondToolTip = Component.translatable("gui.minutes",minutes).getString();
						Font font = Minecraft.getInstance().font;

						renderTooltip(matrices,Component.literal(firstToolTip),i+(font.width(secondToolTip)-font.width(firstToolTip))/2,j);
						renderTooltip(matrices,Component.literal(secondToolTip),i,j+10);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);
		x = horizontalSpacing(x);
		regenerationButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, REGENERATION_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(4));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						MobEffect effect = MobEffects.REGENERATION;
						String minutes= "0:30";
						String amplifier = "X";
						String firstToolTip = effect.getDisplayName().getString()+ " "+amplifier;
						String secondToolTip = Component.translatable("gui.minutes",minutes).getString();
						Font font = Minecraft.getInstance().font;

						renderTooltip(matrices,Component.literal(firstToolTip),i+(font.width(secondToolTip)-font.width(firstToolTip))/2,j);
						renderTooltip(matrices,Component.literal(secondToolTip),i,j+10);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);

		guistate.put("button:haste_button", hasteButton);
		guistate.put("button:jump_boost_button", jumpBoostButton);
		guistate.put("button:speed_button", speedButton);
		guistate.put("button:strength_button", strengthButton);
		guistate.put("button:regeneration_button", regenerationButton);
		this.addRenderableWidget(hasteButton);
		this.addRenderableWidget(jumpBoostButton);
		this.addRenderableWidget(speedButton);
		this.addRenderableWidget(strengthButton);
		this.addRenderableWidget(regenerationButton);

	}
	private void setSecondRowButtons(int x,int y){

		absortionButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, ABSORTION_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(5));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						MobEffect effect = MobEffects.ABSORPTION;
						String minutes= "10:00";
						String amplifier = "X";
						String firstToolTip = effect.getDisplayName().getString()+ " "+amplifier;
						String secondToolTip = Component.translatable("gui.minutes",minutes).getString();
						Font font = Minecraft.getInstance().font;

						renderTooltip(matrices,Component.literal(firstToolTip),i+(font.width(secondToolTip)-font.width(firstToolTip))/2,j);
						renderTooltip(matrices,Component.literal(secondToolTip),i,j+10);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);

		x = horizontalSpacing(x);
		fireResistanceButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, FIRE_RESISTANCE_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(6));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						MobEffect effect = MobEffects.FIRE_RESISTANCE;
						String minutes= "30:00";
						String amplifier = "I";
						String firstToolTip = effect.getDisplayName().getString()+ " "+amplifier;
						String secondToolTip = Component.translatable("gui.minutes",minutes).getString();
						Font font = Minecraft.getInstance().font;

						renderTooltip(matrices,Component.literal(firstToolTip),i+(font.width(secondToolTip)-font.width(firstToolTip))/2,j);
						renderTooltip(matrices,Component.literal(secondToolTip),i,j+10);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);

		x = horizontalSpacing(x);
		healthBoostButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, HEALTH_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(7));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						MobEffect effect = MobEffects.HEALTH_BOOST;
						String minutes= "10:00";
						String amplifier = "X";
						String firstToolTip = effect.getDisplayName().getString()+ " "+amplifier;
						String secondToolTip = Component.translatable("gui.minutes",minutes).getString();
						Font font = Minecraft.getInstance().font;

						renderTooltip(matrices,Component.literal(firstToolTip),i+(font.width(secondToolTip)-font.width(firstToolTip))/2,j);
						renderTooltip(matrices,Component.literal(secondToolTip),i,j+10);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);
		x = horizontalSpacing(x);
		nightVisionButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, NIGHT_VISION_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(8));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						MobEffect effect = MobEffects.NIGHT_VISION;
						String minutes= "30:00";
						String amplifier = "I";
						String firstToolTip = effect.getDisplayName().getString()+ " "+amplifier;
						String secondToolTip = Component.translatable("gui.minutes",minutes).getString();
						Font font = Minecraft.getInstance().font;

						renderTooltip(matrices,Component.literal(firstToolTip),i+(font.width(secondToolTip)-font.width(firstToolTip))/2,j);
						renderTooltip(matrices,Component.literal(secondToolTip),i,j+10);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);
		x = horizontalSpacing(x);
		resistanceButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, RESISTANCE_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(9));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						MobEffect effect = MobEffects.DAMAGE_RESISTANCE;
						String minutes= "5:00";
						String amplifier = "IV";
						String firstToolTip = effect.getDisplayName().getString()+ " "+amplifier;
						String secondToolTip = Component.translatable("gui.minutes",minutes).getString();
						Font font = Minecraft.getInstance().font;

						renderTooltip(matrices,Component.literal(firstToolTip),i+(font.width(secondToolTip)-font.width(firstToolTip))/2,j);
						renderTooltip(matrices,Component.literal(secondToolTip),i,j+10);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);

		guistate.put("button:absortion_button", absortionButton);
		guistate.put("button:fire_resistance_button", fireResistanceButton);
		guistate.put("button:health_boost_button", healthBoostButton);
		guistate.put("button:night_vision_button", nightVisionButton);
		guistate.put("button:resistance_button", resistanceButton);
		this.addRenderableWidget(absortionButton);
		this.addRenderableWidget(fireResistanceButton);
		this.addRenderableWidget(healthBoostButton);
		this.addRenderableWidget(nightVisionButton);
		this.addRenderableWidget(resistanceButton);

	}
	private int horizontalSpacing(int x){
		return x+(this.imageWidth/11)*2;
	}



}
