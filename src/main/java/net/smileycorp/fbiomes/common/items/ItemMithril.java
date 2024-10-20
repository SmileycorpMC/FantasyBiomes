package net.smileycorp.fbiomes.common.items;

import net.minecraft.item.Item;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FBiomesConfig;
import net.smileycorp.fbiomes.common.FantasyBiomes;

public class ItemMithril extends Item {
	public ItemMithril(String name){
		if(FBiomesConfig.changedMithrilName){
			setUnlocalizedName(Constants.name("AstralSilver"+name));
		} else {
			setUnlocalizedName(Constants.name("Mithril"+name));
		}
		
		setCreativeTab(FantasyBiomes.creativeTab);
		setRegistryName(Constants.loc("Mithril_"+name));
	}
}
