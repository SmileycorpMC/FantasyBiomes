package net.smileycorp.fbiomes.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.items.FBiomesItems.ItemRegistry;
import net.smileycorp.fbiomes.common.world.biomes.FBiomes;

@Mod(modid = "fbiomes")
public class FantasyBiomes {
	
	public static CreativeTabs creativeTab = new CreativeTabs("TabFantasyBiomes"){
		 @Override
		 public ItemStack getTabIconItem(){
			 return new ItemStack(FBiomesBlocks.GREEN_SHROOM);
		 }
	 };
	 
	 @EventHandler
	 public void preInit(FMLPreInitializationEvent event){
	 	FBiomesConfig.config = new Configuration(event.getSuggestedConfigurationFile());
	 	FBiomesConfig.syncConfig();
	 }
	 
	 @EventHandler
	 public void init(FMLInitializationEvent event){
		 FBiomes.init();
		 ItemRegistry.registerLateItems();
		 RecipesRegistry.registerLateRegistry();
	 }
	 
	@EventHandler
	 public void postInit(FMLPostInitializationEvent event){
	 }
}
