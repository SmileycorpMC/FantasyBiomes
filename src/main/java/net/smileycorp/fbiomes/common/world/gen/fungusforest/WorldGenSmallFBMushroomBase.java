package net.smileycorp.fbiomes.common.world.gen.fungusforest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public abstract class WorldGenSmallFBMushroomBase extends WorldGenerator {
	
	protected IBlockState stem;
	protected IBlockState cap;
	protected EnumFacing facing;
	
	public WorldGenSmallFBMushroomBase(Random rand) {
		pickType(rand);
	}
	
	public WorldGenSmallFBMushroomBase(Random rand, EnumFacing facing) {
		pickType(rand);
	}
	
	public WorldGenSmallFBMushroomBase(IBlockState stem, IBlockState cap) {
		this.stem=stem;
		this.cap=cap;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		int height;
		if (facing==null) {
			IBlockState soil = world.getBlockState(pos.down());
			if (!soil.getBlock().canSustainPlant(soil, world, pos, EnumFacing.UP, (IPlantable) Blocks.SAPLING)) {
				return false;
			}
			height = rand.nextInt(3)+ 2;
		} else {
			height = rand.nextInt(1)+ 2;
			placeStem(world, rand, pos, pos, height);
			placeStem(world, rand, pos, pos.offset(facing), height);
			placeStem(world, rand, pos, pos.offset(facing).up(), height);
			pos = pos.offset(facing, 2).up();
		}
		
		for (int j = 0; j<height+2; j++) {
			IBlockState state = world.getBlockState(pos.up(j));
			if (!state.getBlock().canBeReplacedByLeaves(state, world, pos)) {
				return false;
			}
		}
		BlockPos startPos = pos;
		//stem
		for (int j = 0; j<height+1; j++) {
			if(j>0)pos = pos.up();
			placeStem(world, rand, startPos, pos, height);
		}
		//cap
		pos = pos.up();
		for (int i = -2; i<6; i++) {
			for (int k = -2; k<6; k++) {
				BlockPos newPos = pos.east(i).north(k);
				double distance = pos.getDistance(newPos.getX(), newPos.getY(), newPos.getZ());
				if (distance < 1.5) {
					placeCap(world, rand, startPos, newPos, height);
				} else if (distance < 2.5) {
					placeCap(world, rand, startPos, newPos.down(), height);
				}
			}
		}
		return true;
	}
	
	protected void placeStem(World world, Random rand, BlockPos startPos, BlockPos currentPos, int height) {
		IBlockState state = world.getBlockState(currentPos);
		if (state.getBlock().canBeReplacedByLeaves(state, world, currentPos)) {
			world.setBlockState(currentPos, stem, 18);
		}
	}
	
	protected void placeCap(World world, Random rand, BlockPos startPos, BlockPos currentPos, int height) {
		IBlockState state = world.getBlockState(currentPos);
		if (state.getBlock().canBeReplacedByLeaves(state, world, currentPos)) {
			world.setBlockState(currentPos, cap, 18);
		}
	}
	
	protected abstract void pickType(Random random);

}
