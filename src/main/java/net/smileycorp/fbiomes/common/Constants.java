package net.smileycorp.fbiomes.common;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	
	public static Map<String, ItemStack> wood_types = new HashMap<>();
	
	public static final String MODID = "fbiomes";
	public static final String NAME = "Smileycorp's Fantasy Biomes";
	public static final String VERSION = "alpha 1.1a";
	
	public static String name(String name) {
		return MODID + "." + name.replace("_", "");
	}
	
	public static ResourceLocation loc(String name) {
		return new ResourceLocation(MODID, name.toLowerCase());
	}
	
	static {
		String[] woods = {"elderwood", "gnarlroot", "ash", "pine", "cedar"};
		for (String wood: woods) {
			wood_types.put(wood, null);
		}
	}
}
