package net.smileycorp.fbiomes.common;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ModDefinitions {
	public static Map<String, ItemStack> wood_types = new HashMap<String, ItemStack>();
	
	public static final String modid = "fbiomes";
	public static final String name = "Smileycorp's Fantasy Biomes";
	public static final String version = "alpha 1.1a";
	
	public static String getName(String name) {
		return modid + "." + name.replace("_", "");
	}
	
	public static ResourceLocation getResource(String name) {
		return new ResourceLocation(modid, name.toLowerCase());
	}
	
	static {
		String[] woods = {"greatoak", "gnarlroot", "ash", "pine", "cedar"};
		for (String wood: woods) {
			wood_types.put(wood, null);
		}
	}
}
