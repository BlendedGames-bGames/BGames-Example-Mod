package net.gsimken.bgamesmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.gsimken.bgameslibrary.bgames.ClientBGamesPlayerData;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.client.menus.CognitiveCategoryMenu;
import net.gsimken.bgamesmod.client.utils.BGamesButton;
import net.gsimken.bgamesmod.client.utils.ScreenHelper;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.gsimken.bgamesmod.networking.packet.ButtonOpenScreenC2SPacket;
import net.gsimken.bgamesmod.networking.packet.ButtonsBGamesInteractPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class CognitiveCategoryEffectsScreen extends HandledScreen<CognitiveCategoryMenu> {
	private final static HashMap<String, Object> guistate = ChooseCategoriesMenu.guistate;
	private final PlayerEntity player;
	BGamesButton experienceButton;
	BGamesButton reachButton;
	BGamesButton pickupButton;
	ScreenHelper screenHelper;

	private static final Identifier BACKGROUND_TEXTURE = new Identifier("bgamesmod:textures/screens/backgrounds/generic_background.png");
	private static final Identifier BACK_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/backgrounds/back_20x18.png");
	private static final Identifier EXPERIENCE_POINT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/cognitive_category/experience_bottle.png");
	private static final Identifier REACH_BOOST_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/cognitive_category/reach_boost.png");
	private static final Identifier PICKUP_BOOST_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/cognitive_category/pickup_boost.png");

	private static final int BUTTONS_WIDTH = 29; //width of button texture
	private static final int BUTTONS_HEIGHT = 26; //size of button texture unhovered
	private static final int BUTTONS_OFFSET = 2; //separation with hovered texture

	private static final int BUTTONS_TOTAL_HEIGHT = 2*BUTTONS_HEIGHT+BUTTONS_OFFSET; //image total height



	public CognitiveCategoryEffectsScreen(CognitiveCategoryMenu container, PlayerInventory inventory, Text text) {
		super(container, inventory, text);

		this.player = container.player;
		this.backgroundWidth = 400;
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

		if (!experienceButton.isReady()) {
			experienceButton.decreaseCooldown();
		}
		effectCheck();
	}

	@Override
	protected void drawForeground(MatrixStack poseStack, int mouseX, int mouseY) {
		int x;
		Text consumePoints= Text.translatable("gui.bgamesmod.choose_category.label_consume_points");
		Text cognitiveLabel=Text.translatable("gui.bgamesmod.choose_category.label_cognitive");
		Text points  = ScreenHelper.getPoints(ClientBGamesPlayerData.getPlayerCognitivePoints());


		this.textRenderer.draw(poseStack,cognitiveLabel , this.screenHelper.tittleOffset(cognitiveLabel), 5, -12829636);
		this.textRenderer.draw(poseStack, consumePoints, this.screenHelper.tittleOffset(consumePoints), 15, -12829636);
		this.textRenderer.draw(poseStack, points, this.screenHelper.pointsTextOffset(points), 5, -12829636);
		int y = (BUTTONS_HEIGHT*5/2) + 1;
		Text effectValue = ScreenHelper.getPoints(-1);
		Text[] effectNamesFirstRow= {Text.translatable("gui.bgamesmod.cognitive.experience_name"),ModEffects.REACH_BOOST.get().getDisplayName(),ModEffects.PICKUP_BOOST.get().getDisplayName()};
		for(int i=0; i<3;i++){
			 x = this.screenHelper.labelOffSet(effectNamesFirstRow[i],0,i);
			this.textRenderer.draw(poseStack,effectNamesFirstRow[i],x, y, -12829636);
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
					ModMessages.sendToServer(new ButtonOpenScreenC2SPacket(0));

				}
		));
		int y = this.y + BUTTONS_HEIGHT*3/2;
		this.screenHelper = new ScreenHelper(this.backgroundWidth,3,this.x);
		setFirstRowButtons(y);
		y+= BUTTONS_HEIGHT*5/2;


	}
	private void effectCheck(){
		if(player.hasStatusEffect(ModEffects.REACH_BOOST.get()) && reachButton!=null){
			reachButton.setOff();
		}else{
			if(reachButton!=null && !reachButton.isEnabled()) {
				reachButton.setOn();
			}
		}
		if(player.hasStatusEffect(ModEffects.PICKUP_BOOST.get()) && pickupButton!=null){
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
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(3,0));
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						List<Text> tooltip = new ArrayList<>();
						tooltip.add(Text.translatable("gui.bgamesmod.cognitive.experience_button_description") );
						tooltip.add(Text.translatable("gui.bgamesmod.cognitive.experience_button_details") );
						renderTooltip(matrices,tooltip,i,j);
						}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
			);

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,1);


		reachButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, REACH_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(3,1));
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						List<Text> tooltip = new ArrayList<>();
						String minutes="20:00";
						String amplifier= "X";
						tooltip.add(Text.literal(ModEffects.REACH_BOOST.get().getDisplayName().getString()+" "+amplifier) );
						tooltip.add( Text.translatable("gui.minutes",minutes) );
						tooltip.add(Text.translatable("gui.bgamesmod.cognitive.reach_boost_description") );
						renderTooltip(matrices,tooltip,i,j);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,2);
		pickupButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, PICKUP_BOOST_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					ModMessages.sendToServer(new ButtonsBGamesInteractPacket(3,2));
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						List<Text> tooltip = new ArrayList<>();
						String minutes="20:00";
						String amplifier= "XX";
						tooltip.add(Text.literal(ModEffects.REACH_BOOST.get().getDisplayName().getString()+" "+amplifier) );
						tooltip.add( Text.translatable("gui.minutes",minutes) );
						tooltip.add(Text.translatable("gui.bgamesmod.cognitive.pickup_boost_description") );
						renderTooltip(matrices,tooltip,i,j);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);


		guistate.put("button:experience_button", experienceButton);
		guistate.put("button:reach_boost_button", reachButton);
		guistate.put("button:pickup_boost_button", pickupButton);
		this.addDrawableChild(experienceButton);
		this.addDrawableChild(reachButton);
		this.addDrawableChild(pickupButton);


	}



}
