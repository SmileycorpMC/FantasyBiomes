package net.smileycorp.phantasiai.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.smileycorp.phantasiai.client.gui.GuiPixieWorkshop;
import net.smileycorp.phantasiai.common.blocks.tiles.TilePixieWorkshop;
import net.smileycorp.phantasiai.common.inventory.ContainerPixieTable;

import javax.annotation.Nullable;

public class GUIHandler implements IGuiHandler {
    
    //Credit: slava_110
    
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case 1:
                TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
                if(tile instanceof TilePixieWorkshop) {
                    return new GuiPixieWorkshop((TilePixieWorkshop) tile, new ContainerPixieTable((TilePixieWorkshop) tile, player.inventory));
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
                if(tile instanceof TilePixieWorkshop) {
                    return new ContainerPixieTable((TilePixieWorkshop) tile, player.inventory);
                }
        }
        return null;
    }
    
}
