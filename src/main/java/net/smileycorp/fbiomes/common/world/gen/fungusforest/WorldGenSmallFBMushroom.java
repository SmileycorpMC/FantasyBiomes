package net.smileycorp.fbiomes.common.world.gen.fungusforest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.smileycorp.fbiomes.common.blocks.BlockBigMushroom;
import net.smileycorp.fbiomes.common.blocks.BlockBigMushroom.EnumShape;
import net.smileycorp.fbiomes.common.blocks.BlockBigMushroom.EnumVariant;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WorldGenSmallFBMushroom extends WorldGenSmallFBMushroomBase {
	
	protected IBlockState spot;
	
	List<Vec3i> spots = new ArrayList<Vec3i>();
	
	public WorldGenSmallFBMushroom(Random rand) {
		super(rand);
	}
	
	public WorldGenSmallFBMushroom(IBlockState stem, IBlockState cap) {
		super(stem, cap);
		spot = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumShape.SPOT).withProperty(BlockBigMushroom.VARIANT, cap.getValue(BlockBigMushroom.VARIANT));	
	}
	
	public WorldGenSmallFBMushroom(Random rand, EnumFacing facing) {
		super(rand, facing);
	}

	@Override
	protected void pickType(Random rand) {
		EnumVariant variant = EnumVariant.values()[rand.nextInt(EnumVariant.values().length)];
		stem = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumShape.STEM).withProperty(BlockBigMushroom.VARIANT, variant);
		cap = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumShape.CAP).withProperty(BlockBigMushroom.VARIANT, variant);
		spot = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumShape.SPOT).withProperty(BlockBigMushroom.VARIANT, variant);	
	}
	
	@Override
	public void placeCap(World world, Random rand, BlockPos startPos, BlockPos currentPos, int height) {
		IBlockState state = world.getBlockState(currentPos);
		if (state.getBlock().canBeReplacedByLeaves(state, world, currentPos)) {
			if (spots.isEmpty()) genSpots(rand);
			Vec3i vec = new Vec3i(currentPos.getX()-startPos.getX(), 0, currentPos.getZ()-startPos.getZ());
			if (spots.contains(vec)) world.setBlockState(currentPos, spot);
			else world.setBlockState(currentPos, cap);
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
