package net.smileycorp.fbiomes.common.world.gen.mushroom;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.fbiomes.common.blocks.BlockBigMushroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomShape;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomVariant;

import java.util.List;
import java.util.Random;

public class WorldGenSmallToadstool extends WorldGenerator {
	
	protected static final IBlockState STEM = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.VARIANT, EnumMushroomVariant.TOADSTOOL)
			.withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.STEM);
	protected static final IBlockState CAP = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.VARIANT, EnumMushroomVariant.TOADSTOOL)
			.withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.CAP);
	protected static final IBlockState SPOT = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.VARIANT, EnumMushroomVariant.TOADSTOOL)
			.withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.SPOT);
	private final boolean natural;

	private List<Vec3i> spots = Lists.newArrayList();

	public WorldGenSmallToadstool(boolean natural) {
		this.natural = true;
	}

	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		int height = rand.nextInt(3) + 2;
		IBlockState soil = world.getBlockState(pos.down());
		if (!natural &! soil.getBlock().canSustainPlant(soil, world, pos, EnumFacing.UP, FBiomesBlocks.TOADSTOOL)) return false;
		for (int j = 0; j<height+2; j++) {
			IBlockState state = world.getBlockState(pos.up(j));
			if (!state.getBlock().canBeReplacedByLeaves(state, world, pos)) return false;
		}
		BlockPos startPos = pos;
		//stem
		for (int j = 0; j<height+1; j++) {
			if(j > 0) pos = pos.up();
			placeStem(world, rand, startPos, pos, height);
		}
		//cap
		pos = pos.up();
		for (int i = -3; i <= 3; i++) for (int j = -3; j <= 3; j++) if (Math.abs(i) + Math.abs(j) < 5)
			placeCap(world, rand, startPos, pos.east(i).north(j), height);
		return true;
	}
	
	protected void placeStem(World world, Random rand, BlockPos startPos, BlockPos currentPos, int height) {
		IBlockState state = world.getBlockState(currentPos);
		if (state.getBlock().canBeReplacedByLeaves(state, world, currentPos)) setBlockAndNotifyAdequately(world, currentPos, STEM);
	}
	
	public void placeCap(World world, Random rand, BlockPos startPos, BlockPos currentPos, int height) {
		IBlockState state = world.getBlockState(currentPos);
		if (state.getBlock().canBeReplacedByLeaves(state, world, currentPos)) {
			if (spots.isEmpty()) genSpots(rand);
			Vec3i vec = new Vec3i(currentPos.getX()-startPos.getX(), 0, currentPos.getZ()-startPos.getZ());
			if (spots.contains(vec)) setBlockAndNotifyAdequately(world, currentPos, SPOT);
			else setBlockAndNotifyAdequately(world, currentPos, CAP);
		}
	}
	
	private void genSpots(Random rand) {
		int count = rand.nextInt(3)+3;
		for (int i = 0; i < count; i++) {
			int x = 2;
			int z = 2;
			while ((Math.abs(x)+Math.abs(z))>3 && !spots.contains(new Vec3i(x, 0, z))) {
				x = rand.nextInt(5)-2;
				z = rand.nextInt(5)-2;
			}
			spots.add(new Vec3i(x, 0, z));
		}
	}

}
