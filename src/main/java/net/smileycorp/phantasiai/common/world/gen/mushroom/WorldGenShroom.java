package net.smileycorp.phantasiai.common.world.gen.mushroom;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;

import java.util.Random;

public class WorldGenShroom extends WorldGenerator {
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
	 	for (int i = 0; i < rand.nextInt(7) + 3; ++i) {
            BlockPos newpos = pos.add(rand.nextInt(8) - rand.nextInt(8),
					rand.nextInt(3) - rand.nextInt(3), rand.nextInt(8) - rand.nextInt(8));
			IBlockState soil = world.getBlockState(newpos.down());
            if ((soil.getMaterial() == Material.GRASS || soil.getMaterial() == Material.GROUND) && soil.isFullBlock() &&
				world.isAirBlock(newpos) && (!world.provider.isNether() || newpos.getY() < world.getHeight()))
            	setBlockAndNotifyAdequately(world, newpos, getRandomShroom(rand).getDefaultState());
       	}
	 	return true;
	}

	private Block getRandomShroom(Random rand) {
		return rand.nextInt(5) < 2 ? PhantasiaiBlocks.glowshrooms[rand.nextInt(PhantasiaiBlocks.glowshrooms.length)] :
			rand.nextBoolean() ? PhantasiaiBlocks.shrooms[rand.nextInt(PhantasiaiBlocks.shrooms.length)] :
					rand.nextBoolean() ? Blocks.RED_MUSHROOM : Blocks.BROWN_MUSHROOM;
	}

}
