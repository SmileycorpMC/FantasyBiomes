package net.smileycorp.phantasiai.common.blocks;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.Phantasiai;

import java.util.Optional;

public class BlockShelfMushroom extends BlockBush implements BlockProperties {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", dir -> dir.getAxis().isHorizontal());
	private static final AxisAlignedBB[] AABBS = {
			new AxisAlignedBB(0.3, 0.2, 0.6, 0.7, 0.9, 1),
			new AxisAlignedBB(0.3, 0.2, 0, 0.7, 0.9, 0.4),
			new AxisAlignedBB(0.7, 0.2, 0.3, 1, 0.9, 0.7),
			new AxisAlignedBB(0, 0.2, 0.3, 0.4, 0.9, 0.7)
	};

	public BlockShelfMushroom() {
		super(Material.PLANTS);
		String name =  "shelf_mushroom";
		setCreativeTab(Phantasiai.TAB);
		setSoundType(SoundType.PLANT);
		setUnlocalizedName(Constants.name(name));
		setRegistryName(Constants.loc(name.toLowerCase()));
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(placer.getHeldItem(hand).getMetadata()).withProperty(FACING, getFacing(world, pos, facing));
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABBS[state.getValue(FACING).ordinal() - 2];
	}
	
	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Plains;
	}
	
	private EnumFacing getFacing(World world, BlockPos pos, EnumFacing facing) {
		if (canFacingSustain(world, pos, facing)) return facing;
		for (EnumFacing facing0 : FACING.getAllowedValues()) if (facing0 != facing && canFacingSustain(world, pos, facing0)) return facing0;
		return EnumFacing.UP;
	}

	private boolean canFacingSustain(World world, BlockPos pos, EnumFacing facing) {
		if (facing == EnumFacing.DOWN) return false;
		return canSustain(world, pos, facing);
	}
	
	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos_) {
		return true;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta % 5 + 1]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).ordinal() - 1;
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos){
        for (EnumFacing facing : FACING.getAllowedValues()) if (canSustain(world, pos, facing)) return true;
        return false;
    }
		
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		return canSustain(world, pos, state.getValue(FACING));
    }
	 
	protected boolean canSustain(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		IBlockState state = world.getBlockState(pos.offset(facing.getOpposite()));
		return (state.isFullCube() && state.getBlock() instanceof BlockLog) ||
				state.getBlock().canSustainPlant(state, world, pos.offset(facing.getOpposite()), facing, this);
	}
	
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		if (world instanceof World) if (!canBlockStay((World) world, pos, world.getBlockState(pos))) ((World) world).setBlockToAir(pos);
	 }
	
	private Optional<BlockPos> getHugeCenter(World world, BlockPos pos, IBlockState state) {
		if (isCenter(world, pos)) return Optional.of(pos);
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			BlockPos pos1 = pos.offset(facing);
			if (state.equals(world.getBlockState(pos1)) && isCenter(world, pos1)) return Optional.of(pos1);
		}
		return Optional.empty();
	}
	
	private boolean isCenter(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		for (EnumFacing facing : EnumFacing.HORIZONTALS) if (!state.equals(world.getBlockState(pos.offset(facing)))) return false;
		return true;
	}
	
}
