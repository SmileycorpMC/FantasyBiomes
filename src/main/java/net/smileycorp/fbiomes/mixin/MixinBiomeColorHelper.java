package net.smileycorp.fbiomes.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.smileycorp.fbiomes.client.ClientProxy;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BiomeColorHelper.class)
public class MixinBiomeColorHelper {
    
    @Inject(at = @At("HEAD"), method = "getFoliageColorAtPos")
    private static void fbiomes$getFoliageColorAtPos$HEAD(IBlockAccess blockAccess, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        ClientProxy.stateCache = blockAccess.getBlockState(pos);
    }
    
    @Inject(at = @At("HEAD"), method = "getFoliageColorAtPos")
    private static void fbiomes$getFoliageColorAtPos$RETURN(IBlockAccess blockAccess, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
        ClientProxy.stateCache = null;
    }
    
}
