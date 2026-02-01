package net.smileycorp.phantasiai.common.world.gen.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockVine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.smileycorp.phantasiai.common.blocks.BlockTwistedGnarlwillow;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;
import net.smileycorp.phantasiai.common.blocks.enums.EnumWoodType;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class WorldGenGnarlwillow extends WorldGenAbstractTree {
	
	protected static final IBlockState LOG = PhantasiaiBlocks.WOOD.getLogState(EnumWoodType.GNARLWILLOW, BlockLog.EnumAxis.Y);
	protected static final IBlockState BARK = PhantasiaiBlocks.WOOD.getLogState(EnumWoodType.GNARLWILLOW, BlockLog.EnumAxis.NONE);
	protected static final IBlockState LEAVES = PhantasiaiBlocks.WOOD.getLeavesState(EnumWoodType.GNARLWILLOW)
			.withProperty(BlockLeaves.DECAYABLE, false).withProperty(BlockLeaves.CHECK_DECAY, false);
	
	protected final Map<BlockPos, EnumFacing> vines = Maps.newHashMap();
	
	public WorldGenGnarlwillow(boolean notify) {
		super(notify);
	}
	
	public boolean canGenerate(World world, BlockPos pos) {
		BlockPos ground = pos.down();
		IBlockState soil = world.getBlockState(ground);
		return soil.getBlock().canSustainPlant(soil, world, ground, EnumFacing.UP, (BlockSapling) Blocks.SAPLING);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		if (!canGenerate(world, pos)) return false;
		int h = rand.nextInt(3) + 5;
		int face = rand.nextInt(3) + 1;
		List<EnumFacing> facings = Lists.newArrayList(EnumFacing.HORIZONTALS);
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos);
		for (int i = 0; i <= h; i++) {
			setBlockAndNotifyAdequately(world, mutable, i == face && rand.nextInt(3) == 0 ?
					PhantasiaiBlocks.TWISTED_GNARLWILLOW.getDefaultState().withProperty(BlockTwistedGnarlwillow.FACING, EnumFacing.HORIZONTALS[rand.nextInt(4)]) : LOG);
			if (i > 3 && rand.nextInt(5) < facings.size() - 1 &! facings.isEmpty()) {
				int x = rand.nextInt(facings.size());
				generateBranch(world, rand, mutable, facings.get(x), h - i);
				facings.remove(x);
			}
			mutable.move(EnumFacing.UP);
		}
		generateLeaves(world, mutable);
		for (Map.Entry<BlockPos, EnumFacing> entry : vines.entrySet()) {
			mutable.setPos(entry.getKey());
			for (int j = 1; j >= -world.rand.nextInt(3) - 2; j--) {
				if (!world.isAirBlock(mutable)) break;
				setBlockAndNotifyAdequately(world, mutable, PhantasiaiBlocks.GNARLED_VINES.getDefaultState()
						.withProperty(BlockVine.getPropertyFor(entry.getValue()), true));
				mutable.move(EnumFacing.DOWN);
			}
		}
		vines.clear();
		return true;
	}
	
	private void generateBranch(World world, Random rand, BlockPos pos, EnumFacing dir, int h) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos).move(dir);
		EnumFacing offset = rand.nextBoolean() ? dir.rotateY() : dir.rotateY().getOpposite();
		for (int i = 0; i <= h; i++) {;
			setBlockAndNotifyAdequately(world, mutable, BARK);
			if (rand.nextBoolean()) mutable.move(dir);
			mutable.move(EnumFacing.UP);
			if (rand.nextInt(3) == 0) mutable.move(offset);
		}
		generateLeaves(world, mutable);
	}
	
	private void generateLeaves(World world, BlockPos pos) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos);
		for (int i = -3; i <= 3; i++) for (int k = -3; k <= 3; k++) {
			int absI = Math.abs(i);
			int absK = Math.abs(k);
			if (absI < 3 && absK < 3) for (int j = 0; j < 4; j++) {
				if (j == 2 && absI + absK == 4) continue;
				if (j == 3 && absI + absK > 2) continue;
				mutable.setPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k);
				IBlockState state = world.getBlockState(mutable);
				if (state.getBlock().canBeReplacedByLeaves(state, world, mutable)) setBlockAndNotifyAdequately(world, mutable, LEAVES);
			} else if (absI != absK) {
				mutable.setPos(pos.getX() + i, pos.getY() + 1, pos.getZ() + k);
				if (!world.isAirBlock(mutable)) continue;
				EnumFacing facing = EnumFacing.NORTH;
				if (k == -3) facing = EnumFacing.SOUTH;
				else if (i == -3) facing = EnumFacing.EAST;
				else if (i == 3) facing = EnumFacing.WEST;
				vines.put(mutable.toImmutable(), facing);
			}
		}
	}
	
}	
