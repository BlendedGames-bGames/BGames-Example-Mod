package net.gsimken.bgamesmod.client.utils;

import java.util.function.Consumer;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class BGamesToolTip implements ButtonWidget.TooltipSupplier {
    @Override
    public void onTooltip(ButtonWidget p_93753_, MatrixStack p_93754_, int p_93755_, int p_93756_) {

    }

    @Override
    public void supply(Consumer<Text> p_168842_) {
        ButtonWidget.TooltipSupplier.super.supply(p_168842_);
    }
}
