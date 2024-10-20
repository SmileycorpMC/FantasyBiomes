package net.smileycorp.fbiomes.common.world.gen.features;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class WorldGenStoneCircle extends WorldGenerator {
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		if (!world.getBlockState(pos.down()).isFullBlock()) return false;
		Map<BlockPos, Block> pillars = new HashMap<>();
		int r = rand.nextInt(3) + 4;
		int max = 12;
		for (int i = 0; i < max; i++) {
			double angle = (2 * Math.PI) / max * i;
			int x = pos.getX() + (int) Math.round(Math.cos(angle) * r);
			int z = pos.getZ() + (int) Math.round(Math.sin(angle) * r);
			BlockPos newpos = new BlockPos(x, world.getHeight(x, z), z);
			Block block = rand.nextBoolean() ? Blocks.COBBLESTONE : Blocks.MOSSY_COBBLESTONE;
			if (!(world.getBlockState(newpos.down()).getMaterial() == Material.GRASS
					|| world.getBlockState(newpos.down()).getMaterial() == Material.GROUND)) {
				System.out.println(newpos.down() +  " is not grass");
				return false;
			}
			for (BlockPos ppos : pillars.keySet()) {
				if (newpos.getY() + 3 <= ppos.getY() || newpos.getY() - 3 >= ppos.getY()) {
					System.out.println(newpos +  " is too far from " + ppos);
					return false;
				}
			}
			pillars.put(newpos, block);
		}
		for (Entry<BlockPos, Block> entry : pillars.entrySet()) {
			for (int h = 0; h < rand.nextInt(3)+3; h++) {
				world.setBlockState(entry.getKey().up(h), entry.getValue().getDefaultState(), 18);
				System.out.println(entry.getKey());
			}
		}
		return true;
	}

}
