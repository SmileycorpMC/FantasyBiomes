package net.smileycorp.fbiomes.common.world.gen;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.fbiomes.common.blocks.BlockBrambleBush;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

public class WorldGenBrambles extends WorldGenerator {

	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		 boolean flag = false;

	        for (int i = 0; i < 8; ++i)
	        {
	            BlockPos blockpos = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));

	            flag = generateBramble(world, rand, blockpos, 1);
	        }

	        return flag;
	}
	
	public boolean generateBramble(World world, Random rand, BlockPos pos, int bound) {
	    if (world.isAirBlock(pos) && (!world.provider.isNether() || pos.getY() < 254) && FBiomesBlocks.BRAMBLES.canPlaceBlockAt(world, pos)){
    		int a = rand.nextInt(4);
    		world.setBlockState(pos, FBiomesBlocks.BRAMBLES.getDefaultState().withProperty(BlockBrambleBush.AGE, a), 18);
    		if (rand.nextInt(bound)==0) {
    			generateBramble(world, rand, pos.north(), bound+2);
    		}
    		if (rand.nextInt(bound)==0) {
    			generateBramble(world, rand, pos.south(), bound+2);
    		}
    		if (rand.nextInt(bound)==0) {
    			generateBramble(world, rand, pos.west(), bound+2);
    		}
    		if (rand.nextInt(bound)==0) {
    			generateBramble(world, rand, pos.east(), bound+2);
    		}
    		return true;
    	 }
	    return false;
	}

}
