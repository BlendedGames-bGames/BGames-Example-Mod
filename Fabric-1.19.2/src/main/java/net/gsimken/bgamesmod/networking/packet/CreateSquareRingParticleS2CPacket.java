package net.gsimken.bgamesmod.networking.packet;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.gsimken.bgameslibrary.utils.IBGamesDataSaver;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;

public class CreateSquareRingParticleS2CPacket {
    public static void receive(MinecraftClient client,
                               ClientPlayNetworkHandler handler,
                               PacketByteBuf buf,
                               PacketSender responseSender) {

        //clientside
        World level = client.world;
        if(level!=null) {
            int particleId = buf.readInt();
            double x = buf.readDouble();
            double y = buf.readDouble();
            double z = buf.readDouble();
            double radius = buf.readDouble();
            int particlesCount = buf.readInt();

            DefaultParticleType particle = ParticleTypes.HAPPY_VILLAGER;
            ;
            switch (particleId) {
                case 0:
                    particle = ParticleTypes.HEART;
                    break;
                case 1:
                    particle = ParticleTypes.FLAME;
                    break;
            }


            double sideLength = radius / particlesCount;
            // Calcular la posición inicial del cuadrado
            double startX = x - radius / 2.0;
            double startZ = z - radius / 2.0;

            // Crear el cuadrado de partículas
            for (int i = 0; i < particlesCount; i++) {
                double posX = startX + i * sideLength;
                double posZ = startZ;
                // Generar partícula en el borde inferior del cuadrado
                level.addParticle(particle, posX, y, posZ, 0, 0, 0);
                posX = startX + radius;
                posZ = startZ + i * sideLength;
                // Generar partícula en el borde derecho del cuadrado
                level.addParticle(particle, posX, y, posZ, 0, 0, 0);
                posX = startX + (particlesCount - 1 - i) * sideLength;
                posZ = startZ + radius;
                // Generar partícula en el borde superior del cuadrado
                level.addParticle(particle, posX, y, posZ, 0, 0, 0);
                posX = startX;
                posZ = startZ + (particlesCount - 1 - i) * sideLength;
                // Generar partícula en el borde izquierdo del cuadrado
                level.addParticle(particle, posX, y, posZ, 0, 0, 0);
            }
        }
    }
}
