package net.smileycorp.phantasiai.common.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.phantasiai.common.Constants;
import net.smileycorp.phantasiai.common.Phantasiai;
import net.smileycorp.phantasiai.common.blocks.enums.EnumMultifaceDirection;
import net.smileycorp.phantasiai.common.items.PhantasiaiItems;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nonnull;
import java.util.*;

public class BlockLichen extends BlockBush implements BlockProperties, IMultifaceBlock<BlockLichen>, IShearable, IGrowable {
	
	private final int ordinal;
	//stores the position and state to set in bonemeal logic, so we don't have to re-check
	private Tuple<BlockPos, IBlockState> growPos = null;
	
	public BlockLichen(int ordinal) {
		this.ordinal = ordinal;
        setCreativeTab(Phantasiai.TAB);
		setUnlocalizedName(Constants.name("lichen"));
		setRegistryName(Constants.loc("lichen_" + ordinal));
		setDefaultState(EnumMultifaceDirection.getDefaultState(blockState.getBaseState()));
		setSoundType(SoundType.PLANT);
	}
	
	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return growPos != null;
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		EnumFacing[] facings = IMultifaceBlock.getFacings(state);
		for (EnumFacing facing : EnumFacing.values()) {
			if (IMultifaceBlock.hasFacing(state, facing)) continue;
			if (!world.isSideSolid(pos.offset(facing), facing.getOpposite())) continue;
			if (!isClient) growPos = new Tuple<>(pos, getBlockStateFromFacing(ArrayUtils.add(facings, facing)));
			return true;
		}
		for (EnumFacing facing : facings) {
			for (EnumFacing dir : EnumFacing.values()) {
				if (dir.getAxis() == facing.getAxis()) continue;
				BlockPos pos1 = pos.offset(dir);
				IBlockState state1 = world.getBlockState(pos1);
				if (state1.getMaterial() == Material.AIR) {
					if (!world.isSideSolid(pos1.offset(facing), facing.getOpposite())) continue;
					if (!isClient) growPos = new Tuple<>(pos1, getBlockStateFromFacing(facing));
					return true;
				}
				if (state1.getBlock() instanceof BlockLichen) {
					if (IMultifaceBlock.hasFacing(state1, facing)) continue;
					if (!world.isSideSolid(pos1.offset(facing), facing.getOpposite())) continue;
					if (!isClient) growPos = new Tuple<>(pos1, getBlockStateFromFacing(ArrayUtils.add(IMultifaceBlock.getFacings(state1), facing)));
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		//in case mods try to force the block to grow before the field is set
		if (growPos == null) if (!canGrow(world, pos, state, world.isRemote)) return;
		world.setBlockState(growPos.getFirst(), growPos.getSecond());
		growPos = null;
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
		if (changed) world.setBlockState(pos, getBlockState(facings.toArray(new EnumFacing[facings.size()])));
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(PhantasiaiItems.LICHEN);
	}

	@Override
	public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		return true;
	}
	
	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state) {
		return new ItemStack(PhantasiaiItems.LICHEN, IMultifaceBlock.getFacings(state).length);
	}

	@Override
	public boolean isShearable(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos) {
		return true;
	}

	@Nonnull
	@Override
	public List<ItemStack> onSheared(@Nonnull ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return Lists.newArrayList(getSilkTouchDrop(world.getBlockState(pos)));
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
		return BlockRenderLayer.TRANSLUCENT;
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
		return world.setBlockState(pos, getBlockState(Arrays.stream(IMultifaceBlock.getFacings(world.getBlockState(pos)))
				.map(f -> f.rotateAround(facing.getAxis())).toArray(EnumFacing[]::new)));
	}
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		return getBlockState(Arrays.stream(IMultifaceBlock.getFacings(state)).map(f -> mirror.mirror(f)).toArray(EnumFacing[]::new));
	}
	
	@Override
	public int getOrdinal() {
		return ordinal;
	}
	
	@Override
	public List<BlockLichen> getBlocks() {
		return PhantasiaiBlocks.LICHEN;
	}
	
	@Override
	public Class<BlockLichen> multipartClass() {
		return BlockLichen.class;
	}
	
	public String getName() {
		return "lichen";
	}
	
	public static IBlockState getBlockState(int meta) {
		return meta == 0 ? Blocks.AIR.getDefaultState() : PhantasiaiBlocks.LICHEN.get(meta / 16).getDefaultState().withProperty(META, meta % 16);
	}
	
	public static IBlockState getBlockState(EnumFacing... facings) {
		return getBlockState(IMultifaceBlock.getMeta(facings));
	}

}
