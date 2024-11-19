package net.smileycorp.fbiomes.common.world.gen.fungusforest;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.fbiomes.common.blocks.BlockBigGlowshroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomShape;

import java.util.Random;


public class WorldGenBigGlowshroom extends WorldGenerator {
	
	protected final IBlockState stem;
	protected final IBlockState cap;
	
	public WorldGenBigGlowshroom(Random rand) {
		this(EnumGlowshroomVariant.values()[rand.nextInt(EnumGlowshroomVariant.values().length)]);
	}
	
	public WorldGenBigGlowshroom(EnumGlowshroomVariant type) {
		super();
		stem = FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigGlowshroom.VARIANT, type);
		cap = FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigGlowshroom.VARIANT, type);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		int height = rand.nextInt(3) + 3;
		IBlockState soil = world.getBlockState(pos.down());
		if (!soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, FBiomesBlocks.TOADSTOOL)) return false;
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos);
		for (int j = 0; j <= height + 1; j++) {
			IBlockState state = world.getBlockState(mutable);
			mutable.move(EnumFacing.UP);
			if (!state.getBlock().canBeReplacedByLeaves(state, world, mutable)) return false;
		}
		mutable.setPos(pos.getX(), pos.getY(), pos.getZ());
		//stem
		for (int j = 0; j <= height; j++) {
			setBlockAndNotifyAdequately(world, mutable, stem);
			mutable.move(EnumFacing.UP);
		}
		//cap
		for (int j = 0; j < 4; j++) {
			for (int i = -2; i < 3; i++) {
				int absi = Math.abs(i);
				if (j > 1 && absi == 2) continue;
				for (int k = -2; k <= 2; k++) {
					int absk = Math.abs(k);
					if (j < 2 && absi < 2 && absk < 2) continue;
					if (j > 1 && absk == 2) continue;
					if (absi + absk == 4) continue;
					if (j == 3 && absi + absk > 1) continue;
					mutable.setPos(pos.getX() + i, pos.getY() + j + height - 2, pos.getZ() + k);
					IBlockState state = world.getBlockState(mutable);
					if (state.getBlock().canBeReplacedByLeaves(state, world, mutable)) setBlockAndNotifyAdequately(world, mutable, cap);
				}
			}
		}
		return true;
	}
	
}
