package net.smileycorp.fbiomes.common.world.gen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.Random;

public class WorldGenDisc extends WorldGenerator {
    
    private final int size;
    private final IBlockState state, replace;
    
    public WorldGenDisc(int size, IBlockState state, IBlockState replace) {
        this.size = size;
        this.state = state;
        this.replace = replace;
    }
    
    public boolean generate(World world, Random rand, BlockPos pos) {
        if (world.getBlockState(pos.down()) != replace) {
            System.out.println(world.getBlockState(pos) + ", " + pos);
            return false;
        }
        int i = rand.nextInt(this.size - 2) + 2;
        for (int k = pos.getX() - i; k <= pos.getX() + i; ++k) {
            for (int l = pos.getZ() - i; l <= pos.getZ() + i; ++l) {
                int i1 = k - pos.getX();
                int j1 = l - pos.getZ();
                if (i1 * i1 + j1 * j1 > i * i) continue;
                for (int k1 = pos.getY() - 1; k1 <= pos.getY() + 1; ++k1) {
                    BlockPos blockpos = new BlockPos(k, k1, l);
                    IBlockState block = world.getBlockState(blockpos);
                    if (!(block == state || block == replace)) continue;
                    setBlockAndNotifyAdequately(world, blockpos, state);
                }
            }
        }
        return true;
    }

}
