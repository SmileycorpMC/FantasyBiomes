package net.smileycorp.fbiomes.common.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.smileycorp.fbiomes.common.blocks.tiles.TileMysticStump;

import javax.annotation.Nonnull;

/**
 * Pixie table inventory
 * Slots 0-8 - Grid
 * Slot 9-11 - Result
 * Slot 12-13 - Pixie jar slot
 * Slots 14-20 - Food
 */
public class InventoryPixieTable extends ItemStackHandler {
    
    private final TileMysticStump tile;
    private final InventoryWrapper wrapper = new InventoryWrapper(this);
    private boolean dirty = false;

    public InventoryPixieTable(TileMysticStump tile) {
        this.tile = tile;
        setSize(21);
    }

    public InventoryCrafting getCraftingWrapper() {
        return wrapper;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        dirty = true;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        if(slot >= 0 && slot <= 8)
            return true; //TODO
        else if(slot == 12)
            return stack.getItem() == Items.MUSHROOM_STEW;
        else if(slot >= 14 && slot < 19)
            return stack.getItem() instanceof ItemFood;
        else
            return false;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < 9 || slot > 11) return ItemStack.EMPTY;
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if(slot >= 0 && slot <= 8) tile.tryFindingRecipe();
    }

    private static class InventoryWrapper extends InventoryCrafting {
        private final InventoryPixieTable inventory;

        public InventoryWrapper(InventoryPixieTable inventory) {
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
        public ItemStack getStackInRowAndColumn(int row, int column) {
            return (row < 0 || row > 3 || column < 0 || column > 3) ? ItemStack.EMPTY : getStackInSlot(row * 3 + column);
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
