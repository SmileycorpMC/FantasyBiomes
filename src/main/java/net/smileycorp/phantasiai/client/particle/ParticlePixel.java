package net.smileycorp.phantasiai.client.particle;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.World;

public class ParticlePixel extends Particle {
    
    public static TextureAtlasSprite SPRITE;
    
    public ParticlePixel(World world, double x, double y, double z, int colour, int maxAge, double motionX, double motionY, double motionZ) {
        super(world, x, y, z, 0, 0, 0);
        setPosition(x, y, z);
        setRBGColorF((colour >> 16) / 255f, (colour >> 8 & 255) / 255f, (colour & 255) / 255f);
        particleTexture = SPRITE;
        particleTextureJitterX = 0;
        particleTextureJitterY = 0;
        particleTextureIndexX = 0;
        particleTextureIndexY = 0;
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
        particleMaxAge = maxAge;
        particleScale = 0.5f;
        setSize(0.05f, 0.05f);
    }
    
    @Override
    public void onUpdate() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (particleAge++ >= particleMaxAge) setExpired();
        posX += motionX;
        posY += motionY;
        posZ += motionZ;
    }
    
    @Override
    public int getFXLayer() {
        return 1;
    }
    
    @Override
    public void setParticleTextureIndex(int index) {}
    
}
