package net.smileycorp.phantasiai.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.recipe.PixieRecipeManager;

import javax.annotation.Nonnull;

public class SlotPixieFood extends SlotItemHandler {
    
    public static final ResourceLocation TEXTURE = Constants.loc("textures/gui/container/pixie_table_food_empty.png");
    
    public SlotPixieFood(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    
    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return PixieRecipeManager.isPixieFood(stack) && super.isItemValid(stack);
    }
    
}
