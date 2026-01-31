package net.smileycorp.phantasiai.common.world.gen.features.moorland;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.phantasiai.common.blocks.BlockBrambleBush;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;

import java.util.Random;

public class WorldGenBrambles extends WorldGenerator {
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		 boolean generated = false;
		for (int i = 0; i < 8; ++i) generated = generateBramble(world, rand, pos.add(rand.nextInt(8) - rand.nextInt(8),
				rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8)), 1);
		return generated;
	}
	
	public boolean generateBramble(World world, Random rand, BlockPos pos, int bound) {
	    if (world.isAirBlock(pos) && (!world.provider.isNether() || pos.getY() < 254) && PhantasiaiBlocks.BRAMBLES.canPlaceBlockAt(world, pos)){
    		setBlockAndNotifyAdequately(world, pos, PhantasiaiBlocks.BRAMBLES.getDefaultState().withProperty(BlockBrambleBush.AGE, rand.nextInt(4)));
			for (EnumFacing facing : EnumFacing.Plane.HORIZONTAL.facings()) if (rand.nextInt(bound) == 0) generateBramble(world, rand, pos.offset(facing), bound + 2);
    		return true;
    	 }
	    return false;
	}

}
