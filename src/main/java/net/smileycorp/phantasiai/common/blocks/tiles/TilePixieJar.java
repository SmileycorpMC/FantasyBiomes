package net.smileycorp.phantasiai.common.blocks.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.smileycorp.phantasiai.common.entities.PixieData;

public class TilePixieJar extends TileEntity {

    private PixieData pixie;

    public boolean hasPixie() {
        return pixie != null;
    }

    public PixieData getPixie() {
        return pixie;
    }

    public void setPixie(PixieData pixie) {
        this.pixie = pixie;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        pixie = PixieData.fromNbt(nbt.getCompoundTag("pixie"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("pixie", pixie.toNbt());
        return super.writeToNBT(nbt);
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        pixie = PixieData.fromNbt(nbt.getCompoundTag("pixie"));
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setTag("pixie", pixie.toNbt());
        return nbt;
    }

}
