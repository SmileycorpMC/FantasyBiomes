package net.smileycorp.fbiomes.common.world.biomes;

import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.chunk.ChunkPrimer;
import net.smileycorp.fbiomes.common.Constants;

import java.util.Random;

public class BiomeAbyss extends BiomeOcean {
    
    public BiomeAbyss() {
        super(new BiomeProperties("Abyss").setBaseHeight(-1.95f).setHeightVariation(0F));
        setRegistryName(Constants.loc("abyss"));
    }
    
    @Override
    public int getWaterColorMultiplier() {
        return 0xFF094BBC;
    }
    
    @Override
    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noise) {
        int seaLevel = world.getSeaLevel();
        int k = (int)(noise / 3.0D + 3.0D + rand.nextDouble() * 0.25D);
        int l = x & 15;
        int i1 = z & 15;
        for (int j1 = 255; j1 >= 0; --j1) {
            if (j1 <= rand.nextInt(5)) {
                primer.setBlockState(i1, j1, l, BEDROCK);
                continue;
            }
            if (j1 < 5) {
                primer.setBlockState(i1, j1, l, Blocks.SANDSTONE.getDefaultState());
                continue;
            }
            if (j1 < k + 6) {
                primer.setBlockState(i1, j1, l, Blocks.SAND.getDefaultState());
                continue;
            }
            if (j1 < seaLevel) primer.setBlockState(i1, j1, l, Blocks.WATER.getDefaultState());
        }
    }
}
