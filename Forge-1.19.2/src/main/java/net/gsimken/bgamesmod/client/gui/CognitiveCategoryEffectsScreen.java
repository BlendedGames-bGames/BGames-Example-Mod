package net.gsimken.bgamesmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.gsimken.bgameslibrary.bgames.ClientBGamesPlayerData;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.client.menus.CognitiveCategoryMenu;
import net.gsimken.bgamesmod.client.utils.BGamesButton;
import net.gsimken.bgamesmod.client.utils.ScreenUtils;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.gsimken.bgamesmod.effects.ReachBoostEffect;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.gsimken.bgamesmod.networking.packet.ButtonsBGamesInteractPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class CognitiveCategoryEffectsScreen extends AbstractContainerScreen<CognitiveCategoryMenu> {
	private final static HashMap<String, Object> guistate = ChooseCategoriesMenu.guistate;
	private final Player player;
	BGamesButton experienceButton;
	BGamesButton reachButton;
	BGamesButton pickupButton;


	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/backgrounds/generic_background.png");
	private static final ResourceLocation EXPERIENCE_POINT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/cognitivecategory/experience_bottle.png");
	private static final ResourceLocation REACH_BOOST_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/cognitivecategory/reach_boost.png");
	private static final ResourceLocation PICKUP_BOOST_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/cognitivecategory/pickup_boost.png");

	private static final int BUTTONS_WIDTH = 29; //width of button texture
	private static final int BUTTONS_HEIGHT = 26; //size of button texture unhovered
	private static final int BUTTONS_OFFSET = 2; //separation with hovered texture

	private static final int BUTTONS_TOTAL_HEIGHT = 2*BUTTONS_HEIGHT+BUTTONS_OFFSET; //image total height



	public CognitiveCategoryEffectsScreen(CognitiveCategoryMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);

		this.player = container.player;
		this.imageWidth = 300;
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

		if (!experienceButton.isReady()) {
			experienceButton.decreaseCooldown();
		}
		effectCheck();
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		Component consumePoints= Component.translatable("gui.bgamesmod.choose_category.label_consume_points");
		Component cognitiveLabel=Component.translatable("gui.bgamesmod.choose_category.label_cognitive");
		Component points  = ScreenUtils.getPoints(ClientBGamesPlayerData.getPlayerPhysicalPoints());


		this.font.draw(poseStack,cognitiveLabel , (this.imageWidth/2)-(this.font.width(cognitiveLabel)/2), 5, -12829636);
		this.font.draw(poseStack, consumePoints, (this.imageWidth/2)-(this.font.width(consumePoints)/2), 15, -12829636);
		this.font.draw(poseStack, points, this.imageWidth-this.font.width(points)-5, 5, -12829636);
		int x = (this.imageWidth/7);
		int y = (BUTTONS_HEIGHT*5/2) + 1;
		Component effectValue = ScreenUtils.getPoints(-1);
		Component[] effectNamesFirstRow= {Component.translatable("gui.bgamesmod.cognitive.experience_name"),ModEffects.REACH_BOOST.get().getDisplayName(),ModEffects.PICKUP_BOOST.get().getDisplayName()};
		for(int i=0; i<3;i++){
			this.font.draw(poseStack,effectNamesFirstRow[i], ScreenUtils.calculateCenteredX(x, BUTTONS_WIDTH, effectNamesFirstRow[i]) , y, -12829636);
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
		int x = this.leftPos + (this.imageWidth/7);
		int y = this.topPos + BUTTONS_HEIGHT*3/2;
		setFirstRowButtons(x,y);
		y+= BUTTONS_HEIGHT*5/2;


	}
	private void effectCheck(){
		if(player.hasEffect(ModEffects.REACH_BOOST.get()) && reachButton!=null){
			reachButton.setOff();
		}else{
			if(reachButton!=null && !reachButton.isEnabled()) {
				reachButton.setOn();
			}
		}
		if(player.hasEffect(ModEffects.PICKUP_BOOST.get()) && pickupButton!=null){
			pickupButton.setOff();
		}else{
			if(pickupButton!=null && !pickupButton.isEnabled()) {
				pickupButton.setOn();
			}
		}


	}
	private void setFirstRowButtons(int x,int y){

		experienceButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, EXPERIENCE_POINT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					experienceButton.setCooldown();
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(10));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						List<Component> tooltip = new ArrayList<>();
						tooltip.add(Component.translatable("gui.bgamesmod.cognitive.experience_button_description") );
						tooltip.add(Component.translatable("gui.bgamesmod.cognitive.experience_button_details") );
						renderComponentTooltip(matrices,tooltip,i,j);
						}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
			);

		x = horizontalSpacing(x);
		reachButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, REACH_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(11));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						List<Component> tooltip = new ArrayList<>();
						String minutes="20:00";
						String amplifier= "X";
						tooltip.add(Component.literal(ModEffects.REACH_BOOST.get().getDisplayName().getString()+" "+amplifier) );
						tooltip.add( Component.translatable("gui.minutes",minutes) );
						tooltip.add(Component.translatable("gui.bgamesmod.cognitive.reach_boost_description") );
						renderComponentTooltip(matrices,tooltip,i,j);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);

		x = horizontalSpacing(x);
		pickupButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, PICKUP_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(12));
				},
				new Button.OnTooltip(){
					@Override
					public void onTooltip(Button buttonWidget, PoseStack matrices, int i, int j) {
						List<Component> tooltip = new ArrayList<>();
						String minutes="20:00";
						String amplifier= "XX";
						tooltip.add(Component.literal(ModEffects.REACH_BOOST.get().getDisplayName().getString()+" "+amplifier) );
						tooltip.add( Component.translatable("gui.minutes",minutes) );
						tooltip.add(Component.translatable("gui.bgamesmod.cognitive.pickup_boost_description") );
						renderComponentTooltip(matrices,tooltip,i,j);
					}
					@Override
					public void narrateTooltip(Consumer<Component> consumer) {Button.OnTooltip.super.narrateTooltip(consumer);}
				}
		);


		guistate.put("button:experience_button", experienceButton);
		guistate.put("button:reach_boost_button", reachButton);
		guistate.put("button:pickup_boost_button", pickupButton);
		this.addRenderableWidget(experienceButton);
		this.addRenderableWidget(reachButton);
		this.addRenderableWidget(pickupButton);


	}
		private int horizontalSpacing(int x){
		return x+(this.imageWidth/7)*2;
	}



}
