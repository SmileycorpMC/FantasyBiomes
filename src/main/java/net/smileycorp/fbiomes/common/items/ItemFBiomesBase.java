package net.smileycorp.fbiomes.common.items;

import net.minecraft.item.Item;

import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.ModDefinitions;

public class ItemFBiomesBase extends Item {
	public ItemFBiomesBase(String name){
		setUnlocalizedName(ModDefinitions.getName(name));
		setCreativeTab(FantasyBiomes.creativeTab);
		setRegistryName(ModDefinitions.getResource(name));
	}
}
