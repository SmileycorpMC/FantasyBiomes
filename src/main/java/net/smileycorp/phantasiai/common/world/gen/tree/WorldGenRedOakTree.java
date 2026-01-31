package net.smileycorp.phantasiai.common.world.gen.tree;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenTrees;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.phantasiai.common.blocks.BlockFBMushroom;
import net.smileycorp.phantasiai.common.blocks.BlockLichen;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;
import net.smileycorp.phantasiai.common.blocks.enums.EnumVanillaWoodType;

import java.util.Random;

public class WorldGenRedOakTree extends WorldGenTrees {
    
    private static final IBlockState LEAVES = PhantasiaiBlocks.VANILLA_LEAVES.getDefaultState()
            .withProperty(PhantasiaiBlocks.VANILLA_LEAVES.getVariantProperty(), EnumVanillaWoodType.RED_OAK)
            .withProperty(BlockLeaves.DECAYABLE, true).withProperty(BlockLeaves.CHECK_DECAY, false);
    private final boolean isNatural;
    
    public WorldGenRedOakTree(boolean notify, boolean isNatural) {
        super(notify);
        this.isNatural = isNatural;
    }
    
    @Override
    protected void setBlockAndNotifyAdequately(World world, BlockPos pos, IBlockState state) {
        if (state.getBlock() == Blocks.LEAVES) state = LEAVES;
        super.setBlockAndNotifyAdequately(world, pos, state);
        if (!isNatural || state.getBlock() != Blocks.LOG) return;
        Random rand = world.rand;
        for (EnumFacing facing : EnumFacing.HORIZONTALS) if (rand.nextBoolean() && world.isAirBlock(pos.offset(facing)))
            setBlockAndNotifyAdequately(world, pos.offset(facing), BlockLichen.getBlockState(facing.getOpposite()));
        if (rand.nextInt(5) > 1) return;
        EnumFacing facing = DirectionUtils.getRandomDirectionXZ(rand);
        BlockPos facePos = pos.offset(facing);
        IBlockState shroom = (rand.nextInt(5) < 2 ? PhantasiaiBlocks.glowshrooms[rand.nextInt(PhantasiaiBlocks.glowshrooms.length)]:
                PhantasiaiBlocks.shrooms[rand.nextInt(PhantasiaiBlocks.shrooms.length)]).getDefaultState();
        if (world.isAirBlock(facePos)) setBlockAndNotifyAdequately(world, facePos, shroom.withProperty(BlockFBMushroom.FACING, facing));
    }
    
}
