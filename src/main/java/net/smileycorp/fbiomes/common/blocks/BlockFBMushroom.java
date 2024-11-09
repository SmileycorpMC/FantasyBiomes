package net.smileycorp.fbiomes.common.blocks;

import net.minecraft.block.*;
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
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.fbiomes.common.Constants;
import net.smileycorp.fbiomes.common.FantasyBiomes;

import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

public class BlockFBMushroom extends BlockBush implements IGrowable, BlockProperties {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing", dir -> dir != EnumFacing.DOWN);
	private final Supplier<WorldGenerator> bigShroom;
	private final Supplier<WorldGenerator> hugeShroom;
	
	public BlockFBMushroom(String name, float light, Supplier<WorldGenerator> bigShroom, Supplier<WorldGenerator> hugeShroom) {
		super(Material.PLANTS);
		setLightLevel(light);
		setCreativeTab(FantasyBiomes.TAB);
		setSoundType(SoundType.PLANT);
		setUnlocalizedName(Constants.name(name));
		setRegistryName(Constants.loc(name.toLowerCase()));
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
		this.bigShroom = bigShroom;
		this.hugeShroom = hugeShroom;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(placer.getHeldItem(hand).getMetadata()).withProperty(FACING, getFacing(world, pos, facing));
	}
	
	private EnumFacing getFacing(World world, BlockPos pos, EnumFacing facing) {
		if (canFacingSustain(world, pos, facing)) return facing;
		for (EnumFacing facing0 : FACING.getAllowedValues()) if (facing0 != facing && canFacingSustain(world, pos, facing0)) return facing0;
		return EnumFacing.UP;
	}

	private boolean canFacingSustain(World world, BlockPos pos, EnumFacing facing) {
		if (facing == EnumFacing.DOWN) return false;
		IBlockState state = world.getBlockState(pos.offset(facing, -1));
		Block block = state.getBlock();
		if (state.isFullBlock() && (block instanceof BlockLog || state.getMaterial() == Material.GRASS || state.getMaterial() == Material.GROUND)) return true;
		for (int id : OreDictionary.getOreIDs(new ItemStack(block))) if (OreDictionary.getOreName(id).equals("stone")) return true;
		return false;
	}
	
	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos_) {
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BUSH_AABB;
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
        IBlockState soil = world.getBlockState(pos.down());
        for (EnumFacing facing : FACING.getAllowedValues()) {
        	IBlockState state = world.getBlockState(pos.offset(facing.getOpposite()));
        	if (state.isFullCube()&&(state.getBlock() instanceof BlockLog || state.getMaterial() == Material.GROUND
					|| state.getMaterial() == Material.GRASS)) return true;
        }
        return soil.isFullBlock() && (soil.getMaterial() == Material.GRASS || soil.getMaterial() == Material.GROUND);
    }
	
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		IBlockState soil = world.getBlockState(pos.down());
        if (state.getBlock() == this) return canPlaceBlockAt(world, pos);
        return soil.isFullBlock() && (soil.getMaterial() == Material.GRASS || soil.getMaterial() == Material.GROUND);
    }
	 
	@Override
	public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor){
		IBlockState soil = world.getBlockState(pos.down());
		EnumFacing facing = world.getBlockState(pos).getValue(FACING);
    	if ((world.getBlockState(pos.offset(facing.getOpposite())).getBlock() instanceof BlockLog)) return;
		if (!(facing == EnumFacing.UP && (soil.isFullBlock() && (soil.getMaterial() == Material.GRASS || soil.getMaterial() == Material.GROUND))))
			if (world instanceof World) ((World) world).setBlockToAir(pos);
	 }
	
	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean remote) {
		return bigShroom != null && state.getValue(FACING) == EnumFacing.UP;
	}
	
	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos blockPos, IBlockState state) {
		return rand.nextFloat() < 0.4;
	}
	
	@Override
	public void grow(World world, Random random, BlockPos pos, IBlockState state) {
		if (hugeShroom != null) {
			Optional<BlockPos> center = getHugeCenter(world, pos, state);
			if (center.isPresent()) {
				hugeShroom.get().generate(world, random, center.get());
				return;
			}
		}
		world.setBlockToAir(pos);
		if (bigShroom != null) if (!bigShroom.get().generate(world, world.rand, pos)) world.setBlockState(pos, state, 2);
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
