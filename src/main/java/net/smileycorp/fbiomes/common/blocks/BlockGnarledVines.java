package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.BlockVine;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;

import java.util.Random;

public class BlockGnarledVines extends BlockVine implements IGrowable, BlockProperties {
	
    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");
    
	public BlockGnarledVines() {
		setCreativeTab(FantasyBiomes.TAB);
		setUnlocalizedName(Constants.name("gnarled_vine"));
		setRegistryName(Constants.loc("gnarled_vine"));
        setDefaultState(blockState.getBaseState().withProperty(UP, false).withProperty(NORTH, false).withProperty(EAST, false)
                .withProperty(SOUTH, false).withProperty(WEST, Boolean.valueOf(false)).withProperty(BOTTOM, false));
	}
    
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, UP, NORTH, EAST, SOUTH, WEST, BOTTOM);
    }
    
    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        if (!state.getValue(UP) && world.getBlockState(pos.down()).getBlock() != this) return state.withProperty(BOTTOM, true);
        return state;
    }
    
    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean b) {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(World world, Random random, BlockPos blockPos, IBlockState iBlockState) {
        return true;
    }
    
    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }
    
    @Override
    public void grow(World world, Random random, BlockPos pos, IBlockState state) {
        world.setBlockState(pos.down(), state);
    }
    
}
