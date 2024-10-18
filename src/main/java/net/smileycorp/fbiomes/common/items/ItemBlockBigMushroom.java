package net.smileycorp.fbiomes.common.items;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.util.TextUtils;
import net.smileycorp.fbiomes.common.blocks.BlockBigMushroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

public class ItemBlockBigMushroom extends ItemBlock {
	
	public ItemBlockBigMushroom() {
		this(FBiomesBlocks.BIG_SHROOM);
	}
	
	public ItemBlockBigMushroom(Block block) {
		super(block);
		this.setRegistryName(block.getRegistryName());
		this.setUnlocalizedName(block.getUnlocalizedName());
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
        return getNameForState(block.getStateFromMeta(stack.getMetadata()));
    }

	protected String getNameForState(IBlockState state) {
		String blockName = block.getUnlocalizedName();
		String shape = TextUtils.toProperCase(state.getValue(BlockBigMushroom.SHAPE).getName());
		String variant = TextUtils.toProperCase(state.getValue(BlockBigMushroom.VARIANT).getName());
		return blockName + shape + variant;
	}

}
