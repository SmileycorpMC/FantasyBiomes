package net.smileycorp.phantasiai.common.world.gen.tree;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBirchTree;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.phantasiai.common.blocks.BlockFBMushroom;
import net.smileycorp.phantasiai.common.blocks.BlockLichen;
import net.smileycorp.phantasiai.common.blocks.PhantasiaiBlocks;
import net.smileycorp.phantasiai.common.blocks.enums.EnumVanillaWoodType;

import java.util.Random;

public class WorldGenGoldenBirchTree extends WorldGenBirchTree {
    
    private static final IBlockState LEAVES = PhantasiaiBlocks.VANILLA_LEAVES.getDefaultState()
            .withProperty(PhantasiaiBlocks.VANILLA_LEAVES.getVariantProperty(), EnumVanillaWoodType.GOLD_BIRCH)
            .withProperty(BlockLeaves.DECAYABLE, true).withProperty(BlockLeaves.CHECK_DECAY, false);
    private static final IBlockState LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
    private final boolean isNatural;
    private BlockPos floor;
    
    public WorldGenGoldenBirchTree(boolean notify, boolean isNatural) {
        super(notify, true);
        this.isNatural = isNatural;
    }

    public boolean generate(World world, Random rand, BlockPos position) {
        floor = position;
        int i = 5 + rand.nextInt(3);
        boolean flag = true;
        if (position.getY() >= 1 && position.getY() + i + 1 <= 256) {
            for (int j = position.getY(); j <= position.getY() + 1 + i; ++j)
            {
                int k = 1;

                if (j == position.getY())
                {
                    k = 0;
                }

                if (j >= position.getY() + 1 + i - 2)
                {
                    k = 2;
                }

                BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

                for (int l = position.getX() - k; l <= position.getX() + k && flag; ++l)
                {
                    for (int i1 = position.getZ() - k; i1 <= position.getZ() + k && flag; ++i1)
                    {
                        if (j >= 0 && j < world.getHeight())
                        {
                            if (!this.isReplaceable(world, blockpos$mutableblockpos.setPos(l, j, i1)))
                            {
                                flag = false;
                            }
                        }
                        else
                        {
                            flag = false;
                        }
                    }
                }
            }

            if (!flag)
            {
                return false;
            }
            else
            {
                BlockPos down = position.down();
                IBlockState state = world.getBlockState(down);
                boolean isSoil = isNatural || state.getBlock().canSustainPlant(state, world, down, net.minecraft.util.EnumFacing.UP, (net.minecraft.block.BlockSapling)Blocks.SAPLING);

                if (isSoil && position.getY() < world.getHeight() - i - 1)
                {
                    state.getBlock().onPlantGrow(state, world, down, position);

                    for (int i2 = position.getY() - 3 + i; i2 <= position.getY() + i; ++i2)
                    {
                        int k2 = i2 - (position.getY() + i);
                        int l2 = 1 - k2 / 2;

                        for (int i3 = position.getX() - l2; i3 <= position.getX() + l2; ++i3)
                        {
                            int j1 = i3 - position.getX();

                            for (int k1 = position.getZ() - l2; k1 <= position.getZ() + l2; ++k1)
                            {
                                int l1 = k1 - position.getZ();

                                if (Math.abs(j1) != l2 || Math.abs(l1) != l2 || rand.nextInt(2) != 0 && k2 != 0)
                                {
                                    BlockPos blockpos = new BlockPos(i3, i2, k1);
                                    IBlockState state2 = world.getBlockState(blockpos);

                                    if (state2.getBlock().isAir(state2, world, blockpos) || state2.getBlock().isAir(state2, world, blockpos))
                                    {
                                        this.setBlockAndNotifyAdequately(world, blockpos, LEAVES);
                                    }
                                }
                            }
                        }
                    }

                    for (int j2 = 0; j2 < i; ++j2) {
                        BlockPos upN = position.up(j2);
                        IBlockState state2 = world.getBlockState(upN);
                        if (state2.getBlock().isAir(state2, world, upN) || state2.getBlock().isLeaves(state2, world, upN)) setBlockAndNotifyAdequately(world, position.up(j2), LOG);
                    }

                    return true;
                }
                return false;
            }
        }
        return false;
    }
    
    @Override
    protected void setBlockAndNotifyAdequately(World world, BlockPos pos, IBlockState state) {
        super.setBlockAndNotifyAdequately(world, pos, state);
        Random rand = world.rand;
        //branch stump
        if (pos.getY() - floor.getY() >= 5 && state.getBlock() == Blocks.LOG
                && state.getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.Y && rand.nextInt(25) == 0) {
            EnumFacing facing = EnumFacing.HORIZONTALS[rand.nextInt(4)];
            if (world.isAirBlock(pos.offset(facing))) {
                setBlockAndNotifyAdequately(world, pos.offset(facing), Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH)
                        .withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.fromFacingAxis(facing.getAxis())));
                return;
            }
        }
        if (!isNatural || state.getBlock() != Blocks.LOG) return;
        //decorations
        for (EnumFacing facing : EnumFacing.HORIZONTALS) if (rand.nextBoolean() && world.isAirBlock(pos.offset(facing)))
            setBlockAndNotifyAdequately(world, pos.offset(facing), BlockLichen.getBlockState(facing.getOpposite()));
        if (rand.nextInt(5) > 1) return;
        EnumFacing facing = DirectionUtils.getRandomDirectionXZ(rand);
        BlockPos facePos = pos.offset(facing);
        IBlockState shroom = (rand.nextInt(5) < 2 ? PhantasiaiBlocks.glowshrooms[rand.nextInt(PhantasiaiBlocks.glowshrooms.length)]:
                PhantasiaiBlocks.shrooms[rand.nextInt(PhantasiaiBlocks.shrooms.length)]).getDefaultState();
        if (world.isAirBlock(facePos)) setBlockAndNotifyAdequately(world, facePos, shroom.withProperty(BlockFBMushroom.FACING, facing));
    }
}
