package net.smileycorp.fbiomes.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.network.PacketHandler;
import net.smileycorp.fbiomes.common.world.WorldEventHandler;

@Mod(modid = Constants.MODID)
public class FantasyBiomes {
	
	@Mod.Instance
	public static FantasyBiomes instance;
	
	public static CreativeTabs TAB = new CreativeTabs(Constants.MODID){
		 @Override
		 public ItemStack getTabIconItem() {
			 return new ItemStack(FBiomesBlocks.GREEN_SHROOM);
		 }
	 };
	
	public FantasyBiomes() {
		instance = this;
	}
	 
	 @EventHandler
	 public void preInit(FMLPreInitializationEvent event) {
		FBiomesConfig.config = new Configuration(event.getSuggestedConfigurationFile());
		FBiomesConfig.syncConfig();
		PacketHandler.initPackets();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());
		MinecraftForge.TERRAIN_GEN_BUS.register(new WorldEventHandler());
	 }
	 
	 @EventHandler
	 public void init(FMLInitializationEvent event) {
		 //ItemRegistry.registerLateItems();
		 RecipesRegistry.registerLateRegistry();
	 }
	 
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {}
	
}
