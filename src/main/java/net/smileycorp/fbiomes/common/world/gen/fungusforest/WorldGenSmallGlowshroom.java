package net.smileycorp.fbiomes.common.world.gen.fungusforest;

import net.minecraft.util.EnumFacing;
import net.smileycorp.fbiomes.common.blocks.BlockBigGlowshroom;
import net.smileycorp.fbiomes.common.blocks.BlockBigGlowshroom.EnumVariant;
import net.smileycorp.fbiomes.common.blocks.BlockBigMushroom;
import net.smileycorp.fbiomes.common.blocks.BlockBigMushroom.EnumShape;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

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
		EnumVariant variant = EnumVariant.values()[rand.nextInt(EnumVariant.values().length)];
		stem = FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumShape.STEM).withProperty(BlockBigGlowshroom.VARIANT, variant);
		cap = FBiomesBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumShape.CAP).withProperty(BlockBigGlowshroom.VARIANT, variant);
	}
	

}
