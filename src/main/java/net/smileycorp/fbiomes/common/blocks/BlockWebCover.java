package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.BlockWeb;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;

import java.util.Random;

public class BlockWebCover extends BlockWeb implements BlockProperties {
	
	protected static final AxisAlignedBB[] AABBS = {
			new AxisAlignedBB(0, 0.8125, 0, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, 1, 0.1875D, 1),
			new AxisAlignedBB(0, 0, 0.8125, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, 1, 1, 0.1875D),
			new AxisAlignedBB(0.8125, 0, 0, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, 0.1875D, 1, 1)
	};
	
	public static PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockWebCover() {
        setCreativeTab(FantasyBiomes.TAB);
		setUnlocalizedName(Constants.name("web_cover"));
		setRegistryName(Constants.loc("web_cover"));
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
		setSoundType(SoundType.PLANT);
		setTickRandomly(true);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABBS[state.getValue(FACING).ordinal()];
	}
	 
	@Override
	public int quantityDropped(Random rand) {
        return 0;
    }
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side) {
		return world.getBlockState(pos.offset(side.getOpposite())).isSideSolid(world, pos, side);
	}
	
	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos_) {
		return true;
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(FACING, facing);
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	 
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
	}
    
    @Override
	public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).ordinal();
    }
	
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	
}
