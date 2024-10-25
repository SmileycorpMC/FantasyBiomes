package net.smileycorp.fbiomes.common.world.gen.tree;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigTree;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.fbiomes.common.blocks.BlockFBMushroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumVanillaWoodType;

import java.util.Random;

public class WorldGenBigRedOakTree extends WorldGenBigTree {
    
    private static final IBlockState LEAVES = FBiomesBlocks.VANILLA_LEAVES.getDefaultState()
            .withProperty(FBiomesBlocks.VANILLA_LEAVES.getVariantProperty(), EnumVanillaWoodType.RED_OAK)
            .withProperty(BlockLeaves.DECAYABLE, true).withProperty(BlockLeaves.CHECK_DECAY, false);
    private final boolean hasMushrooms;
    
    public WorldGenBigRedOakTree(boolean notify, boolean hasMushrooms) {
        super(notify);
        this.hasMushrooms = hasMushrooms;
    }
    
    @Override
    protected void setBlockAndNotifyAdequately(World world, BlockPos pos, IBlockState state) {
        if (state.getBlock() == Blocks.LEAVES) state = LEAVES;
        super.setBlockAndNotifyAdequately(world, pos, state);
        if (hasMushrooms && state.getBlock() == Blocks.LOG) {
            Random rand = world.rand;
            if (rand.nextInt(7) < 3) {
                EnumFacing facing = DirectionUtils.getRandomDirectionXZ(rand);
                BlockPos facePos = pos.offset(facing);
                IBlockState shroom = (rand.nextInt(3)==1 ? FBiomesBlocks.glowshrooms[rand.nextInt(FBiomesBlocks.glowshrooms.length)]:
                        FBiomesBlocks.shrooms[rand.nextInt(FBiomesBlocks.shrooms.length)]).getDefaultState();
                if (world.isAirBlock(facePos)) setBlockAndNotifyAdequately(world, facePos, shroom.withProperty(BlockFBMushroom.FACING, facing));
            }
        }
    }
}
