package net.smileycorp.fbiomes.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.EnumParticle;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;

import java.util.Random;

public class PotionBioLuminescence extends PotionFBiomes {
    
    public PotionBioLuminescence() {
        super(false, 0x47E1D3, "bioluminescence");
    }
    
    @Override
    public String getName() {
        return super.getName();
    }
    
    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        Random rand = entity.getRNG();
        if (entity.isInvisible()) return;
        if (rand.nextBoolean()) return;
        EnumParticle.PIXEL.send(entity.dimension, entity.posX + (rand.nextDouble() - 0.5) * (double)entity.width,
                entity.posY + rand.nextDouble() * (double)entity.height, entity.posZ + (rand.nextDouble() - 0.5) * (double)entity.width,
                (double) EnumGlowshroomVariant.get(amplifier).getColour(), 50d, 0d, -0.05, 0d);
    }
    
    @Override
    protected ResourceLocation getTexture(PotionEffect effect) {
        return Constants.loc("textures/mob_effect/" + EnumGlowshroomVariant.get(effect.getAmplifier()) + ".png");
    }
    
}
