package net.smileycorp.fbiomes.common.world.gen.fungusforest;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.smileycorp.fbiomes.common.blocks.BlockBigMushroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomShape;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomVariant;

import java.util.List;
import java.util.Random;


public class WorldGenBigFBMushroom extends WorldGenBigMushroomBase {
	
	protected IBlockState spot;
	
	List<Vec3i> spots = Lists.newArrayList();
	
	public WorldGenBigFBMushroom(Random rand) {
		super(rand);
	}
	
	public WorldGenBigFBMushroom(EnumMushroomVariant type) {
		super(FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigMushroom.VARIANT, type)
				, FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigMushroom.VARIANT, type));
		spot = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.SPOT).withProperty(BlockBigMushroom.VARIANT, type);
	}
	
	@Override
	protected void pickType(Random rand) {
		EnumMushroomVariant variant = EnumMushroomVariant.values()[rand.nextInt(EnumMushroomVariant.values().length)];
		stem = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigMushroom.VARIANT, variant);
		cap = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigMushroom.VARIANT, variant);
		spot = FBiomesBlocks.BIG_SHROOM.getDefaultState().withProperty(BlockBigMushroom.SHAPE, EnumMushroomShape.SPOT).withProperty(BlockBigMushroom.VARIANT, variant);
	}
	
	@Override
	public void placeCap(World world, Random rand, BlockPos startPos, BlockPos currentPos, int height) {
		IBlockState state = world.getBlockState(currentPos);
		if (state.getBlock().canBeReplacedByLeaves(state, world, currentPos)) {
			if (spots.isEmpty()) genSpots(rand);
			Vec3i vec = new Vec3i(currentPos.getX()-startPos.getX(), 0, currentPos.getZ()-startPos.getZ());
			if (spots.contains(vec)) setBlockAndNotifyAdequately(world, currentPos, spot);
			else setBlockAndNotifyAdequately(world, currentPos, cap);
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
