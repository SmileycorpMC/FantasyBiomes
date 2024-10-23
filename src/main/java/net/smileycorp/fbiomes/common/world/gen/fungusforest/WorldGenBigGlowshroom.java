package net.smileycorp.fbiomes.common.world.gen.fungusforest;

import net.smileycorp.fbiomes.common.blocks.BlockBigGlowshroom;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumBigMushroomShape;

import java.util.Random;


public class WorldGenBigGlowshroom extends WorldGenBigFBMushroomBase {

	public WorldGenBigGlowshroom(Random rand) {
		super(rand);
	}

	@Override
	protected void pickType(Random rand) {
		EnumGlowshroomVariant variant = EnumGlowshroomVariant.values()[rand.nextInt(EnumGlowshroomVariant.values().length)];
		stem = FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumBigMushroomShape.STEM).withProperty(BlockBigGlowshroom.VARIANT, variant);
		cap = FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumBigMushroomShape.CAP).withProperty(BlockBigGlowshroom.VARIANT, variant);
	}
	

}
