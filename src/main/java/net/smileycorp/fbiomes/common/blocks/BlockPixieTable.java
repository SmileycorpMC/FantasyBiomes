package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.tile.TilePixieTable;
import net.smileycorp.fbiomes.common.blocks.tile.TilePixieTableChild;

public class BlockPixieTable extends BlockBase {
    
    //Credit: slava_110
    
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    
    public BlockPixieTable () {
        super("pixie_table", Constants.MODID, Material.WOOD, SoundType.WOOD, 2, 5,  "axe", 0, FantasyBiomes.TAB);
        setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.NORTH));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
    
    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for(BlockPos pos1 : BlockPos.getAllInBoxMutable(
                pos.add(-1, 0, -1),
                pos.add(1, 0, 1)
        )) {
            if(!worldIn.getBlockState(pos1).getBlock().isReplaceable(worldIn, pos1))
                return false;
        }
        return true;
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        for(BlockPos pos1 : BlockPos.getAllInBoxMutable(
                pos.add(-1, 0, -1),
                pos.add(1, 0, 1)
        )) {
            if(!pos1.equals(pos)) {
                worldIn.setBlockState(pos1, FBiomesBlocks.PIXIE_TABLE_CHILD.getDefaultState());
                TileEntity bec = worldIn.getTileEntity(pos1);
                
                if(bec instanceof TilePixieTableChild) {
                    ((TilePixieTableChild) bec).setMasterTilePos(pos);
                }
            }
        }
    }
    
    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
    }
    
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        super.breakBlock(worldIn, pos, state);
        for(BlockPos pos1 : BlockPos.getAllInBoxMutable(
                pos.add(-1, 0, -1),
                pos.add(1, 0, 1)
        )) {
            if(!pos1.equals(pos)) {
                worldIn.setBlockState(pos1, Blocks.AIR.getDefaultState());
            }
        }
    }
    
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }
    
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withProperty(FACING, mirror.mirror(state.getValue(FACING)));
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public EnumPushReaction getMobilityFlag(IBlockState p_149656_1_) {
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
            //playerIn.openGui(PixieTableMod.INSTANCE, 1, worldIn, pos.getX(), pos.getY(), pos.getZ());
        }
        
        return true;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 0b011));
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex() | 0b100;
    }
    
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }
    
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }
    
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePixieTable();
    }
    
}
