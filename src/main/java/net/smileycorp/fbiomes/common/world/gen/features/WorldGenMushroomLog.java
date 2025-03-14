package net.smileycorp.fbiomes.common.world.gen.features;

import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.fbiomes.common.blocks.BlockFBMushroom;
import net.smileycorp.fbiomes.common.blocks.BlockLichen;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumWoodType;

import java.util.Random;

public class WorldGenMushroomLog extends WorldGenerator {
    
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        if (world.getBlockState(pos.down()).getBlock() != Blocks.GRASS) return false;
        EnumFacing direction = rand.nextBoolean() ? EnumFacing.NORTH : EnumFacing.WEST;
        IBlockState log = FBiomesBlocks.WOOD.getLogState(EnumWoodType.ORANTIKKU, BlockLog.EnumAxis.fromFacingAxis(direction.getAxis()));
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            if (facing.getAxis() == direction.getAxis()) continue;
            for (int i = -rand.nextInt(2) - 2; i <= 2 + rand.nextInt(2); i++) for (int j = 0; j < 3; j++) {
                BlockPos pos1 = pos.offset(facing).offset(direction, i).up(j);
                setBlockAndNotifyAdequately(world, pos1, log);
                placeDecorations(world, rand, pos1, facing);
                if (j == 2) placeDecorations(world, rand, pos1, EnumFacing.UP);
            }
        }
        for (int i = -rand.nextInt(2) - 2; i <= 2 + rand.nextInt(2); i++) {
            BlockPos pos1 = pos.offset(direction, i).up(2);
            setBlockAndNotifyAdequately(world, pos1, log);
            placeDecorations(world, rand, pos1, EnumFacing.UP);
        }
        return true;
    }
    
    private void placeDecorations(World world, Random rand, BlockPos pos, EnumFacing facing) {
        pos = pos.offset(facing);
        if (rand.nextBoolean() && world.isAirBlock(pos))
            setBlockAndNotifyAdequately(world, pos, FBiomesBlocks.LICHEN.getDefaultState().withProperty(BlockLichen.FACING, facing));
        else if (rand.nextInt(10) < 1) if (world.isAirBlock(pos)) setBlockAndNotifyAdequately(world, pos,
                (rand.nextInt(5) < 2 ? FBiomesBlocks.glowshrooms[rand.nextInt(FBiomesBlocks.glowshrooms.length)]:
                    FBiomesBlocks.shrooms[rand.nextInt(FBiomesBlocks.shrooms.length)]).getDefaultState().withProperty(BlockFBMushroom.FACING, facing));
    }
    
}
