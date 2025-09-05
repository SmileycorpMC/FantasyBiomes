package net.smileycorp.fbiomes.mixin;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.smileycorp.fbiomes.client.BioluminescenceTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class MixinWorld {

    @Shadow @Final public boolean isRemote;

    @Inject(at = @At("TAIL"), method = "getLightFor", cancellable = true)
    public void fbiomes$getLightFor(EnumSkyBlock type, BlockPos pos, CallbackInfoReturnable<Integer> callback) {
        if (!isRemote) return;
        if (type != EnumSkyBlock.BLOCK) return;
        if (callback.getReturnValue() >= 7) return;
        if (BioluminescenceTracker.instance.isLightSource(pos)) callback.setReturnValue(7);
    }

}
