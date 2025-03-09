package net.smileycorp.fbiomes.client.particle;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleTwinkle extends ParticlePixel{
    
    public ParticleTwinkle(World world, double x, double y, double z, int colour, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, colour, 20, motionX, motionY, motionZ);
        particleAngle = rand.nextFloat();
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        particleScale = 0.5f + 0.1f * MathHelper.sin(particleAge * 0.2f);
    }
    
}
