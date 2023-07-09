package net.gsimken.bgamesmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.gsimken.bgameslibrary.BgamesLibrary;
import net.gsimken.bgameslibrary.bgames.BGamesLibraryTools;
import net.gsimken.bgameslibrary.bgames.ClientBGamesPlayerData;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.client.menus.CognitiveCategoryMenu;
import net.gsimken.bgamesmod.client.utils.BGamesButton;
import net.gsimken.bgamesmod.client.utils.ScreenHelper;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.gsimken.bgamesmod.networking.packet.ButtonOpenScreenC2SPacket;
import net.gsimken.bgamesmod.networking.packet.ButtonsBGamesInteractC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

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
	ScreenHelper screenHelper;

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/backgrounds/generic_background.png");
	private static final ResourceLocation BACK_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/backgrounds/back_20x18.png");
	private static final ResourceLocation EXPERIENCE_POINT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/cognitive_category/experience_bottle.png");
	private static final ResourceLocation REACH_BOOST_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/cognitive_category/reach_boost.png");
	private static final ResourceLocation PICKUP_BOOST_EFFECT_BUTTON_TEXTURE = new ResourceLocation("bgamesmod:textures/screens/cognitive_category/pickup_boost.png");

	private static final int BUTTONS_WIDTH = 29; //width of button texture
	private static final int BUTTONS_HEIGHT = 26; //size of button texture unhovered
	private static final int BUTTONS_OFFSET = 2; //separation with hovered texture

	private static final int BUTTONS_TOTAL_HEIGHT = 2*BUTTONS_HEIGHT+BUTTONS_OFFSET; //image total height



	public CognitiveCategoryEffectsScreen(CognitiveCategoryMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);

		this.player = container.player;
		this.imageWidth = 400;
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

		if (!experienceButton.isReady()) {
			experienceButton.decreaseCooldown();
		}
		effectCheck();
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		int x;
		Component consumePoints= Component.translatable("gui.bgamesmod.choose_category.label_consume_points");
		Component cognitiveLabel=Component.translatable("gui.bgamesmod.choose_category.label_cognitive");
		Component points  = ScreenHelper.getPoints(BGamesLibraryTools.getPoints(BgamesLibrary.bgames_cognitive_name,player));



		this.font.draw(poseStack,cognitiveLabel , this.screenHelper.tittleOffset(cognitiveLabel), 5, -12829636);
		this.font.draw(poseStack, consumePoints, this.screenHelper.tittleOffset(consumePoints), 15, -12829636);
		this.font.draw(poseStack, points, this.screenHelper.pointsTextOffset(points), 5, -12829636);
		int y = (BUTTONS_HEIGHT*5/2) + 1;
		Component effectValue = ScreenHelper.getPoints(-1);
		Component[] effectNamesFirstRow= {Component.translatable("gui.bgamesmod.cognitive.experience_name"),ModEffects.REACH_BOOST.get().getDisplayName(),ModEffects.PICKUP_BOOST.get().getDisplayName()};
		for(int i=0; i<3;i++){
			 x = this.screenHelper.labelOffSet(effectNamesFirstRow[i],0,i);
			this.font.draw(poseStack,effectNamesFirstRow[i],x, y, -12829636);
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
		int y = this.topPos + BUTTONS_HEIGHT*3/2;
		this.screenHelper = new ScreenHelper(this.imageWidth,3,this.leftPos);
		setFirstRowButtons(y);
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
	private void setFirstRowButtons(int y){
		int x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,0);
		experienceButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, EXPERIENCE_POINT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					experienceButton.setCooldown();
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(3,0));
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

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,1);


		reachButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, REACH_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(3,1));
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

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,2);
		pickupButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, PICKUP_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractC2SPacket(3,2));
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



}
