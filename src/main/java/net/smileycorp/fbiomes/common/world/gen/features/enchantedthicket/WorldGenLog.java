package net.smileycorp.fbiomes.common.world.gen.features.enchantedthicket;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.fbiomes.common.blocks.BlockFBMushroom;
import net.smileycorp.fbiomes.common.blocks.BlockLichen;
import net.smileycorp.fbiomes.common.blocks.BlockWebCover;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumWoodType;

import java.util.Arrays;
import java.util.Random;

public class WorldGenLog extends WorldGenerator {
    
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        if (world.getBlockState(pos.down()).getBlock() != Blocks.GRASS) return false;
        EnumFacing direction = rand.nextBoolean() ? EnumFacing.NORTH : EnumFacing.EAST;
        int length = 3 + rand.nextInt(3);
        for (int i = 0; i <= length; i++) if (world.isAirBlock(pos.offset(direction, i).down())
                |! world.isAirBlock(pos.offset(direction, i))) return false;
        IBlockState state = rand.nextInt(3) ==  0 ? FBiomesBlocks.WOOD.getLogState(EnumWoodType.ORANTIKKU,
                BlockLog.EnumAxis.fromFacingAxis(direction.getAxis())) : Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT,
                rand.nextBoolean() ? BlockPlanks.EnumType.BIRCH : BlockPlanks.EnumType.OAK)
                .withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(direction.getAxis()));
        for (int i = 0; i <= length; i++) setBlockAndNotifyAdequately(world, pos.offset(direction, i), state);
        return true;
    }
    
    @Override
    protected void setBlockAndNotifyAdequately(World world, BlockPos pos, IBlockState state) {
        super.setBlockAndNotifyAdequately(world, pos, state);
        Block block = state.getBlock();
        if (block != Blocks.LOG || block != FBiomesBlocks.WOOD.getLogState(EnumWoodType.ORANTIKKU, BlockLog.EnumAxis.Y).getBlock()) return;
        Random rand = world.rand;
        EnumFacing[] facings = getFacings(state);
        for (EnumFacing facing : facings) if (rand.nextBoolean() && world.isAirBlock(pos.offset(facing)))
            setBlockAndNotifyAdequately(world, pos.offset(facing), rand.nextInt(15) == 0 ? BlockWebCover.getBlockState(facing.getOpposite())
                    : BlockLichen.getBlockState(facing.getOpposite()));
        if (rand.nextInt(5) > 1) return;
        if (rand.nextInt(3) == 0) {
            if (world.isAirBlock(pos.up())) setBlockAndNotifyAdequately(world, pos.up(), (rand.nextBoolean() ? Blocks.RED_MUSHROOM
                    : Blocks.BROWN_MUSHROOM).getDefaultState());
            return;
        }
        EnumFacing facing = facings[rand.nextInt(facings.length)];
        BlockPos facePos = pos.offset(facing);
        IBlockState shroom = (rand.nextInt(5) < 2 ? FBiomesBlocks.glowshrooms[rand.nextInt(FBiomesBlocks.glowshrooms.length)]:
                FBiomesBlocks.shrooms[rand.nextInt(FBiomesBlocks.shrooms.length)]).getDefaultState();
        if (world.isAirBlock(facePos)) setBlockAndNotifyAdequately(world, facePos, shroom.withProperty(BlockFBMushroom.FACING, facing));
    }
    
    private EnumFacing[] getFacings(IBlockState state) {
        return Arrays.stream(EnumFacing.values()).filter(facing -> facing != EnumFacing.DOWN && BlockLog.EnumAxis.fromFacingAxis(facing.getAxis())
                != state.getValue(BlockLog.LOG_AXIS)).toArray(EnumFacing[]::new);
    }
    
}
