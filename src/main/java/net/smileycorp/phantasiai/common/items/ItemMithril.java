package net.smileycorp.phantasiai.common.items;

import net.minecraft.item.ItemStack;
import net.smileycorp.phantasiai.common.ConfigHandler;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.Phantasiai;

public class ItemMithril extends ItemFBiomes {
	
	private final String name;
	
	public ItemMithril(String name) {
        super("mithril_" + name);
        this.name = name;
		setCreativeTab(Phantasiai.TAB);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "item." + Constants.MODID + "." + (ConfigHandler.changedMithrilName ? "astralsilver_" : "mithril_") + name;
	}
	
}
