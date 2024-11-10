package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;
import net.smileycorp.fbiomes.common.blocks.enums.EnumWoodType;

public class BlockTwistedGnarlwillow extends BlockBase {
    
    public static final PropertyDirection FACING = PropertyDirection.create("facing", dir -> dir.getAxis() != EnumFacing.Axis.Y);
    
    public BlockTwistedGnarlwillow() {
        super("twisted_gnarlwillow", Constants.MODID, Material.WOOD, SoundType.WOOD, 2f, 5f, "axe", 0, FantasyBiomes.TAB);
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta % 4 + 2]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() - 2;
    }
    
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rotation) {
        return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
    }
    
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase entity, EnumHand hand) {
        return getDefaultState().withProperty(FACING, entity.getHorizontalFacing().getOpposite());
    }
    
    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }
    
    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        drops.add(FBiomesBlocks.WOOD.getLogStack(EnumWoodType.GNARLWILLOW, 1));
    }
    
}
