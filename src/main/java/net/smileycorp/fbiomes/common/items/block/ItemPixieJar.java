package net.smileycorp.fbiomes.common.items.block;

import net.minecraft.item.ItemStack;
import net.smileycorp.fbiomes.common.blocks.BlockPixieJar;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.items.ItemBlockFBiomes;

public class ItemPixieJar extends ItemBlockFBiomes<BlockPixieJar> {

    public ItemPixieJar() {
        super(FBiomesBlocks.PIXIE_JAR);
        setMaxStackSize(1);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName();
    }

}
