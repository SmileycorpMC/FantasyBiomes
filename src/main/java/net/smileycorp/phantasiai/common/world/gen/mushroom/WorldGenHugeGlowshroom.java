package net.smileycorp.phantasiai.common.world.gen.mushroom;

import net.smileycorp.phantasiai.common.blocks.BlockBigGlowshroom;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;
import net.smileycorp.phantasiai.common.blocks.enums.EnumGlowshroomVariant;
import net.smileycorp.phantasiai.common.blocks.enums.EnumMushroomShape;

import java.util.Random;


public class WorldGenHugeGlowshroom extends WorldGenHugeMushroomBase {

	public WorldGenHugeGlowshroom(Random rand) {
		super(rand);
	}
	
	public WorldGenHugeGlowshroom(EnumGlowshroomVariant type) {
		super(PhantasiaiBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigGlowshroom.VARIANT, type),
				PhantasiaiBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigGlowshroom.VARIANT, type));
	}

	@Override
	protected void pickType(Random rand) {
		EnumGlowshroomVariant variant = EnumGlowshroomVariant.values()[rand.nextInt(EnumGlowshroomVariant.values().length)];
		stem = PhantasiaiBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.STEM).withProperty(BlockBigGlowshroom.VARIANT, variant);
		cap = PhantasiaiBlocks.BIG_GLOWSHROOM.getDefaultState().withProperty(BlockBigGlowshroom.SHAPE, EnumMushroomShape.CAP).withProperty(BlockBigGlowshroom.VARIANT, variant);
	}
	

}
