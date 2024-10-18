package net.smileycorp.fbiomes.common.items;

import net.minecraft.item.Item;

import net.smileycorp.fbiomes.common.FBiomesConfig;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.ModDefinitions;

public class ItemMithril extends Item {
	public ItemMithril(String name){
		if(FBiomesConfig.changedMithrilName){
			setUnlocalizedName(ModDefinitions.getName("AstralSilver"+name));
		} else {
			setUnlocalizedName(ModDefinitions.getName("Mithril"+name));
		}
		
		setCreativeTab(FantasyBiomes.creativeTab);
		setRegistryName(ModDefinitions.getResource("Mithril_"+name));
	}
}
