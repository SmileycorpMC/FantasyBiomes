package net.smileycorp.fbiomes.common.world.gen.features;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenBoulder extends WorldGenerator {

	int meta;
	int startRadius = 2;
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos)
    {
		
        while (true)
        {
            generator:
            {
                if (pos.getY() > 3)
                {
                    if (world.isAirBlock(pos.down()))
                    {
                        break generator;
                    }

                    Block block = world.getBlockState(pos.down()).getBlock();

                    if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.STONE)
                    {
                        break generator;
                    }
                }

                if (pos.getY() <= 3)
                {
                    return false;
                }

                int i1 = this.startRadius;

                for (int i = 0; i1 >= 0 && i < 3; ++i)
                {
                    int j = i1 + rand.nextInt(2);
                    int k = i1 + rand.nextInt(2);
                    int l = i1 + rand.nextInt(2);
                    float f = (j + k + l) * 0.333F + 0.5F;

                    for (BlockPos newpos : BlockPos.getAllInBox(pos.add(-j, -k, -l), pos.add(j, k, l)))
                    {
                        if (newpos.distanceSq(pos) <= f * f)
                        {
                            placeBlock(world, rand, newpos);
                        }
                    }

                    pos = pos.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2), 0 - rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
                }

                return true;
            }
            pos = pos.down();
        }
    }
	
	public void placeBlock(World world, Random rand, BlockPos pos) {
		switch (rand.nextInt(5)) {
		case 0: 
			world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState(), 3);
			break;
		case 1: 
			world.setBlockState(pos, Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), 3);
			break;
		case 2: 
			world.setBlockState(pos, Blocks.GRAVEL.getDefaultState(), 3);
			break;
		default: 
			world.setBlockState(pos, Blocks.STONE.getDefaultState(), 3);
			break;
		}
	}

}
