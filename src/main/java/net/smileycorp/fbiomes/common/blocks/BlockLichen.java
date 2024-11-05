package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
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

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

public class BlockLichen extends BlockBush implements IGrowable, BlockProperties {
	
	protected static final AxisAlignedBB[] AABBS = {
			new AxisAlignedBB(0, 0.8125, 0, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, 1, 0.1875D, 1),
			new AxisAlignedBB(0, 0, 0.8125, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, 1, 1, 0.1875D),
			new AxisAlignedBB(0.8125, 0, 0, 1, 1, 1),
			new AxisAlignedBB(0, 0, 0, 0.1875D, 1, 1)
	};
	
	public static PropertyDirection FACING = PropertyDirection.create("facing");
	
	public BlockLichen() {
        setCreativeTab(FantasyBiomes.TAB);
		setUnlocalizedName(Constants.name("lichen"));
		setRegistryName(Constants.loc("lichen"));
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
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
        EnumFacing facing = Arrays.stream(EnumFacing.values()).filter(f -> f.getAxis() != state.getValue(FACING).getAxis())
				.collect(Collectors.toList()).get(rand.nextInt(4));
        BlockPos pos1 = pos.offset(facing);
        if (world.isAirBlock(pos1) && canBlockStay(world, pos1, state)) world.setBlockState(pos1, state, 18);
	}
	
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		return canPlaceBlockOnSide(world, pos, state.getValue(FACING));
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
