package net.smileycorp.fbiomes.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FBiomesConfig;
import net.smileycorp.fbiomes.common.FantasyBiomes;

public class ItemMithril extends Item {
	
	private final String name;
	
	public ItemMithril(String name) {
		this.name = name;
		setCreativeTab(FantasyBiomes.TAB);
		setRegistryName(Constants.loc("mithril_" + name));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + Constants.MODID + "." + (FBiomesConfig.changedMithrilName ? "astralsilver_" : "mithril_") + name;
	}
	
}
