package net.smileycorp.fbiomes.common.world.gen.fungusforest;

import net.minecraft.util.EnumFacing;
import net.smileycorp.fbiomes.common.blocks.BlockBigGlowshroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumGlowshroomVariant;
import net.smileycorp.fbiomes.common.blocks.enums.EnumMushroomShape;

import java.util.Random;


public class WorldGenSmallGlowshroom extends WorldGenSmallFBMushroomBase {

	public WorldGenSmallGlowshroom(Random rand) {
		super(rand);
	}

	public WorldGenSmallGlowshroom(Random rand, EnumFacing facing) {
		super(rand, facing);
	}

	@Override
	protected void pickType(Random rand) {
		EnumGlowshroomVariant variant = EnumGlowshroomVariant.values()[rand.nextInt(EnumGlowshroomVariant.values().length)];
		stem = FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigGlowshroom.VARIANT, variant);
		cap = FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigGlowshroom.VARIANT, variant);
	}
	

}
