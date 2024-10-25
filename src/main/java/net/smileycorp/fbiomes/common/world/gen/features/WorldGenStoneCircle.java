package net.smileycorp.fbiomes.common.world.gen.features;

import com.google.common.collect.Sets;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Collection;
import java.util.Random;

public class WorldGenStoneCircle extends WorldGenerator {
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		if (!world.getBlockState(pos.down()).isFullBlock()) return false;
		int r = rand.nextInt(3) + 4;
		int max = 12;
		Collection<BlockPos> pillars = Sets.newHashSet();
		for (int i = 0; i < max; i++) {
			double angle = (2 * Math.PI) / max * i;
			int x = pos.getX() + (int) Math.round(Math.cos(angle) * r);
			int z = pos.getZ() + (int) Math.round(Math.sin(angle) * r);
			BlockPos newpos = new BlockPos(x, world.getHeight(x, z), z);
			if (!(world.getBlockState(newpos.down()).getMaterial() == Material.GRASS
					|| world.getBlockState(newpos.down()).getMaterial() == Material.GROUND)) {
				return false;
			}
			pillars.add(newpos);
		}
		for (BlockPos pillar : pillars) for (int h = 0; h < rand.nextInt(3) + 3; h++)
			setBlockAndNotifyAdequately(world, pillar.up(h), (rand.nextBoolean() ? Blocks.COBBLESTONE : Blocks.STONE).getDefaultState());
		return true;
	}

}
