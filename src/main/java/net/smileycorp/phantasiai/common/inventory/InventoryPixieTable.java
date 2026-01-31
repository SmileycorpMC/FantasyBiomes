package net.smileycorp.phantasiai.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.smileycorp.atlas.api.util.RecipeUtils;
import net.smileycorp.phantasiai.common.blocks.tiles.TilePixieWorkshop;
import net.smileycorp.phantasiai.common.recipe.PixieRecipeManager;

import javax.annotation.Nonnull;

/**
 * Pixie table inventory
 * Slots 0-8 - Grid
 * Slot 9-11 - Result
 * Slot 12-18 - Food
 */
public class InventoryPixieTable extends ItemStackHandler {

    private final TilePixieWorkshop tile;
    private final CraftingWrapper wrapper = new CraftingWrapper(this);
    private final WrappedItemStackHandler capabilityHandler = new WrappedItemStackHandler(this) {

        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (slot > 8 && slot < 12) return stack;
            if (slot > 11 && slot < 19) return PixieRecipeManager.isPixieFood(stack) ? super.insertItem(slot, stack, simulate) : stack;
            if (tile.isCrafting()) return inventory.getStackInSlot(slot).isEmpty() ? stack : super.insertItem(slot, stack, simulate);
            return RecipeUtils.compareItemStacks(tile.getLastRecipe().get(slot), stack, true) ?
                    super.insertItem(slot, stack, simulate) : stack;
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot < 9 || slot > 11) return ItemStack.EMPTY;
            return super.extractItem(slot, amount, simulate);
        }

    };

    public InventoryPixieTable(TilePixieWorkshop tile) {
        this.tile = tile;
        setSize(19);
    }

    public WrappedItemStackHandler getCapabilityWrapper() {
        return capabilityHandler;
    }

    public InventoryCrafting getCraftingWrapper() {
        return wrapper;
    }

    public void markDirty() {
        tile.markDirty();
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot >= 0 && slot <= 8)
            return true; //TODO
        else if(slot >= 12 && slot < 19)
            return PixieRecipeManager.isPixieFood(stack);
        else
            return false;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (slot >= 0 && slot <= 8) tile.tryFindingRecipe();
    }

    private static class CraftingWrapper extends InventoryCrafting {

        private final InventoryPixieTable inventory;

        public CraftingWrapper(InventoryPixieTable inventory) {
            super(new Container() {
                @Override
                public boolean canInteractWith(EntityPlayer playerIn) {
                    return true;
                }
            }, 3, 3);
            this.inventory = inventory;
        }

        @Override
        public ItemStack getStackInSlot(int index) {
            return (index >= getSizeInventory()) ? ItemStack.EMPTY : inventory.getStackInSlot(index);
        }

        @Override
        public ItemStack getStackInRowAndColumn(int column, int row) {
            return (row < 0 || row >= 3 || column < 0 || column >= 3) ? ItemStack.EMPTY : getStackInSlot(row * 3 + column);
        }

        @Override
        public void setInventorySlotContents(int index, ItemStack stack) {
            inventory.setStackInSlot(index, stack);
        }

        @Override
        public void markDirty() {
            inventory.markDirty();
        }

    }
}
