package net.gsimken.bgamesmod.client.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public class BGamesToolTip implements Button.OnTooltip {
    @Override
    public void onTooltip(Button p_93753_, PoseStack p_93754_, int p_93755_, int p_93756_) {

    }

    @Override
    public void narrateTooltip(Consumer<Component> p_168842_) {
        Button.OnTooltip.super.narrateTooltip(p_168842_);
    }
}
