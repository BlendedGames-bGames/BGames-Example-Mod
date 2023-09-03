package net.gsimken.bgamesmod.mixin;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.gsimken.bgameslibrary.networking.BGamesLibraryModMessages;
import net.gsimken.bgamesmod.networking.ModMessages;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(InventoryScreen.class)
public class MixinInventoryScreen extends Screen {
    protected MixinInventoryScreen(Text text_1) {
        super(text_1);
    }


    private static final Identifier BGAMES_BUTTON_MOD_TEXTURE=new Identifier("bgamesmod:textures/screens/b_games_mod_logo.png");


    @Inject(method = "init", at = @At("TAIL"))
    private void bGamesButtonInit(CallbackInfo callbackinfo) {

        if (!this.client.interactionManager.hasCreativeInventory()) {

            int posX = this.width / 2;
            int posY = this.height / 2;
            int buttonPosX=  posX + 44; //recipeBook.updateScreenPosition(posX + 64,20);
            int buttonPosY =posY + -101;
            TexturedButtonWidget bGamesLogo= new TexturedButtonWidget(buttonPosX,buttonPosY , 20, 18, 0, 0, 19,  BGAMES_BUTTON_MOD_TEXTURE,20,37,

                    e -> {
                        PacketByteBuf buf = PacketByteBufs.create();
                        buf.writeInt(0);
                        ClientPlayNetworking.send(ModMessages.BUTTON_OPEN_SCREEN,  buf);
                    });

            this.addDrawableChild(bGamesLogo);
        }    }
}

