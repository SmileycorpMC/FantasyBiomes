package net.smileycorp.fbiomes.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.smileycorp.fbiomes.client.gui.GuiPixieTable;
import net.smileycorp.fbiomes.common.inventory.ContainerPixieTable;
import net.smileycorp.fbiomes.common.blocks.tile.TilePixieTable;

import javax.annotation.Nullable;

public class GUIHandler implements IGuiHandler {
    
    //Credit: slava_110
    
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 1:
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                if(tile instanceof TilePixieTable) {
                    return new GuiPixieTable(new ContainerPixieTable((TilePixieTable) tile, player.inventory));
                }
        }
        return null;
    }
    
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 1:
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                if(tile instanceof TilePixieTable) {
                    return new ContainerPixieTable((TilePixieTable) tile, player.inventory);
                }
        }
        return null;
    }
    
}
