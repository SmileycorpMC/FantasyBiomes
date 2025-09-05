package net.smileycorp.fbiomes.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;
import net.smileycorp.fbiomes.client.particle.ParticleFullbrightPixel;
import net.smileycorp.fbiomes.client.particle.ParticlePixel;
import net.smileycorp.fbiomes.client.particle.ParticleTwinkle;
import net.smileycorp.fbiomes.common.EnumParticle;

public class ClientHandler {
    
    public static void spawnParticle(EnumParticle type, double x, double y, double z, Double... data) {
        Minecraft mc = Minecraft.getMinecraft();
        switch (type) {
            case PIXEL:
                mc.effectRenderer.addEffect(new ParticlePixel(mc.world, x, y, z, (int)(double)data[0], (int)(double)data[1], data[2], data[3], data[4]));
                break;
            case PIXEL_FULLBRIGHT:
                mc.effectRenderer.addEffect(new ParticleFullbrightPixel(mc.world, x, y, z, (int)(double)data[0], (int)(double)data[1], data[2], data[3], data[4]));
                break;
            case TWINKLE:
                mc.effectRenderer.addEffect(new ParticleTwinkle(mc.world, x, y, z, (int)(double)data[0], data[1], data[2], data[3]));
                break;
        }
    }
    
    public static World getWorld() {
        return Minecraft.getMinecraft().world;
    }
    
}
