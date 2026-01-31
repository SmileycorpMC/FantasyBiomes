package net.smileycorp.phantasiai.common.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.Phantasiai;
import net.smileycorp.phantasiai.common.blocks.enums.EnumMultifaceDirection;
import net.smileycorp.phantasiai.common.items.PhantasiaiItems;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class BlockWebCover extends BlockWeb implements BlockProperties, IMultifaceBlock<BlockWebCover> {
	
	private final int ordinal;
	
	public BlockWebCover(int ordinal) {
		this.ordinal = ordinal;
        setCreativeTab(Phantasiai.TAB);
		setUnlocalizedName(Constants.name("web_cover"));
		setRegistryName(Constants.loc("web_cover_" + ordinal));
		setDefaultState(EnumMultifaceDirection.getDefaultState(blockState.getBaseState()));
		setSoundType(SoundType.PLANT);
	}
	
	@Override
	public boolean canBeReplacedByLeaves(IBlockState state, IBlockAccess world, BlockPos pos_) {
		return true;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, ArrayUtils.add(EnumMultifaceDirection.properties(), META));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(IMultifaceBlock.META, meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(IMultifaceBlock.META);
	}
	
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IBlockState state1 = getDefaultState();
		for (EnumFacing facing : IMultifaceBlock.getFacings(state)) state1 = state1.withProperty(EnumMultifaceDirection.getProperty(facing), true);
		return state1;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos from) {
		boolean changed = false;
		List<EnumFacing> facings = Lists.newArrayList(IMultifaceBlock.getFacings(state));
		Iterator<EnumFacing> iterator = facings.iterator();
		while (iterator.hasNext()) {
			EnumFacing facing = iterator.next();
			BlockPos pos1 = pos.offset(facing);
			IBlockState state1 = world.getBlockState(pos1);
			if (isSideSolid(state1, world, pos1, facing.getOpposite())) continue;
			iterator.remove();
			changed = true;
		}
		if (changed) world.setBlockState(pos, getBlockStateFromFacing(facings.toArray(new EnumFacing[facings.size()])));
	}
	
	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(PhantasiaiItems.WEB_COVER);
	}
	
	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(PhantasiaiItems.WEB_COVER, IMultifaceBlock.getFacings(state).length);
	}
	
	@Override
	public int quantityDropped(Random rand) {
		return 0;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		AxisAlignedBB aabb = IMultifaceBlock.EMPTY_AABB;
		for (EnumFacing facing : IMultifaceBlock.getFacings(state)) {
			if (aabb != IMultifaceBlock.EMPTY_AABB) return FULL_BLOCK_AABB;
			else aabb = EnumMultifaceDirection.getBoundingBox(facing);
		}
		return aabb;
	}
	
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end) {
		for (EnumFacing facing : IMultifaceBlock.getFacings(state)) {
			RayTraceResult result = rayTrace(pos, start, end, EnumMultifaceDirection.getBoundingBox(facing));
			if (result != null) return result;
		}
		return null;
	}
	
	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing facing) {
		return world.isSideSolid(pos.offset(facing.getOpposite()), facing);
	}
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, BlockPos pos) {
		return NULL_AABB;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	//spin
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing facing) {
		return world.setBlockState(pos, getBlockStateFromFacing(Arrays.stream(IMultifaceBlock.getFacings(world.getBlockState(pos)))
				.map(f -> f.rotateAround(facing.getAxis())).toArray(EnumFacing[]::new)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return getBlockStateFromFacing(Arrays.stream(IMultifaceBlock.getFacings(state)).map(f -> mirror.mirror(f)).toArray(EnumFacing[]::new));
	}
	
	@Override
	public int getOrdinal() {
		return ordinal;
	}
	
	@Override
	public List<BlockWebCover> getBlocks() {
		return PhantasiaiBlocks.WEB_COVER;
	}
	
	@Override
	public Class<BlockWebCover> multipartClass() {
		return BlockWebCover.class;
	}
	
	public String getName() {
		return "web_cover";
	}
	
	public static IBlockState getBlockState(int meta) {
		return meta == 0 ? Blocks.AIR.getDefaultState() : PhantasiaiBlocks.WEB_COVER.get(meta / 16).getDefaultState().withProperty(META, meta % 16);
	}
	
	public static IBlockState getBlockState(EnumFacing... facings) {
		return getBlockState(IMultifaceBlock.getMeta(facings));
	}
	
}
