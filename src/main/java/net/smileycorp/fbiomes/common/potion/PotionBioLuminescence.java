package net.smileycorp.fbiomes.common.potion;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;

public class PotionBioLuminescence extends PotionFBiomes {
    
    public PotionBioLuminescence() {
        super(true, 0x47E1D3, "bioluminescence");
    }
    
    @Override
    protected ResourceLocation getTexture(PotionEffect effect) {
        return Constants.loc("textures/mob_effect/" + EnumGlowshroomVariant.get(effect.getAmplifier()) + ".png");
    }
}
