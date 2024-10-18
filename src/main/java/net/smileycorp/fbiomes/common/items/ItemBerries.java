package net.smileycorp.fbiomes.common.items;

import net.minecraft.item.ItemFood;

import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.ModDefinitions;

public class ItemBerries extends ItemFood {

	public ItemBerries() {
		super(1, 0.5f, false);
		setCreativeTab(FantasyBiomes.creativeTab);
		setUnlocalizedName(ModDefinitions.getName("Berries"));
		setRegistryName(ModDefinitions.getResource("Berries"));
	}

}
