package net.gsimken.bgamesmod.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.gsimken.bgameslibrary.BgamesLibrary;
import net.gsimken.bgamesmod.BgamesMod;
import net.gsimken.bgamesmod.networking.packet.ButtonOpenScreenC2SPacket;
import net.gsimken.bgamesmod.networking.packet.ButtonsBGamesInteractC2SPacket;
import net.gsimken.bgamesmod.networking.packet.CreateSquareRingParticleS2CPacket;
import net.minecraft.util.Identifier;

public class ModMessages {
    public static final Identifier BUTTON_BGAMES_INTERACT =new Identifier(BgamesMod.MOD_ID,"button_bgames_interact");
    public static final Identifier BUTTON_OPEN_SCREEN =new Identifier(BgamesMod.MOD_ID,"button_open_screen");
    public static final Identifier CREATE_AREA_PARTICLES =new Identifier(BgamesMod.MOD_ID,"area_particles");

    public static void registerC2SPackets(){
        ServerPlayNetworking.registerGlobalReceiver(BUTTON_BGAMES_INTERACT, ButtonsBGamesInteractC2SPacket ::receive);
        ServerPlayNetworking.registerGlobalReceiver(BUTTON_OPEN_SCREEN, ButtonOpenScreenC2SPacket::receive);


    }
    public static void registerS2CPackets(){
        ClientPlayNetworking.registerGlobalReceiver(CREATE_AREA_PARTICLES, CreateSquareRingParticleS2CPacket::receive);

    }

}
