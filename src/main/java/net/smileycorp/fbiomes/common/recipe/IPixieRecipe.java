package net.smileycorp.fbiomes.common.recipe;

import net.minecraft.world.World;
import net.smileycorp.fbiomes.common.inventory.InventoryPixieTable;

public interface IPixieRecipe {

    int getCraftingDuration();

    boolean matches(InventoryPixieTable inv, World world);

    void process(InventoryPixieTable inv);


}
