package net.smileycorp.fbiomes.common.recipe;

import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.smileycorp.fbiomes.common.inventory.InventoryPixieTable;

public class PixieRecipeCustom extends IForgeRegistryEntry.Impl<PixieRecipeCustom> implements IPixieRecipe {

    @Override
    public int getCraftingDuration() {
        return 0;
    }

    @Override
    public boolean matches(InventoryPixieTable inv, World world) {
        return false;
    }

    @Override
    public void process(InventoryPixieTable inv) {

    }

}
