package net.smileycorp.fbiomes.common.world.gen.mushroom;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.IPlantable;
import net.smileycorp.atlas.api.util.DirectionUtils;

import java.util.Random;

public abstract class WorldGenHugeMushroomBase extends WorldGenerator {
	
	protected IBlockState stem;
	protected IBlockState cap;
	
	public WorldGenHugeMushroomBase(Random rand) {
		pickType(rand);
	}
	
	public WorldGenHugeMushroomBase(IBlockState stem, IBlockState cap) {
		this.stem = stem;
		this.cap =  cap;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		IBlockState soil = world.getBlockState(pos.down());
		if (!soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, (IPlantable) Blocks.SAPLING)) return false;
		int height = rand.nextInt(3) + 5;
		for (int j = 0; j < height + 2; j++) {
			IBlockState state = world.getBlockState(pos.up(j));
			if (!state.getBlock().canBeReplacedByLeaves(state, world, pos)) return false;
		}
		EnumFacing bendDir = DirectionUtils.getRandomDirectionXZ(rand);
		BlockPos startPos = pos;
		//stem
		for (int j = 0; j < height+1; j++) {
			if(j > 0)pos = pos.up();
			for (int i = -1; i < 2; i++) for (int k = -1; k < 2; k++) if ((Math.abs(i) + Math.abs(k) < 2) || (j == 0 || j == height))
				placeStem(world, rand, startPos, pos.east(i).north(k), height, bendDir);
			if (j==1) {
				pos = pos.offset(bendDir);
				for (int i = -1; i < 2; i++) for (int k = -1; k < 2; k++) if (Math.abs(i) + Math.abs(k) < 2||(j == 0 || j == height))
					placeStem(world, rand, startPos, pos.east(i).north(k), height, bendDir);
			}
		}
		//cap
		pos = pos.up();
		for (int i = -5; i<6; i++) for (int k = -5; k<6; k++) {
			BlockPos newPos = pos.east(i).north(k);
			double distance = pos.getDistance(newPos.getX(), newPos.getY(), newPos.getZ());
			if (distance < 2.5) placeCap(world, rand, startPos, newPos, height, bendDir);
			else if (distance < 5) placeCap(world, rand, startPos, newPos.down(), height, bendDir);
			else if (distance < 6) placeCap(world, rand, startPos, newPos.down(2), height, bendDir);
		}
		return true;
	}
	
	protected void placeStem(World world, Random rand, BlockPos startPos, BlockPos currentPos, int height, EnumFacing bendDir) {
		IBlockState state = world.getBlockState(currentPos);
		if (state.getBlock().canBeReplacedByLeaves(state, world, currentPos)) setBlockAndNotifyAdequately(world, currentPos, stem);
	}
	
	protected void placeCap(World world, Random rand, BlockPos startPos, BlockPos currentPos, int height, EnumFacing bendDir) {
		IBlockState state = world.getBlockState(currentPos);
		if (state.getBlock().canBeReplacedByLeaves(state, world, currentPos)) setBlockAndNotifyAdequately(world, currentPos, cap);
	}
	
	protected abstract void pickType(Random random);

}
