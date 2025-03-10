package net.smileycorp.fbiomes.client.particle;

import net.minecraft.world.World;

public class ParticleFullbrightPixel extends ParticlePixel {
    
    public ParticleFullbrightPixel(World world, double x, double y, double z, int colour, int maxAge, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, colour, maxAge, motionX, motionY, motionZ);
    }
    
    @Override
    public int getBrightnessForRender(float pt) {
        return 0x400040;
    }
    
}
