package net.smileycorp.fbiomes.common.world.gen.fungusforest;

import net.smileycorp.fbiomes.common.blocks.BlockBigGlowshroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomShape;

import java.util.Random;


public class WorldGenBigGlowshroom extends WorldGenBigMushroomBase {

	public WorldGenBigGlowshroom(Random rand) {
		super(rand);
	}

	public WorldGenBigGlowshroom(EnumGlowshroomVariant type) {
		super(FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigGlowshroom.VARIANT, type),
				FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigGlowshroom.VARIANT, type));
	}

	@Override
	protected void pickType(Random rand) {
		EnumGlowshroomVariant variant = EnumGlowshroomVariant.values()[rand.nextInt(EnumGlowshroomVariant.values().length)];
		stem = FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigGlowshroom.VARIANT, variant);
		cap = FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigGlowshroom.VARIANT, variant);
	}
	
}
