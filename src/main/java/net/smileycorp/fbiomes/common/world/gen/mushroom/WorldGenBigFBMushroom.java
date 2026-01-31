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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WorldGenBigFBMushroom extends WorldGenerator {
	
	protected final IBlockState stem;
	protected final IBlockState cap;
	protected final IBlockState spot;
	protected boolean natural = false;
	
	public WorldGenBigFBMushroom(Random rand) {
		this(EnumMushroomVariant.values()[rand.nextInt(1) + 1]);
		natural = true;
	}
	
	public WorldGenBigFBMushroom(EnumMushroomVariant type) {
		super();
		stem = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigMushroom.VARIANT, type);
		cap = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigMushroom.VARIANT, type);
		spot = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.SPOT).withProperty(BlockBigMushroom.VARIANT, type);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		ArrayList<Vec3i> spots = Lists.newArrayList();
		int count = rand.nextInt(3) + 3;
		for (int i = 0; i < count; i++) {
			int x = 2;
			int z = 2;
			while ((Math.abs(x) + Math.abs(z)) > 3 && !spots.contains(new Vec3i(x, 0, z))) {
				x = rand.nextInt(5 ) - 2;
				z = rand.nextInt(5) - 2;
			}
			spots.add(new Vec3i(x, 0, z));
		}
		int height = rand.nextInt(3) + 2;
		IBlockState soil = world.getBlockState(pos.down());
		if (!natural &! soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, FBiomesBlocks.TOADSTOOL)) return false;
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos);
		for (int j = 0; j <= height + 1; j++) {
			IBlockState state = world.getBlockState(mutable);
			mutable.move(EnumFacing.UP);
			if (!state.getBlock().canBeReplacedByLeaves(state, world, mutable)) return false;
		}
		BlockPos startPos = pos.up(height + 1);
		mutable.setPos(pos.getX(), pos.getY(), pos.getZ());
		//stem
		for (int j = 0; j <= height; j++) {
			setBlockAndNotifyAdequately(world, mutable, stem);
			mutable.move(EnumFacing.UP);
		}
		//cap
		for (int i = -2; i < 3; i++) for (int k = -2; k < 3; k++) {
			mutable.setPos(pos.getX() + i, pos.getY() + height + 1, pos.getZ() + k);
			double distance = startPos.getDistance(mutable.getX(), mutable.getY(), mutable.getZ());
			if (distance < 1.5) placeCap(world, startPos, mutable, spots);
			else if (distance < 2.5) placeCap(world, startPos, mutable.move(EnumFacing.DOWN), spots);
		}
		return true;
	}
	
	public void placeCap(World world, BlockPos startPos, BlockPos currentPos, List<Vec3i> spots) {
		IBlockState state = world.getBlockState(currentPos);
		if (!state.getBlock().canBeReplacedByLeaves(state, world, currentPos)) return;
		Vec3i vec = new Vec3i(currentPos.getX() - startPos.getX(), 0, currentPos.getZ() - startPos.getZ());
		if (spots.contains(vec)) setBlockAndNotifyAdequately(world, currentPos, spot);
		else setBlockAndNotifyAdequately(world, currentPos, cap);
	}

}
