package net.gsimken.bgamesmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.gsimken.bgameslibrary.BgamesLibrary;
import net.gsimken.bgameslibrary.bgames.BGamesLibraryTools;
import net.gsimken.bgameslibrary.bgames.ClientBGamesPlayerData;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.client.menus.PhysicalCategoryMenu;
import net.gsimken.bgamesmod.client.utils.BGamesButton;
import net.gsimken.bgamesmod.client.utils.ScreenHelper;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.gsimken.bgamesmod.networking.packet.ButtonOpenScreenC2SPacket;
import net.gsimken.bgamesmod.networking.packet.ButtonsBGamesInteractC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
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
	ScreenHelper screenHelper;

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/backgrounds/generic_background.png");
	private static final ResourceLocation BACK_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/backgrounds/back_20x18.png");

	private static final ResourceLocation HASTE_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_category/haste.png");
	private static final ResourceLocation JUMP_BOOST_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_category/jump_boost.png");
	private static final ResourceLocation SPEED_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_category/speed.png");
	private static final ResourceLocation STRENGTH_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_category/strength.png");
	private static final ResourceLocation REGENERATION_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_category/regeneration.png");
	private static final ResourceLocation ABSORTION_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_category/absortion.png");
	private static final ResourceLocation FIRE_RESISTANCE_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_category/fire_resistance.png");
	private static final ResourceLocation HEALTH_BOOST_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_category/health_boost.png");
	private static final ResourceLocation NIGHT_VISION_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_category/night_vision.png");
	private static final ResourceLocation RESISTANCE_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/physical_category/resistance.png");

	private static final int BUTTONS_WIDTH = 29; //width of button texture
	private static final int BUTTONS_HEIGHT = 26; //size of button texture unhovered
	private static final int BUTTONS_OFFSET = 2; //separation with hovered texture

	private static final int BUTTONS_TOTAL_HEIGHT = 2*BUTTONS_HEIGHT+BUTTONS_OFFSET; //image total height



	public PhysicalCategoryEffectsScreen(PhysicalCategoryMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.player = container.player;
		this.imageWidth = 500;
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
		Component points  = ScreenHelper.getPoints(BGamesLibraryTools.getPoints(BgamesLibrary.bgames_physical_name,player));


		this.font.draw(poseStack,physicalLabel , this.screenHelper.tittleOffset(physicalLabel), 5, -12829636);
		this.font.draw(poseStack, consumePoints, this.screenHelper.tittleOffset(consumePoints), 15, -12829636);
		this.font.draw(poseStack, points, this.screenHelper.pointsTextOffset(points), 5, -12829636);
		int x;
		int y = (BUTTONS_HEIGHT*5/2) + 1;
		Component effectValue = ScreenHelper.getPoints(-1);
		Component[] effectNamesFirstRow= {MobEffects.DIG_SPEED.getDisplayName(),MobEffects.JUMP.getDisplayName(),MobEffects.MOVEMENT_SPEED.getDisplayName(),MobEffects.DAMAGE_BOOST.getDisplayName(),MobEffects.REGENERATION.getDisplayName()};
		for(int i=0; i<5;i++){
			x = this.screenHelper.labelOffSet(effectNamesFirstRow[i],0,i);
			this.font.draw(poseStack,effectNamesFirstRow[i],x, y, -12829636);
			x = this.screenHelper.labelOffSet(effectValue, 0,i);
			this.font.draw(poseStack,effectValue,x, y+10, -12829636);
		}
		y+= BUTTONS_HEIGHT*5/2;
		Component[] effectNamesSecondRow= {MobEffects.ABSORPTION.getDisplayName(),MobEffects.FIRE_RESISTANCE.getDisplayName(),MobEffects.HEALTH_BOOST.getDisplayName(),MobEffects.NIGHT_VISION.getDisplayName(),MobEffects.DAMAGE_RESISTANCE.getDisplayName()};
		for(int i=0; i<5;i++){
			x = this.screenHelper.labelOffSet(effectNamesSecondRow[i],0,i);
			this.font.draw(poseStack,effectNamesSecondRow[i],x, y, -12829636);
			x = this.screenHelper.labelOffSet(effectValue, 0,i);
			this.font.draw(poseStack,effectValue,x, y+10, -12829636);
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
		this.addRenderableWidget(new ImageButton(this.leftPos + 5, this.topPos + 5, 20, 18, 0, 0, 19,BACK_BUTTON_TEXTURE,20,37,
				e ->{
						ModMessages.sendToServer(new ButtonOpenScreenC2SPacket(0));

					}
		));

		this.screenHelper = new ScreenHelper(this.imageWidth,5,this.leftPos);
		int y = this.topPos + BUTTONS_HEIGHT*3/2;
		this.screenHelper.addRow(5);
		setFirstRowButtons(y);
		y+= BUTTONS_HEIGHT*5/2;
		setSecondRowButtons(y);


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
	private void setFirstRowButtons(int y){
		int x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,0);

		hasteButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, HASTE_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(1,0));
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

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,1);

		jumpBoostButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, JUMP_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(1,1));
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

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,2);

		speedButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, SPEED_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(1,2));
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
		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,3);

		strengthButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, STRENGTH_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(1,3));
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
		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,4);

		regenerationButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, REGENERATION_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(1,4));
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
	private void setSecondRowButtons(int y){
		int x = this.screenHelper.elementOffset(BUTTONS_WIDTH,1,0);

		absortionButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, ABSORTION_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(1,5));
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

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,1,1);
		fireResistanceButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, FIRE_RESISTANCE_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(1,6));
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

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,1,2);
		healthBoostButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, HEALTH_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(1,7));
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
		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,1,3);
		nightVisionButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, NIGHT_VISION_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(1,8));
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
		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,1,4);
		resistanceButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, RESISTANCE_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(1,9));
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



}
