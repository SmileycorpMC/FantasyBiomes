package net.smileycorp.phantasiai.common.items.block;

import net.minecraft.item.ItemStack;
import net.smileycorp.phantasiai.common.blocks.BlockPixieJar;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;
import net.smileycorp.phantasiai.common.items.ItemBlockFBiomes;

public class ItemPixieJar extends ItemBlockFBiomes<BlockPixieJar> {

    public ItemPixieJar() {
        super(PhantasiaiBlocks.PIXIE_JAR);
        setMaxStackSize(1);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName();
    }

}
