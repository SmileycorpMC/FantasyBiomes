package net.smileycorp.phantasiai.common.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class WrappedItemStackHandler implements IItemHandler, IItemHandlerModifiable, INBTSerializable<NBTTagCompound> {

    protected final ItemStackHandler inventory;

    public WrappedItemStackHandler(ItemStackHandler inventory) {
        this.inventory = inventory;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return inventory.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        inventory.deserializeNBT(nbt);
    }

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        inventory.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return inventory.getSlots();
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return inventory.insertItem(slot, stack, simulate);
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return inventory.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return inventory.getSlotLimit(slot);
    }

}
