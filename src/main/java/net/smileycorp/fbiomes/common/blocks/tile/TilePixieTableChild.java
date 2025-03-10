package net.smileycorp.fbiomes.common.blocks.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TilePixieTableChild extends TileEntity {
    
    //Credit: slava_110
    
    private BlockPos masterTilePos;
    
    public BlockPos getMasterTilePos() {
        return masterTilePos;
    }
    
    public void setMasterTilePos(BlockPos masterTilePos) {
        this.masterTilePos = masterTilePos;
    }
    
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        masterTilePos = NBTUtil.getPosFromTag(compound.getCompoundTag("masterTilePos"));
    }
    
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setTag("masterTilePos", NBTUtil.createPosTag(masterTilePos));
        return compound;
    }
    
}
