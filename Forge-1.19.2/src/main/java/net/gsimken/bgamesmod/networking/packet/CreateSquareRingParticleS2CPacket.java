package net.gsimken.bgamesmod.networking.packet;

import ca.weblite.objc.Client;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CreateSquareRingParticleS2CPacket {
    /*
   Clase que se encarga de sincronizar la data en el servidor y en el cliente
   */
    private final int particleId;
    private final double x;
    private final double y;
    private final double z;
    private final double radius;
    private final int particlesCount;


    public CreateSquareRingParticleS2CPacket(int particleId, double x, double y, double z, double radius, int particlesCount) {
        this.particleId = particleId;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.particlesCount = particlesCount;

    }

    public CreateSquareRingParticleS2CPacket(FriendlyByteBuf buf) {
        this.particleId = buf.readInt();
        this.x = buf.readDouble();
        this.y = buf.readDouble();
        this.z = buf.readDouble();
        this.radius = buf.readDouble();
        this.particlesCount = buf.readInt();

    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(particleId);
        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);
        buf.writeDouble(radius);
        buf.writeInt(particlesCount);

    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();

        context.enqueueWork(() -> {

            Level level = Minecraft.getInstance().level;
            SimpleParticleType particle = ParticleTypes.HAPPY_VILLAGER;;
            switch (this.particleId){
                case 0:
                    particle = ParticleTypes.HEART;
                    break;
                case 1:
                    particle = ParticleTypes.FLAME;
                    break;
            }


            double sideLength = radius/particlesCount;
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
        });
        return true;
    }
}
