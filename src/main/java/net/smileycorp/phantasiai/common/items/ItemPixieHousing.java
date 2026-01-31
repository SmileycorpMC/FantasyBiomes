package net.smileycorp.phantasiai.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.smileycorp.phantasiai.common.Constants;

public class ItemPixieHousing extends ItemFBiomes {
    
    private static final String[] VARIANTS = {"pixie_housing_bundle", "mushroom_hut", "pixie_tools"};
    
    public ItemPixieHousing() {
        super("pixie_housing");
        setHasSubtypes(true);
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < VARIANTS.length; i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + Constants.MODID + "." + VARIANTS[stack.getMetadata() % VARIANTS.length];
    }
    
    @Override
    public int getMaxMeta() {
        return VARIANTS.length;
    }
    
    @Override
    public String byMeta(int meta) {
        return VARIANTS[meta % VARIANTS.length];
    }
    
}
