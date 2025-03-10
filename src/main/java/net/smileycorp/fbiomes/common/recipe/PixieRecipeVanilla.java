package net.smileycorp.fbiomes.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.smileycorp.fbiomes.common.inventory.InventoryPixieTable;

public class PixieRecipeVanilla implements IPixieRecipe {
    
    private final IShapedRecipe vanillaRecipe;

    public PixieRecipeVanilla(IShapedRecipe vanillaRecipe) {
        this.vanillaRecipe = vanillaRecipe;
    }

    @Override
    public int getCraftingDuration() {
        return 500;
    }

    @Override
    public boolean matches(InventoryPixieTable inv, World world) {
        return vanillaRecipe.matches(inv.getCraftingWrapper(), world);
    }

    @Override
    public void process(InventoryPixieTable inv) {
        ItemStack result = vanillaRecipe.getCraftingResult(inv.getCraftingWrapper());


    }
    
}
