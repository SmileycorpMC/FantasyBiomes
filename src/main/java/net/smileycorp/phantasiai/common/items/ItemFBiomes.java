package net.smileycorp.phantasiai.common.items;

import net.minecraft.item.Item;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.Phantasiai;

public class ItemFBiomes extends Item implements IMetaItem {
	
	public ItemFBiomes(String name) {
		setUnlocalizedName(Constants.name(name));
		setCreativeTab(Phantasiai.TAB);
		setRegistryName(Constants.loc(name));
	}
	
}
