package net.smileycorp.fbiomes.common;

import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class Constants {
	
	public static final String MODID = "fbiomes";
	public static final String NAME = "Smileycorp's Fantasy Biomes";
	public static final String VERSION = "alpha 1.1a";
	
	public static String name(String name) {
		return MODID + "." + name.toLowerCase(Locale.US);
	}
	
	public static ResourceLocation loc(String name) {
		return new ResourceLocation(MODID, name.toLowerCase(Locale.US));
	}
	
}
