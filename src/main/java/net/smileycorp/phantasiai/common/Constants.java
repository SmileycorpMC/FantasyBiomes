package net.smileycorp.phantasiai.common;

import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class Constants {
	
	public static final String MODID = "phantasiai";
	public static final String NAME = "Phantasiai";
	public static final String VERSION = "alpha 1.1b";
	
	public static String name(String name) {
		return MODID + "." + name.toLowerCase(Locale.US);
	}
	
	public static ResourceLocation loc(String name) {
		return new ResourceLocation(MODID, name.toLowerCase(Locale.US));
	}
    
    public static String locStr(String name) {
		return loc(name).toString();
    }
	
}
