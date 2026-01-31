package net.smileycorp.phantasiai.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.smileycorp.phantasiai.common.blocks.enums.EnumGlowshroomVariant;
import net.smileycorp.phantasiai.common.potion.PhantasiaiPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PotionUtils.class)
public class MixinPotionUtils {

    @WrapOperation(method = "getPotionColorFromEffectList", at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/Potion;getLiquidColor()I", remap = false))
    private static int phantasiai$getPotionColorFromEffectList$getLiquidColor(Potion instance, Operation<Integer> original, @Local PotionEffect effect) {
        System.out.println(effect.getPotion() + ", " + effect.getAmplifier());
        return instance != PhantasiaiPotions.BIOLUMINESCENCE ? original.call(instance) :
                0xFF000000 + EnumGlowshroomVariant.get(effect.getAmplifier()).getColour();
    }

}
