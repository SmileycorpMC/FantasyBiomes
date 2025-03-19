package net.smileycorp.fbiomes.common.items;

import net.minecraft.item.ItemStack;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FBiomesConfig;
import net.smileycorp.fbiomes.common.FantasyBiomes;

public class ItemMithril extends ItemFBiomes {
	
	private final String name;
	
	public ItemMithril(String name) {
        super("mithril_" + name);
        this.name = name;
		setCreativeTab(FantasyBiomes.TAB);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + Constants.MODID + "." + (FBiomesConfig.changedMithrilName ? "astralsilver_" : "mithril_") + name;
	}
	
}
