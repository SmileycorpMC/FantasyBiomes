package net.smileycorp.fbiomes.common.items;

import net.minecraft.block.state.IBlockState;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.fbiomes.common.blocks.BlockBigGlowshroom;
import net.smileycorp.fbiomes.common.blocks.BlockBigMushroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

public class ItemBlockBigGlowshroom extends ItemBlockBigMushroom {
	
	public ItemBlockBigGlowshroom() {
		super(FBiomesBlocks.BIG_GLOWSHROOM);
	}
	
	@Override
	protected String getNameForState(IBlockState state) {
		String blockName = block.getUnlocalizedName();
		String shape = TextUtils.toProperCase(state.getValue(BlockBigMushroom.SHAPE).getName());
		String variant = TextUtils.toProperCase(state.getValue(BlockBigGlowshroom.VARIANT).getName());
		return blockName + shape + variant;
	}
}
