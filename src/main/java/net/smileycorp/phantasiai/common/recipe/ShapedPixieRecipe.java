package net.smileycorp.phantasiai.common.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedPixieRecipe extends ShapedOreRecipe implements IPixieRecipe {
    
    private final int duration;
    
    public ShapedPixieRecipe(ResourceLocation group, ItemStack result, int duration, Object... recipe) {
        super(group, result, recipe);
        this.duration = duration;
    }
    
    @Override
    public int getCraftingDuration() {
        return duration;
    }

}
