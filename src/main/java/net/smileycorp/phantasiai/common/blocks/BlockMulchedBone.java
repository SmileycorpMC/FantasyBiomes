package net.smileycorp.phantasiai.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.PhantasiaiLootTables;
import net.smileycorp.phantasiai.common.Phantasiai;

import javax.annotation.Nullable;

public class BlockMulchedBone extends BlockBase {

    public BlockMulchedBone() {
        super("mulched_bone", Constants.MODID, Material.CLAY, SoundType.GROUND, 1f, 1,  "shovel", 0, Phantasiai.TAB);
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess blockAccess, BlockPos pos, IBlockState state, int fortune) {
        if (!(blockAccess instanceof WorldServer)) return;
        WorldServer world = (WorldServer) blockAccess;
        LootTable table = world.getLootTableManager().getLootTableFromLocation(PhantasiaiLootTables.MULCHED_BONE);
        if (table == null) return;
        LootContext.Builder builder = new LootContext.Builder(world);
        EntityPlayer player = harvesters.get();
        builder.withPlayer(player);
        builder.withLuck(player.getLuck() + fortune);
        drops.add(table.generateLootForPools(world.rand, builder == null ? null : builder.build()).get(0));
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
        return BlockMud.AABB;
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        entity.motionX *= 0.8d;
        entity.motionZ *= 0.8d;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

}
