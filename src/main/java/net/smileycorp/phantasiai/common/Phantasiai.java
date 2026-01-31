package net.smileycorp.phantasiai.common;

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
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;
import net.smileycorp.phantasiai.common.network.PacketHandler;
import net.smileycorp.phantasiai.common.world.WorldEventHandler;

@Mod(modid = Constants.MODID)
public class Phantasiai {
	
	@Mod.Instance
	public static Phantasiai instance;
	
	public static CreativeTabs TAB = new CreativeTabs(Constants.MODID){
		 @Override
		 public ItemStack getTabIconItem() {
			 return new ItemStack(PhantasiaiBlocks.GREEN_SHROOM);
		 }
	 };
	
	public Phantasiai() {
		instance = this;
	}
	 
	 @EventHandler
	 public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.config = new Configuration(event.getSuggestedConfigurationFile());
		ConfigHandler.syncConfig();
		PacketHandler.initPackets();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GUIHandler());
		MinecraftForge.TERRAIN_GEN_BUS.register(new WorldEventHandler());
		MinecraftForge.EVENT_BUS.register(new EventListener());
	 }
	 
	 @EventHandler
	 public void init(FMLInitializationEvent event) {
		 //ItemRegistry.registerLateItems();
		 RecipesRegistry.registerLateRegistry();
	 }
	 
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	
	}
	
}
