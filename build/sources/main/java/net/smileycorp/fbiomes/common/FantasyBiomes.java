package net.smileycorp.fbiomes.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.world.biomes.FBiomes;

@Mod(modid = "fbiomes")
public class FantasyBiomes {
	
	public static CreativeTabs creativeTab = new CreativeTabs("TabFantasyBiomes"){
		 @Override
		 public ItemStack getTabIconItem(){
			 return new ItemStack(FBiomesBlocks.FLOWER);
		 }
	 };
	 
	
	@EventHandler
	 public void init(FMLPostInitializationEvent event){
		 System.out.println("[beyond] init started.");
		 FBiomes.init();
		 System.out.println("[beyond] init complete.");
	 }
}
