package net.smileycorp.fbiomes.common.world.gen.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.fbiomes.common.blocks.BlockLichen;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

import java.util.Random;

public class WorldGenBoulder extends WorldGenerator {
 
	private static final int startRadius = 2;
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
        while (true) {
            if (pos.getY() > 60) {
                if (world.isAirBlock(pos.down()))  {
                    pos = pos.down();
                    continue ;
                }
                Block block = world.getBlockState(pos.down()).getBlock();
                if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.STONE) {
                    pos = pos.down();
                    continue ;
                }
            }
            if (pos.getY() <= 3) return false;
            int i1 = startRadius;
            for (int i = 0; i1 >= 0 && i < 3; ++i) {
                int j = i1 + rand.nextInt(2);
                int k = i1 + rand.nextInt(2);
                int l = i1 + rand.nextInt(2);
                float f = (j + k + l) * 0.333F + 0.5F;
                for (BlockPos newpos : BlockPos.getAllInBox(pos.add(-j, -k, -l), pos.add(j, k, l)))
                    if (newpos.distanceSq(pos) <= f * f) {
                        setBlockAndNotifyAdequately(world, newpos, getBlockState(rand));
                        EnumFacing facing = EnumFacing.values()[rand.nextInt(EnumFacing.values().length)];
                        BlockPos p1 = pos.offset(facing);
                        if (world.isAirBlock(p1)) setBlockAndNotifyAdequately(world, pos, FBiomesBlocks.LICHEN.getDefaultState().withProperty(BlockLichen.FACING, facing));
                    }
                pos = pos.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
            }
            return true;
        }
    }
    
    protected IBlockState getBlockState(Random rand) {
        switch (rand.nextInt(5)) {
            case 0:
                return Blocks.COBBLESTONE.getDefaultState();
            case 1:
                return Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);
            case 2:
                return Blocks.GRAVEL.getDefaultState();
        }
        return Blocks.STONE.getDefaultState();
    }

}
