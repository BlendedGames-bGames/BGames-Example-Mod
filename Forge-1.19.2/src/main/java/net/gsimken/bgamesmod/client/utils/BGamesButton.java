package net.gsimken.bgamesmod.client.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class BGamesButton extends Button {
    private final ResourceLocation texture;
    private final int u;
    private final int v;
    private final int hoveredVOffset;
    private final int textureWidth;
    private final int textureHeight;
    private boolean enabled;
    private int cooldown; // ticks
    public BGamesButton(int x, int y, int width, int height, int u, int v, ResourceLocation texture, Button.OnPress pressAction) {
        this(x, y, width, height, u, v, height, texture, 256, 256, pressAction);
    }

    public BGamesButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, Button.OnPress pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, 256, 256, pressAction);
    }

    public BGamesButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, int textureWidth, int textureHeight, Button.OnPress pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction,  CommonComponents.EMPTY);
    }

    public BGamesButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, int textureWidth, int textureHeight, Button.OnPress pressAction,Button.OnTooltip tooltipSupplier) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction, tooltipSupplier ,  CommonComponents.EMPTY);
    }
    public BGamesButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, int textureWidth, int textureHeight, Button.OnPress pressAction, Component text) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction,  NO_TOOLTIP , text);
    }
    public BGamesButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, int textureWidth, int textureHeight, Button.OnPress pressAction, Button.OnTooltip tooltipSupplier, Component text) {
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
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            this.onPress.onPress(this);
        }
    }
    @Override
    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.texture);
        int i = this.v;
        if (!this.isActive()) {
            i += this.hoveredVOffset * 2;
        } else if (this.isHoveredOrFocused() || !this.enabled) {
            i += this.hoveredVOffset;
        }

        RenderSystem.enableDepthTest();
        blit(matrices, this.x, this.y, this.u, i, this.width, this.height, this.textureWidth, this.textureHeight);
        if (this.isHovered) {
            this.renderToolTip(matrices, mouseX, mouseY);
        }

    }

}
