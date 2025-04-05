package net.smileycorp.fbiomes.common.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.smileycorp.fbiomes.common.inventory.InventoryPixieTable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PixieRecipeManager {
    
    private static final Map<ResourceLocation, IRecipe> recipes = new HashMap<>();

    @Nullable
    public static IRecipe findRecipe(InventoryPixieTable inventory, World world) {
        for (IRecipe recipe : recipes.values()) if(recipe.matches(inventory.getCraftingWrapper(), world)) return recipe;
        for (IRecipe recipe : CraftingManager.REGISTRY) if(recipe.matches(inventory.getCraftingWrapper(), world)) return recipe;
        return null;
    }
    
    public static float getFoodEfficiency(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof ItemFood ?  1 + 0.01f * (float) ((ItemFood) item).getHealAmount(stack)
                * (1 + 0.5f * ((ItemFood) item).getSaturationModifier(stack)) : 1;
    }
    
}
