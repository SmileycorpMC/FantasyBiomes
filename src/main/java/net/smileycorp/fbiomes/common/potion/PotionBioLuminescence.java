package net.smileycorp.fbiomes.common.potion;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.EnumFBiomesParticle;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;
import net.smileycorp.fbiomes.common.network.FBiomesParticleMessage;
import net.smileycorp.fbiomes.common.network.PacketHandler;

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
        PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new FBiomesParticleMessage(EnumFBiomesParticle.PIXEL,
                        entity.posX + (rand.nextDouble() - 0.5) * (double)entity.width, entity.posY + rand.nextDouble() * (double)entity.height,
                        entity.posZ + (rand.nextDouble() - 0.5) * (double)entity.width,
                        (double) EnumGlowshroomVariant.get(amplifier).getColour(), 50d, 0d, -0.05, 0d),
                new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 32));
    }
    
    @Override
    protected ResourceLocation getTexture(PotionEffect effect) {
        return Constants.loc("textures/mob_effect/" + EnumGlowshroomVariant.get(effect.getAmplifier()) + ".png");
    }
    
}
