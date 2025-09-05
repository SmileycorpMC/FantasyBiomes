package net.smileycorp.fbiomes.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.smileycorp.fbiomes.common.entities.ICreatureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldEntitySpawner.class)
public class MixinWorldEntitySpawner {
    
    @Inject(method = "findChunksForSpawning", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;canEntitySpawn(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/world/World;FFFZ)Lnet/minecraftforge/fml/common/eventhandler/Event$Result;", remap = false))
    public void fbiomes$findChunksForSpawning(WorldServer world, boolean hostiles, boolean peaceful, boolean spawnOnSetTick, CallbackInfoReturnable<Integer> callback, @Local EnumCreatureType type, @Local EntityLiving entity) {
        ((ICreatureType)entity).setCreatureType(type);
    }
    
}
