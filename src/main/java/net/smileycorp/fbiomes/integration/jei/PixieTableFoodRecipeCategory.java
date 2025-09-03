package net.smileycorp.fbiomes.integration.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.fbiomes.client.gui.GuiPixieTable;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.recipe.PixieRecipeManager;

import java.util.List;

public class PixieTableFoodRecipeCategory implements IRecipeCategory<PixieTableFoodRecipeCategory.Wrapper> {
    
    public static final String ID = Constants.locStr("pixie_table_food");
    public static final ResourceLocation TEXTURE = GuiPixieTable.TEXTURE;
    private final IDrawable background, bowl;
    
    public PixieTableFoodRecipeCategory(IGuiHelper helper) {
        background = helper.createDrawable(TEXTURE, 0, 177, 158, 28);
        bowl = helper.createAnimatedDrawable(helper.createDrawable(TEXTURE, 179, 71, 25, 7),
                200, IDrawableAnimated.StartDirection.TOP, true);
    }
    
    @Override
    public String getUid() {
        return ID;
    }
    
    @Override
    public String getTitle() {
        return I18n.format("jei.category.fbiomes.pixie_table_food");
    }
    
    @Override
    public String getModName() {
        return Constants.MODID;
    }
    
    @Override
    public IDrawable getBackground() {
        return background;
    }
    
    @Override
    public void drawExtras(Minecraft minecraft) {
        bowl.draw(minecraft, 31, 10);
    }
    
    @Override
    public void setRecipe(IRecipeLayout layout, Wrapper wrapper, IIngredients ingredients) {
        IGuiItemStackGroup items = layout.getItemStacks();
        items.init(0, true, 6, 4);
        items.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
    }
    
    public static List<Wrapper> getRecipes() {
        List<Wrapper> recipes = Lists.newArrayList();
        for (Item item : ForgeRegistries.ITEMS) {
            if (!(item instanceof ItemFood)) continue;
            NonNullList<ItemStack> stacks = NonNullList.create();
            item.getSubItems(CreativeTabs.SEARCH, stacks);
            stacks.forEach(stack -> recipes.add(new Wrapper(stack, PixieRecipeManager.getFoodEfficiency(stack))));
        }
        return recipes;
    }
    
    public static class Wrapper implements IRecipeWrapper {
        
        private final ItemStack stack;
        private final float efficiency;
        
        public Wrapper(ItemStack stack, float efficiency) {
            this.stack = stack;
            this.efficiency = (efficiency - 1) * 100f;
        }
        
        @Override
        public void getIngredients(IIngredients ingredients) {
            List<List<ItemStack>> inputs = Lists.newArrayList();
            inputs.add(Lists.newArrayList(stack));
            ingredients.setInputLists(VanillaTypes.ITEM, inputs);
        }
        
        @Override
        public void drawInfo(Minecraft mc, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
            String str = I18n.format("jei.tooltip.fbiomes.efficiency", String.format("%.2f", efficiency) + "%");
            mc.fontRenderer.drawString(str, 154 - mc.fontRenderer.getStringWidth(str), 9 , 0xFFEFC772);
        }
        
    }
    
}
