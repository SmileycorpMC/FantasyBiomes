package net.smileycorp.phantasiai.common.recipe;

import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.smileycorp.phantasiai.common.inventory.InventoryPixieTable;

import javax.annotation.Nullable;
import java.util.Map;

public class PixieRecipeManager {
    
    private static final Map<ResourceLocation, IRecipe> recipes = Maps.newHashMap();

    @Nullable
    public static IRecipe findRecipe(InventoryPixieTable inventory, World world) {
        for (IRecipe recipe : recipes.values()) if (recipe.matches(inventory.getCraftingWrapper(), world)) return recipe;
        return CraftingManager.findMatchingRecipe(inventory.getCraftingWrapper(), world);
    }

    public static boolean isPixieFood(ItemStack stack) {
        return stack.getItem() instanceof ItemFood;
    }
    
    public static float getFoodEfficiency(ItemStack stack) {
        Item item = stack.getItem();
        if (!(item instanceof ItemFood)) return 1;
        float multiplier = 0.03f * (float) ((ItemFood) item).getHealAmount(stack)
                * (1 + ((ItemFood) item).getSaturationModifier(stack));
        return 1 + multiplier;
    }
    
}
