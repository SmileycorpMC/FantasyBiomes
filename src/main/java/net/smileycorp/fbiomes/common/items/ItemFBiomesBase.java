package net.smileycorp.fbiomes.common.items;

import net.minecraft.item.Item;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;

public class ItemFBiomesBase extends Item {
	
	public ItemFBiomesBase(String name) {
		setUnlocalizedName(Constants.name(name));
		setCreativeTab(FantasyBiomes.TAB);
		setRegistryName(Constants.loc(name));
	}
	
}
