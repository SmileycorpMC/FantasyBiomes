package net.smileycorp.fbiomes.common.items;

import net.minecraft.item.ItemFood;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;

public class ItemBerries extends ItemFood {

	public ItemBerries() {
		super(1, 0.5f, false);
		setCreativeTab(FantasyBiomes.creativeTab);
		setUnlocalizedName(Constants.name("Berries"));
		setRegistryName(Constants.loc("Berries"));
	}

}
