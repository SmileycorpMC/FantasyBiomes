package net.smileycorp.fbiomes.common.recipe;

import net.minecraft.world.World;
import net.smileycorp.fbiomes.common.inventory.InventoryPixieTable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class PixieRecipeManager {
    private static final Map<String, IPixieRecipe> recipes = new HashMap<>();

    @Nullable
    public static IPixieRecipe findRecipe(InventoryPixieTable inventory, World world) {
        for (Map.Entry<String, IPixieRecipe> en : recipes.entrySet()) {
            if(en.getValue().matches(inventory, world)) {
                return en.getValue();
            }
        }
        return null;
    }
}
