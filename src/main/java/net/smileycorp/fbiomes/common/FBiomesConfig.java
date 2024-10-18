package net.smileycorp.fbiomes.common;

import net.minecraftforge.common.config.Configuration;

public class FBiomesConfig {

	public static Configuration config;
	
	public static boolean changedMithrilName = false;

	public static void syncConfig() {
		try{
			config.load();
			//general
			changedMithrilName = config.get("general", "changedMithrilName",
					false, "If true mithril will be registered as Astral-Silver instead, ignoring mithril ore dictionaries.").getBoolean();
			
		} catch (Exception e) {
		} finally {
    	if (config.hasChanged()) config.save();
		}
	}

}
