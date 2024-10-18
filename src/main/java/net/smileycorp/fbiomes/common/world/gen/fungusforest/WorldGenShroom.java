package net.smileycorp.fbiomes.common.world.gen.fungusforest;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

public class WorldGenShroom extends WorldGenerator {
	
	Block shroom;
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		//boolean flag = false;
	 	for (int i = 0; i < 16; ++i) {
	 		getNextShroom(rand);
            BlockPos newpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

            if ((world.getBlockState(newpos.down()).getMaterial()==Material.GRASS || world.getBlockState(newpos.down()).getMaterial()==Material.GROUND)
            		&& world.isAirBlock(newpos) && (!world.provider.isNether() || newpos.getY() < 255)) {
            	//IBlockState state;
        		world.setBlockState(newpos, shroom.getDefaultState(), 18);
            }
       }

        return true;
	}

	private void getNextShroom(Random rand) {
		switch (rand.nextInt(3)) {
		case 0:
			shroom = FBiomesBlocks.shrooms[rand.nextInt(FBiomesBlocks.shrooms.length)];
			break;
		case 1:
			shroom = FBiomesBlocks.glowshrooms[rand.nextInt(FBiomesBlocks.glowshrooms.length)];
			break;
		default:
			shroom = rand.nextInt(2)==1 ? Blocks.RED_MUSHROOM : Blocks.BROWN_MUSHROOM;
			break;
		}
	}

}
