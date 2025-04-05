package net.smileycorp.fbiomes.integration.jei;

import mezz.jei.api.*;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

@JEIPlugin
public class JeiIntegration implements IModPlugin {
    
    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IJeiHelpers helpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = helpers.getGuiHelper();
        registry.addRecipeCategories(new PixieTableFoodRecipeCategory(guiHelper));
    }
    
    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(FBiomesBlocks.MYSTIC_STUMP, 1, 1), VanillaRecipeCategoryUid.CRAFTING,
                PixieTableFoodRecipeCategory.ID);
        registry.handleRecipes(PixieTableFoodRecipeCategory.Wrapper.class, r -> r, PixieTableFoodRecipeCategory.ID);
        registry.addRecipes(PixieTableFoodRecipeCategory.getRecipes(), PixieTableFoodRecipeCategory.ID);
    }
    
}
