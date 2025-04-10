package net.smileycorp.fbiomes.common.items;

import net.minecraft.item.Item;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;

public class ItemFBiomes extends Item implements IMetaItem {
	
	public ItemFBiomes(String name) {
		setUnlocalizedName(Constants.name(name));
		setCreativeTab(FantasyBiomes.TAB);
		setRegistryName(Constants.loc(name));
	}
	
}
