package net.smileycorp.fbiomes.common;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.block.FuelHandler;
import net.smileycorp.fbiomes.common.blocks.BlockFBMushroom;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = "fbiomes")
public class RecipesRegistry {
	
	@SubscribeEvent
	public static void recipeRegisty(RegistryEvent.Register<IRecipe> event) {
		registerFuel();
		registerOredict();
		registerCrafting();
		registerBrewing();
		FBiomesBlocks.WOOD.registerRecipes();
	}
	
	private static void registerFuel() {
		FuelHandler fuel = FuelHandler.getInstance();
		fuel.registerFuel(new ItemStack(FBiomesBlocks.MUD, 1, 1), 400);
	}
	
	private static void registerOredict() {
		for (BlockFBMushroom glowshroom : FBiomesBlocks.glowshrooms) OreDictionary.registerOre("glowshroom", glowshroom);
	}
	
	private static void registerCrafting() {
	
	}
	
	private static void registerBrewing() {
		//registerPotion("glowshroom", FBiomesPotions.GLOWING, FBiomesPotions.LONGER_GLOWING, null);
	}
	
	private static void registerPotion(String ingredient, PotionType basic, @Nullable PotionType extended, @Nullable PotionType stronger) {
		//basic
		BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionTypes.AWKWARD),
				ingredient, PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), basic));
		BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), PotionTypes.AWKWARD),
				ingredient, PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), basic));
		BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), PotionTypes.AWKWARD),
				ingredient, PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), basic));
		BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), basic),
				"gunpowder", PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), basic));
		BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), basic),
				new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), basic));
		//extended
		if (extended != null) {
			BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), basic),
					"dustRedstone", PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), extended));
			BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), basic),
					"dustRedstone", PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), extended));
			BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), basic),
					"dustRedstone", PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), extended));
			BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), extended),
					"gunpowder", PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), extended));
			BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), extended),
					new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), extended));
		}
		//strengthened
		if (stronger != null) {
			BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), basic),
					"dustGlowstone", PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), stronger));
			BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), basic),
					"dustGlowstone", PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), stronger));
			BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), basic),
					"dustGlowstone", PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), stronger));
			BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), stronger),
					"gunpowder", PotionUtils.addPotionToItemStack(new ItemStack(Items.SPLASH_POTION), stronger));
			BrewingRecipeRegistry.addRecipe(PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), stronger),
					new ItemStack(Items.DRAGON_BREATH), PotionUtils.addPotionToItemStack(new ItemStack(Items.LINGERING_POTION), stronger));
		}
	}
	
	public static void registerLateRegistry() {
	
	}
	
}
