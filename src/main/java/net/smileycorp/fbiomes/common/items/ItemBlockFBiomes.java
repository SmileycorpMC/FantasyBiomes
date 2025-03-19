package net.smileycorp.fbiomes.common.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.fbiomes.common.Constants;

public class ItemBlockFBiomes<T extends Block & BlockProperties> extends ItemBlock implements IMetaItem {
    
    public ItemBlockFBiomes(T block) {
        super(block);
        setRegistryName(block.getRegistryName());
        setUnlocalizedName(block.getUnlocalizedName().substring(0, 5));
        if (block.getMaxMeta() > 1) setHasSubtypes(true);
    }
    
    @Override
    public int getMaxMeta() {
        return ((BlockProperties)block).getMaxMeta();
    }
    
    @Override
    public String byMeta(int meta) {
        return ((BlockProperties)block).byMeta(meta);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getMaxMeta() > 0 ? "tile." + Constants.name(byMeta(stack.getMetadata())) : super.getUnlocalizedName();
    }
    
}
