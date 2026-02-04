package net.smileycorp.phantasiai.common.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.smileycorp.phantasiai.common.entities.PixieData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface PixieStorage<T extends ICapabilityProvider> {

    T getContainer();

    PixieData getPixie();

    void setPixie(PixieData data);

    class PixieStorageItemStack implements PixieStorage<ItemStack>, ICapabilityProvider {

        private ItemStack stack;

        public PixieStorageItemStack(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public ItemStack getContainer() {
            return stack;
        }

        @Override
        public PixieData getPixie() {
            if (!stack.hasTagCompound()) return PixieData.getDefault(stack.getMetadata());
            NBTTagCompound nbt = stack.getTagCompound();
            if (!nbt.hasKey("pixie")) return PixieData.getDefault(stack.getMetadata());
            PixieData pixie = PixieData.fromNbt(nbt.getCompoundTag("pixie"));
            pixie.setVariant(PixieData.Variant.get((byte) stack.getMetadata()));
            if (stack.hasDisplayName()) pixie.setName(stack.getDisplayName());
            nbt.setTag("pixie", pixie.toNbt());
            return pixie;
        }

        @Override
        public void setPixie(PixieData pixie) {
            stack.setItemDamage(pixie.getVariant().ordinal());
            if (pixie.hasName()) stack.setStackDisplayName(pixie.getName());
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setTag("pixie", pixie.toNbt());
        }

        @Override
        public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
            return capability == PhantasiaiCapabilities.PIXIE_STORAGE;
        }

        @Nullable
        @Override
        public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
            return capability == PhantasiaiCapabilities.PIXIE_STORAGE ? PhantasiaiCapabilities.PIXIE_STORAGE.cast(this) : null;
        }

    }

    Capability.IStorage<PixieStorage> STORAGE = new Capability.IStorage<PixieStorage>() {
        @Nullable
        @Override
        public NBTBase writeNBT(Capability<PixieStorage> capability, PixieStorage instance, EnumFacing side) {
            return null;
        }

        @Override
        public void readNBT(Capability<PixieStorage> capability, PixieStorage instance, EnumFacing side, NBTBase nbt) {}

    };

}
