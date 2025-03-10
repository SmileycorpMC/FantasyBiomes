package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.tile.TilePixieTableChild;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPixieTableChild extends BlockBase {
    
    //Credit: slava_110
    
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    
    public BlockPixieTableChild() {
        super("pixie_table_child", Constants.MODID, Material.WOOD, SoundType.WOOD, 2, 5,  "axe", 0, FantasyBiomes.TAB);
        disableStats();
        translucent = true;
    }
    
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        return false;
    }
    
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(FBiomesBlocks.PIXIE_TABLE);
    }
    
    @Override
    public boolean addDestroyEffects(World world, BlockPos pos, ParticleManager manager) {
        return true;
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        BlockPos masterPos = getMasterPos(worldIn, pos);
        
        if(masterPos != null) {
            worldIn.setBlockState(masterPos, Blocks.AIR.getDefaultState());
        }
        
        super.breakBlock(worldIn, pos, state);
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState state) {
        return EnumPushReaction.BLOCK;
    }
    
    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }
    
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if(!worldIn.isRemote) {
            BlockPos masterPos = getMasterPos(worldIn, pos);
            if(masterPos != null)
                playerIn.openGui(FantasyBiomes.instance, 1, worldIn, masterPos.getX(), masterPos.getY(), masterPos.getZ());
        }
        
        return true;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePixieTableChild();
    }
    
    @Nullable
    private static BlockPos getMasterPos(IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if(tile instanceof TilePixieTableChild) {
            return ((TilePixieTableChild) tile).getMasterTilePos();
        }
        return null;
    }
    
    @Override
    public boolean usesCustomItemHandler() {
        return true;
    }
    
}
