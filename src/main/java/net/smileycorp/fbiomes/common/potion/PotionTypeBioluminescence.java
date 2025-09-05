package net.smileycorp.fbiomes.common.potion;

import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;

public class PotionTypeBioluminescence extends PotionType {

    private final EnumGlowshroomVariant variant;

    private PotionTypeBioluminescence(EnumGlowshroomVariant variant, String name, PotionEffect effect) {
        super(name, effect);
        this.variant = variant;
        setRegistryName(name);
    }

    public PotionTypeBioluminescence(EnumGlowshroomVariant variant, boolean extended) {
        this(variant, (extended ? "longer_" : "") + "bioluminescence_" + variant.getName(),
                new PotionEffect(FBiomesPotions.BIOLUMINESCENCE, extended ? 9600 : 3600, variant.ordinal()));
    }

}
