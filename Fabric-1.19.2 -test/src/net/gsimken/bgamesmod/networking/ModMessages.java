package net.gsimken.bgamesmod.networking;

import net.gsimken.bgameslibrary.networking.packet.BGamesPlayerDataSyncS2CPacket;
import net.gsimken.bgamesmod.BgamesMod;

import net.gsimken.bgamesmod.networking.packet.ButtonOpenScreenC2SPacket;
import net.gsimken.bgamesmod.networking.packet.ButtonsBGamesInteractPacket;
import net.gsimken.bgamesmod.networking.packet.CreateSquareRingParticleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new Identifier(BgamesMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(ButtonOpenScreenC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ButtonOpenScreenC2SPacket::new)
                .encoder(ButtonOpenScreenC2SPacket::toBytes)
                .consumerMainThread(ButtonOpenScreenC2SPacket::handle)
                .add();
        net.messageBuilder(ButtonsBGamesInteractPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(ButtonsBGamesInteractPacket::new)
                .encoder(ButtonsBGamesInteractPacket::toBytes)
                .consumerMainThread(ButtonsBGamesInteractPacket::handle)
                .add();
        net.messageBuilder(CreateSquareRingParticleS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CreateSquareRingParticleS2CPacket::new)
                .encoder(CreateSquareRingParticleS2CPacket::toBytes)
                .consumerMainThread(CreateSquareRingParticleS2CPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayerEntity player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }
}