package net.smileycorp.phantasiai.client;

import net.minecraft.client.Minecraft;
import net.smileycorp.phantasiai.client.particle.ParticleFullbrightPixel;
import net.smileycorp.phantasiai.client.particle.ParticlePixel;
import net.smileycorp.phantasiai.client.particle.ParticleTwinkle;
import net.smileycorp.phantasiai.common.EnumParticle;

public class ClientHandler {
    
    public static void spawnParticle(EnumParticle type, double x, double y, double z, Double... data) {
        Minecraft mc = Minecraft.getMinecraft();
        switch (type) {
            case PIXEL:
                mc.effectRenderer.addEffect(new ParticlePixel(mc.world, x, y, z, (int)(double)data[0], (int)(double)data[1], data[2], data[3], data[4], data[5]));
                break;
            case PIXEL_FULLBRIGHT:
                mc.effectRenderer.addEffect(new ParticleFullbrightPixel(mc.world, x, y, z, (int)(double)data[0], (int)(double)data[1], data[2], data[3], data[4], data[5]));
                break;
            case TWINKLE:
                mc.effectRenderer.addEffect(new ParticleTwinkle(mc.world, x, y, z, (int)(double)data[0], data[1], data[2], data[3]));
                break;
        }
    }
    
}
