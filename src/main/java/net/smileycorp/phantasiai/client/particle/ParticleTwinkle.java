package net.smileycorp.phantasiai.client.particle;

import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticleTwinkle extends ParticleFullbrightPixel {
    
    public ParticleTwinkle(World world, double x, double y, double z, int colour, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, colour, 30, motionX, motionY, motionZ, 0.25);
        particleAngle = rand.nextFloat();
        prevParticleAngle = particleAngle;
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        particleScale = 0.125f + 0.03f * MathHelper.cos(particleAge * 0.1f);
    }
    
}
