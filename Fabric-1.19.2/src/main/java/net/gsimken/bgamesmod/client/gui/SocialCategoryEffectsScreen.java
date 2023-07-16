package net.gsimken.bgamesmod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.gsimken.bgameslibrary.BgamesLibrary;
import net.gsimken.bgameslibrary.bgames.BGamesLibraryTools;
import net.gsimken.bgameslibrary.bgames.BGamesPlayerData;
import net.gsimken.bgameslibrary.utils.IBGamesDataSaver;
import net.gsimken.bgamesmod.client.menus.ChooseCategoriesMenu;
import net.gsimken.bgamesmod.client.menus.SocialCategoryMenu;
import net.gsimken.bgamesmod.client.utils.BGamesButton;
import net.gsimken.bgamesmod.client.utils.ScreenHelper;
import net.gsimken.bgamesmod.effects.ModEffects;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class SocialCategoryEffectsScreen extends HandledScreen<SocialCategoryMenu> {
	private final static HashMap<String, Object> guistate = ChooseCategoriesMenu.guistate;
	private final PlayerEntity player;
	BGamesButton heroButton;
	BGamesButton areaRegenerationButton;
	BGamesButton areaStrengthButton;
	ScreenHelper screenHelper;

	private static final Identifier BACKGROUND_TEXTURE = new Identifier("bgamesmod:textures/screens/backgrounds/generic_background.png");
	private static final Identifier BACK_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/backgrounds/back_20x18.png");
	private static final Identifier HERO_VILLAGE_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/social_category/hero_village.png");
	private static final Identifier AREA_REGENERATION_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/social_category/area_regeneration.png");
	private static final Identifier AREA_STRENGTH_EFFECT_BUTTON_TEXTURE = new Identifier("bgamesmod:textures/screens/social_category/area_strength.png");

	private static final int BUTTONS_WIDTH = 29; //width of button texture
	private static final int BUTTONS_HEIGHT = 26; //size of button texture unhovered
	private static final int BUTTONS_OFFSET = 2; //separation with hovered texture

	private static final int BUTTONS_TOTAL_HEIGHT = 2*BUTTONS_HEIGHT+BUTTONS_OFFSET; //image total height



	public SocialCategoryEffectsScreen(SocialCategoryMenu container, PlayerInventory inventory, Text text) {
		super(container, inventory, text);

		this.player = container.player;
		this.backgroundWidth = 450;
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
		int x;
		Text consumePoints= Text.translatable("gui.bgamesmod.choose_category.label_consume_points");
		Text socialLabel=Text.translatable("gui.bgamesmod.choose_category.label_social");
		Text points  = ScreenHelper.getPoints(BGamesLibraryTools.getPoints(BgamesLibrary.bgames_social_name,player));




		this.textRenderer.draw(poseStack,socialLabel , this.screenHelper.tittleOffset(socialLabel), 5, -12829636);
		this.textRenderer.draw(poseStack, consumePoints, this.screenHelper.tittleOffset(consumePoints), 15, -12829636);
		this.textRenderer.draw(poseStack, points, this.screenHelper.pointsTextOffset(points), 5, -12829636);
		int y = (BUTTONS_HEIGHT*5/2) + 1;
		Text effectValue = ScreenHelper.getPoints(-1);
		Text[] effectNamesFirstRow= {StatusEffects.HERO_OF_THE_VILLAGE.getName(),ModEffects.AREA_REGENERATION.getName(),ModEffects.AREA_STRENGTH.getName()};
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
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(0);
					ClientPlayNetworking.send(ModMessages.BUTTON_OPEN_SCREEN,  buf);
				}
		));
		int y = this.y + BUTTONS_HEIGHT*3/2;
		this.screenHelper = new ScreenHelper(this.backgroundWidth,3,this.x);
		setFirstRowButtons(y);
		y+= BUTTONS_HEIGHT*5/2;


	}
	private void effectCheck(){

		if(player.hasStatusEffect(ModEffects.AREA_REGENERATION) && areaRegenerationButton!=null){
			areaRegenerationButton.setOff();
		}else{
			if(areaRegenerationButton!=null && !areaRegenerationButton.isEnabled()) {
				areaRegenerationButton.setOn();
			}
		}
		if(player.hasStatusEffect(ModEffects.AREA_STRENGTH) && areaStrengthButton!=null){
			areaStrengthButton.setOff();
		}else{
			if(areaStrengthButton!=null && !areaStrengthButton.isEnabled()) {
				areaStrengthButton.setOn();
			}
		}


	}
	private void setFirstRowButtons(int y){
		int x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,0);
		heroButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, HERO_VILLAGE_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {

					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(0);
					buf.writeInt(0);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						List<Text> tooltip = new ArrayList<>();
						String minutes="30:00";
						String amplifier= "V";
						tooltip.add(Text.literal(StatusEffects.HERO_OF_THE_VILLAGE.getName().getString()+" "+amplifier) );
						tooltip.add( Text.translatable("gui.minutes",minutes) );
						renderTooltip(matrices,tooltip,i,j);
						}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
			);

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,1);


		areaRegenerationButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, AREA_REGENERATION_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(0);
					buf.writeInt(1);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);

				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						List<Text> tooltip = new ArrayList<>();
						String minutes="20:00";
						String amplifier= "X";
						tooltip.add(Text.literal(ModEffects.AREA_REGENERATION.getName().getString()+" "+amplifier) );
						tooltip.add( Text.translatable("gui.minutes",minutes) );
						tooltip.add(Text.translatable("gui.bgamesmod.social.area_regeneration_description") );
						renderTooltip(matrices,tooltip,i,j);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);

		x = this.screenHelper.elementOffset(BUTTONS_WIDTH,0,2);
		areaStrengthButton = new BGamesButton(x, y, BUTTONS_WIDTH, BUTTONS_HEIGHT, 0, 0, BUTTONS_HEIGHT+BUTTONS_OFFSET, AREA_STRENGTH_EFFECT_BUTTON_TEXTURE,BUTTONS_WIDTH,BUTTONS_TOTAL_HEIGHT,
				e -> {
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeInt(0);
					buf.writeInt(2);
					ClientPlayNetworking.send(ModMessages.BUTTON_BGAMES_INTERACT,  buf);
				},
				new ButtonWidget.TooltipSupplier(){
					@Override
					public void onTooltip(ButtonWidget buttonWidget, MatrixStack matrices, int i, int j) {
						List<Text> tooltip = new ArrayList<>();
						String minutes="20:00";
						String amplifier= "X";
						tooltip.add(Text.literal(ModEffects.AREA_STRENGTH.getName().getString()+" "+amplifier) );
						tooltip.add( Text.translatable("gui.minutes",minutes) );
						tooltip.add(Text.translatable("gui.bgamesmod.social.area_strength_description") );
						renderTooltip(matrices,tooltip,i,j);
					}
					@Override
					public void supply(Consumer<Text> consumer) {ButtonWidget.TooltipSupplier.super.supply(consumer);}
				}
		);


		guistate.put("button:hero_button", heroButton);
		guistate.put("button:area_regeneration_button", areaRegenerationButton);
		guistate.put("button:area_strength_button", areaStrengthButton);
		this.addDrawableChild(heroButton);
		this.addDrawableChild(areaRegenerationButton);
		this.addDrawableChild(areaStrengthButton);


	}



}
