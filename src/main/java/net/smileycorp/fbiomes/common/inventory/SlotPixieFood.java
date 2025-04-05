package net.smileycorp.fbiomes.common.inventory;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.smileycorp.fbiomes.common.Constants;

import javax.annotation.Nonnull;

public class SlotPixieFood extends SlotItemHandler {
    
    public static final ResourceLocation TEXTURE = Constants.loc("textures/gui/container/pixie_table_food_empty.png");
    
    public SlotPixieFood(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }
    
    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        return stack.getItem() instanceof ItemFood && super.isItemValid(stack);
    }
    
}
