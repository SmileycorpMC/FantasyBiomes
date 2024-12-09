package net.smileycorp.fbiomes.client;

import net.minecraft.client.Minecraft;
import net.smileycorp.fbiomes.client.particle.ParticlePixel;
import net.smileycorp.fbiomes.common.EnumFBiomesParticle;

public class ClientHandler {
    
    public static void spawnParticle(EnumFBiomesParticle type, double x, double y, double z, Double... data) {
        Minecraft mc = Minecraft.getMinecraft();
        switch (type) {
            case PIXEL:
                mc.effectRenderer.addEffect(new ParticlePixel(mc.world, x, y, z, (int)(double)data[0], (int)(double)data[1], data[2], data[3], data[4]));
                break;
        }
    }
    
}
