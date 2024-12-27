package net.smileycorp.fbiomes.common.world.gen.fungusforest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.smileycorp.fbiomes.common.blocks.BlockBigMushroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomShape;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomVariant;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class WorldGenHugeFBMushroom extends WorldGenHugeMushroomBase {
	
	protected IBlockState spot;
	
	private final List<Vec3i> spots = new ArrayList<Vec3i>();
	
	public WorldGenHugeFBMushroom(Random rand) {
		super(rand);
	}
	
	public WorldGenHugeFBMushroom(EnumMushroomVariant type) {
		super(FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigMushroom.VARIANT, type),
				FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigMushroom.VARIANT, type));
		spot = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.SPOT).withProperty(BlockBigMushroom.VARIANT, type);
	}
	
	@Override
	protected void pickType(Random rand) {
		EnumMushroomVariant variant = EnumMushroomVariant.values()[rand.nextInt(1) + 1];
		stem = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigMushroom.VARIANT, variant);
		cap = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigMushroom.VARIANT, variant);
		spot = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.SPOT).withProperty(BlockBigMushroom.VARIANT, variant);
	}
	
	@Override
	public void placeCap(World world, Random rand, BlockPos startPos, BlockPos currentPos, int height, EnumFacing bendDir) {
		IBlockState state = world.getBlockState(currentPos);
		if (!state.getBlock().canBeReplacedByLeaves(state, world, currentPos)) return;
		if (spots.isEmpty()) genSpots(rand);
		BlockPos center = startPos.offset(bendDir);
		Vec3i vec = new Vec3i(currentPos.getX()-center.getX(), 0, currentPos.getZ()-center.getZ());
		if (spots.contains(vec)) world.setBlockState(currentPos, spot);
		else world.setBlockState(currentPos, cap);
	}

	private void genSpots(Random rand) {
		int count = rand.nextInt(4)+5;
		List<Vec3i> vecs = new ArrayList<Vec3i>();
		for (int i = 0; i < count; i++) {
			int size = rand.nextInt(5)+1;
			int x = 5;
			int z = 5;
			while ((Math.abs(x)+Math.abs(z))>8 && !vecs.contains(new Vec3i(x, 0, z))) {
				x = rand.nextInt(11)-5;
				z = rand.nextInt(11)-5;
			}
			List<Vec3i> localVecs = new ArrayList<Vec3i>();
			localVecs.add(new Vec3i(0, 0, 0));
			vecs.add(new Vec3i(x, 0, z));
			for (int j = 1; j < size; j++) {
				while (localVecs.size()<j+1) {
					Vec3i vec = selectDir(rand, localVecs.get(rand.nextInt(j)));
					if (!localVecs.contains(vec)) localVecs.add(vec);
				}
			}
			for (Vec3i vec : localVecs) spots.add(new Vec3i(vec.getX() + x, 0, vec.getZ() + z));
		}
		spots.addAll(vecs);
	}
	
	private Vec3i selectDir(Random rand, Vec3i start) {
		switch (rand.nextInt(4)) {
		case 0:
			return new Vec3i(start.getX() + 1, 0, start.getZ());
		case 1:
			return new Vec3i(start.getX() - 1, 0, start.getZ());
		case 2:
			return new Vec3i(start.getX(), 0, start.getZ() + 1);
		case 3:
			return new Vec3i(start.getX(), 0, start.getZ() - 1);
		}
		return start;
	}

}
