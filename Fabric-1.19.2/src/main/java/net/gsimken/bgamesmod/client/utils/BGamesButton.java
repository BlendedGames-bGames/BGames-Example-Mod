package net.gsimken.bgamesmod.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BGamesButton extends ButtonWidget {
    private final Identifier texture;
    private final int u;
    private final int v;
    private final int hoveredVOffset;
    private final int textureWidth;
    private final int textureHeight;
    private boolean enabled;
    private int cooldown; // ticks
    public BGamesButton(int x, int y, int width, int height, int u, int v, Identifier texture, ButtonWidget.PressAction pressAction) {
        this(x, y, width, height, u, v, height, texture, 256, 256, pressAction);
    }

    public BGamesButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, ButtonWidget.PressAction pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, 256, 256, pressAction);
    }

    public BGamesButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction,  ScreenTexts.EMPTY);
    }

    public BGamesButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction,ButtonWidget.TooltipSupplier tooltipSupplier) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction, tooltipSupplier ,  ScreenTexts.EMPTY);
    }
    public BGamesButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction, Text text) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction,  EMPTY , text);
    }
    public BGamesButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, int textureWidth, int textureHeight, ButtonWidget.PressAction pressAction, ButtonWidget.TooltipSupplier tooltipSupplier, Text text) {
        super(x, y, width, height, text, pressAction, tooltipSupplier);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.u = u;
        this.v = v;
        this.hoveredVOffset = hoveredVOffset;
        this.texture = texture;
        this.enabled = true;
        this.cooldown = 0;
    }
    public void setOff(){
        this.enabled=false;
    }
    public void setOn(){
        if(this.cooldown==0) {
            this.enabled = true;
        }
    }
    public boolean isEnabled(){
        return this.enabled;
    }

    public int getCooldown() {
        return cooldown;
    }
    public boolean isReady(){
        if(this.cooldown==0){
            return true;
        }
        return false;
    }
    public void decreaseCooldown(){
        this.cooldown--;
        if(this.cooldown<=0){
            this.setOn();
            this.cooldown = Math.max(this.cooldown, 0);
        }

    }
    public void setCooldown(){
        this.setOff();
        this.cooldown = 10; //1sec

    }
    public void setCooldown(int ticks){
        this.setOff();
        this.cooldown = ticks; //1sec

    }
    @Override
    public void onPress() {
        if(this.enabled) {
            this.playDownSound(MinecraftClient.getInstance().getSoundManager());
            this.onPress.onPress(this);
        }
    }
    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.texture);
        int i = this.v;
        if (!this.isNarratable()) {
            i += this.hoveredVOffset * 2;
        } else if (this.isHovered() || !this.enabled) {
            i += this.hoveredVOffset;
        }

        RenderSystem.enableDepthTest();
        drawTexture(matrices, this.x, this.y, this.u, i, this.width, this.height, this.textureWidth, this.textureHeight);
        if (this.hovered) {
            this.renderTooltip(matrices, mouseX, mouseY);
        }

    }

}
