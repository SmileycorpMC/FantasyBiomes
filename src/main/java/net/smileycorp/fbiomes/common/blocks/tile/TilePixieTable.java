package net.smileycorp.fbiomes.common.blocks.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.smileycorp.fbiomes.common.inventory.InventoryPixieTable;
import net.smileycorp.fbiomes.common.recipe.IPixieRecipe;
import net.smileycorp.fbiomes.common.recipe.PixieRecipeManager;

import javax.annotation.Nullable;

public class TilePixieTable extends TileEntity implements ITickable {
    
    //Credit: slava_110
    
    public final InventoryPixieTable inventory = new InventoryPixieTable(this);
    @Nullable
    private IPixieRecipe currentRecipe = null;
    private int recipeProgress = 0;
    
    @Override
    public void update() {
        if(currentRecipe != null) {
            if(currentRecipe.matches(inventory, world)) {
                if(recipeProgress++ >= currentRecipe.getCraftingDuration()) {
                    currentRecipe.process(inventory);
                    recipeProgress = 0;
                }
                return;
            } else {
                // TODO maybe call tryFindingRecipe or might be a bug here
                currentRecipe = null;
                recipeProgress = 0;
            }
        }
    }
    
    public void tryFindingRecipe() {
        currentRecipe = PixieRecipeManager.findRecipe(inventory, world);
        
        if(currentRecipe != null) {
            if(recipeProgress++ >= currentRecipe.getCraftingDuration()) {
                currentRecipe.process(inventory);
                recipeProgress = 0;
            }
        }
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        inventory.deserializeNBT(compound.getCompoundTag("inventory"));
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(compound);
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (facing == EnumFacing.UP || facing == EnumFacing.DOWN);
    }
    
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && (facing == EnumFacing.UP || facing == EnumFacing.DOWN))
            return (T) inventory;
        return null;
    }
    
}
