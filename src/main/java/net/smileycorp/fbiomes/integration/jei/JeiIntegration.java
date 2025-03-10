package net.smileycorp.fbiomes.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.item.ItemStack;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;

@JEIPlugin
public class JeiIntegration implements IModPlugin {
    
    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCatalyst(new ItemStack(FBiomesBlocks.PIXIE_TABLE), VanillaRecipeCategoryUid.CRAFTING);
    }
    
}
