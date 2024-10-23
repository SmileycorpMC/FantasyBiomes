package net.smileycorp.fbiomes.common;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.block.FuelHandler;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.items.FBiomesItems;

@Mod.EventBusSubscriber(modid = "fbiomes")
public class RecipesRegistry {
	
	@SubscribeEvent
	public static void recipeRegisty(RegistryEvent.Register<IRecipe> event) {
		FuelHandler.getInstance().registerFuel(FBiomesBlocks.PEAT, 400);
		if(FBiomesConfig.changedMithrilName){
			OreDictionary.registerOre("ingotAstralSilver", FBiomesItems.MITHRIL_INGOT);
			OreDictionary.registerOre("nuggetAstralSilver", FBiomesItems.MITHRIL_NUGGET);
			//OreDictionary.registerOre("oreAstralSilver", FBiomesBlocks.MITHRIL_ORE);
			//OreDictionary.registerOre("blockAstralSIlver", FBiomesBlocks.MITHRIL_BLOCK);
		} else {
			OreDictionary.registerOre("ingotMithril", FBiomesItems.MITHRIL_INGOT);
			OreDictionary.registerOre("nuggetMithril", FBiomesItems.MITHRIL_NUGGET);
			//OreDictionary.registerOre("oreMithril", FBiomesBlocks.MITHRIL_ORE);
			//OreDictionary.registerOre("blockMithril", FBiomesBlocks.MITHRIL_BLOCK);
		}
		FBiomesBlocks.WOOD.registerRecipes();
		//GameRegistry.addSmelting(FBiomesBlocks.MITHRIL_ORE, new ItemStack(FBiomesItems.MITHRIL_INGOT), 1.8f);
	}
	
	public static void registerLateRegistry() {
		if (OreDictionary.doesOreNameExist("dustIron")){
			GameRegistry.addSmelting(FBiomesItems.MITHRIL_DUST, new ItemStack(FBiomesItems.MITHRIL_INGOT), 0.9f);
			if(FBiomesConfig.changedMithrilName){
				OreDictionary.registerOre("dustAstralSilver", FBiomesItems.MITHRIL_DUST);
			} else {
				OreDictionary.registerOre("dustMithril", FBiomesItems.MITHRIL_DUST);
			}
		}
	}
}
