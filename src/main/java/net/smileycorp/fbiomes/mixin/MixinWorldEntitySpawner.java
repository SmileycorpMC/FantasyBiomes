package net.smileycorp.fbiomes.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.smileycorp.fbiomes.common.entities.ICreatureType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.Iterator;

@Mixin(WorldEntitySpawner.class)
public class MixinWorldEntitySpawner {
    
    @Inject(method = "findChunksForSpawning", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/event/ForgeEventFactory;canEntitySpawn(Lnet/minecraft/entity/EntityLiving;Lnet/minecraft/world/World;FFFZ)Lnet/minecraftforge/fml/common/eventhandler/Event$Result;", remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
    public void fbiomes$findChunksForSpawning(WorldServer world, boolean hostiles, boolean peaceful, boolean spawnOnSetTick, CallbackInfoReturnable<Integer> callback,
                                              int i, int j4, BlockPos blockpos1, EnumCreatureType[] var8, int var9, int var10, EnumCreatureType type, int k4,
                                              int l4, ArrayList shuffled, BlockPos.MutableBlockPos blockpos$mutableblockpos, Iterator var16, ChunkPos chunkpos1,
                                              BlockPos blockpos, int k1, int l1, int i2, IBlockState iblockstate, int j2, int k2, int l2, int i3, int j3, int k3,
                                              Biome.SpawnListEntry entry, IEntityLivingData data, int l3, int i4, float f, float f1, EntityLiving entity) {
        ((ICreatureType)entity).setCreatureType(type);
    }
    
}
