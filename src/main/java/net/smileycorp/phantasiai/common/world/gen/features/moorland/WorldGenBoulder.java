package net.smileycorp.phantasiai.common.world.gen.features.moorland;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.smileycorp.phantasiai.common.world.gen.IMultiFacePlacer;

import java.util.Random;
import java.util.Set;

public class WorldGenBoulder extends WorldGenerator implements IMultiFacePlacer {
 
	private static final int startRadius = 2;

    private final Set<BlockPos> positions = Sets.newHashSet();
    private final Set<BlockPos> lichen = Sets.newHashSet();
    private int shift = 0;
    
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
        while (true) {
            if (pos.getY() > 60) {
                if (world.isAirBlock(pos.down()))  {
                    pos = pos.down();
                    continue ;
                }
                Block block = world.getBlockState(pos.down()).getBlock();
                if (block != Blocks.GRASS && block != Blocks.DIRT && block != Blocks.STONE) {
                    pos = pos.down();
                    continue ;
                }
            }
            if (pos.getY() <= 3) return false;
            int i1 = startRadius;
            BlockPos start = pos;
            int min = start.getY();
            int max = start.getY();
            for (int i = 0; i1 >= 0 && i < 3; ++i) {
                int j = i1 + rand.nextInt(2);
                int k = i1 + rand.nextInt(2);
                int l = i1 + rand.nextInt(2);
                float f = (j + k + l) * 0.333F + 0.5F;
                for (BlockPos newpos : BlockPos.getAllInBox(pos.add(-j, -k, -l), pos.add(j, k, l)))
                    if (newpos.distanceSq(pos) <= f * f) {
                        positions.add(newpos);
                        if (newpos.getY() < min) min = newpos.getY();
                        if (newpos.getY() > max) max = newpos.getY();
                        EnumFacing facing = EnumFacing.values()[rand.nextInt(EnumFacing.values().length)];
                        BlockPos p1 = pos.offset(facing);
                        if (world.isAirBlock(p1)) lichen.add(p1);
                    }
                pos = pos.add(-(i1 + 1) + rand.nextInt(2 + i1 * 2),  rand.nextInt(2), -(i1 + 1) + rand.nextInt(2 + i1 * 2));
            }
            shift = start.getY() - (min + (int) Math.ceil((float)(max - min) / 4f));
            for (BlockPos pos1 : positions) setBlockAndNotifyAdequately(world, pos1, getBlockState(rand));
            for (BlockPos pos1 : lichen) {
                pos1 = pos1.up(shift);
                if (world.isAirBlock(pos1)) setBlockAndNotifyAdequately(world, pos1, getMultiface(lichen(), world, pos1));
            }
            positions.clear();
            lichen.clear();
            return true;
        }
    }
    
    protected IBlockState getBlockState(Random rand) {
        switch (rand.nextInt(7)) {
            case 0:
                return Blocks.COBBLESTONE.getDefaultState();
            case 1:
                return Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE);
            case 2:
                return Blocks.GRAVEL.getDefaultState();
            case 3:
            case 4:
                return Blocks.MOSSY_COBBLESTONE.getDefaultState();
        }
        return Blocks.STONE.getDefaultState();
    }

    @Override
    protected void setBlockAndNotifyAdequately(World world, BlockPos pos, IBlockState state) {
        super.setBlockAndNotifyAdequately(world, pos.up(shift), state);
    }

    @Override
    public boolean supportsMultiFace(IBlockState state) {
        Block block = state.getBlock();
        return block == Blocks.STONE || block == Blocks.COBBLESTONE || block == Blocks.GRAVEL;
    }
    
}
