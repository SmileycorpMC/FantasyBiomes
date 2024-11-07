package net.smileycorp.fbiomes.common.world.gen.tree;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.fbiomes.common.blocks.BlockFBMushroom;
import net.smileycorp.fbiomes.common.blocks.BlockLichen;
import net.smileycorp.fbiomes.common.blocks.FBiomesBlocks;
import net.smileycorp.fbiomes.common.blocks.enums.EnumWoodType;

import java.util.Random;

public class WorldGenOrantikkuTree extends WorldGenAbstractTree {
	
	protected static final IBlockState LOG = FBiomesBlocks.WOOD.getLogState(EnumWoodType.ORANTIKKU, BlockLog.EnumAxis.Y);
	protected static final IBlockState BARK = FBiomesBlocks.WOOD.getLogState(EnumWoodType.ORANTIKKU, BlockLog.EnumAxis.NONE);
	protected static final IBlockState LEAVES = FBiomesBlocks.WOOD.getLeavesState(EnumWoodType.ORANTIKKU)
			.withProperty(BlockLeaves.DECAYABLE, true).withProperty(BlockLeaves.CHECK_DECAY, false);
	
	protected final boolean natural;
	
	public WorldGenOrantikkuTree(boolean notify, boolean natural) {
		super(notify);
		this.natural = natural;
	}
	
	public boolean canGenerate(World world, BlockPos pos) {
		BlockPos ground = pos.down();
		IBlockState soil = world.getBlockState(ground);
		if (!soil.getBlock().canSustainPlant(soil, world, ground, EnumFacing.UP, (BlockSapling) Blocks.SAPLING)) return false;
		for (EnumFacing facing : EnumFacing.HORIZONTALS) {
			if (blocksPlacement(world, pos.offset(facing))) return false;
			if (blocksPlacement(world, pos.offset(facing).up())) return false;
		}
		return true;
	}
	
	private boolean blocksPlacement(World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return !state.getBlock().canBeReplacedByLeaves(state, world, pos);
	}
	
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		if (!natural &! canGenerate(world, pos)) return false;
		int h = rand.nextInt(5) + 16;
		//trunk
		for (int i = 0; i < h; i++) if (blocksPlacement(world, pos.up(i))) {
			if (h > 16) {
				h = 13;
				break;
			}
			return false;
		}
		for (int i = 0; i < h; i++) setBlock(world, rand, pos.up(i), LOG);
		EnumFacing stump = DirectionUtils.getRandomDirectionXZ(rand);
		setBlockAndNotifyAdequately(world, pos.up(rand.nextInt(3) + 7).offset(stump),
				FBiomesBlocks.WOOD.getLogState(EnumWoodType.ORANTIKKU, BlockLog.EnumAxis.fromFacingAxis(stump.getAxis())));
		//roots
		for (int i = 0; i < 4; i++) generateRoot(world, rand, pos, DirectionUtils.getXZDirection(i));
		//branches
		setBlockAndNotifyAdequately(world, pos.up(h), BARK);
		generateLeaves(world, pos.up(h - 1), 7);
		int branches = rand.nextInt(4) + 5;
		for (int i = 0; i < branches; i++) generateBranch(world, rand, new BlockPos.MutableBlockPos(pos.up(h)), rand.nextInt(5) + 7,
					DirectionUtils.getDirectionVecXZ(2f * Math.PI * (float) i / (float) branches).addVector((rand.nextFloat() - 0.5f) * 0.2f,
							0.1f, (rand.nextFloat() - 0.5f) * 0.2f));
		return true;
	}

	public void generateRoot(World world, Random rand, BlockPos pos, EnumFacing dir) {
		BlockPos root = pos.offset(dir);
		setBlock(world, rand, root, BARK);
		setBlock(world, rand, root.up(), BARK);
		int i = 1;
		IBlockState state = world.getBlockState(root.down(i));
		while (state.getBlock().canBeReplacedByLeaves(state, world, root.down(i))) {
			setBlock(world, rand, root.down(i), BARK);
			state = world.getBlockState(root.down(i++));
		}
		//randomly spawn big roots
		if (rand.nextBoolean()) {
			setBlock(world, rand, root.up(2), BARK);
			for (EnumFacing dir1 : EnumFacing.HORIZONTALS) if (dir1 != dir.getOpposite()) {
				setBlock(world, rand, root.offset(dir1), BARK);
				i = 1;
				while (state.getBlock().canBeReplacedByLeaves(state, world, root.down(i))) {
					setBlock(world, rand, root.down(i), BARK);
					state = world.getBlockState(root.down(i++));
				}
			}
		}
	}

	private void setBlock(World world, Random rand, BlockPos pos, IBlockState state) {
		setBlockAndNotifyAdequately(world, pos, state);
		if (!natural) return;
		for (EnumFacing facing : EnumFacing.HORIZONTALS) if (rand.nextBoolean() && world.isAirBlock(pos.offset(facing)))
			setBlockAndNotifyAdequately(world, pos.offset(facing), FBiomesBlocks.LICHEN.getDefaultState().withProperty(BlockLichen.FACING, facing));
		if (rand.nextFloat() < 0.1775) {
			EnumFacing facing = DirectionUtils.getRandomDirectionXZ(rand);
			BlockPos facePos = pos.offset(facing);
			IBlockState shroom = (rand.nextFloat() < 0.4 ? FBiomesBlocks.glowshrooms[rand.nextInt(FBiomesBlocks.glowshrooms.length)]:
				FBiomesBlocks.shrooms[rand.nextInt(FBiomesBlocks.shrooms.length)]).getDefaultState();
			if (world.isAirBlock(facePos)) setBlockAndNotifyAdequately(world, facePos, shroom.withProperty(BlockFBMushroom.FACING, facing));
		}
	}

	public void generateBranch(World world, Random rand, BlockPos pos, int length, Vec3d dir) {
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
		Vec3d bpos = DirectionUtils.centerOf(pos);
		for (int i = 0; i < length; i++) {
			bpos = bpos.add(dir.normalize());
			mutable.setPos(bpos.x, bpos.y, bpos.z);
			if (world.isAirBlock(mutable) || world.getBlockState(mutable) == LEAVES) setBlockAndNotifyAdequately(world, mutable, BARK);
			generateLeaves(world, mutable, i < 2 ? 6 : 5);
			dir = dir.addVector(0.5 * (rand.nextFloat() - 0.5), 0.5 * (rand.nextFloat() - 0.5), 0.5 * (rand.nextFloat() - 0.5));
		}
	}
	
	private void generateLeaves(World world, BlockPos pos, int r) {
		for (int i = -r; i <= r; i++) for (int j = -r; j <= r; j++) for (int k = -r; k <= r; k++) {
			if (i * i + j * j + k * k >= r * r) continue;
			BlockPos newpos = pos.north(i).up(j).east(k);
			IBlockState state = world.getBlockState(newpos);
			if (state == LEAVES) continue;
			if (!state.getBlock().canBeReplacedByLeaves(state, world, newpos)) continue;
			setBlockAndNotifyAdequately(world, newpos, LEAVES);
		}
	}
	
}	
